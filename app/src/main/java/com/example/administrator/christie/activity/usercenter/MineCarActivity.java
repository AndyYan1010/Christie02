package com.example.administrator.christie.activity.usercenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.christie.R;
import com.example.administrator.christie.activity.BaseActivity;
import com.example.administrator.christie.activity.LoginActivity;
import com.example.administrator.christie.adapter.LvCarNoAdapter;
import com.example.administrator.christie.modelInfo.PersonalPlateInfo;
import com.example.administrator.christie.modelInfo.RequestParamsFM;
import com.example.administrator.christie.modelInfo.UserInfo;
import com.example.administrator.christie.util.HttpOkhUtils;
import com.example.administrator.christie.util.SPref;
import com.example.administrator.christie.util.ToastUtils;
import com.example.administrator.christie.view.MyListView;
import com.example.administrator.christie.websiteUrl.NetConfig;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

/**
 * @创建者 AndyYan
 * @创建时间 2018/10/12 11:12
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class MineCarActivity extends BaseActivity implements View.OnClickListener {
    private TextView                tv_title;
    private LinearLayout            linear_back;
    private MyListView              lv_carno;
    private RelativeLayout          relative_bind;//添加车牌
    private UserInfo                userinfo;
    private List<PersonalPlateInfo> mCarNoData;
    private LvCarNoAdapter          carNoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_carno);
        initView();
        initData();
    }

    private void initView() {
        linear_back = (LinearLayout) findViewById(R.id.linear_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        lv_carno = (MyListView) findViewById(R.id.lv_carno);
        relative_bind = (RelativeLayout) findViewById(R.id.relative_bind);
    }

    private void initData() {
        userinfo = SPref.getObject(this, UserInfo.class, "userinfo");
        tv_title.setText("我的车辆");
        linear_back.setOnClickListener(this);
        relative_bind.setOnClickListener(this);
        mCarNoData = new ArrayList();
        carNoAdapter = new LvCarNoAdapter(this, mCarNoData);
        lv_carno.setAdapter(carNoAdapter);
        //获取个人车牌号
        getPersonalCarNo();
    }

    private void getPersonalCarNo() {
        if (null != userinfo) {
            String userid = userinfo.getUserid();
            String getPlateUrl = NetConfig.GETPLATE;
            RequestParamsFM params = new RequestParamsFM();
            params.put("userid", userid);
            HttpOkhUtils.getInstance().doGetWithParams(getPlateUrl, params, new HttpOkhUtils.HttpCallBack() {
                @Override
                public void onError(Request request, IOException e) {
                    ToastUtils.showToast(MineCarActivity.this, "网络错误");
                }

                @Override
                public void onSuccess(int code, String resbody) {
                    if (code != 200) {
                        ToastUtils.showToast(MineCarActivity.this, "网络请求失败，错误码" + code);
                        return;
                    }
                    if (null == mCarNoData) {
                        mCarNoData = new ArrayList();
                    } else {
                        mCarNoData.clear();
                    }
                    Gson gson = new Gson();
                    try {
                        JSONArray jsonArray = new JSONArray(resbody);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            PersonalPlateInfo info = gson.fromJson(jsonArray.get(i).toString(), PersonalPlateInfo.class);
                            mCarNoData.add(info);
                        }
                        carNoAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            ToastUtils.showToast(MineCarActivity.this, "读取错误，请重新登录");
            Intent intent = new Intent(MineCarActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.linear_back:
                finish();
                break;
            case R.id.relative_bind:
                //跳转添加车牌界面
                startActivity(new Intent(this, AddCarNoActivity.class));
                break;
        }
    }
}
