package com.example.administrator.christie.util;

import com.example.administrator.christie.modelInfo.RequestParamsFM;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @创建者 AndyYan
 * @创建时间 2018/4/26 8:42
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class HttpOkhUtils {
    // 网络请求超时时间值(s)
    private static final int DEFAULT_TIMEOUT = 10;
    private static HttpOkhUtils okhUtils;
    private        OkHttpClient client;

    private HttpOkhUtils() {
        client = new OkHttpClient.Builder()
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();
    }

    public static HttpOkhUtils getInstance() {
        if (okhUtils == null) {
            synchronized (HttpOkhUtils.class) {
                if (okhUtils == null)
                    okhUtils = new HttpOkhUtils();
            }
        }
        return okhUtils;
    }

    public void doGet(String url, HttpCallBack httpCallBack) {
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new StringCallBack(request, httpCallBack));
    }

    public void doGetWithParams(String url, RequestParamsFM bean, HttpCallBack httpCallBack) {
        url = url + "?";
        Iterator iter = bean.entrySet().iterator();
        while (iter.hasNext()) {
            Object next = iter.next();
            if (null != next) {
                RequestParamsFM.Entry entry = (RequestParamsFM.Entry) next;
                Object key = entry.getKey();
                Object value = entry.getValue();
                url = url + key + "=" + value + "&";
            } else {
                RequestParamsFM.Entry entry = (RequestParamsFM.Entry) next;
                Object key = entry.getKey();
                Object value = entry.getValue();
                url = url + key + "=" + value;
            }
        }
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new StringCallBack(request, httpCallBack));
    }

    public void doPost(String url, RequestParamsFM bean, HttpCallBack httpCallBack) {
        RequestBody requestBody;
        boolean toJson = bean.getIsUseJsonStreamer();
        if (toJson) {
            Gson gson = new Gson();
            //使用Gson将对象转换为json字符串
            String json = gson.toJson(bean);
            //MediaType  设置Content-Type 标头中包含的媒体类型值
            requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        } else {
            FormBody.Builder builder = new FormBody.Builder();
            Set<String> set = bean.keySet();
            for (String key : set) {
                String value = bean.get(key).toString();
                builder.add(key, value);
            }
            requestBody = builder.build();
        }

        Request request = new Request.Builder().url(url).post(requestBody).build();
        client.newCall(request).enqueue(new StringCallBack(request, httpCallBack));
    }

    public static class StringCallBack implements Callback {
        private HttpCallBack httpCallBack;
        private Request      request;

        public StringCallBack(Request request, HttpCallBack httpCallBack) {
            this.request = request;
            this.httpCallBack = httpCallBack;
        }

        @Override
        public void onFailure(Call call, final IOException e) {
            ThreadUtils.runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    httpCallBack.onError(request, e);
                }
            });
        }

        @Override
        public void onResponse(Call call, final Response response) {
            if (httpCallBack != null) {
                try {
                    final int code = response.code();
                    final String buffer = response.body().string();
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            httpCallBack.onSuccess(code, buffer);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public interface HttpCallBack {

        void onError(Request request, IOException e);

        void onSuccess(int code, String resbody);
    }
}
