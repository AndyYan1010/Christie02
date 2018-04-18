package com.example.administrator.christie.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.christie.R;

public class AccessdataActivity extends BaseActivity implements View.OnClickListener {

    private TextView mTv_title;
    private ImageView mImg_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accessdata);
        initView();
        initData();
    }

    private void initView() {
        mTv_title = (TextView) findViewById(R.id.tv_title);
        mImg_back = (ImageView) findViewById(R.id.img_back);
        mTv_title.setText("门禁数据查询");
        mImg_back.setOnClickListener(this);
    }

    private void initData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.img_back:
                finish();
                break;
        }
    }
}
