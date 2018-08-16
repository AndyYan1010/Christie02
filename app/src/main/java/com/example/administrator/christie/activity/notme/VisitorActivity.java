package com.example.administrator.christie.activity.notme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.administrator.christie.R;
import com.example.administrator.christie.activity.BaseActivity;

/**
 * 访客邀请页面
 */
public class VisitorActivity extends BaseActivity {
    private LinearLayout ll_yypz,ll_tdmj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitor);
        setViews();
        setListeners();
    }

    protected void setViews(){
        ll_yypz = (LinearLayout) findViewById(R.id.ll_yypz);
        ll_tdmj = (LinearLayout) findViewById(R.id.ll_tdmj);
    }

    protected void setListeners(){
        ll_yypz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(VisitorActivity.this,YypzActivity.class));
            }
        });
        ll_tdmj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(VisitorActivity.this,TdmjActivity.class));
            }
        });
    }
}
