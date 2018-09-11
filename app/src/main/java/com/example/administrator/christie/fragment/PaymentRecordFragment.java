package com.example.administrator.christie.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.christie.R;
import com.example.administrator.christie.modelInfo.MenjinInfo;
import com.example.administrator.christie.modelInfo.PayRecordInfo;
import com.example.administrator.christie.modelInfo.RequestParamsFM;
import com.example.administrator.christie.modelInfo.UserInfo;
import com.example.administrator.christie.util.HttpOkhUtils;
import com.example.administrator.christie.util.SPref;
import com.example.administrator.christie.util.ToastUtils;
import com.example.administrator.christie.view.CustomDatePicker;
import com.example.administrator.christie.websiteUrl.NetConfig;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import okhttp3.Request;

/**
 * @创建者 AndyYan
 * @创建时间 2018/4/20 8:42
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class PaymentRecordFragment extends Fragment implements View.OnClickListener {
    private View     mRootView;
    private LinearLayout linear_back;
    private TextView mTv_title, mTv_start_time, mTv_end_time;
    private ImageView mImg_select_st, mImg_select_end;
    private Button mBt_search;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_access_control, container, false);
        initView();
        initData();
        return mRootView;
    }

    private void initView() {
        linear_back = (LinearLayout) mRootView.findViewById(R.id.linear_back);
        mTv_title = (TextView) mRootView.findViewById(R.id.tv_title);
        mTv_start_time = (TextView) mRootView.findViewById(R.id.tv_start_time);
        mTv_end_time = (TextView) mRootView.findViewById(R.id.tv_end_time);
        mImg_select_st = (ImageView) mRootView.findViewById(R.id.img_select_st);
        mImg_select_end = (ImageView) mRootView.findViewById(R.id.img_select_end);
        mBt_search = (Button) mRootView.findViewById(R.id.bt_search);
        mTv_title.setText("缴费记录");
        linear_back.setOnClickListener(this);
        mImg_select_st.setOnClickListener(this);
        mImg_select_end.setOnClickListener(this);
        mBt_search.setOnClickListener(this);

    }

    private void initData() {
        //获取当前日期
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        String data = simpleDateFormat.format(new Date());
        mTv_start_time.setText(data);
        mTv_end_time.setText(data);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.linear_back:
                getActivity().finish();
                break;
            case R.id.img_select_st:
                //打开时间选择器
                CustomDatePicker dpk1 = new CustomDatePicker(getContext(), new CustomDatePicker.ResultHandler() {
                    @Override
                    public void handle(String time) { // 回调接口，获得选中的时间
                        mTv_start_time.setText(time);
                    }
                }, "2010-01-01 00:00", "2090-12-31 00:00"); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
                dpk1.showSpecificTime(true); // 显示时和分
                dpk1.setIsLoop(true); // 允许循环滚动
                dpk1.show(mTv_start_time.getText().toString());
                break;
            case R.id.img_select_end:
                //打开时间选择器
                CustomDatePicker dpk = new CustomDatePicker(getContext(), new CustomDatePicker.ResultHandler() {
                    @Override
                    public void handle(String time) { // 回调接口，获得选中的时间
                        mTv_end_time.setText(time);
                    }
                }, "2010-01-01 00:00", "2090-12-31 00:00"); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
                dpk.showSpecificTime(true); // 显示时和分
                dpk.setIsLoop(true); // 允许循环滚动
                dpk.show(mTv_end_time.getText().toString());
                break;
            case R.id.bt_search:
                String startT = String.valueOf(mTv_start_time.getText());
                String endT = String.valueOf(mTv_end_time.getText());
                //获取数据
                getMenjinInfo(startT, endT);
                break;
        }
    }

    private void getMenjinInfo(final String startT, final String endT) {
        UserInfo userinfo = SPref.getObject(getContext(), UserInfo.class, "userinfo");
        String userid = userinfo.getUserid();
        String jfInfoUrl = NetConfig.PAYRECORD;
        RequestParamsFM params = new RequestParamsFM();
        params.put("userid", userid);
        params.put("starttime", startT);
        params.put("endtime", endT);
        HttpOkhUtils.getInstance().doGetWithParams(jfInfoUrl, params, new HttpOkhUtils.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                ToastUtils.showToast(getContext(), "网络错误");
            }

            @Override
            public void onSuccess(int code, String resbody) {
                if (code == 200) {
                    ToastUtils.showToast(getContext(), "网络请求成功");
                    //解析返回数据
                    Gson gson = new Gson();
                    PayRecordInfo payRecordInfo = gson.fromJson(resbody, PayRecordInfo.class);
                    List<PayRecordInfo.ListBean> list = payRecordInfo.getList();
                    //获取开始时间 和结束时间，跳转数据结果
                    FragmentTransaction ftt = getFragmentManager().beginTransaction();
                    PaymentResultFragment paymentResultFgt = new PaymentResultFragment();
                    paymentResultFgt.setData(startT, endT);
                    List payInfoList = paymentResultFgt.getPayInfoList();
                    for (PayRecordInfo.ListBean bean : list) {
                        double amount = bean.getAmount();
                        int paycode = bean.getPaycode();
                        String time = bean.getTime();
                        MenjinInfo menjinInfo = new MenjinInfo();
                        menjinInfo.setAmount(amount);
                        menjinInfo.setTime(time);
                        menjinInfo.setPaycode(paycode);
                        payInfoList.add(menjinInfo);
                    }
                    ftt.add(R.id.frame_accessdata, paymentResultFgt, "invitationResultFragment");
                    ftt.addToBackStack(null);
                    ftt.commit();
                }
            }
        });
    }
}
