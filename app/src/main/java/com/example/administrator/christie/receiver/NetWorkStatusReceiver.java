package com.example.administrator.christie.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.widget.Toast;

import com.example.administrator.christie.activity.BaseActivity;
import com.example.administrator.christie.util.NetWorkUtils;

/**
 * Created by Administrator on 2018/1/19 0019.
 */

public class NetWorkStatusReceiver extends BroadcastReceiver {

    public NetWorkStatusReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
//        String action = intent.getAction();
//        if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
//            Toast.makeText(context, "network changed", Toast.LENGTH_LONG).show();
//            BaseActivity.isNetWorkConnected = NetWorkUtils.getAPNType(context)>0;
//        }
        if(!NetWorkUtils.isNetworkConnected(context)){
            Toast.makeText(context, "当前网络不可用", Toast.LENGTH_LONG).show();
        }
    }
}

