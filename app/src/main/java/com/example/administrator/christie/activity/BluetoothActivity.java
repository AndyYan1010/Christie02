package com.example.administrator.christie.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.christie.R;
import com.example.administrator.christie.adapter.LvBlueTInfoAdapter;
import com.example.administrator.christie.util.ShakeHelper;
import com.example.administrator.christie.util.ThreadUtils;
import com.example.administrator.christie.util.ToastUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class BluetoothActivity extends BaseActivity implements View.OnClickListener {

    private ImageView    mImg_back;
    private TextView     mTv_title;
    private ShakeHelper  mShakeHelper;
    private LinearLayout mLiner_add_equipment;
    private static final int     SENSOR_SHAKE = 10;
    private              boolean isShaking    = false;
    private ImageView        mImg_shake;
    private ListView         mLv_old_bt;
    private BluetoothAdapter mBtmAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        initView();
        initData();
    }

    private void initView() {
        mImg_back = (ImageView) findViewById(R.id.img_back);
        mTv_title = (TextView) findViewById(R.id.tv_title);
        mImg_shake = (ImageView) findViewById(R.id.img_shake);
        mLiner_add_equipment = (LinearLayout) findViewById(R.id.liner_add_equipment);
        mLv_old_bt = (ListView) findViewById(R.id.lv_old_bt);
        mTv_title.setText("蓝牙开门");
        mImg_back.setOnClickListener(this);
        mLiner_add_equipment.setOnClickListener(this);
    }

    private void initData() {
        final List<BluetoothDevice> mData = new ArrayList<>();
        mBtmAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> bondedDevices = mBtmAdapter.getBondedDevices();
        for (BluetoothDevice device : bondedDevices) {
            mData.add(device);
        }
        LvBlueTInfoAdapter adapter = new LvBlueTInfoAdapter(this, mData);
        mLv_old_bt.setAdapter(adapter);
        mLv_old_bt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //                BluetoothDevice device = mData.get(i);
                //                new ConnectTask().execute(device.getAddress());
                //获取对应条目的蓝牙设备信息
                final BluetoothDevice btDevice = mData.get(i);
                Intent intent = new Intent(BluetoothActivity.this, Ble_Activity.class);
                intent.putExtra(Ble_Activity.EXTRAS_DEVICE_NAME, btDevice.getName());
                intent.putExtra(Ble_Activity.EXTRAS_DEVICE_ADDRESS, btDevice.getAddress());
                // 启动Ble_Activity
                startActivity(intent);
            }
        });
//        mShakeHelper = new ShakeHelper(this);
//        mShakeHelper.Start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.liner_add_equipment:
                Intent intent = new Intent(this, AddBluetoothActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mShakeHelper) {
            mShakeHelper.Stop();
        }
    }

    //这条是蓝牙串口通用的UUID，不要更改
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private BluetoothSocket bluetoothSocket;
    private OutputStream outStream = null;

    class ConnectTask extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] objects) {

            BluetoothDevice device = mBtmAdapter.getRemoteDevice(objects[0].toString());
            try {
                bluetoothSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
                bluetoothSocket.connect();
            } catch (IOException e) {
                try {
                    bluetoothSocket.close();
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtils.showToast(BluetoothActivity.this, "Socket 创建失败");
                        }
                    });
                    return "Socket 创建失败";
                } catch (IOException e2) {
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtils.showToast(BluetoothActivity.this, "Socket 关闭失败");
                        }
                    });
                    return "Socket 关闭失败";
                }
            }
            //取消搜索
            mBtmAdapter.cancelDiscovery();
            try {
                outStream = bluetoothSocket.getOutputStream();
            } catch (IOException e) {
                ThreadUtils.runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showToast(BluetoothActivity.this, "Socket 流创建失败");
                    }
                });
                return "Socket 流创建失败";
            }
            ThreadUtils.runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    ToastUtils.showToast(BluetoothActivity.this, "蓝牙连接正常,Socket 创建成功");
                }
            });
            return "蓝牙连接正常,Socket 创建成功";
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
        }
    }
}
