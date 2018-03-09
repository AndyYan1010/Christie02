package com.example.administrator.christie.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.christie.R;
import com.example.administrator.christie.TApplication;
import com.example.administrator.christie.util.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ShenqingActivity extends BaseActivity {
    private TextView tv_shenqing,tv_remind;
    private Button btn_now,btn_no;
    private String code;
    public static final int SHOW_RESPONSE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shenqing);
        setViews();
        setListeners();
    }

    protected void setViews(){
        Intent intent =getIntent();
        code = intent.getStringExtra("code");
        Log.i("当前的权限代码",code+"<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        tv_shenqing = (TextView)findViewById(R.id.tv_shenqing);
        tv_shenqing.setText(intent.getStringExtra("title"));
        tv_remind = (TextView)findViewById(R.id.tv_remind);
        btn_now = (Button)findViewById(R.id.btn_now);
        btn_no = (Button)findViewById(R.id.btn_no);
        if(TApplication.user.getApplylist().contains(code)){
            tv_remind.setText(getString(R.string.remind));
            btn_now.setVisibility(View.INVISIBLE);
            btn_no.setText(getString(R.string.no1));
        }
    }

    protected void setListeners(){
        btn_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    JSONObject json = new JSONObject();
                    json.put("id", TApplication.user.getId());
                    json.put("function_code",code);
                    PostThread thread = new PostThread(json);
                    thread.start();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
                    if(response.equals("1")){
                        Toast.makeText(ShenqingActivity.this,"提交成功",Toast.LENGTH_SHORT).show();
                        TApplication.user.getApplylist().add(code);
                        tv_remind.setText(getString(R.string.remind));
                        btn_now.setVisibility(View.INVISIBLE);
                        btn_no.setText(getString(R.string.no1));
                    }else{
                        Toast.makeText(ShenqingActivity.this,"提交失败，请重新尝试！",Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    //子线程：使用POST方法向服务器发送数据
    class PostThread extends Thread {
        JSONObject json;

        public PostThread(JSONObject json) {
            this.json = json;
        }

        @Override
        public void run() {
            HttpClient httpClient = new DefaultHttpClient();
            String url = Consts.URL+"apply";
            //第二步：生成使用POST方法的请求对象
            HttpPost httpPost = new HttpPost(url);
            //NameValuePair对象代表了一个需要发往服务器的键值对
            NameValuePair pair1 = new BasicNameValuePair("application", json.toString());
            //将准备好的键值对对象放置在一个List当中
            ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(pair1);
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
}

