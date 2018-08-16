package com.example.administrator.christie.activity.notme;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.administrator.christie.R;
import com.example.administrator.christie.TApplication;
import com.example.administrator.christie.activity.BaseActivity;
import com.example.administrator.christie.activity.LoginActivity;
import com.example.administrator.christie.util.Consts;
import com.example.administrator.christie.util.SendMsgUtil;
import com.example.administrator.christie.util.Task;
import com.example.administrator.christie.view.CountdownButton;
import com.example.administrator.christie.view.CustomProgress;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MobModifyActivity extends BaseActivity {
    private EditText et_mobile_modify,et_code_modify;
    private CountdownButton btn_code_modify;
    private Button btn_mobmodify;
    private String code="";
    private CustomProgress dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mob_modify);
        setViews();
        setListeners();
    }

    protected void setViews(){
        et_mobile_modify = (EditText)findViewById(R.id.et_mobile_modify);
        et_code_modify = (EditText)findViewById(R.id.et_code_modify);
        btn_code_modify = (CountdownButton)findViewById(R.id.btn_code_modify);
        btn_mobmodify = (Button)findViewById(R.id.btn_mobmodifyy);
    }

    protected void setListeners(){
        btn_code_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et_mobile_modify.getText().toString().equals("")){
                    Toast.makeText(MobModifyActivity.this,"请输入手机号",Toast.LENGTH_SHORT).show();
                }else{
                    //生成随机六位验证码
                    btn_code_modify.start();
                    code = SendMsgUtil.getRandomString();
                    Log.i("当前的验证码",code+"<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
                    //发送短信
                    new Task(et_mobile_modify.getText().toString(),code).execute();
                }
            }
        });
        btn_mobmodify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et_mobile_modify.getText().toString().equals("")||code.equals("")||et_code_modify.getText().toString().equals("")){
                    Toast.makeText(MobModifyActivity.this,"信息未填写完整",Toast.LENGTH_SHORT).show();
                }else if(et_code_modify.getText().toString().equals(code)){
                    //提交手机号
                    Task1 task = new Task1();
                    task.execute();
                }else if(et_mobile_modify.getText().toString().equals(TApplication.user.getFmobile())){
                    Toast.makeText(MobModifyActivity.this,"请输入新的手机号，或者取消修改",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    /**
     * 提交手机号
     */
    class Task1 extends AsyncTask<Void, Integer, Integer> {

        /**
         * 运行在UI线程中，在调用doInBackground()之前执行
         */
        @Override
        protected void onPreExecute() {
            dialog = CustomProgress.show(MobModifyActivity.this, "提交中...", true, null);
        }

        /**
         * 后台运行的方法，可以运行非UI线程，可以执行耗时的方法
         */
        @Override
        protected Integer doInBackground(Void... params) {
            HttpClient httpClient = new DefaultHttpClient();
            String url = Consts.URL + "modifydetail?id=" + TApplication.user.getId() + "&str=" + et_mobile_modify.getText().toString() + "&code=1";
            Log.i("当前请求的URL", url + "  <<<<<<<<<<<<<<<<<<<<<<");
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
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return 1;
        }

        /**
         * 运行在ui线程中，在doInBackground()执行完毕后执行
         */
        @Override
        protected void onPostExecute(Integer integer) {
            dialog.dismiss();
            Toast.makeText(MobModifyActivity.this,"修改成功，请重新登录",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MobModifyActivity.this,LoginActivity.class));
            TApplication.exit();
        }

        /**
         * 在publishProgress()被调用以后执行，publishProgress()用于更新进度
         */
        @Override
        protected void onProgressUpdate(Integer... values) {

        }
    }
}
