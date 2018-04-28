package com.example.administrator.christie.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.administrator.christie.InformationMessege.ProjectInfo;
import com.example.administrator.christie.R;
import com.example.administrator.christie.adapter.ProSpinnerAdapter;
import com.example.administrator.christie.modelInfo.RequestParamsFM;
import com.example.administrator.christie.modelInfo.UserInfo;
import com.example.administrator.christie.util.HttpOkhUtils;
import com.example.administrator.christie.util.SPref;
import com.example.administrator.christie.util.ToastUtils;
import com.example.administrator.christie.websiteUrl.NetConfig;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

/**
 * @创建者 AndyYan
 * @创建时间 2018/4/27 9:33
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class BindProjectActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mImg_back;
    private TextView  mTv_title;
    private Spinner   mSpinner_village, mSpinner_unit;
    private List<ProjectInfo> dataProList, dataInfoList;
    private ProSpinnerAdapter mProjAdapter, mDetailAdapter;
    private Button mBt_submit;
    private String mProjectID, mDetailID;
    private EditText mEt_place;
    private EditText mEt_relation;
    private String   mPlace;
    private String   mRelation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_projcet);
        setViews();
        setData();
    }

    private void setViews() {
        mImg_back = (ImageView) findViewById(R.id.img_back);
        mTv_title = (TextView) findViewById(R.id.tv_title);
        mSpinner_village = (Spinner) findViewById(R.id.spinner_village);
        mSpinner_unit = (Spinner) findViewById(R.id.spinner_unit);
        mEt_place = (EditText) findViewById(R.id.et_place);
        mEt_relation = (EditText) findViewById(R.id.et_relation);
        mBt_submit = (Button) findViewById(R.id.bt_submit);
    }

    private void setData() {
        mTv_title.setText("绑定项目");
        mImg_back.setOnClickListener(this);
        dataProList = new ArrayList<>();
        ProjectInfo projectInfo = new ProjectInfo();
        projectInfo.setProject_name("请选择公司");
        dataProList.add(projectInfo);
        dataInfoList = new ArrayList<>();
        dataInfoList.add(projectInfo);
        mProjAdapter = new ProSpinnerAdapter(BindProjectActivity.this, dataProList);
        mSpinner_village.setAdapter(mProjAdapter);
        //从网络获取项目
        getProjectFromServer("1");
        mSpinner_village.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //获取点击条目ID，给spinner2设置数据
                ProjectInfo projectInfo = dataProList.get(i);
                String project_name = projectInfo.getProject_name();
                if (!project_name.equals("请选择公司")) {
                    mProjectID = projectInfo.getId();
                    //to intnet
                    getDetailFromInt(mProjectID);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mDetailAdapter = new ProSpinnerAdapter(BindProjectActivity.this, dataInfoList);
        mSpinner_unit.setAdapter(mDetailAdapter);
        mSpinner_unit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ProjectInfo projectInfo = dataInfoList.get(i);
                String project_name = projectInfo.getProject_name();
                if (!project_name.equals("请选择公司")) {
                    mDetailID = projectInfo.getId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mBt_submit.setOnClickListener(this);
    }

    private void getDetailFromInt(String id) {
        String url = NetConfig.PROJECT + "?id=" + id;
        HttpOkhUtils.getInstance().doGet(url, new HttpOkhUtils.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                ToastUtils.showToast(BindProjectActivity.this, "网络请求失败");
            }

            @Override
            public void onSuccess(int code, String resbody) {
                if (code != 200) {
                    ToastUtils.showToast(BindProjectActivity.this, "网络错误");
                    return;
                }
                if (null == dataInfoList) {
                    dataInfoList = new ArrayList<>();
                } else {
                    dataInfoList.clear();
                }
                ProjectInfo projectInfo = new ProjectInfo();
                projectInfo.setProject_name("请选择公司");
                dataInfoList.add(projectInfo);
                try {
                    JSONArray jsonArray = new JSONArray(resbody);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonOb = (JSONObject) jsonArray.get(i);
                        ProjectInfo info = new ProjectInfo();
                        info.setId(jsonOb.optString("id"));
                        info.setProject_name(jsonOb.optString("fname"));
                        dataInfoList.add(info);
                    }
                    mDetailAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getProjectFromServer(String i) {
        String url = NetConfig.PROJECT + "?id=" + i;
        HttpOkhUtils.getInstance().doGet(url, new HttpOkhUtils.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                ToastUtils.showToast(BindProjectActivity.this, "网络请求失败");
            }

            @Override
            public void onSuccess(int code, String resbody) {
                if (code != 200) {
                    ToastUtils.showToast(BindProjectActivity.this, "网络错误");
                    return;
                }
                if (null == dataProList) {
                    dataProList = new ArrayList<>();
                } else {
                    dataProList.clear();
                }
                ProjectInfo projectInfo = new ProjectInfo();
                projectInfo.setProject_name("请选择公司");
                dataProList.add(projectInfo);
                Gson gson = new Gson();
                try {
                    JSONArray jsonArray = new JSONArray(resbody);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        ProjectInfo info = gson.fromJson(jsonArray.get(i).toString(), ProjectInfo.class);
                        dataProList.add(info);
                    }
                    mProjAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.bt_submit:
                mPlace = String.valueOf(mEt_place.getText()).trim();
                mRelation = String.valueOf(mEt_relation.getText()).trim();
                if ("".equals(mPlace) || "请输入房屋具体地址".equals(mPlace)) {
                    ToastUtils.showToast(BindProjectActivity.this, "请输入地址");
                    return;
                }
                if ("".equals(mRelation) || "请输入和房屋的关系".equals(mRelation)) {
                    ToastUtils.showToast(BindProjectActivity.this, "请输入关系");
                    return;
                }
                if (null == mProjectID || "".equals(mProjectID)) {
                    ToastUtils.showToast(BindProjectActivity.this, "请选择公司");
                    return;
                }
                if (null == mDetailID || "".equals(mDetailID)) {
                    ToastUtils.showToast(BindProjectActivity.this, "请选择公司详细信息");
                    return;
                }
                //提交网络
                sendToIntnet();
                break;
        }
    }

    private void sendToIntnet() {
        UserInfo userinfo = SPref.getObject(BindProjectActivity.this, UserInfo.class, "userinfo");
        String phone = userinfo.getPhone();
        String psw = userinfo.getPsw();
        String url = NetConfig.AUTHENTICATION;
        RequestParamsFM requestParams = new RequestParamsFM();
        requestParams.put("mobile", phone);
        requestParams.put("password", psw);
        requestParams.put("project_id", mProjectID);
        requestParams.put("projectdetail_id", mDetailID);
        requestParams.put("relation", mRelation);
        requestParams.put("faddress", mPlace);
        requestParams.put("img", "aa.pig");
        requestParams.setUseJsonStreamer(true);
        HttpOkhUtils.getInstance().doPost(url, requestParams, new HttpOkhUtils.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                ToastUtils.showToast(BindProjectActivity.this, "网络错误");
            }

            @Override
            public void onSuccess(int code, String resbody) {
                if (code != 200) {
                    ToastUtils.showToast(BindProjectActivity.this, "网络错误");
                    return;
                }
                ToastUtils.showToast(BindProjectActivity.this, "提交成功，等待审核");
            }
        });


        //        OkHttpClient client = new OkHttpClient.Builder()
        //                .readTimeout(10, TimeUnit.SECONDS)
        //                .build();
        //        ProjectInfo book = new ProjectInfo();
        //        Gson gson = new Gson();
        //        //使用Gson将对象转换为json字符串
        //        String json = gson.toJson(book);
        //        //MediaType  设置Content-Type 标头中包含的媒体类型值
        //        RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8")
        //                , json);
        //        Request request = new Request.Builder()
        //                .url(url)
        //                .post(requestBody)
        //                .build();
        //        Call call = client.newCall(request);
        //        call.enqueue(new Callback() {
        //            @Override
        //            public void onFailure(Call call, IOException e) {
        //
        //            }
        //
        //            @Override
        //            public void onResponse(Call call, Response response) throws IOException {
        //
        //            }
        //        });
    }
}
