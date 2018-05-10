package com.example.administrator.christie.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.christie.R;
import com.example.administrator.christie.modelInfo.MenjinInfo;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.Request;

/**
 * @创建者 AndyYan
 * @创建时间 2018/4/19 8:36
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class AccesscontrolInfoFragment extends Fragment implements View.OnClickListener {
    private View     mRootView;
    private TextView mTv_title, mTv_start_time, mTv_end_time;
    private ImageView mImg_back, mImg_select_st, mImg_select_end;
    private Button mBt_search;
    private String mkind;
    private String mStartTime;//记录开始时间
    private String mEndTime;//记录结束时间

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_access_control, container, false);
        initView();
        initData();
        return mRootView;
    }

    private void initView() {
        mImg_back = (ImageView) mRootView.findViewById(R.id.img_back);
        mTv_title = (TextView) mRootView.findViewById(R.id.tv_title);
        mTv_start_time = (TextView) mRootView.findViewById(R.id.tv_start_time);
        mTv_end_time = (TextView) mRootView.findViewById(R.id.tv_end_time);
        mImg_select_st = (ImageView) mRootView.findViewById(R.id.img_select_st);
        mImg_select_end = (ImageView) mRootView.findViewById(R.id.img_select_end);
        mBt_search = (Button) mRootView.findViewById(R.id.bt_search);
        mTv_title.setText("门禁数据");
        mImg_back.setOnClickListener(this);
        mImg_select_st.setOnClickListener(this);
        mImg_select_end.setOnClickListener(this);
        mBt_search.setOnClickListener(this);
    }

    private void initData() {
        //获取当前日期
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String data = simpleDateFormat.format(new Date());
        mTv_start_time.setText(data);
        mTv_end_time.setText(data);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                getActivity().finish();
                break;
            case R.id.img_select_st:
                //打开时间选择器
                Calendar calendar = Calendar.getInstance();
                showDatePickerDialog(calendar, 2, 1);
                break;
            case R.id.img_select_end:
                //打开时间选择器
                Calendar calendar2 = Calendar.getInstance();
                showDatePickerDialog(calendar2, 2, 2);
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
        String mjInfoUrl = NetConfig.EGDETAIL;
        RequestParamsFM params = new RequestParamsFM();
        params.put("id", userid);
        params.put("starttime", startT);
        params.put("endtime", endT);
        HttpOkhUtils.getInstance().doGetWithParams(mjInfoUrl, params, new HttpOkhUtils.HttpCallBack() {
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
                    try {
                        //获取开始时间 和结束时间，跳转数据结果
                        FragmentTransaction ftt = getFragmentManager().beginTransaction();
                        AccessInfoResultFragment infoResultFragment = new AccessInfoResultFragment();
                        List menjinInfoList = infoResultFragment.getMenjinInfoList();
                        JSONArray jsonArray = new JSONArray(resbody);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            MenjinInfo menjinInfo = gson.fromJson(jsonArray.get(i).toString(), MenjinInfo.class);
                            menjinInfoList.add(menjinInfo);
                        }
                        infoResultFragment.setData(startT, endT);
                        ftt.add(R.id.frame_accessdata, infoResultFragment, "infoResultFragment");
                        ftt.addToBackStack(null);
                        ftt.commit();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    ToastUtils.showToast(getContext(), "网络错误,错误码:" + code);
                }
            }
        });
    }

    private void showDatePickerDialog(Calendar calendar, int themeResId, final int kind) {
        new DatePickerDialog(getContext(), themeResId, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                int iy = i1 + 1;
                String yue = "" + iy;
                String ri = "" + i2;
                if (iy < 10) {
                    yue = "0" + iy;
                }
                if (i2 < 10) {
                    ri = "0" + i2;
                }
                if (kind == 1) {
                    mTv_start_time.setText("" + i + "-" + yue + "-" + ri);
                    //                    mStartTime = "" + i + "/" + yue + "/" + ri;
                }
                if (kind == 2) {
                    mTv_end_time.setText("" + i + "-" + yue + "-" + ri);
                    //                    mEndTime = "" + i + "/" + yue + "/" + ri;
                }
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
}
