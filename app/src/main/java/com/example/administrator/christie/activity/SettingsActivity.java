package com.example.administrator.christie.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.administrator.christie.R;
import com.example.administrator.christie.TApplication;
import com.example.administrator.christie.entity.UserEntity;

public class SettingsActivity extends BaseActivity {
    private Button btn_signout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setViews();
        setListeners();
    }

    protected void setViews(){
        btn_signout = (Button)findViewById(R.id.btn_signout);
    }

    protected void setListeners(){
        btn_signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(SettingsActivity.this).setTitle("确认退出?").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        TApplication.user = new UserEntity();
                        startActivity(new Intent(SettingsActivity.this,LoginActivity.class));
                        finish();
                    }
                }).setNegativeButton("取消",null).show();
            }
        });
    }
}
