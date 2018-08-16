package com.example.administrator.christie.activity.homeAct;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.example.administrator.christie.R;
import com.example.administrator.christie.activity.BaseActivity;
import com.example.administrator.christie.fragment.AccesscontrolInfoFragment;

public class AccessdataActivity extends BaseActivity {//门禁数据

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
        AccesscontrolInfoFragment accessdataFgm = new AccesscontrolInfoFragment();
        FragmentTransaction ftt = getSupportFragmentManager().beginTransaction();
        ftt.add(R.id.frame_accessdata, accessdataFgm, "accessdataFt");
        ftt.commit();
    }

}
