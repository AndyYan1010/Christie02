package com.example.administrator.christie.wxapi;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.example.administrator.christie.global.AppConstants;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

    private IWXAPI api;

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
        Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);
        Message msg = new Message();
        msg.what = SDK_WXPAY_FLAG;
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (resp.errCode == 0) {
                // Toast.makeText(this, "支付成功", Toast.LENGTH_LONG).show();
                msg.obj = "支付成功";
            } else if (resp.errCode == -1) {
                Toast.makeText(this, "支付失败", Toast.LENGTH_LONG).show();
                msg.obj = "支付失败";
            }else {
                Toast.makeText(this, "用户取消支付", Toast.LENGTH_LONG).show();
                msg.obj = "支付失败";
            }
            mHandler.sendMessage(msg);
            finish();
        }
    }
}