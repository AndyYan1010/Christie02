package com.example.administrator.christie.activity;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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
    public static        boolean isSearchBT                      = false;
    private static       int     REQUEST_ENABLE                  = 400;
    private static final int     BLUETOOTH_DISCOVERABLE_DURATION = 120;//Bluetooth 设备可见时间，单位：秒，不设置默认120s。
    private BluetoothAdapter      mBtmAdapter;
    private List<BluetoothDevice> mBtData;
    private LvBlueTInfoAdapter    mBlueTInfoAdapter;
    private ImageView             img_loading;//等待在家动画

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
        img_loading = (ImageView) findViewById(R.id.img_loading);
        mLv_blt = (ListView) findViewById(R.id.listview_bluetooth);
        mTv_title.setText("搜索蓝牙设备");
        mImg_back.setOnClickListener(this);
        mTv_search.setOnClickListener(this);
    }

    private void initData() {
        img_loading.setVisibility(View.INVISIBLE);
        Glide.with(AddBluetoothActivity.this).load(R.drawable.loadgif).into(img_loading);
        mBtData = new ArrayList();
        mBlueTInfoAdapter = new LvBlueTInfoAdapter(this, mBtData);
        mLv_blt.setAdapter(mBlueTInfoAdapter);
        mLv_blt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //连接点击的蓝牙设备
                connectBT(i);
            }
        });
    }

    private void connectBT(int position) {
        //连接前先关闭蓝牙搜索功能
        isSearchBT = false;
        mTv_search.setText("开始搜索");
        img_loading.setVisibility(View.INVISIBLE);
        stopSearchBT();
        //获取对应条目的蓝牙设备信息
        final BluetoothDevice btDevice = mBtData.get(position);
        Intent intent = new Intent(AddBluetoothActivity.this, Ble_Activity.class);
        intent.putExtra(Ble_Activity.EXTRAS_DEVICE_NAME, btDevice.getName());
        intent.putExtra(Ble_Activity.EXTRAS_DEVICE_ADDRESS, btDevice.getAddress());
        // 启动Ble_Activity
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_search:
                if (!isSearchBT) {
                    mBtData.clear();
                    mBlueTInfoAdapter.notifyDataSetChanged();
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
                            needLoactionRight();
                            //开始搜索
                            //startSearchBluetooth();
                        }
                    } else {
                        ToastUtils.showToast(this, "当前设备不支持蓝牙功能");
                    }
                } else {
                    isSearchBT = false;
                    mTv_search.setText("开始搜索");
                    img_loading.setVisibility(View.INVISIBLE);
                    ToastUtils.showToast(this, "已停止搜索");
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
                    needLoactionRight();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                //用户选择开启 Bluetooth，Bluetooth 会被开启
                ToastUtils.showToast(this, "蓝牙开启了");
                //开始搜索
                startSearchBluetooth();
                break;
            default:
                break;
        }
    }

    private void startSearchBluetooth() {
        isSearchBT = true;
        mTv_search.setText("停止搜索");
        img_loading.setVisibility(View.VISIBLE);
        mBtData.clear();
        mBlueTInfoAdapter.notifyDataSetChanged();
        //注册广播接收器
        registerRec();
        ToastUtils.showToast(this, "正在搜索。。");
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        //获取BluetoothAdapter
        if (bluetoothManager != null) {
            mBtmAdapter = bluetoothManager.getAdapter();
            if (null != mBtmAdapter && !mBtmAdapter.isDiscovering())
                mBtmAdapter.startDiscovery();
        }
    }

    private SearchBlueThBcr mReceiver;

    private void registerRec() {
        //3.注册蓝牙广播
        mReceiver = new SearchBlueThBcr(mBtData, mBlueTInfoAdapter);
        mReceiver.setUI(mTv_search, img_loading);
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);//搜索到蓝牙
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);//搜索结束
        registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mReceiver) {
            //解除广播接收器
            unregisterReceiver(mReceiver);
        }
    }

    //申请定位权限
    private void needLoactionRight() {
        //判断是否已经赋予权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //如果应用之前请求过此权限但用户拒绝了请求，此方法将返回 true。
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                //这里可以写个对话框之类的项向用户解释为什么要申请权限，并在对话框的确认键后续再次申请权限
                ToastUtils.showToast(this, "请手动开启相关权限");
            } else {
                //申请权限，字符串数组内是一个或多个要申请的权限，1是申请权限结果的返回参数，在onRequestPermissionsResult可以得知申请结果
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,}, 1);
            }
        } else {
            startSearchBluetooth();
        }
    }
}
