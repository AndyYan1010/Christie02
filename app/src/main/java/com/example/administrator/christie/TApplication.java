package com.example.administrator.christie;

import android.app.Activity;
import android.app.Application;

import com.example.administrator.christie.entity.UserEntity;
import com.example.administrator.christie.util.ExceptionUtil;
import com.example.administrator.christie.util.LogUtil;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/1/4 0004.
 */

public class TApplication extends Application{
    /**
     * release=true 软件发布 false:开发中
     */
    public static boolean isRelease = false;
    public static ArrayList<Activity> listActivity = new ArrayList<Activity>();
    public static UserEntity user = new UserEntity();

    public static void exit(){
        try {
            for (Activity activity : listActivity) {
                activity.finish();
                LogUtil.i("退出", activity.toString() + " finish了");
            }
            // 结束进程
//            System.exit(0);
        } catch (Exception e) {
            ExceptionUtil.handleException(e);
        }
    }
}
