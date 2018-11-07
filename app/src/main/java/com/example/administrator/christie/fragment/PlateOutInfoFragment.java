package com.example.administrator.christie.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.christie.InformationMessege.ProjectMsg;
import com.example.administrator.christie.R;
import com.example.administrator.christie.modelInfo.ParkPayInfo;
import com.example.administrator.christie.modelInfo.PersonalPlateInfo;
import com.example.administrator.christie.modelInfo.RequestParamsFM;
import com.example.administrator.christie.modelInfo.UserInfo;
import com.example.administrator.christie.util.HttpOkhUtils;
import com.example.administrator.christie.util.SPref;
import com.example.administrator.christie.util.ToastUtils;
import com.example.administrator.christie.view.TimeTextView;
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
 * @创建时间 2018/5/11 14:46
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class PlateOutInfoFragment extends Fragment implements View.OnClickListener {
    private View         mRootView;
    private LinearLayout linear_back;
    private ImageView    mImg_nonet;
    private TextView     mTv_title;
    private TextView     tv_pay4others;
    //    private List         mDatalist;
    //    private Spinner      mSpinner;
    //    private ProSpinnerAdapter mProjAdapter;//选择车牌适配器
    private TextView     mTv_nodata, tv_plateno, mTv_plate, tv_place, mTv_enter_time, mTv_out_time, mTv_state, mTv_explain, mTv_price, mTv_submit;
    private TimeTextView     tv_freetime;
    private List<ProjectMsg> dataPlateList;//车牌数据
    private LinearLayout     mLinear_detail;
    private LinearLayout     mLinear_lock;
    private String           mUserid;
    private ParkPayInfo      mParkPayInfo;
    private String           mPlateNo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_plate_out, container, false);
        initView();
        initData();
        return mRootView;
    }

    private void initView() {
        linear_back = (LinearLayout) mRootView.findViewById(R.id.linear_back);
        mImg_nonet = mRootView.findViewById(R.id.img_nonet);
        mTv_title = mRootView.findViewById(R.id.tv_title);
        tv_plateno = mRootView.findViewById(R.id.tv_plateno);
        tv_pay4others = mRootView.findViewById(R.id.tv_pay4others);
        mTv_nodata = mRootView.findViewById(R.id.tv_nodata);
        //        mSpinner = mRootView.findViewById(R.id.spinner);//车牌选择器
        mLinear_detail = mRootView.findViewById(R.id.linear_detail);//收费详细信息
        mTv_plate = mRootView.findViewById(R.id.tv_plate);
        tv_place = mRootView.findViewById(R.id.tv_place);//停车场
        tv_freetime = mRootView.findViewById(R.id.tv_freetime);//剩余免费离场时长
        mTv_enter_time = mRootView.findViewById(R.id.tv_enter_time);
        mTv_out_time = mRootView.findViewById(R.id.tv_out_time);
        mLinear_lock = mRootView.findViewById(R.id.linear_lock);//车位锁定信息
        mTv_state = mRootView.findViewById(R.id.tv_state);
        mTv_explain = mRootView.findViewById(R.id.tv_explain);
        mTv_price = mRootView.findViewById(R.id.tv_price);     //停车费用
        mTv_submit = mRootView.findViewById(R.id.tv_submit);
        //mLv_plate = mRootView.findViewById(R.id.lv_plate);
    }

    private void initData() {
        linear_back.setOnClickListener(this);
        mTv_title.setText("停车缴费");
        tv_plateno.setText(mPlateNo);
        //测试 缴费。
        //testPay();

        //设置车牌选择器
        //        dataPlateList = new ArrayList();
        //        ProjectMsg projectInfo = new ProjectMsg();
        //        projectInfo.setProject_name("请选择车牌");
        //        dataPlateList.add(projectInfo);
        //        mProjAdapter = new ProSpinnerAdapter(getContext(), dataPlateList);
        //        mSpinner.setAdapter(mProjAdapter);
        //        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        //            @Override
        //            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        //                ProjectMsg msg = dataPlateList.get(i);
        //                String project_name = msg.getProject_name();
        //                if (!"请选择车牌".equals(project_name)) {
        //                    //获取停车缴费数据
        //                    mPlateNo = project_name;
        //                    tv_plateno.setText(project_name);
        //                    getParkingPayInfo(project_name);
        //                }
        //            }
        //
        //            @Override
        //            public void onNothingSelected(AdapterView<?> adapterView) {
        //
        //            }
        //        });
        UserInfo userinfo = SPref.getObject(getContext(), UserInfo.class, "userinfo");
        mUserid = userinfo.getUserid();
        //从网络获取个人车牌
        //        choosePlate(mUserid);
        mTv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String kind = String.valueOf(mTv_submit.getText());
                if ("付费".equals(kind)) {
                    //跳转付费界面
                    FragmentTransaction ftt = getFragmentManager().beginTransaction();
                    PayForPackingFragment payPackFragment = new PayForPackingFragment();
                    payPackFragment.setParkPayInfo(mParkPayInfo);
                    payPackFragment.setPlateNo(mPlateNo);
                    ftt.add(R.id.frame_pay, payPackFragment, "payPackFragment");
                    ftt.addToBackStack(null);
                    ftt.commit();
                }
            }
        });
        tv_pay4others.setOnClickListener(this);
        //获取停车缴费信息
        getParkingPayInfo(mPlateNo);
    }

    private void testPay() {
        //跳转付费界面
        FragmentTransaction ftt = getFragmentManager().beginTransaction();
        PayForPackingFragment payPackFragment = new PayForPackingFragment();
        payPackFragment.setParkPayInfo(mParkPayInfo);
        payPackFragment.setPlateNo(mPlateNo);
        ftt.add(R.id.frame_pay, payPackFragment, "payPackFragment");
        ftt.addToBackStack(null);
        ftt.commit();
        return;
    }

    private void choosePlate(String userid) {
        //访问网络获取个人车牌
        String userPlateUrl = NetConfig.GETPLATE;
        RequestParamsFM params = new RequestParamsFM();
        params.put("userid", userid);
        HttpOkhUtils.getInstance().doGetWithParams(userPlateUrl, params, new HttpOkhUtils.HttpCallBack() {
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
                if (null == dataPlateList) {
                    dataPlateList = new ArrayList();
                } else {
                    dataPlateList.clear();
                }
                ProjectMsg projectInfo = new ProjectMsg();
                projectInfo.setProject_name("请选择车牌");
                dataPlateList.add(projectInfo);
                Gson gson = new Gson();
                try {
                    JSONArray jsonArray = new JSONArray(resbody);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        PersonalPlateInfo info = gson.fromJson(jsonArray.get(i).toString(), PersonalPlateInfo.class);
                        String fplateno = info.getFplateno();
                        ProjectMsg plateInfo = new ProjectMsg();
                        plateInfo.setProject_name(fplateno);
                        dataPlateList.add(plateInfo);
                    }
                    //                    mProjAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getParkingPayInfo(final String plateno) {
        String payInfoUrl = NetConfig.PARKINGSEARCH;
        RequestParamsFM params = new RequestParamsFM();
        params.put("plateNo", plateno);
        HttpOkhUtils.getInstance().doPost(payInfoUrl, params, new HttpOkhUtils.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                ToastUtils.showToast(getContext(), "网络错误");
            }

            @Override
            public void onSuccess(int code, String resbody) {
                if (code == 200) {
                    Gson gson = new Gson();
                    mParkPayInfo = gson.fromJson(resbody, ParkPayInfo.class);
                    String resCode = mParkPayInfo.getResCode();
                    if ("0".equals(resCode)) {
                        mLinear_detail.setVisibility(View.VISIBLE);
                        final String plateNo = plateno;
                        double amount = mParkPayInfo.getAmount();
                        final String fstatus = mParkPayInfo.getFstatus();
                        String inTime = mParkPayInfo.getInTime();
                        String outTime = mParkPayInfo.getOutTime();
                        mTv_plate.setText(plateNo);
                        mTv_enter_time.setText(inTime);
                        mTv_out_time.setText(outTime);
                        if ("0".equals(fstatus)) {
                            mTv_state.setText("未锁定");
                            mTv_state.setTextColor(Color.BLACK);
                            mTv_explain.setVisibility(View.GONE);
                            mTv_submit.setText("付费");
                        } else {
                            mTv_state.setText("已锁定");
                            mTv_state.setTextColor(getResources().getColor(R.color.colorAccent));
                            mTv_explain.setVisibility(View.VISIBLE);
                            mTv_submit.setText("请先解锁");
                        }
                        mTv_price.setText("¥" + amount);
                        tv_place.setText(mParkPayInfo.getParkname());
                        mLinear_lock.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (!"0".equals(fstatus)) {
                                    //解锁
                                    changeLockPlate("0", plateNo);
                                }
                            }
                        });
                        try {
                            int times = Integer.parseInt(mParkPayInfo.getFresstime());
                            long milltimes = times * 60 * 1000;
                            tv_freetime.setTimes(milltimes);
                        } catch (Exception e) {
                            tv_freetime.setText("获取失败");
                        }
                    } else {
                        mLinear_detail.setVisibility(View.GONE);//
                        ToastUtils.showToast(getContext(), "未查到需交费车辆");
                    }
                }
            }
        });
    }

    private void changeLockPlate(String state, final String plate) {
        String changeLockUrl = NetConfig.LOCKPLATE;
        RequestParamsFM params = new RequestParamsFM();
        params.put("userid", mUserid);
        params.put("plateno", plate);
        params.put("fstatus", state);
        HttpOkhUtils.getInstance().doPost(changeLockUrl, params, new HttpOkhUtils.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                ToastUtils.showToast(getContext(), "网络错误");
            }

            @Override
            public void onSuccess(int code, String resbody) {
                if (code == 200) {
                    ToastUtils.showToast(getContext(), "修改成功");
                    //改变付费信息
                    mTv_state.setText("未锁定");
                    mTv_state.setTextColor(Color.BLACK);
                    mTv_explain.setVisibility(View.GONE);
                    mTv_submit.setText("付费");
                    mTv_submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String kind = String.valueOf(mTv_submit.getText());
                            if ("付费".equals(kind)) {
                                //跳转付费界面
                                FragmentTransaction ftt = getFragmentManager().beginTransaction();
                                PayForPackingFragment payPackFragment = new PayForPackingFragment();
                                payPackFragment.setParkPayInfo(mParkPayInfo);
                                payPackFragment.setPlateNo(plate);
                                ftt.add(R.id.frame_pay, payPackFragment, "payPackFragment");
                                ftt.addToBackStack(null);
                                ftt.commit();
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.linear_back:
                getFragmentManager().popBackStackImmediate(null, 0);
                break;
            case R.id.tv_pay4others://为其他车辆付费
                //跳转查询其他车辆界面
                FragmentTransaction ftt = getFragmentManager().beginTransaction();
                SearchPay4OtherCarsFragment pay4otherFragment = new SearchPay4OtherCarsFragment();
                pay4otherFragment.setUpFragment(this);
                ftt.add(R.id.frame_pay, pay4otherFragment, "pay4otherFragment");
                ftt.addToBackStack(null);
                ftt.commit();
                break;
        }
    }

    public void setPlateno(String plateNo) {
        mPlateNo = plateNo;
    }

    public void setPlatenoAndSearch(String plateNo) {
        mPlateNo = plateNo;
        //更新界面
        tv_plateno.setText(mPlateNo);
        getParkingPayInfo(plateNo);
    }
}
