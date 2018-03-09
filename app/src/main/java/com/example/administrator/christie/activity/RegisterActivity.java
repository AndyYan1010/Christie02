package com.example.administrator.christie.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.administrator.christie.R;
import com.example.administrator.christie.TApplication;
import com.example.administrator.christie.util.Consts;
import com.example.administrator.christie.util.MD5Util;
import com.example.administrator.christie.util.SendMsgUtil;
import com.example.administrator.christie.util.Task;
import com.example.administrator.christie.view.CountdownButton;
import com.example.administrator.christie.view.CustomProgress;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.UUID;

public class RegisterActivity extends BaseActivity {
    private EditText et_mobile_register,et_code_register,et_pwd,et_repeat;
    private CountdownButton btn_code_register;
    private Button btn_register;
    private String code;
    public static final int SHOW_RESPONSE = 0;
    private CustomProgress dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setViews();
        setListeners();
    }

    protected void setViews(){
        et_mobile_register = (EditText)findViewById(R.id.et_mobile_register);
        et_code_register = (EditText)findViewById(R.id.et_code_register);
        btn_code_register = (CountdownButton)findViewById(R.id.btn_code_register);
        btn_register = (Button)findViewById(R.id.btn_register);
        et_pwd = (EditText)findViewById(R.id.et_pwd);
        et_repeat = (EditText)findViewById(R.id.et_repeat);
    }

    protected void setListeners(){
        btn_code_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et_mobile_register.getText().toString().equals("")||et_mobile_register.getText().toString().length()<11){
                    Toast.makeText(RegisterActivity.this,"请输入正确的手机号！",Toast.LENGTH_SHORT).show();
                }else{
                    btn_code_register.start();
                    //发送短信
                    code = SendMsgUtil.getRandomString();
                    Log.i("短信验证码:",code+"<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
                    new Task(et_mobile_register.getText().toString(),code).execute();
                }
            }
        });
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et_mobile_register.getText().toString().equals("")||et_code_register.getText().toString().equals("")){
                    Toast.makeText(RegisterActivity.this,"信息填写不完整！",Toast.LENGTH_SHORT).show();
                }else{
                    //判断验证码是否一致
                    if(et_code_register.getText().toString().equals(code)){
                        PostThread thread = new PostThread(et_mobile_register.getText().toString(), et_pwd.getText().toString(),UUID.randomUUID().toString());
                        thread.start();
                    }else{
                        Toast.makeText(RegisterActivity.this,"验证码不正确，请重新输入！",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        et_pwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){

                }else{
                    if(!et_pwd.getText().toString().equals("")&&!et_repeat.isFocused()){
                        et_repeat.setHintTextColor(Color.RED);
                        // 新建一个可以添加属性的文本对象
                        SpannableString ss = new SpannableString("请重复输入密码！");
                        // 新建一个属性对象,设置文字的大小
                        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(15,true);
                        // 附加属性到文本
                        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        et_repeat.setHint(new SpannableString(ss));
                    }
                }
            }
        });

        et_repeat.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    if(et_pwd.getText().toString().equals("")){
//                        Toast.makeText(PwdModifyActivity.this,"还未输入新密码！",Toast.LENGTH_LONG).show();
                        et_pwd.setHintTextColor(Color.RED);
                        // 新建一个可以添加属性的文本对象
                        SpannableString ss = new SpannableString("请输入密码！");
                        // 新建一个属性对象,设置文字的大小
                        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(15,true);
                        // 附加属性到文本
                        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        et_pwd.setHint(new SpannableString(ss));
                    }
                }else{
                    if(!et_pwd.getText().toString().equals(et_repeat.getText().toString())&&!et_repeat.getText().toString().equals("")){
//                        Toast.makeText(PwdModifyActivity.this,"两次输入的密码不一致，请重新输入！",Toast.LENGTH_LONG).show();
                        et_repeat.setHintTextColor(Color.RED);
                        // 新建一个可以添加属性的文本对象
                        SpannableString ss = new SpannableString("两次密码不一致，请重新输入！");
                        // 新建一个属性对象,设置文字的大小
                        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(15,true);
                        // 附加属性到文本
                        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        et_repeat.setHint(new SpannableString(ss));
                        et_repeat.setText("");
                    }else if(et_repeat.getText().toString().equals("")&&!et_pwd.getText().toString().equals("")){
                        et_repeat.setHintTextColor(Color.RED);
                        // 新建一个可以添加属性的文本对象
                        SpannableString ss = new SpannableString("请重复输入密码！");
                        // 新建一个属性对象,设置文字的大小
                        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(15,true);
                        // 附加属性到文本
                        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        et_repeat.setHint(new SpannableString(ss));
                    }
                }
            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOW_RESPONSE:
                    String response = (String) msg.obj;
                    String s[] = response.split(",");
                    if(s[0].equals("0")){
                        Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                        //把登录状态放进我的页面
                        TApplication.user.setId(s[2]);
                        TApplication.user.setFmobile(s[1]);
                        TApplication.user.setFname("");
                        TApplication.user.setFunctionlist(new ArrayList<String>());
                        TApplication.user.setApplylist(new ArrayList<String>());
                        startActivity(new Intent(RegisterActivity.this,ValidateActivity.class));
                        finish();
                    }else{
                        Toast.makeText(RegisterActivity.this,"该手机号已被注册，请更换手机号或直接登录！",Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                    break;
                default:
                    break;
            }
        }
    };


    //子线程：使用POST方法向服务器发送用户名、密码等数据
     class PostThread extends Thread {
         String mobile;
         String password;
         String id;

        public PostThread(String mobile,String password,String id) {
            dialog = CustomProgress.show(RegisterActivity.this,"注册中...",true,null);
            this.mobile = mobile;
            this.password = password;
            this.id = id;
        }

                 @Override
         public void run() {
             HttpClient httpClient = new DefaultHttpClient();
             String url = Consts.URL+"register";
             //第二步：生成使用POST方法的请求对象
             HttpPost httpPost = new HttpPost(url);
             //NameValuePair对象代表了一个需要发往服务器的键值对
             NameValuePair pair1 = new BasicNameValuePair("mobile", mobile);
             NameValuePair pair2 = new BasicNameValuePair("password", password);
             NameValuePair pair3 = new BasicNameValuePair("id", id);
             //将准备好的键值对对象放置在一个List当中
             ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
             pairs.add(pair1);
             pairs.add(pair2);
             pairs.add(pair3);
             try {
                 //创建代表请求体的对象（注意，是请求体）
                 HttpEntity requestEntity = new UrlEncodedFormEntity(pairs);
                 //将请求体放置在请求对象当中
                 httpPost.setEntity(requestEntity);
                 //执行请求对象
                 try {
                     //第三步：执行请求对象，获取服务器发还的相应对象
                     HttpResponse response = httpClient.execute(httpPost);
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
                         message.obj = result+","+mobile+","+id;
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



    /*//发送短信
    class Task extends AsyncTask<Void, Integer, Integer> {
        String mobile;
        String code;

        Task(String mobile,String code){
            this.mobile = mobile;
            this.code = code;
        }

        *//**
         * 运行在UI线程中，在调用doInBackground()之前执行
         *//*
        @Override
        protected void onPreExecute() {

        }

        *//**
         * 后台运行的方法，可以运行非UI线程，可以执行耗时的方法
         *//*
        @Override
        protected Integer doInBackground(Void... params) {
            try {
                String url = Consts.URL + "sendMsg?mobile=" + mobile + "&code=" + code;
                HttpResponse response = HttpUtils.GetUtil(url);
                if (response.getStatusLine().getStatusCode() == 200) {
                    //第五步：从相应对象当中取出数据，放到entity当中
                    HttpEntity entity = response.getEntity();
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(entity.getContent()));
                    String result = reader.readLine();
                    Log.d("HTTP", "POST:" + result);
                    return Integer.parseInt(result);
                }else{
                 return 2;
                }
            }catch (Exception e){
                e.printStackTrace();
                return 1;
            }
        }

        *//**
         * 运行在ui线程中，在doInBackground()执行完毕后执行
         *//*
        @Override
        protected void onPostExecute(Integer integer) {

        }

        *//**
         * 在publishProgress()被调用以后执行，publishProgress()用于更新进度
         *//*
        @Override
        protected void onProgressUpdate(Integer... values) {

        }
    }*/
}
