package com.example.administrator.christie.util;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

/**
 * @创建者 AndyYan
 * @创建时间 2018/7/24 16:09
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class ConnectThread extends Thread {
    private BluetoothDevice mDevice;
    private BluetoothSocket mSocket;
    private InputStream     btIs;
    private OutputStream    btOs;
    private boolean         canRecv;
    private PrintWriter     writer;
    private Handler         mHandler;
//    public static final String BT_UUID = "00001101-0000-1000-8000-00805F9B34FB";//uuid
    public static final String BT_UUID = "0000ffe1-0000-1000-8000-00805f9b34fb";//uuid

    public ConnectThread(BluetoothDevice device, Handler handler) {
        mDevice = device;
        canRecv = true;
        mHandler = handler;
    }

    @Override
    public void run() {
        super.run();
        if (mDevice != null) {
            try {
                //获取套接字
                BluetoothSocket temp = mDevice.createInsecureRfcommSocketToServiceRecord(UUID.fromString(BT_UUID));
                //mDevice.createRfcommSocketToServiceRecord(UUID.fromString(BT_UUID));//sdk 2.3以下使用
                mSocket = temp;
                //发起连接请求
                if (mSocket != null) {
                    mSocket.connect();
                }
                sendHandlerMsg("连接 " + mDevice.getName() + "成功！");
                //获取输入输出流
                btIs = mSocket.getInputStream();
                btOs = mSocket.getOutputStream();

                //通讯-接收消息
                BufferedReader reader = new BufferedReader(new InputStreamReader(btIs, "UTF-8"));
                //  BufferedReader reader = new BufferedReader(new InputStreamReader(btIs, "GB2312"));
                String content = null;
                while (canRecv) {
                    content = reader.readLine();
                    sendHandlerMsg("收到消息：" + content);
                }
            } catch (IOException e) {
                e.printStackTrace();
                sendHandlerMsg("错误：" + e.getMessage());
            } finally {
                try {
                    if (mSocket != null) {
                        mSocket.close();
                    }
                    //btIs.close();//两个输出流都依赖socket，关闭socket即可
                    //btOs.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    sendHandlerMsg("错误：" + e.getMessage());
                }
            }
        }
    }

    public void sendMsg(byte[] msg) {
        if (btOs != null) {
            try {
                if (writer == null) {
                    writer = new PrintWriter(new OutputStreamWriter(btOs, "UTF-8"), true);
                    //                    writer = new PrintWriter(new OutputStreamWriter(btOs, "GB2312"), true);
                }
                writer.println(msg);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                writer.close();
                sendHandlerMsg("发送失败：" + e.getMessage());
            }
        }
    }

    private void sendHandlerMsg(String content) {
        Message msg = mHandler.obtainMessage();
        msg.what = 1001;
        msg.obj = content;
        mHandler.sendMessage(msg);
    }
}
