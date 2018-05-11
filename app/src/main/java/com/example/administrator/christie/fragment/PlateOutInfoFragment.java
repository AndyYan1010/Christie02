package com.example.administrator.christie.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.christie.R;
import com.example.administrator.christie.adapter.OutPlateInfoAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @创建者 AndyYan
 * @创建时间 2018/5/11 14:46
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class PlateOutInfoFragment extends Fragment implements View.OnClickListener {
    private View      mRootView;
    private ImageView mImg_back;
    private TextView  mTv_title;
    private ListView  mLv_plate;
    private List      mDatalist;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_plate_out, container, false);
        initView();
        initData();
        return mRootView;
    }

    private void initView() {
        mImg_back = (ImageView) mRootView.findViewById(R.id.img_back);
        mTv_title = (TextView) mRootView.findViewById(R.id.tv_title);
        mLv_plate = mRootView.findViewById(R.id.lv_plate);
    }

    private void initData() {
        mImg_back.setOnClickListener(this);
        mTv_title.setText("停车缴费");
        mDatalist = new ArrayList();
        mDatalist.add("");
        OutPlateInfoAdapter adapter = new OutPlateInfoAdapter(getContext(), mDatalist);
        mLv_plate.setAdapter(adapter);
        mLv_plate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mDatalist.get(i);
                FragmentTransaction ftt = getFragmentManager().beginTransaction();
                PayForPackingFragment payPackFragment = new PayForPackingFragment();
                ftt.add(R.id.frame_pay, payPackFragment, "payPackFragment");
                ftt.addToBackStack(null);
                ftt.commit();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                getActivity().finish();
                break;
        }
    }
}
