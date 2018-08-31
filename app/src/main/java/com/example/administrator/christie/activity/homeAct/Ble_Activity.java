package com.example.administrator.christie.activity.homeAct;

import android.app.Activity;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.administrator.christie.R;
import com.example.administrator.christie.service.BluetoothLeService;
import com.example.administrator.christie.util.ShakeHelper;
import com.example.administrator.christie.util.TDESDoubleUtils;
import com.example.administrator.christie.util.ToastUtils;
import com.skyfishjy.library.RippleBackground;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @创建者 AndyYan
 * @创建时间 2018/7/26 15:02
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class Ble_Activity extends Activity implements View.OnClickListener {

    //蓝牙4.0的UUID,其中0000ffe1-0000-1000-8000-00805f9b34fb是广州汇承信息科技有限公司08蓝牙模块的UUID
    public static String  HEART_RATE_MEASUREMENT = "0000ffe1-0000-1000-8000-00805f9b34fb";
    public static String  EXTRAS_DEVICE_NAME     = "DEVICE_NAME";
    public static String  EXTRAS_DEVICE_ADDRESS  = "DEVICE_ADDRESS";
    //蓝牙连接状态
    private       boolean mConnected             = false;
    private       String  status                 = "disconnected";
    //蓝牙地址
    private String mDeviceAddress;
    private Bundle b;
    private String rev_str = "";
    //蓝牙service,负责后台的蓝牙服务
    private static BluetoothLeService mBluetoothLeService;
    //文本框，显示接受的内容
    private        TextView           rev_tv, connect_state;
    //发送按钮
    //    private Button     send_btn;
    //文本编辑框
    //    private EditText   send_et;
    private ScrollView rev_sv;
    private        ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
    //蓝牙特征值
    private static BluetoothGattCharacteristic                       target_chara         = null;
    private        Handler                                           mhandler             = new Handler();
    private        Handler                                           mSendMsgHandler      = new Handler();
    private String    mDeviceName;
    private TextView  tv_title;
    private ImageView img_back;
    private int times = 0;//记录是第几次连接
    private ShakeHelper      mShakeHelper;//振动类
    private RippleBackground rippleBackground;//脉冲图像
    private ImageView        centerImage;
    private String           mBlueOpenInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ble_activity);
        b = getIntent().getExtras();
        //从意图获取显示的蓝牙信息
        mDeviceAddress = b.getString(EXTRAS_DEVICE_ADDRESS);
        mDeviceName = b.getString(EXTRAS_DEVICE_NAME);
        mBlueOpenInfo = getIntent().getStringExtra("blueOpenInfo");
        /* 启动蓝牙service */
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initView();
        initData();
    }

    private void initView() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        img_back = (ImageView) findViewById(R.id.img_back);
        rev_sv = (ScrollView) this.findViewById(R.id.rev_sv);
        rev_tv = (TextView) this.findViewById(R.id.rev_tv);
        connect_state = (TextView) this.findViewById(R.id.connect_state);
        // send_btn = (Button) this.findViewById(R.id.send_btn);
        // send_et = (EditText) this.findViewById(R.id.send_et);
        rippleBackground = (RippleBackground) findViewById(R.id.content);
        centerImage = (ImageView) findViewById(R.id.centerImage);
    }

    private String needSend = "";//记录每次需传输数据

    private void initData() {
        tv_title.setText(mDeviceName);
        img_back.setOnClickListener(this);
        connect_state.setText(status);
        // send_btn.setOnClickListener(this);
        // send_et.setText("<010000>");
        mShakeHelper = new ShakeHelper(this, myHandler);
        mShakeHelper.Start();
        mSendMsgHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mConnected) {
                    //在判断是第几次发送
                    if (times == 0) {
                        times = 1;
                    } else if (times == 1) {
                        //NO.1发送消息，获取四位随机数
                        sendMsg("<010000>");
                    } else if (times == 2) {
                        //NO.2发送外指令，部认证
                        //sendMsg("<02007F6098536D70BAC000>");7F6098536D70BAC0
                        String cont2 = "<0200" + needSend + "00>";
                        sendMsg(cont2);
                    } else if (times == 3) {
                        //Tip:发送模拟刷卡信息包时，蓝牙控制器对APP的外部认证必须已经成功，外部认证有效期持续3分钟，超出时间后需要重新执行外部认证。
                        //NO.3发送模拟刷卡信息包
                        String key = "71C5A4430AC94865C94A9B8710ECDD29";
                        // String testPackInfo = "000000004D928CFBCEAA6C01A48911B2";
                        String testPackInfo = mBlueOpenInfo;
                        //正式使用
                        //  String cont3 = TDESDoubleUtils.encryptECB3Des(key, testPackInfo);
                        //  String encryStr = "<05F2" + cont3 + "00>";

                        //测试，不需加密
                        String encryStr = "<05F1" + testPackInfo + "00>";
                        sendMsg(encryStr);
                        // sendMsg("<05F238DA815997A8C0B37779486399D5AFED00>");
                    }
                } else {
                    ToastUtils.showToast(Ble_Activity.this, "蓝牙连接中断，请退出重新连接");
                    //finish();
                }
                mSendMsgHandler.postDelayed(this, 1000);
            }
        }, 1000);
        rippleBackground.startRippleAnimation();
        centerImage.setOnClickListener(this);
    }

    private Handler myHandler = new Handler() {
        // 2.重写消息处理函数
        public void handleMessage(Message msg) {
            switch (msg.what) {
                // 判断发送的消息
                case 1:
                    // 更新View
                    String state = msg.getData().getString("connect_state");
                    connect_state.setText(state);
                    if ("connected".equals(state)) {
                        ToastUtils.showToast(Ble_Activity.this, "蓝牙已连接");
                    } else {
                        ToastUtils.showToast(Ble_Activity.this, "蓝牙连接中断，请退出重新连接");
                    }
                    break;
                case 10086://接收到消息要发送数据
                    String message = msg.getData().getString("needSendMsg");
                    String con = String.valueOf(connect_state.getText());
                    if ("ToSendMsg".equals(message)) {
                        //                        if ("connected".equals(con)) {//先判断蓝牙是否还连接着
                        //                            //在判断是第几次发送
                        //                            if (times == 1) {
                        //                                sendMsg("<010000>");
                        //                                send_et.setText("<02007F6098536D70BAC000>");
                        //                            } else if (times == 2) {
                        //                                sendMsg("<02007F6098536D70BAC000>");
                        //                                send_et.setText("<05F238DA815997A8C0B37779486399D5AFED00>");
                        //                            } else if (times == 3) {
                        //                                sendMsg("<05F238DA815997A8C0B37779486399D5AFED00>");
                        //                            }
                        //                            ToastUtils.showToast(Ble_Activity.this, "dier" + times);
                        //                            //                            String editCon = send_et.getText().toString();
                        //                            //                            sendMsg(editCon);
                        //                        } else {
                        //                            ToastUtils.showToast(Ble_Activity.this, "蓝牙连接中断，请重新连接");
                        //                            finish();
                        //                        }
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };

    /*rev_string(接受的数据)(接收到的数据在scrollview上显示)*/
    private void displayData(final String rev_string) {
        rev_str += rev_string;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                rev_tv.setText(rev_str);
                rev_sv.scrollTo(0, rev_tv.getMeasuredHeight());
                if (rev_string.length() < 6) {//第一次连接，接收门禁返回的数据
                    // times++;
                } else {
                    //                    String result = rev_string.substring(3, 5);
                    //                    if ("00".equals(result)) {//第二次及之后接收的结果，00表示成功
                    //                        if (times == 1) {
                    //                            String substr = rev_string.substring(5, 13);
                    //                            String spliStr = substr + "00000000";
                    //                            String key = "DE7FF98AF2D4CED32BA64F9B4708F980";
                    //                            String content = TDESDoubleUtils.encryptECB3Des(key, spliStr);
                    //                            needSend = content;
                    //                        }
                    //                        times++;
                    //                        if (times >= 4) {//已开门
                    //                            ToastUtils.showToast(Ble_Activity.this, "已开门，欢迎您");
                    //                            mSendMsgHandler.removeCallbacksAndMessages(null);
                    //                            finish();
                    //                        }
                    //                    }
                    if (rev_string.startsWith("<0100")) {
                        String substr = rev_string.substring(5, 13);
                        String spliStr = substr + "00000000";
                        String key = "DE7FF98AF2D4CED32BA64F9B4708F980";
                        String content = TDESDoubleUtils.encryptECB3Des(key, spliStr);
                        needSend = content;
                        times = 2;
                    }
                    if (rev_string.startsWith("<0200")) {
                        times = 3;
                    }
                    if (rev_string.startsWith("<0500")) {
                        ToastUtils.showToast(Ble_Activity.this, "已开门，欢迎您");
                        mSendMsgHandler.removeCallbacksAndMessages(null);
                        finish();
                    }
                }
            }
        });
    }

    // Activity出来时候，绑定广播接收器，监听蓝牙连接服务传过来的事件
    @Override
    protected void onResume() {
        super.onResume();
        //绑定广播接收器
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBluetoothLeService != null) {
            //根据蓝牙地址，建立连接
            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mConnected) {
            sendMsg("<0F0000>");
        }
        rippleBackground.stopRippleAnimation();
        mBluetoothLeService = null;
        mSendMsgHandler.removeCallbacksAndMessages(null);
        myHandler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mShakeHelper) {
            mShakeHelper.Stop();
        }
        //解除广播接收器
        unregisterReceiver(mGattUpdateReceiver);
    }

    /* BluetoothLeService绑定的回调函数 */
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                finish();
            }
            // 根据蓝牙地址，连接设备
            mBluetoothLeService.connect(mDeviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    /* 广播接收器，负责接收BluetoothLeService类发送的数据*/
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {//Gatt连接成功
                mConnected = true;
                status = "connected";
                //更新连接状态
                updateConnectionState(status);
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {//Gatt连接失败
                mConnected = false;
                status = "disconnected";
                //更新连接状态
                updateConnectionState(status);
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {//发现GATT服务器
                //获取设备的所有蓝牙服务
                displayGattServices(mBluetoothLeService.getSupportedGattServices());
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {//有效数据
                //处理发送过来的数据
                displayData(intent.getExtras().getString(BluetoothLeService.EXTRA_DATA));
            }
        }
    };

    /* 更新连接状态 */
    private void updateConnectionState(String status) {
        Message msg = new Message();
        msg.what = 1;
        Bundle b = new Bundle();
        b.putString("connect_state", status);
        msg.setData(b);
        //将连接状态更新的UI的textview上
        myHandler.sendMessage(msg);
    }

    /* 意图过滤器 */
    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    /* (处理蓝牙服务)*/
    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null)
            return;

        // 服务数据,可扩展下拉列表的第一级数据
        ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<HashMap<String, String>>();

        // 特征数据（隶属于某一级服务下面的特征值集合）
        ArrayList<ArrayList<HashMap<String, String>>> gattCharacteristicData = new ArrayList<ArrayList<HashMap<String, String>>>();

        // 部分层次，所有特征值集合
        mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();

        for (BluetoothGattService gattService : gattServices) {

            // 获取服务列表
            HashMap<String, String> currentServiceData = new HashMap<String, String>();

            // 查表，根据该uuid获取对应的服务名称。SampleGattAttributes这个表需要自定义。
            gattServiceData.add(currentServiceData);

            ArrayList<HashMap<String, String>> gattCharacteristicGroupData = new ArrayList<HashMap<String, String>>();

            // 从当前循环所指向的服务中读取特征值列表
            List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();

            ArrayList<BluetoothGattCharacteristic> charas = new ArrayList<BluetoothGattCharacteristic>();

            // Loops through available Characteristics.
            // 对于当前循环所指向的服务中的每一个特征值
            for (final BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                charas.add(gattCharacteristic);
                HashMap<String, String> currentCharaData = new HashMap<String, String>();

                if (gattCharacteristic.getUuid().toString().equals(HEART_RATE_MEASUREMENT)) {
                    // 测试读取当前Characteristic数据，会触发mOnDataAvailable.onCharacteristicRead()
                    mhandler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            mBluetoothLeService.readCharacteristic(gattCharacteristic);
                        }
                    }, 200);

                    // 接受Characteristic被写的通知,收到蓝牙模块的数据后会触发mOnDataAvailable.onCharacteristicWrite()
                    mBluetoothLeService.setCharacteristicNotification(gattCharacteristic, true);
                    target_chara = gattCharacteristic;
                }
                List<BluetoothGattDescriptor> descriptors = gattCharacteristic.getDescriptors();
                for (BluetoothGattDescriptor descriptor : descriptors) {
                    // 获取特征值的描述
                    mBluetoothLeService.getCharacteristicDescriptor(descriptor);
                }
                gattCharacteristicGroupData.add(currentCharaData);
            }
            // 按先后顺序，分层次放入特征值集合中，只有特征值
            mGattCharacteristics.add(charas);
            // 构件第二级扩展列表（服务下面的特征值）
            gattCharacteristicData.add(gattCharacteristicGroupData);
        }
    }

    /**
     * 将数据分包
     */
    public int[] dataSeparate(int len) {
        int[] lens = new int[2];
        lens[0] = len / 20;
        lens[1] = len - 20 * lens[0];
        return lens;
    }

    /* 发送按键的响应事件，主要发送文本框的数据*/
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //            case R.id.send_btn:
            //发送数据
            //                String msg = send_et.getText().toString();
            //                sendMsg(msg);
            //                break;
            case R.id.img_back:
                finish();
                break;
            case R.id.centerImage:
                break;
        }
    }

    private void sendMsg(String msg) {
        byte[] buff = msg.getBytes();
        int len = buff.length;
        int[] lens = dataSeparate(len);
        for (int i = 0; i < lens[0]; i++) {
            String str = new String(buff, 20 * i, 20);
            target_chara.setValue(str);//只能一次发送20字节，所以这里要分包发送
            //调用蓝牙服务的写特征值方法实现发送数据
            mBluetoothLeService.writeCharacteristic(target_chara);
        }
        if (lens[1] != 0) {
            String str = new String(buff, 20 * lens[0], lens[1]);
            target_chara.setValue(str);
            //调用蓝牙服务的写特征值方法实现发送数据
            mBluetoothLeService.writeCharacteristic(target_chara);
        }
    }
}
