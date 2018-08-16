package com.example.administrator.christie.activity.notme;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.christie.R;
import com.example.administrator.christie.TApplication;
import com.example.administrator.christie.activity.MainActivity;
import com.example.administrator.christie.util.Consts;
import com.example.administrator.christie.util.HttpUtils;
import com.example.administrator.christie.view.CustomProgress;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ValidateActivity extends AppCompatActivity {
    private TextView tv_wuye,tv_xiaoqu,tv_xiaci;
    private EditText et_huzhu,et_idcard,et_guanxi;
    private Button btn_submits;
    private String wuyuId,xiaoquId=null;
    private CustomProgress dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validate);
        setViews();
        setListeners();
    }

    protected void setViews(){
        tv_wuye = (TextView)findViewById(R.id.tv_wuye);
        tv_xiaoqu = (TextView)findViewById(R.id.tv_xiaoqu);
        tv_xiaci = (TextView)findViewById(R.id.tv_xiaci);
        et_huzhu = (EditText)findViewById(R.id.et_huzhu);
        et_idcard = (EditText)findViewById(R.id.et_idcard);
        et_guanxi = (EditText)findViewById(R.id.et_guanxi);
        btn_submits = (Button) findViewById(R.id.btn_submits);
        tv_xiaci.setVisibility(View.INVISIBLE);
    }

    protected void setListeners(){
        //选择物业公司
        tv_wuye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Task1().execute();
            }
        });
        //选择小区
        tv_xiaoqu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tv_wuye.getText().toString().equals("")) {
                    Toast.makeText(ValidateActivity.this,"请先选择物业公司",Toast.LENGTH_SHORT).show();
                }else{
                    new Task2(wuyuId).execute();
                }
            }
        });
        //提交验证待审核
        btn_submits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    JSONObject json = new JSONObject();
                    json.put("id", TApplication.user.getId());
                    json.put("mobile",TApplication.user.getFmobile());
                    json.put("fname",et_huzhu.getText().toString());
                    json.put("compid",wuyuId);
                    json.put("areaid",xiaoquId);
                    json.put("gender","");
                    json.put("address",tv_wuye.getText().toString()+tv_xiaoqu.getText().toString());
                    json.put("guanx",et_guanxi.getText().toString());
                    json.put("img","");
                    json.put("idcard",et_idcard.getText().toString());
                    new Task(json.toString()).execute();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
        //下次再说
        tv_xiaci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ValidateActivity.this,MainActivity.class));
                finish();
            }
        });
    }

    class Task extends AsyncTask<Void,Integer,Integer>{
        String json;

        Task(String json){
            this.json = json;
        }

        @Override
        protected void onPreExecute() {
            dialog = CustomProgress.show(ValidateActivity.this,"提交中...",true,null);
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            String url = Consts.URL+"register1";
            //NameValuePair对象代表了一个需要发往服务器的键值对
            NameValuePair pair1 = new BasicNameValuePair("json", json);
            //将准备好的键值对对象放置在一个List当中
            ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(pair1);
            try {
                HttpResponse response = HttpUtils.PostUtil(url, pairs);
                Log.i("获取的服务器响应",response.getStatusLine().getStatusCode()+"");
                //第四步：检查相应的状态是否正常：检查状态码的值是200表示正常
                if (response.getStatusLine().getStatusCode() == 200) {
                    //第五步：从相应对象当中取出数据，放到entity当中
                    HttpEntity entity = response.getEntity();
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(entity.getContent()));
                    String result = reader.readLine();
                    Log.d("HTTP", "POST:" + result);
                }
                return 1;
            }catch (Exception e){
                e.printStackTrace();
                return 2;
            }
        }

        @Override
        protected void onPostExecute(Integer integer) {
            dialog.dismiss();
            if(integer==1){
                Toast.makeText(ValidateActivity.this,"认证资料已提交，等待审核",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ValidateActivity.this,MainActivity.class));
                finish();
            }else {
                Toast.makeText(ValidateActivity.this,"提交失败",Toast.LENGTH_SHORT).show();
            }
        }
    }

    class Task1 extends AsyncTask<Void,Integer,String>{

        @Override
        protected String doInBackground(Void... voids) {
            String url = Consts.URL+"depart?id=1";
            try{
                HttpResponse response = HttpUtils.GetUtil(url);
                Log.i("获取的服务器响应",response.getStatusLine().getStatusCode()+"");
                //第四步：检查相应的状态是否正常：检查状态码的值是200表示正常
                if (response.getStatusLine().getStatusCode() == 200) {
                    //第五步：从相应对象当中取出数据，放到entity当中
                    HttpEntity entity = response.getEntity();
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(entity.getContent()));
                    String result = reader.readLine();
                    Log.d("HTTP", "POST:" + result);
                    return result;
                }else{
                    return null;
                }

            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String str) {
            if(str!=null) {
                try {
                    JSONArray array = new JSONArray(str);
                    final String[] strs = new String[array.length()];
                    final String[] ids = new String[array.length()];
                    for(int i=0;i<array.length();i++){
                        JSONObject json = array.getJSONObject(i);
                        strs[i] = json.getString("departname");
                        ids[i] = json.getString("id");
                    }
                    new AlertDialog.Builder(ValidateActivity.this).setTitle("选择物业").setItems(strs, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            tv_wuye.setText(strs[i]);
                            wuyuId = ids[i];
                        }
                    }).show();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }


    class Task2 extends AsyncTask<Void,Integer,String>{
        String id;

        Task2(String id){
            this.id = id;
        }

        @Override
        protected String doInBackground(Void... voids) {
            String url = Consts.URL+"depart?id="+id;
            try{
                HttpResponse response = HttpUtils.GetUtil(url);
                Log.i("获取的服务器响应",response.getStatusLine().getStatusCode()+"");
                //第四步：检查相应的状态是否正常：检查状态码的值是200表示正常
                if (response.getStatusLine().getStatusCode() == 200) {
                    //第五步：从相应对象当中取出数据，放到entity当中
                    HttpEntity entity = response.getEntity();
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(entity.getContent()));
                    String result = reader.readLine();
                    Log.d("HTTP", "POST:" + result);
                    return result;
                }else{
                    return null;
                }

            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String str) {
            if(str!=null) {
                try {
                    JSONArray array = new JSONArray(str);
                    final String[] strs1 = new String[array.length()];
                    final String[] ids1 = new String[array.length()];
                    for(int i=0;i<array.length();i++){
                        JSONObject json = array.getJSONObject(i);
                        strs1[i] = json.getString("departname");
                        ids1[i] = json.getString("id");
                    }
                    new AlertDialog.Builder(ValidateActivity.this).setTitle("选择物业").setItems(strs1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            tv_xiaoqu.setText(strs1[i]);
                            xiaoquId = ids1[i];
                        }
                    }).show();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}
