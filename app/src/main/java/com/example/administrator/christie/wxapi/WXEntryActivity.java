package com.example.administrator.christie.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.administrator.christie.global.AppConstants;
import com.example.administrator.christie.util.ToastUtils;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * @创建者 AndyYan
 * @创建时间 2018/5/10 13:05
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class WXEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {
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
    public void onReq(BaseReq baseReq) {
        //......这里是用来处理接收的请求,暂不做讨论
        //        finish();
    }

    @Override
    public void onResp(BaseResp resp) {//在这个方法中处理微信传回的数据
        //形参resp 有下面两个个属性比较重要
        //1.resp.errCode
        //2.resp.transaction则是在分享数据的时候手动指定的字符创,用来分辨是那次分享(参照4.中req.transaction)
        switch (resp.errCode) { //根据需要的情况进行处理
            case BaseResp.ErrCode.ERR_OK:
                //正确返回
                ToastUtils.showToast(this, "分享成功");
                finish();
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                //用户取消
                ToastUtils.showToast(this, "分享取消");
                finish();
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                //认证被否决
                ToastUtils.showToast(this, "分享微信认证被否决");
                finish();
                break;
            case BaseResp.ErrCode.ERR_SENT_FAILED:
                //发送失败
                ToastUtils.showToast(this, "分享发送失败");
                finish();
                break;
            case BaseResp.ErrCode.ERR_UNSUPPORT:
                //不支持错误
                ToastUtils.showToast(this, "分享失败");
                finish();
                break;
            case BaseResp.ErrCode.ERR_COMM:
                //一般错误
                ToastUtils.showToast(this, "分享失败");
                finish();
                break;
            default:
                //其他不可名状的情况
                ToastUtils.showToast(this, "分享失败");
                finish();
                break;
        }
    }
}
