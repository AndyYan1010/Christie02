package com.example.administrator.christie.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.administrator.christie.R;
import com.example.administrator.christie.adapter.TimePiontAdapter;
import com.example.administrator.christie.util.ToastUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @创建者 AndyYan
 * @创建时间 2018/4/19 14:09
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class ReservatParkingActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mImg_back;
    private TextView  mTv_title, mTv_park_time;
    private RecyclerView mRecv_stop_time;
    private Button       mBt_order;
    private int mSelected = 0;
    private List<String> mTimeData;
    private String       markData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservat_parking);
        initView();
        initData();
    }

    private void initView() {
        mImg_back = (ImageView) findViewById(R.id.img_back);
        mTv_title = (TextView) findViewById(R.id.tv_title);
        mTv_park_time = (TextView) findViewById(R.id.tv_park_time);
        mRecv_stop_time = (RecyclerView) findViewById(R.id.recv_stop_time);
        mBt_order = (Button) findViewById(R.id.bt_order);
    }

    private void initData() {
        mImg_back.setOnClickListener(this);
        mTv_title.setText("车位预约");
        mTv_park_time.setOnClickListener(this);
        mTimeData = new ArrayList();
        mTimeData.add("30分钟");
        mTimeData.add("1小时");
        mTimeData.add("2小时");
        mTimeData.add("2.5小时");
        mTimeData.add("3小时");
        mTimeData.add("3.5小时");
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecv_stop_time.setLayoutManager(layoutManager);
        TimePiontAdapter timePiontAdapter = new TimePiontAdapter(this, mTimeData, mSelected);
        mRecv_stop_time.setAdapter(timePiontAdapter);
        mBt_order.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_park_time:
                //打开时间选择器
                Calendar calendar = Calendar.getInstance();
                showDatePickerDialog(calendar, 2);
                break;
            case R.id.bt_order:
                ToastUtils.showToast(this, mTimeData.get(mSelected));
                break;
        }
    }

    public void setSelected(int num) {
        this.mSelected = num;
    }

    private void showDatePickerDialog(Calendar calendar, int themeResId) {
        new DatePickerDialog(this, themeResId, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                markData = i + "-" + (i1 + 1) + "-" + i2;
                Calendar calendar = Calendar.getInstance();
                showTimePickerDialog(calendar, 2);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void showTimePickerDialog(Calendar calendar, int themeResId) {
        new TimePickerDialog(this, themeResId, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                String fz = "" + i1;
                if (i1 < 10) {
                    fz = "0" + i1;
                }
                mTv_park_time.setText(markData + " " + i + ":" + fz);
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
    }
}
