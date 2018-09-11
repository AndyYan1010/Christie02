package com.example.administrator.christie.activity.usercenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.christie.R;
import com.example.administrator.christie.activity.BaseActivity;
import com.example.administrator.christie.activity.LoginActivity;
import com.example.administrator.christie.adapter.RcvPlateInfoAdapter;
import com.example.administrator.christie.modelInfo.PersonalPlateInfo;
import com.example.administrator.christie.modelInfo.RequestParamsFM;
import com.example.administrator.christie.modelInfo.UserInfo;
import com.example.administrator.christie.util.HttpOkhUtils;
import com.example.administrator.christie.util.SPref;
import com.example.administrator.christie.util.ToastUtils;
import com.example.administrator.christie.websiteUrl.NetConfig;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

public class PlateActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout         linear_back;
    private String               count;
    private FloatingActionButton fab_default, fab_add, fab_back;
    private TextView                mTv_title;
    private RecyclerView            mRcv_plate;
    private List<PersonalPlateInfo> mPlateData;
    private RcvPlateInfoAdapter     mPlateAdapter;
    public static int selectItem = -1;//记录选择的车牌item
    private UserInfo userinfo;
    private static int addRequestCode = 1001;
    private SmartRefreshLayout mSmt_refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plate);
        setview();
        setData();
    }

    private void setview() {
        linear_back = (LinearLayout) findViewById(R.id.linear_back);
        mSmt_refresh = (SmartRefreshLayout) findViewById(R.id.smt_refresh);
        mTv_title = (TextView) findViewById(R.id.tv_title);
        mRcv_plate = (RecyclerView) findViewById(R.id.rcv_plate);
        fab_back = (FloatingActionButton) findViewById(R.id.fab_back);
        fab_add = (FloatingActionButton) findViewById(R.id.fab_add);
        fab_default = (FloatingActionButton) findViewById(R.id.fab_refresh);
    }

    private void setData() {
        linear_back.setOnClickListener(this);
        mTv_title.setText(R.string.license);
        userinfo = SPref.getObject(PlateActivity.this, UserInfo.class, "userinfo");
        mPlateData = new ArrayList();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRcv_plate.setLayoutManager(layoutManager);
        mPlateAdapter = new RcvPlateInfoAdapter(PlateActivity.this, mPlateData);
        mRcv_plate.setAdapter(mPlateAdapter);
        //访问网络获取已有车牌信息
        getPlateInfo();
        mSmt_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                //访问网络获取已有车牌信息
                getPlateInfo();
            }
        });
        fab_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //删除车牌
                deletPlate();
            }
        });
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlateActivity.this, AddPlatenoActivity.class);
                startActivityForResult(intent, addRequestCode);
            }
        });
        fab_default.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //                getFragmentManager().beginTransaction().add(R.id.container, new CardLayoutFragment()).commit();
                //设置默认车牌 刷新
                setDefaultPlate();
            }
        });
    }

    private void setDefaultPlate() {
        if (selectItem < 0) {
            ToastUtils.showToast(PlateActivity.this, "请选择要设置默认的车牌");
        } else {
            //设置默认车牌
            PersonalPlateInfo personalPlateInfo = mPlateData.get(selectItem);
            String fisdefault = personalPlateInfo.getFisdefault();
            if ("Y".equals(fisdefault)) {
                ToastUtils.showToast(PlateActivity.this, "该车牌已是默认车牌");
                return;
            }
            String setDefPlate = NetConfig.CHANGEPLATE;
            RequestParamsFM params = new RequestParamsFM();
            params.put("id", personalPlateInfo.getId());
            params.put("userid", userinfo.getUserid());
            HttpOkhUtils.getInstance().doPost(setDefPlate, params, new HttpOkhUtils.HttpCallBack() {
                @Override
                public void onError(Request request, IOException e) {
                    ToastUtils.showToast(PlateActivity.this, "网络错误");
                }

                @Override
                public void onSuccess(int code, String resbody) {
                    if (code == 200) {
                        ToastUtils.showToast(PlateActivity.this, "设置成功");
                        selectItem = -1;
                        //访问网络获取已有车牌信息
                        getPlateInfo();
                    } else {
                        ToastUtils.showToast(PlateActivity.this, "设置失败");
                    }
                }
            });
        }
    }

    private void deletPlate() {
        if (null != userinfo) {
            if (selectItem < 0) {
                ToastUtils.showToast(PlateActivity.this, "请选择要删除的车牌");
            } else {
                //删除选择的车牌
                PersonalPlateInfo personalPlateInfo = mPlateData.get(selectItem);
                String fisdefault = personalPlateInfo.getFisdefault();
                if ("Y".equals(fisdefault)) {
                    ToastUtils.showToast(PlateActivity.this, "请先修改默认车牌再删除");
                    return;
                }
                String delPlateUrl = NetConfig.DELPLATE;
                RequestParamsFM params = new RequestParamsFM();
                params.put("id", personalPlateInfo.getId());
                params.put("userid", userinfo.getUserid());
                HttpOkhUtils.getInstance().doPost(delPlateUrl, params, new HttpOkhUtils.HttpCallBack() {
                    @Override
                    public void onError(Request request, IOException e) {
                        ToastUtils.showToast(PlateActivity.this, "网络错误");
                    }

                    @Override
                    public void onSuccess(int code, String resbody) {
                        if (code == 200) {
                            //                            Gson gson = new Gson();
                            //                            LoginInfo loginInfo = gson.fromJson(resbody, LoginInfo.class);
                            //                            String result = loginInfo.getResult();
                            //                            String message = loginInfo.getMessage();
                            //                            ToastUtils.showToast(PlateActivity.this, message);
                            //                            if ("1".equals(result)){
                            //                                mPlateData.remove(selectItem);
                            //                                mPlateAdapter.notifyDataSetChanged();
                            //                            }
                            ToastUtils.showToast(PlateActivity.this, "删除成功");
                            selectItem = -1;
                            //访问网络获取已有车牌信息
                            getPlateInfo();
                        } else {
                            ToastUtils.showToast(PlateActivity.this, "网络请求失败");
                        }
                    }
                });
            }
        } else {
            ToastUtils.showToast(PlateActivity.this, "读取账号信息失败，请退出重新登录");
        }
    }

    private void getPlateInfo() {
        if (null != userinfo) {
            String userid = userinfo.getUserid();
            String getPlateUrl = NetConfig.GETPLATE;
            RequestParamsFM params = new RequestParamsFM();
            params.put("userid", userid);
            HttpOkhUtils.getInstance().doGetWithParams(getPlateUrl, params, new HttpOkhUtils.HttpCallBack() {
                @Override
                public void onError(Request request, IOException e) {
                    mSmt_refresh.finishRefresh();
                    ToastUtils.showToast(PlateActivity.this, "网络错误");
                }

                @Override
                public void onSuccess(int code, String resbody) {
                    mSmt_refresh.setEnableRefresh(false);
                    if (code != 200) {
                        ToastUtils.showToast(PlateActivity.this, "网络请求失败，错误码" + code);
                        return;
                    }
                    if (null == mPlateData) {
                        mPlateData = new ArrayList();
                    } else {
                        mPlateData.clear();
                    }
                    Gson gson = new Gson();
                    try {
                        JSONArray jsonArray = new JSONArray(resbody);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            PersonalPlateInfo info = gson.fromJson(jsonArray.get(i).toString(), PersonalPlateInfo.class);
                            mPlateData.add(info);
                        }
                        mPlateAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            ToastUtils.showToast(PlateActivity.this, "读取错误，请重新登录");
            Intent intent = new Intent(PlateActivity.this, LoginActivity.class);
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
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (addRequestCode == requestCode) {
            selectItem = -1;
            //访问网络获取已有车牌信息
            getPlateInfo();
        }
    }
}
