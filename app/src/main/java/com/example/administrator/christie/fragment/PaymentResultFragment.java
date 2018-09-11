package com.example.administrator.christie.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.christie.R;
import com.example.administrator.christie.adapter.LVAccessInfoAdapter;
import com.example.administrator.christie.modelInfo.PayRecordInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @创建者 AndyYan
 * @创建时间 2018/4/20 8:51
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class PaymentResultFragment extends Fragment implements View.OnClickListener {
    private LinearLayout linear_back;
    private View         mRootView;
    private String       mstartT, mendT;
    private TextView mTv_title, mTv_st_end, mTv_date, mTv_door_name, mTv_area, mTv_nodata;
    private ListView  mLv_result;
    private List<PayRecordInfo.ListBean> mPayInfoList = new ArrayList();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_access_info_result, container, false);
        initView();
        initData();
        return mRootView;
    }

    private void initView() {
        linear_back = (LinearLayout) mRootView.findViewById(R.id.linear_back);
        mTv_title = (TextView) mRootView.findViewById(R.id.tv_title);
        mTv_st_end = (TextView) mRootView.findViewById(R.id.tv_st_end);
        mTv_nodata = (TextView) mRootView.findViewById(R.id.tv_nodata);
        mTv_date = (TextView) mRootView.findViewById(R.id.tv_date);
        mTv_door_name = (TextView) mRootView.findViewById(R.id.tv_door_name);
        mTv_area = (TextView) mRootView.findViewById(R.id.tv_area);
        mLv_result = (ListView) mRootView.findViewById(R.id.lv_result);
    }

    private void initData() {
        mTv_title.setText("缴费记录结果");
        linear_back.setOnClickListener(this);
        mTv_st_end.setText("下方数据为" + mstartT + "至" + mendT + "的数据");
        mTv_date.setText("缴费日期");
        mTv_door_name.setText("支付方式");
        mTv_area.setText("金额");
        if (mPayInfoList.size() > 0) {
            mTv_nodata.setVisibility(View.GONE);
        }
        LVAccessInfoAdapter accessInfoAdapter = new LVAccessInfoAdapter(getContext(), mPayInfoList, 1);
        mLv_result.setAdapter(accessInfoAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.linear_back:
                //弹出回退栈最上面的fragment
                getFragmentManager().popBackStackImmediate(null, 0);
                break;
        }
    }

    public void setData(String startTime, String endTime) {
        this.mstartT = startTime;
        this.mendT = endTime;
    }

    public List getPayInfoList() {
        return mPayInfoList;
    }
}
