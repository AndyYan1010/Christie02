package com.example.administrator.christie.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.christie.R;
import com.example.administrator.christie.TApplication;
import com.example.administrator.christie.util.Consts;
import com.example.administrator.christie.view.CustomProgress;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class PersonalActivity extends BaseActivity {
    private LinearLayout ll_yhm,ll_sjh,ll_xb,ll_bd;
    private TextView tv_username,tv_mob,tv_address,tv_gender;
    public static final int SHOW_RESPONSE = 0;
    private CustomProgress dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        setViews();
        setListeners();
    }

    protected void setViews(){
        ll_yhm = (LinearLayout)findViewById(R.id.ll_yhm);
        ll_sjh = (LinearLayout)findViewById(R.id.ll_sjh);
        ll_xb = (LinearLayout)findViewById(R.id.ll_xb);
        ll_bd = (LinearLayout)findViewById(R.id.ll_bd);
        tv_username = (TextView)findViewById(R.id.tv_username);
        tv_mob = (TextView)findViewById(R.id.tv_mob);
        tv_address = (TextView)findViewById(R.id.tv_address);
        tv_gender = (TextView)findViewById(R.id.tv_gender);
        GetThread thread = new GetThread(TApplication.user.getId());
        thread.start();
    }

    protected void setListeners(){
        ll_yhm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText et = new EditText(PersonalActivity.this);
                new AlertDialog.Builder(PersonalActivity.this).setTitle("用户名").setView(
                        et).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Modify thread = new Modify(TApplication.user.getId(),et.getText().toString(),"0");
                        thread.start();
                    }
                })
                        .setNegativeButton("取消", null).show();
            }
        });
        ll_sjh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PersonalActivity.this,ConfirmActivity.class));
            }
        });
        ll_xb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PersonalActivity.this);
                builder.setTitle("修改性别");
                //    指定下拉列表的显示数据
                final String[] genders = {"男", "女"};
                //    设置一个下拉的列表选择项
                builder.setItems(genders, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    String gender = null;
                    switch (which){
                        case 0:
                            gender = "0";
                            break;
                        case 1:
                            gender = "1";
                            break;
                    }
                    if(!gender.equals(null)) {
                        Modify thread = new Modify(TApplication.user.getId(), gender, "2");
                        thread.start();
                    }
                    }
                });
                builder.show();
            }
        });
        ll_bd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PersonalActivity.this,BindProjectActivity.class);
                startActivity(intent);
            }
        });
    }


    //子线程：使用POST方法向服务器发送数据
    class GetThread extends Thread {
        String id;

        public GetThread(String id) {
            dialog = CustomProgress.show(PersonalActivity.this,"加载中...", true, null);
            this.id = id;
        }

        @Override
        public void run() {
            HttpClient httpClient = new DefaultHttpClient();
            String url = Consts.URL+"detail?id="+id;
            Log.i("当前请求的URL",url+"  <<<<<<<<<<<<<<<<<<<<<<");
            //第二步：生成使用POST方法的请求对象
            HttpGet httpGet = new HttpGet(url);
            try {
                try {
                    //第三步：执行请求对象，获取服务器发还的相应对象
                    HttpResponse response = httpClient.execute(httpGet);
                    //第四步：检查相应的状态是否正常：检查状态码的值是200表示正常
                    if (response.getStatusLine().getStatusCode() == 200) {
                        //第五步：从相应对象当中取出数据，放到entity当中
                        HttpEntity entity = response.getEntity();
                        BufferedReader reader = new BufferedReader(
                                new InputStreamReader(entity.getContent()));
                        String result = reader.readLine();
                        Log.d("HTTP", "POST:" + result);
                        //在子线程中将Message对象发出去
                        Message message = new Message();
                        message.what = SHOW_RESPONSE;
                        message.obj = result;
                        handler.sendMessage(message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOW_RESPONSE:
                    String response = (String) msg.obj;
                    try {
                        JSONObject json = new JSONObject(response);
                        String username = json.getString("username");
                        String address = json.getString("address");
                        String gender = json.getString("gender");
                        if(username.equals("null")){
                            tv_username.setText("未填写");
                        }else{
                        tv_username.setText(username);
                        }
                        tv_mob.setText(TApplication.user.getFmobile());
                        if(address.equals("null")) {
                        tv_address.setText("未填写");
                        }else {
                            tv_address.setText(address);
                        }
                        tv_gender.setText(gender);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    dialog.dismiss();
                    break;
                default:
                    break;
            }
        }
    };



    //修改资料
    class Modify extends Thread {
        String id;
        String str;
        String code;

        public Modify(String id,String str,String code) {
            dialog = CustomProgress.show(PersonalActivity.this,"提交中...", true, null);
            this.id = id;
            this.str = str;
            this.code = code;
        }

        @Override
        public void run() {
            HttpClient httpClient = new DefaultHttpClient();
            String url = Consts.URL+"modifydetail?id="+id+"&str="+str+"&code="+code;
            Log.i("当前请求的URL",url+"  <<<<<<<<<<<<<<<<<<<<<<");
            //第二步：生成使用POST方法的请求对象
            HttpGet httpGet = new HttpGet(url);
            try {
                try {
                    //第三步：执行请求对象，获取服务器发还的相应对象
                    HttpResponse response = httpClient.execute(httpGet);
                    //第四步：检查相应的状态是否正常：检查状态码的值是200表示正常
                    if (response.getStatusLine().getStatusCode() == 200) {
                        //第五步：从相应对象当中取出数据，放到entity当中
                        HttpEntity entity = response.getEntity();
                        BufferedReader reader = new BufferedReader(
                                new InputStreamReader(entity.getContent()));
                        String result = reader.readLine();
                        Log.d("HTTP", "POST:" + result);
                        //在子线程中将Message对象发出去
                        Message message = new Message();
                        message.what = SHOW_RESPONSE;
                        message.obj = result;
                        handler1.sendMessage(message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    private Handler handler1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOW_RESPONSE:
                    String response = (String) msg.obj;
                    if(!response.equals("")){
                        Toast.makeText(PersonalActivity.this,"用户名修改成功",Toast.LENGTH_SHORT).show();
                        tv_username.setText(response);
                    }else if(response.equals("3")) {
                        Toast.makeText(PersonalActivity.this,"手机号修改成功",Toast.LENGTH_SHORT).show();
                    }else if(response.equals("0")) {
                        Toast.makeText(PersonalActivity.this,"性别修改成功",Toast.LENGTH_SHORT).show();
                        tv_gender.setText("男");
                    }else if(response.equals("1")) {
                        Toast.makeText(PersonalActivity.this,"性别修改成功",Toast.LENGTH_SHORT).show();
                        tv_gender.setText("女");
                    }else if(response.equals("4")){
                        Toast.makeText(PersonalActivity.this,"操作失败",Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                    break;
                default:
                    break;
            }
        }
    };
}
