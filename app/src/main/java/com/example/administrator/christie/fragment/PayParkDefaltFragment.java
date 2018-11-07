package com.example.administrator.christie.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.christie.R;
import com.example.administrator.christie.activity.LoginActivity;
import com.example.administrator.christie.adapter.LvCarNoAdapter;
import com.example.administrator.christie.modelInfo.PersonalPlateInfo;
import com.example.administrator.christie.modelInfo.RequestParamsFM;
import com.example.administrator.christie.modelInfo.UserInfo;
import com.example.administrator.christie.util.HttpOkhUtils;
import com.example.administrator.christie.util.SPref;
import com.example.administrator.christie.util.ToastUtils;
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
 * @创建时间 2018/11/7 15:34
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class PayParkDefaltFragment extends Fragment implements View.OnClickListener {
    private View                    mRootView;
    private LinearLayout               linear_back;
    private TextView                tv_title;
    private LinearLayout            lin_nomsg;
    private ListView                lv_all_car;
    private List<PersonalPlateInfo> mCarNoData;
    private LvCarNoAdapter          carNoAdapter;
    private UserInfo                userinfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_paypark_defalt, container, false);
        initView();
        initData();
        return mRootView;
    }

    private void initView() {
        linear_back = mRootView.findViewById(R.id.linear_back);
        tv_title = mRootView.findViewById(R.id.tv_title);
        lin_nomsg = mRootView.findViewById(R.id.lin_nomsg);
        lv_all_car = mRootView.findViewById(R.id.lv_all_car);
    }

    private void initData() {
        linear_back.setVisibility(View.VISIBLE);
        linear_back.setOnClickListener(this);
        tv_title.setText("停车缴费");
        mCarNoData = new ArrayList();
        carNoAdapter = new LvCarNoAdapter(getContext(), mCarNoData);
        lv_all_car.setAdapter(carNoAdapter);
        userinfo = SPref.getObject(getContext(), UserInfo.class, "userinfo");
        //获取个人车牌号
        getPersonalCarNo();
        lv_all_car.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //跳转查询缴费界面
                FragmentTransaction ftt = getFragmentManager().beginTransaction();
                PlateOutInfoFragment plateOutFragment = new PlateOutInfoFragment();
                plateOutFragment.setPlateno(mCarNoData.get(i).getFplateno());
                ftt.add(R.id.frame_pay, plateOutFragment, "plateOutFragment");
                ftt.addToBackStack(null);
                ftt.commit();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.linear_back:
                getActivity().finish();
                break;
        }
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
                    ToastUtils.showToast(getContext(), "网络错误");
                }

                @Override
                public void onSuccess(int code, String resbody) {
                    if (code != 200) {
                        ToastUtils.showToast(getContext(), "网络请求失败，错误码" + code);
                        return;
                    }
                    if (null == mCarNoData) {
                        mCarNoData = new ArrayList();
                    } else {
                        mCarNoData.clear();
                    }
                    lin_nomsg.setVisibility(View.VISIBLE);
                    Gson gson = new Gson();
                    try {
                        JSONArray jsonArray = new JSONArray(resbody);
                        if (jsonArray.length() > 0) {
                            lin_nomsg.setVisibility(View.INVISIBLE);
                        }
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
            ToastUtils.showToast(getContext(), "读取错误，请重新登录");
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
    }
}
