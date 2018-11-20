package com.example.administrator.christie.wxapi;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.example.administrator.christie.InformationMessege.MessageEvent;
import com.example.administrator.christie.activity.BaseActivity;
import com.example.administrator.christie.global.AppConstants;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;

public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {
    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
    private IWXAPI       api;
    private MessageEvent messageEvent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, AppConstants.WX_Pay_APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {

    }

    Handler mHandler = new Handler();
    private static final int SDK_WXPAY_FLAG = 1000;

    @SuppressLint("LongLogTag")
    @Override
    public void onResp(BaseResp resp) {
        //        Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);
        Message msg = new Message();
        msg.what = SDK_WXPAY_FLAG;
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (resp.errCode == 0) {
                msg.obj = "支付成功";
                Toast.makeText(this, "支付成功", Toast.LENGTH_LONG).show();
                messageEvent = new MessageEvent("WX支付成功");
                EventBus.getDefault().post(messageEvent);
            } else if (resp.errCode == -1) {
                msg.obj = "支付失败";
                Toast.makeText(this, "支付失败", Toast.LENGTH_LONG).show();
            } else {
                msg.obj = "支付失败";
                Toast.makeText(this, "用户取消支付", Toast.LENGTH_LONG).show();
            }
            mHandler.sendMessage(msg);
            finish();
        }
    }

}