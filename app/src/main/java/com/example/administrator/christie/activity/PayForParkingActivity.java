package com.example.administrator.christie.activity;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.christie.R;
import com.example.administrator.christie.global.PayResult;
import com.example.administrator.christie.modelInfo.RequestParamsFM;
import com.example.administrator.christie.util.HttpOkhUtils;
import com.example.administrator.christie.util.ToastUtils;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Map;

import okhttp3.Request;

/**
 * @创建者 AndyYan
 * @创建时间 2018/4/19 15:49
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class PayForParkingActivity extends BaseActivity implements View.OnClickListener {
    private ImageView mImg_back;
    private TextView  mTv_title;
    private Button    mBt_pay;
    private CheckBox  mCb_weixin, mCb_zfb;
    private int payKind = 0;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payfor_parking);
        mContext = PayForParkingActivity.this;
        initView();
        initData();
    }

    private void initView() {
        mImg_back = (ImageView) findViewById(R.id.img_back);
        mTv_title = (TextView) findViewById(R.id.tv_title);
        mCb_weixin = (CheckBox) findViewById(R.id.cb_weixin);
        mCb_zfb = (CheckBox) findViewById(R.id.cb_zfb);
        mBt_pay = (Button) findViewById(R.id.bt_pay);
    }

    private void initData() {
        mImg_back.setOnClickListener(this);
        mTv_title.setText("停车缴费");

        mCb_weixin.setOnClickListener(this);
        mCb_zfb.setOnClickListener(this);
        mBt_pay.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.cb_weixin:
                if (mCb_zfb.isChecked())
                    mCb_zfb.setChecked(false);
                if (mCb_weixin.isChecked()) {
                    payKind = 1;
                } else {
                    payKind = 0;
                }
                break;
            case R.id.cb_zfb:
                if (mCb_weixin.isChecked())
                    mCb_weixin.setChecked(false);
                if (mCb_zfb.isChecked()) {
                    payKind = 2;
                } else {
                    payKind = 0;
                }
                break;
            case R.id.bt_pay:
                if (payKind == 0) {
                    ToastUtils.showToast(this, "请选择支付方式");
                    return;
                }
                if (payKind == 1) {
                    ToastUtils.showToast(this, "您选择了微信支付");

                }
                if (payKind == 2) {
                    ToastUtils.showToast(this, "你选择了支付宝支付");
                    RequestParamsFM params=new RequestParamsFM();
                    params.put("","");
                    HttpOkhUtils.getInstance().doPost("", params, new HttpOkhUtils.HttpCallBack() {
                        @Override
                        public void onError(Request request, IOException e) {
                            ToastUtils.showToast(mContext, "下单失败");
                        }

                        @Override
                        public void onSuccess(int code, String resbody) {
                            if (code != 200) {
                                ToastUtils.showToast(mContext, "下单失败");
                                return;
                            }
                            DecimalFormat df = new DecimalFormat("######0.00");
//                            double resultPrice = Double.parseDouble(df.format(price));
//                            if (resultPrice < 0.01) {
//                                orderOverOK(orderStr, "", "zhifubao");
//                            } else {
//                                if (checkAliPayInstalled(mContext)) {
//                                    testSend(resultPrice);
//                                } else {
//                                    ToastUtils.showToast(mContext, "您未安装支付宝");
//                                }
//                            }
                        }
                    });
                }
                break;
        }
    }

    private void orderOverOK(String order_num, String pay_no, String pay_type) {

        RequestParamsFM params=new RequestParamsFM();
        params.put("","");
        HttpOkhUtils.getInstance().doPost("", params, new HttpOkhUtils.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {

            }

            @Override
            public void onSuccess(int code, String resbody) {
                if (code != 200) {

                }
                if (code == 200) {
                    ToastUtils.showToast(mContext, "下单成功");
                    finish();
                }
            }
        });
    }

    private static final int SDK_PAY_FLAG = 1001;

    private void testSend(double price) {
        /**
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * orderInfo的获取必须来自服务端；
         */
//        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(AppConstants.ALI_PLAY_APPID, mOrder.getFname(), price);
//        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);
//
//        String sign = OrderInfoUtil2_0.getSign(params, AppConstants.ALI_PLAY_RSA2_PRIVATE, true);
//        final String orderInfo = orderParam + "&" + sign;

//        Runnable payRunnable = new Runnable() {
//            @Override
//            public void run() {
//                PayTask alipay = new PayTask(PayForParkingActivity.this);
//                Map<String, String> result = alipay.payV2(orderInfo, true);
//                Log.i("msp", result.toString());
//
//                Message msg = new Message();
//                msg.what = SDK_PAY_FLAG;
//                msg.obj = result;
//
//                mHandler.sendMessage(msg);
//            }
//        };
//        Thread payThread = new Thread(payRunnable);
//        payThread.start();
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SDK_PAY_FLAG:
                    //                    Map<String,String> map = (Map<String, String>)msg.obj;
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    //同步获取结果
                    String resultInfo = payResult.getResult();
                    Log.i("Pay", "Pay:" + resultInfo);
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(mContext, "支付成功", Toast.LENGTH_SHORT).show();
                        String tradeNo = resultInfo.split("trade_no\":\"")[1];
                        tradeNo = tradeNo.substring(0, tradeNo.indexOf("\""));
//                        orderOverOK(orderStr, tradeNo, "zhifubao");
                    } else {
                        Toast.makeText(mContext, "支付失败", Toast.LENGTH_SHORT).show();
                        //                        sendDataToIntnet();
                    }
                    break;
            }
        }
    };

    public static boolean checkAliPayInstalled(Context context) {
        Uri uri = Uri.parse("alipays://platformapi/startApp");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        ComponentName componentName = intent.resolveActivity(context.getPackageManager());
        return componentName != null;
    }
}
