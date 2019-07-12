package com.example.administrator.christie.activity.homeAct;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.administrator.christie.InformationMessege.ProjectMsg;
import com.example.administrator.christie.R;
import com.example.administrator.christie.activity.BaseActivity;
import com.example.administrator.christie.adapter.LvBlueTInfoAdapter;
import com.example.administrator.christie.adapter.ProSpinnerAdapter;
import com.example.administrator.christie.broadcastReceiver.SearchBlueThBcr;
import com.example.administrator.christie.modelInfo.BlueOpenInfo;
import com.example.administrator.christie.modelInfo.LoginInfo;
import com.example.administrator.christie.modelInfo.RequestParamsFM;
import com.example.administrator.christie.modelInfo.UserInfo;
import com.example.administrator.christie.util.BluetoothManagerUtils;
import com.example.administrator.christie.util.HttpOkhUtils;
import com.example.administrator.christie.util.IsInternetUtil;
import com.example.administrator.christie.util.ProgressDialogUtil;
import com.example.administrator.christie.util.SPref;
import com.example.administrator.christie.util.SpUtils;
import com.example.administrator.christie.util.ToastUtils;
import com.example.administrator.christie.websiteUrl.NetConfig;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

/**
 * @创建者 AndyYan
 * @创建时间 2018/4/18 10:44
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class AddBluetoothActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout linear_back, linear_selc_pro;
    private LinearLayout linear_search;//点击搜索蓝牙
    private TextView     mTv_title, mTv_search, tv_proName;
    private ListView mLv_blt;//蓝牙列表
    private static int REQUEST_ENABLE = 400;
    private List<ProjectMsg>   mBtData;
    private LvBlueTInfoAdapter mBlueTInfoAdapter;
    private ImageView          img_loading;//等待加载动画
    private Spinner            mSpinner_village;
    private List<ProjectMsg>   dataDetList;//记录项目地址小区信息
    private ProSpinnerAdapter  mSpDetAdapter;
    private String             mBlueOpenInfo;//蓝牙信息
    private String mUpperID = "";//选择的小区id
    private List<ProjectMsg> sumDataList;//存放所有的授权蓝牙信息
    private int REQUEST_BLE_CODE = 10998;//开门界面响应码
    private int RESULT_BLE_CODE  = 10999;//开门成功后，关闭前面的界面
    //    private Handler mProhandler;
    //    private int count            = 180;//搜索时间、单位秒
    //    private boolean          autoOpen;//是否只绑定了一个蓝牙，是就自动开门
    // 描述扫描蓝牙的状态
    private boolean          mScanning;
    private boolean          scan_flag;
    private BluetoothAdapter mBluetoothAdapter;// 蓝牙适配器
    private              int  REQUEST_ENABLE_BT = 1;
    // 蓝牙扫描时间
    private static final long SCAN_PERIOD       = 10000;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bluetooth);
        initView();
        initData();
    }

    private void initView() {
        linear_back = (LinearLayout) findViewById(R.id.linear_back);
        linear_selc_pro = (LinearLayout) findViewById(R.id.linear_selc_pro);//选择项目UI条目
        linear_search = (LinearLayout) findViewById(R.id.linear_search);//搜索蓝牙
        mTv_title = (TextView) findViewById(R.id.tv_title);
        tv_proName = (TextView) findViewById(R.id.tv_proName);
        mTv_search = (TextView) findViewById(R.id.tv_search);
        img_loading = (ImageView) findViewById(R.id.img_loading);
        mLv_blt = (ListView) findViewById(R.id.listview_bluetooth);
        mSpinner_village = (Spinner) findViewById(R.id.spinner_village);
        mTv_title.setText("连接门禁设备");
        linear_back.setOnClickListener(this);
        linear_search.setOnClickListener(this);
    }

    private void initData() {
        Glide.with(AddBluetoothActivity.this).load(R.drawable.loadgif).into(img_loading);
        mBtData = new ArrayList();
        sumDataList = new ArrayList<>();
        mHandler = new Handler();

        //初始化蓝牙列表
        mBlueTInfoAdapter = new LvBlueTInfoAdapter(this, mBtData);
        mLv_blt.setAdapter(mBlueTInfoAdapter);
        mLv_blt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ProjectMsg msg = mBtData.get(i);
                String toNext = msg.getToNext();
                if ("0".equals(toNext)) {
                    ToastUtils.showToast(AddBluetoothActivity.this, "该设备不在附近，不可连接");
                    return;
                }
                //连接点击的蓝牙设备
                connectBT(i);
                //判断是否联网
                boolean networkAvalible = IsInternetUtil.isNetworkAvalible(AddBluetoothActivity.this);
                if (networkAvalible) {//有网,通知服务器开了哪个门
                    String id = msg.getId();
                    String type = msg.getType();
                    sendOpenMsgToService(id, type);
                }
            }
        });

        //详细地址填充
        dataDetList = new ArrayList<>();
        ProjectMsg projectInfo = new ProjectMsg();
        projectInfo.setProject_name("请选择项目");
        dataDetList.add(projectInfo);
        mSpDetAdapter = new ProSpinnerAdapter(AddBluetoothActivity.this, dataDetList);
        mSpinner_village.setAdapter(mSpDetAdapter);
        mSpinner_village.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //获取点击条目ID
                ProjectMsg projectInfo = dataDetList.get(i);
                String project_name = projectInfo.getProject_name();
                if (!project_name.equals("请选择项目")) {
                    mUpperID = projectInfo.getUpperID();
                    mBlueOpenInfo = projectInfo.getDetail_name();//信息包
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            ToastUtils.showToast(this, "您的手机不支持BLE设备");
            finish();
        }
        // 初始化蓝牙
        init_ble();
        scan_flag = true;

        //判断是否联网
        boolean networkAvalible = IsInternetUtil.isNetworkAvalible(this);
        if (networkAvalible) {
            //获取刷卡信息包
            getBlueOpenInfo();
        } else {
            //没有网络，获取本地存储数据解析
            getLocalBlueInfo();
        }

        //自动搜索
        //        mProhandler = new Handler();
        //        mProhandler.postDelayed(new Runnable() {
        //            public void run() {
        //                mProhandler.postDelayed(this, 1000);//递归执行，一秒执行一次
        //                count--;
        //                if (count == 0) {
        //                    //连接时间超过*分钟，可关闭界面
        //                    ToastUtils.showToast(AddBluetoothActivity.this, "超出连接时间，请退出重新连接");
        //                    mProhandler.removeCallbacks(this);
        //                    //finish();
        //                } else {
        //                    if (autoOpen && mBtData.size() == 1) {
        //                        connectBT(0);
        //                        autoOpen = false;
        //                    }
        //                }
        //            }
        //        }, 1000);

        //        if (BluetoothManagerUtils.isBluetoothEnabled()) {
        //            //蓝牙是打开的情况下自动搜索
        //            scanLeDevice(true);
        //        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.linear_back:
                finish();
                break;
            case R.id.linear_search:
                if (null == mBlueOpenInfo || "".equals(mBlueOpenInfo)) {
                    ToastUtils.showToast(AddBluetoothActivity.this, "未获取到刷卡信息");
                    return;
                }
                //判断权限
                needLoactionRight();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (BluetoothManagerUtils.isBluetoothEnabled()) {
            //蓝牙是打开的情况下自动搜索
            scanLeDevice(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //        mProhandler.removeCallbacksAndMessages(null);
        //        mProhandler = null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE) {
            switch (resultCode) {
                case Activity.RESULT_OK:// 点击确认按钮
                    scanLeDevice(true);
                    break;
                case Activity.RESULT_CANCELED: // 点击取消按钮或点击返回键
                    //用户拒绝打开 Bluetooth, Bluetooth 不会被开启
                    ToastUtils.showToast(this, "开启蓝牙功能，才能连接设备");
                    break;
                default:
                    break;
            }
        }
        if (requestCode == REQUEST_BLE_CODE) {
            if (resultCode == RESULT_BLE_CODE) {
                finish();
            }
        }
    }

    private void init_ble() {
        // 手机硬件支持蓝牙
        if (!getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "不支持BLE", Toast.LENGTH_SHORT).show();
            finish();
        }
        // Initializes Bluetooth adapter.
        // 获取手机本地的蓝牙适配器
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        // 打开蓝牙权限
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    //获取刷卡信息包
    private void getBlueOpenInfo() {
        ProgressDialogUtil.startShow(this, "正在查找,请稍等");
        UserInfo userinfo = SPref.getObject(this, UserInfo.class, "userinfo");
        String id = userinfo.getUserid();
        String url = NetConfig.USERCARD;
        RequestParamsFM params = new RequestParamsFM();
        params.put("user_id", id);
        HttpOkhUtils.getInstance().doGetWithParams(url, params, new HttpOkhUtils.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                ProgressDialogUtil.hideDialog();
                ToastUtils.showToast(AddBluetoothActivity.this, "网络连接错误");
            }

            @Override
            public void onSuccess(int code, String resbody) {
                ProgressDialogUtil.hideDialog();
                if (code != 200) {
                    ToastUtils.showToast(AddBluetoothActivity.this, "网络错误，获取刷卡信息失败");
                    return;
                }
                Gson gson = new Gson();
                BlueOpenInfo info = gson.fromJson(resbody, BlueOpenInfo.class);
                List<BlueOpenInfo.ArrBean> arr = info.getArr();
                for (BlueOpenInfo.ArrBean bean : arr) {
                    ProjectMsg proInfo = new ProjectMsg();
                    proInfo.setProject_name(bean.getProjectname());
                    proInfo.setDetail_name(bean.getXinxi());
                    proInfo.setUpperID(bean.getProjectdetail_id());//小区id
                    dataDetList.add(proInfo);
                    List<BlueOpenInfo.ArrBean.LanyaBean> lanya = bean.getLanya();
                    for (BlueOpenInfo.ArrBean.LanyaBean lanyaBean : lanya) {
                        String fangxiang = lanyaBean.getFangxiang();
                        String id1 = lanyaBean.getId();//蓝牙id
                        if ("0".equals(fangxiang)) {
                            ProjectMsg lanyaInfo = new ProjectMsg();
                            lanyaInfo.setProject_name(lanyaBean.getName1());
                            lanyaInfo.setId(id1);//具体门的id
                            lanyaInfo.setUpperID(bean.getProjectdetail_id());//所属小区id
                            lanyaInfo.setType("1");
                            lanyaInfo.setDetail_name(lanyaBean.getAddress1());//蓝牙地址
                            sumDataList.add(lanyaInfo);
                        } else {
                            ProjectMsg lanyaInfo1 = new ProjectMsg();
                            lanyaInfo1.setProject_name(lanyaBean.getName1());
                            lanyaInfo1.setDetail_name(lanyaBean.getAddress1());
                            lanyaInfo1.setId(id1);
                            lanyaInfo1.setUpperID(bean.getProjectdetail_id());//所属小区id
                            lanyaInfo1.setType("1");
                            ProjectMsg lanyaInfo2 = new ProjectMsg();
                            lanyaInfo2.setProject_name(lanyaBean.getName2());
                            lanyaInfo2.setDetail_name(lanyaBean.getAddress2());
                            lanyaInfo2.setId(id1);
                            lanyaInfo2.setUpperID(bean.getProjectdetail_id());//所属小区id
                            lanyaInfo2.setType("2");
                            sumDataList.add(lanyaInfo1);
                            sumDataList.add(lanyaInfo2);
                        }
                    }
                }
                mSpDetAdapter.notifyDataSetChanged();
                if (dataDetList.size() == 1) {
                    linear_selc_pro.setVisibility(View.GONE);
                    ToastUtils.showToast(AddBluetoothActivity.this, "您未绑定任何项目，无法使用设备搜索功能");
                } else if (dataDetList.size() == 2) {
                    mSpinner_village.setSelection(1);
                    mBlueOpenInfo = dataDetList.get(1).getDetail_name();
                    linear_selc_pro.setVisibility(View.GONE);
                    //只绑定一个蓝牙设备、、自动开门
                    //autoOpen = true;
                    tv_proName.setText(dataDetList.get(1).getProject_name());
                }
                //本地保存授权蓝牙信息
                SpUtils.putString(AddBluetoothActivity.this, "BlueRight", resbody);
            }
        });
    }

    private void connectBT(int position) {
        ProjectMsg btDevice = mBtData.get(position);
        final Intent intent;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            intent = new Intent(AddBluetoothActivity.this, Ble_Activityblewo8.class);
            intent.putExtra(Ble_Activityblewo8.EXTRAS_DEVICE_NAME, btDevice.getProject_name());
            intent.putExtra(Ble_Activityblewo8.EXTRAS_DEVICE_ADDRESS, btDevice.getDetail_name());
            intent.putExtra("blueOpenInfo", mBlueOpenInfo);
        } else {
            intent = new Intent(AddBluetoothActivity.this, Ble_Activity.class);
            intent.putExtra(Ble_Activity.EXTRAS_DEVICE_NAME, btDevice.getProject_name());
            intent.putExtra(Ble_Activity.EXTRAS_DEVICE_ADDRESS, btDevice.getDetail_name());
        }
        intent.putExtra("blueOpenInfo", mBlueOpenInfo);
        if (mScanning) {
            /* 停止扫描设备 */
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
            mScanning = false;
            mTv_search.setText("开始搜索");
            img_loading.setVisibility(View.INVISIBLE);
        }
        try {
            // 启动Ble_Activity
            startActivityForResult(intent, REQUEST_BLE_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
        }
    }

    /**
     * 蓝牙扫描回调函数 实现扫描蓝牙设备，回调蓝牙BluetoothDevice，可以获取name MAC等信息
     **/
    private boolean isHave;
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, byte[] scanRecord) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    /* 讲扫描到设备的信息输出到listview的适配器 */
                    //先判断扫描到的蓝牙设备是不是已绑定的设备//是就添加到listview//不是的忽略
                    for (ProjectMsg msg : sumDataList) {
                        isHave = false;
                        for (ProjectMsg dev : mBtData) {
                            if (msg.getDetail_name().equals(dev.getDetail_name()))
                                isHave = true;
                        }
                        if (!isHave) {
                            if (null != msg.getDetail_name() && !"".equals(msg.getDetail_name())) {
                                if (msg.getDetail_name().equals(device.getAddress())) {
                                    ProjectMsg proMsg = new ProjectMsg();
                                    proMsg.setProject_name(msg.getProject_name());
                                    proMsg.setDetail_name(msg.getDetail_name());
                                    proMsg.setToNext("1");
                                    proMsg.setId(msg.getId());
                                    proMsg.setType(msg.getType());
                                    proMsg.setRssi(rssi);
                                    mBtData.add(proMsg);
                                }
                            }
                        }
                    }
                    //给已添加的蓝牙设备按强度排序
                    //排序,信号强的在前面
                    for (int m = 0; m < mBtData.size(); m++) {
                        ProjectMsg msg = mBtData.get(m);
                        for (int n = m + 1; n < mBtData.size(); n++) {
                            ProjectMsg msg1 = mBtData.get(n);
                            if ("1".equals(msg.getToNext()) && "1".equals(msg1.getToNext())) {
                                if (msg.getRssi() < msg1.getRssi()) {
                                    mBtData.set(m, msg1);
                                    mBtData.set(n, msg);
                                }
                            }
                        }
                    }
                    mBlueTInfoAdapter.notifyDataSetChanged();
                }
            });
        }
    };

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    scan_flag = true;
                    mTv_search.setText("开始连接");
                    img_loading.setVisibility(View.INVISIBLE);
                    Log.i("SCAN", "stop.....................");
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                }
            }, SCAN_PERIOD);
            /* 开始扫描蓝牙设备，带mLeScanCallback 回调函数 */
            Log.i("SCAN", "begin.....................");
            mScanning = true;
            scan_flag = false;
            mTv_search.setText("停止连接");
            ToastUtils.showToast(this, "正在连接。。。");
            mBluetoothAdapter.startLeScan(mLeScanCallback);
            img_loading.setVisibility(View.VISIBLE);
            mBtData.clear();
            mBlueTInfoAdapter.notifyDataSetChanged();
        } else {
            Log.i("Stop", "stoping................");
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
            img_loading.setVisibility(View.INVISIBLE);
            scan_flag = true;
        }
    }

    private void getLocalBlueInfo() {
        String blueRightInfo = SpUtils.getString(this, "BlueRight", "");
        if (null == blueRightInfo || "".equals(blueRightInfo)) {
            ToastUtils.showToast(this, "未获取到本地设备信息");
        } else {
            Gson gson = new Gson();
            BlueOpenInfo info = gson.fromJson(blueRightInfo, BlueOpenInfo.class);
            List<BlueOpenInfo.ArrBean> arr = info.getArr();
            for (BlueOpenInfo.ArrBean bean : arr) {
                ProjectMsg proInfo = new ProjectMsg();
                proInfo.setProject_name(bean.getProjectname());
                proInfo.setDetail_name(bean.getXinxi());
                proInfo.setUpperID(bean.getProjectdetail_id());//小区id
                dataDetList.add(proInfo);
                List<BlueOpenInfo.ArrBean.LanyaBean> lanya = bean.getLanya();
                for (BlueOpenInfo.ArrBean.LanyaBean lanyaBean : lanya) {
                    String fangxiang = lanyaBean.getFangxiang();
                    if ("0".equals(fangxiang)) {
                        ProjectMsg lanyaInfo = new ProjectMsg();
                        lanyaInfo.setProject_name(lanyaBean.getName1());
                        lanyaInfo.setDetail_name(lanyaBean.getAddress1());
                        sumDataList.add(lanyaInfo);
                    } else {
                        ProjectMsg lanyaInfo1 = new ProjectMsg();
                        lanyaInfo1.setProject_name(lanyaBean.getName1());
                        lanyaInfo1.setDetail_name(lanyaBean.getAddress1());
                        ProjectMsg lanyaInfo2 = new ProjectMsg();
                        lanyaInfo2.setProject_name(lanyaBean.getName2());
                        lanyaInfo2.setDetail_name(lanyaBean.getAddress2());
                        sumDataList.add(lanyaInfo1);
                        sumDataList.add(lanyaInfo2);
                    }
                }
            }
            if (dataDetList.size() == 1) {
                linear_selc_pro.setVisibility(View.GONE);
                ToastUtils.showToast(AddBluetoothActivity.this, "您未绑定任何项目，无法使用设备连接功能");
            } else if (dataDetList.size() == 2) {
                mSpinner_village.setSelection(1);
                mBlueOpenInfo = dataDetList.get(1).getDetail_name();
                linear_selc_pro.setVisibility(View.GONE);
            }
        }
    }

    //发送开门信息给服务器
    private void sendOpenMsgToService(String deviceID, String type) {
        UserInfo userinfo = SPref.getObject(this, UserInfo.class, "userinfo");
        String id = userinfo.getUserid();
        String insertMJUrl = NetConfig.INSERTMENJIN;
        RequestParamsFM params = new RequestParamsFM();
        params.put("userid", id);
        params.put("lanyaid", deviceID);
        params.put("type", type);
        params.setUseJsonStreamer(true);
        HttpOkhUtils.getInstance().doPost(insertMJUrl, params, new HttpOkhUtils.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                ToastUtils.showToast(AddBluetoothActivity.this, "网络连接错误");
            }

            @Override
            public void onSuccess(int code, String resbody) {
                if (code == 200) {
                    Gson gson = new Gson();
                    LoginInfo loginInfo = gson.fromJson(resbody, LoginInfo.class);
                    String result = loginInfo.getResult();
                    if ("1".equals(result)) {
                        ToastUtils.showToast(AddBluetoothActivity.this, "提交成功");
                    }
                } else {
                    ToastUtils.showToast(AddBluetoothActivity.this, "网络连接错误");
                }
            }
        });
    }

    //申请定位权限
    private void needLoactionRight() {
        //判断是否已经赋予权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //如果应用之前请求过此权限但用户拒绝了请求，此方法将返回 true。
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                //这里可以写个对话框之类的项向用户解释为什么要申请权限，并在对话框的确认键后续再次申请权限
                ToastUtils.showToast(this, "请手动开启定位、蓝牙相关权限");
            } else {
                //申请权限，字符串数组内是一个或多个要申请的权限，1是申请权限结果的返回参数，在onRequestPermissionsResult可以得知申请结果
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,}, 1);
            }
        } else {
            if (scan_flag) {
                scanLeDevice(true);
            } else {
                scanLeDevice(false);
                mTv_search.setText("开始连接");
            }
        }
    }

    public class BluetoothListenerReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case BluetoothAdapter.ACTION_STATE_CHANGED:
                    int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
                    switch (blueState) {
                        case BluetoothAdapter.STATE_TURNING_ON:
                            //                            Log.e("onReceive---------蓝牙正在打开中");

                            break;
                        case BluetoothAdapter.STATE_ON:
                            //                            Log.e("onReceive---------蓝牙已经打开");
                            scanLeDevice(true);
                            break;
                        case BluetoothAdapter.STATE_TURNING_OFF:
                            //                            Log.e("onReceive---------蓝牙正在关闭中");

                            break;
                        case BluetoothAdapter.STATE_OFF:
                            //                            Log.e("onReceive---------蓝牙已经关闭");

                            break;
                    }
                    break;
            }

        }
    }

    private SearchBlueThBcr mReceiver;

    private void registerRec() {
        //3.注册蓝牙广播
        mReceiver = new SearchBlueThBcr(mBtData, mBlueTInfoAdapter, sumDataList);
        mReceiver.setUI(mTv_search, img_loading, mUpperID);
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);//搜索到蓝牙
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);//搜索结束
        registerReceiver(mReceiver, filter);
    }

    //    @Override
    //    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    //        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    //        switch (requestCode) {
    //            case 1:
    //                //用户选择开启 Bluetooth，Bluetooth 会被开启
    //                ToastUtils.showToast(this, "蓝牙开启了");
    //                //开始搜索
    //                //                startSearchBluetooth();
    //                break;
    //            default:
    //                break;
    //        }
    //    }

    //
    //    boolean bluetoothSupported = BluetoothManagerUtils.isBluetoothSupported();
    //                    if (bluetoothSupported) {
    //        boolean bluetoothEnabled = BluetoothManagerUtils.isBluetoothEnabled();
    //        if (!bluetoothEnabled) {
    //            //弹出对话框提示用户是否打开
    //            Intent enabler = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
    //            // 设置 Bluetooth 设备可以被其它 Bluetooth 设备扫描到
    //            enabler.setAction(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
    //            // 设置 Bluetooth 设备可见时间
    //            enabler.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, BLUETOOTH_DISCOVERABLE_DURATION);
    //            startActivityForResult(enabler, REQUEST_ENABLE);
    //        } else {
    //            //开始搜索
    //            needLoactionRight();
    //        }
    //    } else {
    //        ToastUtils.showToast(this, "当前设备不支持蓝牙功能");
    //    }
}
