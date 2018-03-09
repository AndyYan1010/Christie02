package com.example.administrator.christie.util;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by Administrator on 2018/1/24 0024.
 */

public class Task extends AsyncTask<Void, Integer, Integer> {
    String mobile;
    String code;

    public Task(String mobile,String code){
        this.mobile = mobile;
        this.code = code;
    }

    @Override
    protected Integer doInBackground(Void... voids) {
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
}
