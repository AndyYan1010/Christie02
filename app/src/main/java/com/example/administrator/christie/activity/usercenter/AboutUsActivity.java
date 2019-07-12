package com.example.administrator.christie.activity.usercenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.christie.R;
import com.example.administrator.christie.activity.BaseActivity;

/**
 * @创建者 AndyYan
 * @创建时间 2018/6/8 15:29
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class AboutUsActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout linear_back;
    private TextView     mTv_title;
    private LinearLayout ll_statement;
    private LinearLayout ll_help;
    private LinearLayout ll_privacy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        initView();
        initData();
    }

    private void initView() {
        linear_back = (LinearLayout) findViewById(R.id.linear_back);
        mTv_title = (TextView) findViewById(R.id.tv_title);
        ll_statement = findViewById(R.id.ll_statement);
        ll_help = findViewById(R.id.ll_help);
        ll_privacy = findViewById(R.id.ll_privacy);
    }

    private void initData() {
        linear_back.setOnClickListener(this);
        mTv_title.setText("关于");
        ll_statement.setOnClickListener(this);
        ll_help.setOnClickListener(this);
        ll_privacy.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.linear_back:
                finish();
                break;
            case R.id.ll_statement://跳转webview页面
                showContent(1);//1声明
                break;
            case R.id.ll_help:
                showContent(2);//2帮助
                break;
            case R.id.ll_privacy:
                showContent(3);//3隐私
                break;
        }
    }

    private void showContent(int i) {
        Intent intent = new Intent(this, WebMoreInfoActivity.class);
        intent.putExtra("kind", i);
        startActivity(intent);
    }
}
