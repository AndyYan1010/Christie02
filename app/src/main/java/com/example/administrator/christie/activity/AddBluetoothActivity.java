package com.example.administrator.christie.activity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.christie.R;
import com.example.administrator.christie.adapter.LvBlueTInfoAdapter;
import com.example.administrator.christie.broadcastReceiver.SearchBlueThBcr;
import com.example.administrator.christie.util.BluetoothManagerUtils;
import com.example.administrator.christie.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @创建者 AndyYan
 * @创建时间 2018/4/18 10:44
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class AddBluetoothActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mImg_back;
    private TextView  mTv_title, mTv_search;
    private ListView mLv_blt;
    private              boolean isSearchBT                      = false;
    private static       int     REQUEST_ENABLE                  = 400;
    private static final int     BLUETOOTH_DISCOVERABLE_DURATION = 120;//Bluetooth 设备可见时间，单位：秒，不设置默认120s。
    private BluetoothAdapter   mBtmAdapter;
    private List               mBtData;
    private LvBlueTInfoAdapter mBlueTInfoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bluetooth);
        initView();
        initData();
    }

    private void initView() {
        mImg_back = (ImageView) findViewById(R.id.img_back);
        mTv_title = (TextView) findViewById(R.id.tv_title);
        mTv_search = (TextView) findViewById(R.id.tv_search);
        mLv_blt = (ListView) findViewById(R.id.listview_bluetooth);
        mTv_title.setText("添加蓝牙设备");
        mImg_back.setOnClickListener(this);
        mTv_search.setOnClickListener(this);
    }

    private void initData() {
        mBtData = new ArrayList();
        mBtData.add("1");
        mBtData.add("1");
        mBtData.add("1");
        mBlueTInfoAdapter = new LvBlueTInfoAdapter(this, mBtData);
        mLv_blt.setAdapter(mBlueTInfoAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_search:
                if (!isSearchBT) {
                    boolean bluetoothSupported = BluetoothManagerUtils.isBluetoothSupported();
                    if (bluetoothSupported) {
                        boolean bluetoothEnabled = BluetoothManagerUtils.isBluetoothEnabled();
                        if (!bluetoothEnabled) {
                            //弹出对话框提示用户是否打开
                            Intent enabler = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                            // 设置 Bluetooth 设备可以被其它 Bluetooth 设备扫描到
                            enabler.setAction(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                            // 设置 Bluetooth 设备可见时间
                            enabler.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, BLUETOOTH_DISCOVERABLE_DURATION);
                            startActivityForResult(enabler, REQUEST_ENABLE);
                        } else {
                            //开始搜索
                            startSearchBluetooth();
                        }
                    } else {
                        ToastUtils.showToast(this, "当前设备不支持蓝牙功能");
                    }
                } else {
                    isSearchBT = false;
                    mTv_search.setText("开始搜索");
                    ToastUtils.showToast(this, "停止搜索");
                    stopSearchBT();
                }
                break;
        }
    }

    private void stopSearchBT() {
        if (mBtmAdapter != null && mBtmAdapter.isDiscovering()) {
            mBtmAdapter.cancelDiscovery();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE) {
            switch (resultCode) {
                // 点击确认按钮
                case Activity.RESULT_OK: {
                    //用户选择开启 Bluetooth，Bluetooth 会被开启
                    ToastUtils.showToast(this, "蓝牙开启了");
                    //开始搜索
                    startSearchBluetooth();
                }
                break;
                // 点击取消按钮或点击返回键
                case Activity.RESULT_CANCELED: {
                    //用户拒绝打开 Bluetooth, Bluetooth 不会被开启
                    ToastUtils.showToast(this, "开启蓝牙功能，才能搜索");
                }
                break;
                default:
                    break;
            }
        }
    }

    private void startSearchBluetooth() {
        isSearchBT = true;
        mTv_search.setText("停止搜索");
        mBtData.clear();
        mBlueTInfoAdapter.notifyDataSetChanged();
        //注册广播接收器
        registerRec();
        ToastUtils.showToast(this, "正在搜索。。哈哈");
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        //获取BluetoothAdapter
        if (bluetoothManager != null) {
            mBtmAdapter = bluetoothManager.getAdapter();
            if (null != mBtmAdapter)
                mBtmAdapter.startDiscovery();
        }
    }

    private void registerRec() {
        //3.注册蓝牙广播
        SearchBlueThBcr mReceiver = new SearchBlueThBcr(mBtData, mBlueTInfoAdapter);
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);//搜索到蓝牙
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);//搜索结束
        registerReceiver(mReceiver, filter);
    }
}
