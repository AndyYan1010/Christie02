package com.example.administrator.christie.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/1/22 0022.
 */

public class HttpUtils {
    /**
     * post请求
     * @param url
     * @param pairs
     * @return
     */
    public static HttpResponse PostUtil(String url,ArrayList<NameValuePair> pairs){
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            HttpEntity requestEntity = new UrlEncodedFormEntity(pairs,"utf-8");
            httpPost.setEntity(requestEntity);
            HttpResponse response = httpClient.execute(httpPost);
            return response;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static HttpResponse GetUtil(String url){
        try{
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            HttpResponse response = httpClient.execute(httpGet);
            return response;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
