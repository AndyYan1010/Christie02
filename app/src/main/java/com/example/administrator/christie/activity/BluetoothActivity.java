package com.example.administrator.christie.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.christie.R;
import com.example.administrator.christie.util.ShakeHelper;

public class BluetoothActivity extends BaseActivity implements View.OnClickListener {

    private ImageView    mImg_back;
    private TextView     mTv_title;
    private ShakeHelper  mShakeHelper;
    private LinearLayout mLiner_add_equipment;
    private static final int     SENSOR_SHAKE = 10;
    private              boolean isShaking    = false;
    private ImageView mImg_shake;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        initView();
        initData();
    }

    private void initView() {
        mImg_back = (ImageView) findViewById(R.id.img_back);
        mTv_title = (TextView) findViewById(R.id.tv_title);
        mImg_shake = (ImageView) findViewById(R.id.img_shake);
        mLiner_add_equipment = (LinearLayout) findViewById(R.id.liner_add_equipment);
        mTv_title.setText("蓝牙开门");
        mImg_back.setOnClickListener(this);
        mLiner_add_equipment.setOnClickListener(this);
    }

    private void initData() {
        mShakeHelper = new ShakeHelper(this);
        mShakeHelper.Start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.liner_add_equipment:
                Intent intent = new Intent(this, AddBluetoothActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mShakeHelper) {
            mShakeHelper.Stop();
        }
    }
}
