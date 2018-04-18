package com.example.administrator.christie.activity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.christie.R;
import com.example.administrator.christie.util.BluetoothManagerUtils;
import com.example.administrator.christie.util.ToastUtils;

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
    private ListView mListview_bluetooth;
    private static int REQUEST_ENABLE = 4000;
    private BluetoothAdapter mBluetoothAdapter;
    private static final int BLUETOOTH_DISCOVERABLE_DURATION = 120;//Bluetooth 设备可见时间，单位：秒，不设置默认120s。

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
        mListview_bluetooth = (ListView) findViewById(R.id.listview_bluetooth);
        mTv_title.setText("添加蓝牙设备");
        mImg_back.setOnClickListener(this);
        mTv_search.setOnClickListener(this);
    }

    private void initData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_search:
                boolean bluetoothSupported = BluetoothManagerUtils.isBluetoothSupported();
                if (bluetoothSupported) {
                    boolean bluetoothEnabled = BluetoothManagerUtils.isBluetoothEnabled();
                    if (!bluetoothEnabled) {
                        //弹出对话框提示用户是后打开
                        Intent enabler = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        // 设置 Bluetooth 设备可以被其它 Bluetooth 设备扫描到
                        enabler.setAction(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                        // 设置 Bluetooth 设备可见时间
                        enabler.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,BLUETOOTH_DISCOVERABLE_DURATION);
                        startActivityForResult(enabler, REQUEST_ENABLE);
                    }else {
                        //开始搜索
                        startSearchBluetooth();
                    }
                } else {
                    ToastUtils.showToast(this, "当前设备不支持蓝牙功能");
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE) {
            switch (resultCode) {
                // 点击确认按钮
                case Activity.RESULT_OK: {
                    // TODO 用户选择开启 Bluetooth，Bluetooth 会被开启
                    ToastUtils.showToast(this,"蓝牙开启了");
                    //开始搜索
                    startSearchBluetooth();
                }
                break;
                // 点击取消按钮或点击返回键
                case Activity.RESULT_CANCELED: {
                    //用户拒绝打开 Bluetooth, Bluetooth 不会被开启
                    ToastUtils.showToast(this,"开启蓝牙功能，才能搜索");
                }
                break;
                default:
                    break;
            }
        }
    }

    private void startSearchBluetooth() {
        ToastUtils.showToast(this,"正在搜索。。哈哈");
    }
}
