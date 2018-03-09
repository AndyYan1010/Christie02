package com.example.administrator.christie.activity;

import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.administrator.christie.R;
import com.example.administrator.christie.TApplication;
import com.example.administrator.christie.fragment.CountdownFragment;
import com.example.administrator.christie.fragment.SearchFragment;
import com.example.administrator.christie.fragment.TempayFragment;
import com.example.administrator.christie.util.Consts;
import com.example.administrator.christie.util.HttpUtils;
import com.example.administrator.christie.view.CustomProgress;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class TempayActivity extends BaseActivity {
    CustomProgress dialog;
    public SearchFragment searchFragment;
    public TempayFragment tempayFragment;
    public CountdownFragment countdownFragment;
    private Long future;

    public Long getFuture() {
        return future;
    }

    public void setFuture(Long future) {
        this.future = future;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tempay);
        searchFragment = new SearchFragment();
        tempayFragment = new TempayFragment();
        countdownFragment = new CountdownFragment();
        new Task().execute();
    }

    class Task extends AsyncTask<Void,String,String>{
        @Override
        protected void onPreExecute() {
            dialog = CustomProgress.show(TempayActivity.this,"加载中...",true,null);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                String url = Consts.URL + "checkplateno?userid=" + TApplication.user.getId();
                HttpResponse response = HttpUtils.GetUtil(url);
                HttpEntity entity = response.getEntity();
                BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
                String result = reader.readLine();
                Log.d("HTTP", "POST:" + result);
                if(result.equals("n")){
                    return "";
                }else if(result.equals("a")){
                    return "";
                }else{
                   return result;
                }
            }catch (Exception e){
                e.printStackTrace();
                return "";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            dialog.dismiss();
            if(s.equals("")){
                Toast.makeText(TempayActivity.this,"未查询到车牌信息，请输入查询",Toast.LENGTH_SHORT).show();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.add(R.id.frag_container, searchFragment);
                transaction.show(searchFragment);
                transaction.commit();
            }else{
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.add(R.id.frag_container, tempayFragment);
                transaction.show(tempayFragment);
                transaction.commit();
            }
        }

    }
}
