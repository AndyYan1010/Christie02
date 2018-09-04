package com.example.administrator.christie.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.christie.R;
import com.example.administrator.christie.global.AppConstants;
import com.example.administrator.christie.util.ToastUtils;
import com.example.administrator.christie.util.ZxingUtils;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;


/**
 * @创建者 AndyYan
 * @创建时间 2018/4/23 15:19
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class InvitationQRcodeFragment extends Fragment implements View.OnClickListener {
    private View      mRootView;
    private String    mDetailJson;
    private TextView  mTv_title;
    private ImageView mImg_back, mImg_code;
    private Button       mBt_share;
    private LinearLayout mLiner;
    private IWXAPI       api;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_invitation_code, container, false);
        api = WXAPIFactory.createWXAPI(getContext(), AppConstants.WX_Pay_APP_ID, true);
        api.registerApp(AppConstants.WX_Pay_APP_ID);
        initView();
        initData();
        return mRootView;
    }

    private void initView() {
        if (null == mDetailJson || "".equals(mDetailJson)) {
            ToastUtils.showToast(getContext(), "未携带具体邀请信息");
            //弹出回退栈最上面的fragment
            getFragmentManager().popBackStackImmediate(null, 0);
            return;
        }
        mLiner = (LinearLayout) mRootView.findViewById(R.id.liner);
        mImg_back = (ImageView) mRootView.findViewById(R.id.img_back);
        mTv_title = (TextView) mRootView.findViewById(R.id.tv_title);
        mImg_code = (ImageView) mRootView.findViewById(R.id.img_code);
        mBt_share = (Button) mRootView.findViewById(R.id.bt_share);
    }

    private void initData() {
        mLiner.setOnClickListener(this);
        mImg_back.setOnClickListener(this);
        mTv_title.setText("二维码分享");
        mBt_share.setOnClickListener(this);
        //生成二维码
        generateQr(mDetailJson);
    }

    public void setInfoJson(String detailJson) {
        this.mDetailJson = detailJson;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.liner:
                break;
            case R.id.img_back:
                //弹出回退栈最上面的fragment
                getFragmentManager().popBackStackImmediate(null, 0);
                break;
            case R.id.bt_share:
                mImg_code.setDrawingCacheEnabled(true);
                Bitmap bitmap = Bitmap.createBitmap(mImg_code.getDrawingCache());
                mImg_code.setDrawingCacheEnabled(false);
                WXImageObject imgObj = new WXImageObject(bitmap);
                WXMediaMessage msg = new WXMediaMessage();
                msg.mediaObject = imgObj;

                //设置缩略图
                Bitmap mBp = Bitmap.createScaledBitmap(bitmap, 120, 120, true);
                bitmap.recycle();
                msg.thumbData = bmpToByteArray(mBp, true);
                //  Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, THUMB_SIZE, THUMB_SIZE, true);
                //  bitmap.recycle();
                //  msg.thumbData = getBytesByBitmap(bitmap);

                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = buildTransaction("img");
//                req.transaction = String.valueOf(System.currentTimeMillis());
                req.message = msg;
                req.scene = SendMessageToWX.Req.WXSceneSession;
                api.sendReq(req);
                break;
        }
    }

    private void generateQr(String data) {
        Bitmap bitmap = ZxingUtils.createQRImage(data, 200, 200);
        if (null != bitmap) {
            mImg_code.setImageBitmap(bitmap);
        }
    }

    public byte[] getBytesByBitmap(Bitmap bitmap) {
        ByteBuffer buffer = ByteBuffer.allocate(bitmap.getByteCount());
        return buffer.array();
    }

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
}
