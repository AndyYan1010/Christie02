package com.example.administrator.christie.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.administrator.christie.TApplication;

public class BaseActivity extends AppCompatActivity {

    public static boolean isNetWorkConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TApplication.listActivity.add(this);
    }
}
