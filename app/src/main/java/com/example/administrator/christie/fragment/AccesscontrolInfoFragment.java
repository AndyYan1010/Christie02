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

import java.util.Calendar;

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
                //获取开始时间 和结束时间，跳转数据结果
                FragmentTransaction ftt = getFragmentManager().beginTransaction();
                AccessInfoResultFragment infoResultFragment = new AccessInfoResultFragment();
                String startT = String.valueOf(mTv_start_time.getText());
                String endT = String.valueOf(mTv_end_time.getText());
                infoResultFragment.setData(startT, endT);
                ftt.add(R.id.frame_accessdata, infoResultFragment, "infoResultFragment");
                ftt.addToBackStack(null);
                ftt.commit();
                break;
        }
    }

    private void showDatePickerDialog(Calendar calendar, int themeResId, final int kind) {
        new DatePickerDialog(getContext(), themeResId, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                if (kind == 1)
                    mTv_start_time.setText("" + i + "-" + (i1 + 1) + "-" + i2);
                if (kind == 2)
                    mTv_end_time.setText("" + i + "-" + (i1 + 1) + "-" + i2);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
}
