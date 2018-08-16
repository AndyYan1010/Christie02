package com.example.administrator.christie.activity.homeAct;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.example.administrator.christie.R;
import com.example.administrator.christie.activity.BaseActivity;
import com.example.administrator.christie.fragment.InvitationRecordFragment;

/**
 * @创建者 AndyYan
 * @创建时间 2018/4/19 11:11
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class InvitationRecordActivity extends BaseActivity {

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
        InvitationRecordFragment invitationRecordFt = new InvitationRecordFragment();
        FragmentTransaction ftt = getSupportFragmentManager().beginTransaction();
        ftt.add(R.id.frame_accessdata, invitationRecordFt, "invitationRecordFt");
        ftt.commit();
    }
}
