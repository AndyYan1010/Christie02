package com.example.administrator.christie.activity.usercenter;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.christie.R;
import com.example.administrator.christie.activity.BaseActivity;
import com.example.administrator.christie.activity.LoginActivity;
import com.example.administrator.christie.modelInfo.UserInfo;

public class SettingsActivity extends BaseActivity implements View.OnClickListener {
    private Button       btn_signout;
    private LinearLayout linear_back;
    private TextView     mTv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setViews();
        setData();
        setListeners();
    }

    protected void setViews() {
        linear_back = (LinearLayout) findViewById(R.id.linear_back);
        mTv_title = (TextView) findViewById(R.id.tv_title);
        btn_signout = (Button) findViewById(R.id.btn_signout);
    }

    private void setData() {
        linear_back.setOnClickListener(this);
        mTv_title.setText(getResources().getString(R.string.settings));
    }

    protected void setListeners() {
        btn_signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(SettingsActivity.this).setTitle("确认退出?").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharedPreferences sharedPreferences = getSharedPreferences(UserInfo.class.getName(), MODE_PRIVATE);
                        SharedPreferences.Editor edit = sharedPreferences.edit();
                        edit.remove("userinfo");
                        edit.commit();
                        startActivity(new Intent(SettingsActivity.this, LoginActivity.class));
                        finish();
                    }
                }).setNegativeButton("取消", null).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.linear_back:
                finish();
                break;
        }
    }
}
