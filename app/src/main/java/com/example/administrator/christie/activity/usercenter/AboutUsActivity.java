package com.example.administrator.christie.activity.usercenter;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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

    private ImageView mImg_back;
    private TextView mTv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        initView();
        initData();
    }

    private void initView() {
        mImg_back = (ImageView) findViewById(R.id.img_back);
        mTv_title = (TextView) findViewById(R.id.tv_title);
    }

    private void initData() {
        mImg_back.setOnClickListener(this);
        mTv_title.setText("关于");
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
