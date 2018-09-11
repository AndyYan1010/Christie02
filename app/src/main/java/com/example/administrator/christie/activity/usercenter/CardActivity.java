package com.example.administrator.christie.activity.usercenter;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.christie.R;
import com.example.administrator.christie.activity.BaseActivity;

public class CardActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout linear_back;
    private TextView     mTv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        initView();
        initData();
    }

    private void initView() {
        linear_back = (LinearLayout) findViewById(R.id.linear_back);
        mTv_title = (TextView) findViewById(R.id.tv_title);
    }

    private void initData() {
        linear_back.setOnClickListener(this);
        mTv_title.setText(R.string.card);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.linear_back:
                finish();
                break;
        }
    }
}
