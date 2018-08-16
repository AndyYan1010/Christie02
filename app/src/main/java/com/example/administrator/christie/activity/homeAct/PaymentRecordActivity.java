package com.example.administrator.christie.activity.homeAct;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.example.administrator.christie.R;
import com.example.administrator.christie.activity.BaseActivity;
import com.example.administrator.christie.fragment.PaymentRecordFragment;

/**
 * @创建者 AndyYan
 * @创建时间 2018/4/20 8:34
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class PaymentRecordActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accessdata);
        initView();
        initData();
    }

    private void initView() {

    }
    private void initData() {
        PaymentRecordFragment paymentRecordFt = new PaymentRecordFragment();
        FragmentTransaction ftt = getSupportFragmentManager().beginTransaction();
        ftt.add(R.id.frame_accessdata, paymentRecordFt, "paymentRecordFt");
        ftt.commit();
    }
}
