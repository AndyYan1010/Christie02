package com.example.administrator.christie.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.christie.R;
import com.example.administrator.christie.fragment.AccesscontrolInfoFragment;

public class AccessdataActivity extends BaseActivity {

    private TextView  mTv_title;
    private ImageView mImg_back;

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
        Fragment accessdataFt = new AccesscontrolInfoFragment();
        FragmentTransaction ftt = getSupportFragmentManager().beginTransaction();
        ftt.add(R.id.frame_accessdata, accessdataFt, "accessdataFt");
        ftt.commit();
    }

}
