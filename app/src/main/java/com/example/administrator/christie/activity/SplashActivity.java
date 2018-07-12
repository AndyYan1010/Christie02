package com.example.administrator.christie.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.administrator.christie.R;
import com.example.administrator.christie.util.ThreadUtils;


/**
 * @desc 启动页
 */
public class SplashActivity extends Activity {
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 判断是否是第一次开启应用
        //        boolean isFirstOpen = SpUtils.getBoolean(this, AppConstants.FIRST_OPEN);
        // 如果是第一次启动，则先进入功能引导页
        //        if (!isFirstOpen) {
        //            Intent intent = new Intent(this, WelcomeGuideActivity.class);
        //            startActivity(intent);
        //            finish();
        //            return;
        //        }

        // 如果不是第一次启动app，则正常显示启动屏
        setContentView(R.layout.activity_splash);
        ThreadUtils.runOnSubThread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);//线程休眠2s后运行
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

//            handler.postDelayed(new Runnable() {
//
//                @Override
//                public void run() {
//                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
//                    startActivity(intent);
//                    finish();
//                }
//            }, 1000);
    }

}
