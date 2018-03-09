package com.example.administrator.christie.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import com.example.administrator.christie.R;
import com.example.administrator.christie.TApplication;
import com.example.administrator.christie.util.Consts;
import com.example.administrator.christie.util.HttpUtils;
import com.example.administrator.christie.view.CustomProgress;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class QrcodeActivity extends BaseActivity {
    ImageView iv_code;
    Thread thread;
    private boolean isAlive = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        setViews();
        thread = new Thread(new MyThread());
        thread.start();
    }

    protected void setViews(){
        iv_code = (ImageView)findViewById(R.id.iv_code);
    }


    Bitmap encodeAsBitmap(String str){
        Bitmap bitmap = null;
        BitMatrix result = null;
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            result = multiFormatWriter.encode(str, BarcodeFormat.QR_CODE, 800, 800);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            bitmap = barcodeEncoder.createBitmap(result);
        } catch (WriterException e){
            e.printStackTrace();
        } catch (IllegalArgumentException iae){
            return null;
        }
        return bitmap;
    }

    @Override
    protected void onDestroy() {
        isAlive = false;
        super.onDestroy();
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    iv_code.setImageBitmap(encodeAsBitmap(msg.obj.toString()));
                    break;
            }
            super.handleMessage(msg);
        }
    };

    class MyThread implements Runnable {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            while (isAlive) {
                try {
                    String url = Consts.URL+"qrcode?userid="+ TApplication.user.getId();
                    HttpResponse response = HttpUtils.GetUtil(url);
                    HttpEntity entity = response.getEntity();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
                    String result = reader.readLine();
                    Log.d("HTTP", "POST:" + result);
                    Message message = new Message();
                    message.what = 1;
                    message.obj = result;
                    handler.sendMessage(message);// 发送消息
                    Thread.sleep(5*60*1000);// 线程暂停5分钟，单位毫秒
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
}
