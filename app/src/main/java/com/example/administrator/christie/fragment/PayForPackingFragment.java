package com.example.administrator.christie.fragment;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.christie.R;
import com.example.administrator.christie.global.AppConstants;
import com.example.administrator.christie.global.PayResult;
import com.example.administrator.christie.modelInfo.RequestParamsFM;
import com.example.administrator.christie.util.HttpOkhUtils;
import com.example.administrator.christie.util.ToastUtils;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Map;

import okhttp3.Request;

/**
 * @创建者 AndyYan
 * @创建时间 2018/5/11 14:52
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class PayForPackingFragment extends Fragment implements View.OnClickListener {
    private View      mRootView;
    private ImageView mImg_back;
    private TextView  mTv_title;
    private Button    mBt_pay;
    private CheckBox  mCb_weixin, mCb_zfb;
    private int payKind = 0;
    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_payfor_plate, container, false);
        mContext = getContext();
        initView();
        initData();
        return mRootView;
    }

    private void initView() {
        mImg_back = (ImageView) mRootView.findViewById(R.id.img_back);
        mTv_title = (TextView) mRootView.findViewById(R.id.tv_title);
        mCb_weixin = (CheckBox) mRootView.findViewById(R.id.cb_weixin);
        mCb_zfb = (CheckBox) mRootView.findViewById(R.id.cb_zfb);
        mBt_pay = (Button) mRootView.findViewById(R.id.bt_pay);
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
                //弹出回退栈最上面的fragment
                getFragmentManager().popBackStackImmediate(null, 0);
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
                    ToastUtils.showToast(getContext(), "请选择支付方式");
                    return;
                }
                if (payKind == 1) {
                    ToastUtils.showToast(getContext(), "您选择了微信支付");
                    /*1.客户端（APP）提交订单信息给服务端，服务端提交微信，根据微信接口：统一下单接口，
                        *2.生成预支付Id(prepay_id)返回给客户端，客户端（APP）根据预支付Id（prepay_id）调起微信支付
                        *3.接收微信返回的支付信息：成功，错误，取消
                         *4.支付成功，再在服务器上下单，确认订单已支付.该订单支付完成*/
                    //提交订单信息，服务器获取订单信息后调用微信统一下单接口，请求完成，将prepay_id返回app
                    // （具体返回参数：appid，商户号：partnerid，预支付id：prepay_id，扩展字段package（固定值Sign=WXPay）,随机字符串noncestr，时间戳timestamp，签名sign），
                    //获取返回参数后，调用微信app支付
                    toWXPay();
                    return;
                }
                if (payKind == 2) {
                    ToastUtils.showToast(getContext(), "你选择了支付宝支付");
                    //先提交下单信息到服务器(状态时已下单，未支付)，获取订单号
                    //获取订单信息后，调用支付宝支付,
                    //接收支付宝返回的支付信息：取消支付，成功支付，支付失败。
                    //支付成功，再在服务器上下单，确认订单已支付.该订单支付完成
                    RequestParamsFM params = new RequestParamsFM();
                    params.put("", "");
                    //                    params.put("member_id", UserID);
                    //                    params.put("ticket_count", ticket_count);
                    //                    if (mOrder.getPrice() * mBuyNumber<=0.01){
                    //                        params.put("fstatus","1");
                    //                    }else {
                    //                        params.put("fstatus","0");
                    //                    }
                    //                    params.put("amount", amount);
                    //                    params.put("table_name", table_name);
                    //                    params.put("name", mEdit_order_name.getText());
                    //                    params.put("phone", mEdit_phone_num.getText());
                    //                    params.put("begin_time", mOrder.getFdate());
                    //                    params.put("fname", fname);
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
                            //                            orderStr = response.optString("id");
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
    private IWXAPI iwxapi; //微信支付api

    private void toWXPay() {
        iwxapi = WXAPIFactory.createWXAPI(getContext(), AppConstants.WX_Pay_APP_ID); //初始化微信api
        //        iwxapi.registerApp(AppConstants.WX_Pay_APP_ID); //注册appid  appid可以在开发平台获取
        //        Runnable payRunnable = new Runnable() {  //这里注意要放在子线程
        //            @Override
        //            public void run() {
        //                PayReq request = new PayReq(); //调起微信APP的对象
        //                //下面是设置必要的参数，也就是前面说的参数,这几个参数从何而来请看上面说明
        //                request.appId = AppConstants.WX_Pay_APP_ID;
        //                request.partnerId = partnerId;
        //                request.prepayId = prepayId;
        //                request.packageValue = "Sign=WXPay";
        //                request.nonceStr = nonceStr;
        //                request.timeStamp = timeStamp;
        //                request.sign = sign;
        //                iwxapi.sendReq(request);//发送调起微信的请求
        //            }
        //        };
        //        Thread payThread = new Thread(payRunnable);
        //        payThread.start();
    }

    private void orderOverOK(String order_num, String pay_no, String pay_type) {
        RequestParamsFM params = new RequestParamsFM();
        params.put("", "");
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
                    getActivity().finish();
                }
            }
        });
    }

    private static final int    SDK_ALPAY_FLAG = 1001;
    private static final int    SDK_WXPAY_FLAG = 1000;
    private              String orderStr       = "";

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
        ////
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
                case SDK_ALPAY_FLAG:
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
                case SDK_WXPAY_FLAG:
                    String result = String.valueOf(msg.obj);
                    if ("支付成功".equals(result)) {
                        ToastUtils.showToast(getContext(), "支付成功");
                    } else {
                        ToastUtils.showToast(getContext(), "支付失败");
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
