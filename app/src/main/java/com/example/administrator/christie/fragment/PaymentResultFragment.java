package com.example.administrator.christie.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.christie.R;
import com.example.administrator.christie.adapter.LVAccessInfoAdapter;

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

    private View   mRootView;
    private String mstartT, mendT;
    private TextView mTv_title, mTv_st_end;
    private ImageView mImg_back;
    private ListView  mLv_result;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_access_info_result, container, false);
        initView();
        initData();
        return mRootView;
    }

    private void initView() {
        mTv_title = (TextView) mRootView.findViewById(R.id.tv_title);
        mTv_st_end = (TextView) mRootView.findViewById(R.id.tv_st_end);
        mImg_back = (ImageView) mRootView.findViewById(R.id.img_back);
        mLv_result = (ListView) mRootView.findViewById(R.id.lv_result);
    }

    private void initData() {
        mTv_title.setText("缴费记录结果");
        mImg_back.setOnClickListener(this);
        mTv_st_end.setText("下方数据为" + mstartT + "至" + mendT + "的数据");
        List mData = new ArrayList();
        mData.add("1");
        mData.add("1");
        mData.add("1");
        mData.add("1");
        mData.add("1");
        LVAccessInfoAdapter accessInfoAdapter = new LVAccessInfoAdapter(getContext(), mData);
        mLv_result.setAdapter(accessInfoAdapter);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                //弹出回退栈最上面的fragment
                getFragmentManager().popBackStackImmediate(null, 0);
                break;
        }
    }

    public void setData(String startTime, String endTime) {
        this.mstartT = startTime;
        this.mendT = endTime;
    }
}
