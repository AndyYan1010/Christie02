package com.example.administrator.christie.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.christie.R;
import com.example.administrator.christie.adapter.InvitationRecyViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @创建者 AndyYan
 * @创建时间 2018/4/19 13:13
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class InvitationResultFragment extends Fragment implements View.OnClickListener {
    private View   mRootView;
    private String mstartT, mendT;
    private TextView mTv_title, mTv_st_end;
    private ImageView mImg_back;
    private RecyclerView mRecview_result;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_invitation_result, container, false);
        initView();
        initData();
        return mRootView;
    }


    private void initView() {
        mImg_back = (ImageView) mRootView.findViewById(R.id.img_back);
        mTv_title = (TextView) mRootView.findViewById(R.id.tv_title);
        mTv_st_end = (TextView) mRootView.findViewById(R.id.tv_st_end);
        mRecview_result = (RecyclerView) mRootView.findViewById(R.id.recview_result);
    }

    private void initData() {
        mTv_title.setText("门禁数据结果");
        mImg_back.setOnClickListener(this);
        mTv_st_end.setText("下方数据为" + mstartT + "至" + mendT + "的数据");
        List mData= new ArrayList();
        mData.add("1");
        mData.add("1");
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        mRecview_result.setLayoutManager(layoutManager);
        InvitationRecyViewAdapter recyViewAdapter = new InvitationRecyViewAdapter(getContext(),mData);
        mRecview_result.setAdapter(recyViewAdapter);
    }

    public void setData(String startTime, String endTime) {
        this.mstartT = startTime;
        this.mendT = endTime;
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
}
