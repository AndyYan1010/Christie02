package com.example.administrator.christie.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.christie.R;
import com.example.administrator.christie.modelInfo.RequestParamsFM;
import com.example.administrator.christie.modelInfo.UserInfo;
import com.example.administrator.christie.util.AllCapTransformationMethod;
import com.example.administrator.christie.util.Consts;
import com.example.administrator.christie.util.HttpOkhUtils;
import com.example.administrator.christie.util.RegexUtils;
import com.example.administrator.christie.util.SPref;
import com.example.administrator.christie.util.ToastUtils;
import com.example.administrator.christie.websiteUrl.NetConfig;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import okhttp3.Request;

public class AddPlatenoActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText et_plateno, et_model, et_color, et_mob;
    private Button btn_submit;
    public static final int SHOW_RESPONSE = 0;
    private ImageView mImg_back;
    private TextView  mTv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plateno);
        setViews();
        setData();
        setListeners();
    }

    protected void setViews() {
        mImg_back = (ImageView) findViewById(R.id.img_back);
        mTv_title = (TextView) findViewById(R.id.tv_title);
        et_plateno = (EditText) findViewById(R.id.et_plateno);
        et_model = (EditText) findViewById(R.id.et_model);
        et_color = (EditText) findViewById(R.id.et_color);
        et_mob = (EditText) findViewById(R.id.et_mob);
        btn_submit = (Button) findViewById(R.id.btn_submit);
    }

    private void setData() {
        mImg_back.setOnClickListener(this);
        mTv_title.setText(R.string.addlicense);
    }

    protected void setListeners() {
        et_plateno.setTransformationMethod(new AllCapTransformationMethod());
        btn_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_submit:
                String plateno = String.valueOf(et_plateno.getText()).trim();
                String model = String.valueOf(et_model.getText()).trim();
                String color = String.valueOf(et_color.getText()).trim();
                String mobile = String.valueOf(et_mob.getText()).trim();
                if ("".equals(plateno) || "请输入车牌号码".equals(plateno)) {
                    ToastUtils.showToast(AddPlatenoActivity.this, "请输入车牌号码");
                    return;
                }
                if ("".equals(model) || "请输入汽车型号".equals(model)) {
                    ToastUtils.showToast(AddPlatenoActivity.this, "请输入车型");
                    return;
                }
                if ("".equals(color) || "请输入汽车颜色".equals(color)) {
                    ToastUtils.showToast(AddPlatenoActivity.this, "请输入汽车颜色");
                    return;
                }
                boolean b = RegexUtils.checkMobile(mobile);
                if (!b) {
                    ToastUtils.showToast(AddPlatenoActivity.this, "请输入正确的手机号码");
                    return;
                }
                //添加汽车信息
                addPersonalCar(plateno, model, color, mobile);
                //                if (plateno.equals("") || model.equals("") || color.equals("") || mobile.equals("")) {
                //                    Toast.makeText(AddPlatenoActivity.this, "信息需填写完整", Toast.LENGTH_SHORT).show();
                //                } else {
                //                    try {
                //                        JSONObject json = new JSONObject();
                //                        json.put("id", TApplication.user.getId());
                //                        json.put("plateno", plateno.toUpperCase());
                //                        json.put("model", model);
                //                        json.put("color", color);
                //                        json.put("mobile", mobile);
                //                        PostThread thread = new PostThread(json);
                //                        thread.start();
                //                    } catch (Exception e) {
                //                        e.printStackTrace();
                //                    }
                //                }
                break;
            default:
                break;
        }
    }

    private void addPersonalCar(String plateno, String model, String color, String mobile) {
        UserInfo userinfo = SPref.getObject(AddPlatenoActivity.this, UserInfo.class, "userinfo");
        String phone = userinfo.getPhone();
        String addCarUrl = NetConfig.ADDPLATE;
        RequestParamsFM params = new RequestParamsFM();
        params.put("user_id", phone);
        params.put("plateno", plateno);
        params.put("model", model);
        params.put("color", color);
        params.put("mobile", mobile);
        params.setUseJsonStreamer(true);
        HttpOkhUtils.getInstance().doPost(addCarUrl, params, new HttpOkhUtils.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                ToastUtils.showToast(AddPlatenoActivity.this, "网络错误");
            }

            @Override
            public void onSuccess(int code, String resbody) {
                if (code != 200) {
                    ToastUtils.showToast(AddPlatenoActivity.this, "网络请求失败");
                } else {
                    ToastUtils.showToast(AddPlatenoActivity.this, "添加成功");
                }
            }
        });

    }


    //子线程：使用POST方法向服务器发送数据
    class PostThread extends Thread {
        JSONObject json;

        public PostThread(JSONObject json) {
            this.json = json;
        }

        @Override
        public void run() {
            try {
                HttpClient httpClient = new DefaultHttpClient();
                String url = Consts.URL + "addplate";
                //第二步：生成使用POST方法的请求对象
                HttpPost httpPost = new HttpPost(url);
                httpPost.setHeader(new BasicHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8"));
                //NameValuePair对象代表了一个需要发往服务器的键值对
                NameValuePair pair1 = new BasicNameValuePair("platejson", json.toString());
                //将准备好的键值对对象放置在一个List当中
                ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
                pairs.add(pair1);
                //创建代表请求体的对象（注意，是请求体）
                HttpEntity requestEntity = new UrlEncodedFormEntity(pairs, "utf-8");
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


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOW_RESPONSE:
                    String response = (String) msg.obj;
                    if (response.equals("1")) {
                        Toast.makeText(AddPlatenoActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AddPlatenoActivity.this, PlateActivity.class));
                        finish();
                    } else {
                        Toast.makeText(AddPlatenoActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
        }
    };
}
