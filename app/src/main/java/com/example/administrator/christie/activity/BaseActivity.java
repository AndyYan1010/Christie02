package com.example.administrator.christie.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.administrator.christie.TApplication;

public class BaseActivity extends AppCompatActivity {

    public static boolean isNetWorkConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (TApplication.flag == -1) {//flag为-1说明程序被杀掉
            protectApp();
        }
        TApplication.listActivity.add(this);
    }

    protected void protectApp() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//清空栈里MainActivity之上的所有activty
        startActivity(intent);
        finish();
        TApplication.flag = 0;
    }
}
