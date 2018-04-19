package com.example.administrator.christie.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.christie.R;
import com.example.administrator.christie.util.ToastUtils;

/**
 * @创建者 AndyYan
 * @创建时间 2018/4/19 15:49
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class PayForParkingActivity extends BaseActivity implements View.OnClickListener {
    private ImageView mImg_back;
    private TextView  mTv_title;
    private Button    mBt_pay;
    private CheckBox  mCb_weixin, mCb_zfb;
    private int payKind = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payfor_parking);
        initView();
        initData();
    }

    private void initView() {
        mImg_back = (ImageView) findViewById(R.id.img_back);
        mTv_title = (TextView) findViewById(R.id.tv_title);
        mCb_weixin = (CheckBox) findViewById(R.id.cb_weixin);
        mCb_zfb = (CheckBox) findViewById(R.id.cb_zfb);
        mBt_pay = (Button) findViewById(R.id.bt_pay);
    }

    private void initData() {
        mImg_back.setOnClickListener(this);
        mTv_title.setText("停车缴费");

        mCb_weixin.setOnClickListener(this);
        mCb_zfb.setOnClickListener(this);
        mBt_pay.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.cb_weixin:
                if (mCb_zfb.isChecked())
                    mCb_zfb.setChecked(false);
                if (mCb_weixin.isChecked()){
                    payKind = 1;
                }else {
                    payKind = 0;
                }
                break;
            case R.id.cb_zfb:
                if (mCb_weixin.isChecked())
                    mCb_weixin.setChecked(false);
                if (mCb_zfb.isChecked()){
                    payKind = 2;
                }else {
                    payKind = 0;
                }
                break;
            case R.id.bt_pay:
                if (payKind==0){
                    ToastUtils.showToast(this,"请选择支付方式");
                    return;
                }
                if (payKind==1){
                    ToastUtils.showToast(this,"您选择了微信支付");

                }
                if (payKind==2){
                    ToastUtils.showToast(this,"你选择了支付宝支付");

                }
                break;
        }
    }
}
