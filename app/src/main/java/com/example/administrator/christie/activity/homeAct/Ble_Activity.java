package com.example.administrator.christie.activity.homeAct;

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
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.administrator.christie.R;
import com.example.administrator.christie.service.BluetoothLeService;
import com.example.administrator.christie.util.TDESDoubleUtils;
import com.example.administrator.christie.util.ToastUtils;
import com.skyfishjy.library.RippleBackground;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @创建者 AndyYan
 * @创建时间 2019/2/23 21:59
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class Ble_Activity extends AppCompatActivity implements View.OnClickListener {
    private final static String  TAG                    = Ble_Activity.class.getSimpleName();
    //蓝牙4.0的UUID,其中0000ffe1-0000-1000-8000-00805f9b34fb是广州汇承信息科技有限公司08蓝牙模块的UUID
    public static        String  HEART_RATE_MEASUREMENT = "0000ffe1-0000-1000-8000-00805f9b34fb";
    public static        String  EXTRAS_DEVICE_NAME     = "DEVICE_NAME";
    public static        String  EXTRAS_DEVICE_ADDRESS  = "DEVICE_ADDRESS";
    public static        String  EXTRAS_DEVICE_RSSI     = "RSSI";
    //蓝牙连接状态
    private              boolean mConnected             = false;
    private              String  status                 = "disconnected";
    //蓝牙名字
    private String mDeviceName;
    //蓝牙地址
    private String mDeviceAddress;
    //蓝牙信号值
    private String mRssi;
    private Bundle b;
    private String rev_str  = "";
    private String rev_cont = "";
    //蓝牙service,负责后台的蓝牙服务
    private static BluetoothLeService mBluetoothLeService;
    //文本框，显示接受的内容
    private        TextView           rev_tv, connect_state;
    //发送按钮
    private Button     send_btn;
    //文本编辑框
    private EditText   send_et;
    private ScrollView rev_sv;
    private Handler    mProhandler;
    private int count = 180;//搜索时间、单位秒
    private boolean          isSended;
    private TextView         tv_title;
    private LinearLayout     linear_back;
    private RippleBackground rippleBackground;//脉冲图像
    private        ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
    //蓝牙特征值
    private static BluetoothGattCharacteristic                       target_chara         = null;
    private        Handler                                           mhandler             = new Handler();
    private        int                                               RESULT_BLE_CODE      = 10999;//开门界面响应码
    private        Handler                                           myHandler            = new Handler() {
        // 2.重写消息处理函数
        public void handleMessage(Message msg) {
            switch (msg.what) {
                // 判断发送的消息
                case 1:
                    // 更新View
                    String state = msg.getData().getString("connect_state");
                    // connect_state.setText(state);
                    if ("connected".equals(state)) {
                        ToastUtils.showToast(Ble_Activity.this, "蓝牙已连接");
                        mhandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //                                if (!isSended) {
                                //                                    //发送指令一
                                //                                    sendMsg("<010000>", 0);
                                //                                }
                                if (mConnected) {
                                    if (null != mBluetoothLeService && null != mBluetoothLeService.getBlueGatt() && null != target_chara) {
                                        if (!isSended) {
                                            //发送指令一
                                            sendMsg("<010000>", 0);
                                        }
                                    }
                                }
                            }
                        }, 1500);
                    } else {
                        ToastUtils.showToast(Ble_Activity.this, "蓝牙连接中断，请退出重新连接");
                    }
                    break;
                case 1002:
                    Log.i(TAG, "下面第二次发送");
                    String substr = rev_cont.substring(5, 13);
                    String spliStr = substr + "00000000";
                    String key = "DE7FF98AF2D4CED32BA64F9B4708F980";
                    String content = TDESDoubleUtils.encryptECB3Des(key, spliStr);
                    send_et.setText("<0200" + content + "00>");
                    sendMsg("<0200" + content + "00>", 0);
                    break;
                case 1003:
                    Log.i(TAG, "下面第三次发送");
                    send_et.setText("<05F16D9DD21E73FE716000>");
                    sendMsg("<05F16D9DD21E73FE716000>", 0);
                    break;
                case 1004:
                    Log.i(TAG, "开门成功");
                    setResult(RESULT_BLE_CODE);
                    finish();
                    break;
            }
            super.

                    handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ble_activity);
        b = getIntent().getExtras();
        //从意图获取显示的蓝牙信息
        mDeviceName = b.getString(EXTRAS_DEVICE_NAME);
        mDeviceAddress = b.getString(EXTRAS_DEVICE_ADDRESS);
        mRssi = b.getString(EXTRAS_DEVICE_RSSI);

		/* 启动蓝牙service */
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
        init();
    }

    // Activity出来时候，绑定广播接收器，监听蓝牙连接服务传过来的事件
    @Override
    protected void onResume() {
        super.onResume();
        //绑定广播接收器
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
        //解除广播接收器
        unregisterReceiver(mGattUpdateReceiver);
        mBluetoothLeService = null;
    }

    /*
     * 按键的响应事件，主要发送文本框的数据
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linear_back:
                finish();
                break;
            case R.id.send_btn:
                if (null != target_chara)
                    sendMsg(send_et.getText().toString(), 0);
                break;
        }
    }

    private void init() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        linear_back = (LinearLayout) findViewById(R.id.linear_back);
        rippleBackground = (RippleBackground) findViewById(R.id.content);
        rev_sv = (ScrollView) this.findViewById(R.id.rev_sv);
        rev_tv = (TextView) this.findViewById(R.id.rev_tv);
        connect_state = (TextView) this.findViewById(R.id.connect_state);
        send_btn = (Button) this.findViewById(R.id.send_btn);
        send_et = (EditText) this.findViewById(R.id.send_et);
        tv_title.setText(mDeviceName);
        linear_back.setOnClickListener(this);
        //波纹动画
        rippleBackground.startRippleAnimation();
        //        connect_state.setText(status);
        //        send_btn.setOnClickListener(this);
        //        send_et.setText("<010000>");

        //        mProhandler = new Handler();
        //        mProhandler.postDelayed(new Runnable() {
        //            public void run() {
        //                mProhandler.postDelayed(this, 2000);//递归执行，一秒执行一次
        //                count--;
        //                if (count == 0) {
        //                    //连接时间超过*分钟，可关闭界面
        //                    mProhandler.removeCallbacks(this);
        //                } else {
        //                    if (mConnected) {
        //                        if (null != mBluetoothLeService && null != mBluetoothLeService.getBlueGatt() && null != target_chara) {
        //                            if (!isSended) {
        //                                //发送指令一
        //                                sendMsg("<010000>", 0);
        //                            }
        //                        }
        //                    }
        //                }
        //            }
        //        }, 2000);
    }

    /**
     * @param @param rev_string(接受的数据)
     * @return void
     * @throws
     * @Title: displayData
     * @Description:
     */
    private void displayData(final String rev_string) {
        if ("".equals(rev_string)) {
            rev_str += rev_string + "-";
        } else {
            rev_str += rev_string;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                rev_tv.setText(rev_str);
                rev_sv.scrollTo(0, rev_tv.getMeasuredHeight());
                if (rev_string.startsWith("<0100")) {
                    //发送完毕
                    isSended = true;
                    rev_cont = rev_string;
                    mhandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            myHandler.sendEmptyMessage(1002);
                        }
                    }, 500);
                } else if (rev_string.startsWith("<020002>")) {
                    rev_cont = rev_string;
                    mhandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            myHandler.sendEmptyMessage(1003);
                        }
                    }, 500);
                } else if (rev_string.startsWith("<050005>")) {
                    rev_cont = rev_string;
                    myHandler.sendEmptyMessage(1004);
                }
            }
        });
    }

    /* BluetoothLeService绑定的回调函数 */
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }
            // Automatically connects to the device upon successful start-up
            // initialization.
            // 根据蓝牙地址，连接设备
            mBluetoothLeService.connect(mDeviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    /**
     * 广播接收器，负责接收BluetoothLeService类发送的数据
     */
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action))//Gatt连接成功
            {
                mConnected = true;
                status = "connected";
                //更新连接状态
                updateConnectionState(status);
                System.out.println("BroadcastReceiver :" + "device connected");

            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED//Gatt连接失败
                    .equals(action)) {
                mConnected = false;
                status = "disconnected";
                //更新连接状态
                updateConnectionState(status);
                System.out.println("BroadcastReceiver :" + "device disconnected");

            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED//发现GATT服务器
                    .equals(action)) {
                // Show all the supported services and characteristics on the
                // user interface.
                //获取设备的所有蓝牙服务
                displayGattServices(mBluetoothLeService.getSupportedGattServices());
                System.out.println("BroadcastReceiver :" + "device SERVICES_DISCOVERED");
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {//有效数据
                //处理发送过来的数据
                displayData(intent.getExtras().getString(BluetoothLeService.EXTRA_DATA));
                System.out.println("BroadcastReceiver onData:" + intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
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
        System.out.println("connect_state:" + status);
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

    /**
     * @param
     * @return void
     * @throws
     * @Title: displayGattServices
     * @Description: TODO(处理蓝牙服务)
     */
    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null)
            return;
        String uuid = null;
        String unknownServiceString = "unknown_service";
        String unknownCharaString = "unknown_characteristic";

        // 服务数据,可扩展下拉列表的第一级数据
        ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<HashMap<String, String>>();

        // 特征数据（隶属于某一级服务下面的特征值集合）
        ArrayList<ArrayList<HashMap<String, String>>> gattCharacteristicData = new ArrayList<ArrayList<HashMap<String, String>>>();

        // 部分层次，所有特征值集合
        mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();

        // Loops through available GATT Services.
        for (BluetoothGattService gattService : gattServices) {

            // 获取服务列表
            HashMap<String, String> currentServiceData = new HashMap<String, String>();
            uuid = gattService.getUuid().toString();

            // 查表，根据该uuid获取对应的服务名称。SampleGattAttributes这个表需要自定义。

            gattServiceData.add(currentServiceData);

            System.out.println("Service uuid:" + uuid);

            ArrayList<HashMap<String, String>> gattCharacteristicGroupData = new ArrayList<HashMap<String, String>>();

            // 从当前循环所指向的服务中读取特征值列表
            List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();

            ArrayList<BluetoothGattCharacteristic> charas = new ArrayList<BluetoothGattCharacteristic>();

            // Loops through available Characteristics.
            // 对于当前循环所指向的服务中的每一个特征值
            for (final BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                charas.add(gattCharacteristic);
                HashMap<String, String> currentCharaData = new HashMap<String, String>();
                uuid = gattCharacteristic.getUuid().toString();

                if (gattCharacteristic.getUuid().toString().equals(HEART_RATE_MEASUREMENT)) {
                    // 测试读取当前Characteristic数据，会触发mOnDataAvailable.onCharacteristicRead()
                    mhandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            try {
                                mBluetoothLeService.readCharacteristic(gattCharacteristic);
                            } catch (Exception e) {
                            }
                        }
                    }, 200);

                    // 接受Characteristic被写的通知,收到蓝牙模块的数据后会触发mOnDataAvailable.onCharacteristicWrite()
                    mBluetoothLeService.setCharacteristicNotification(gattCharacteristic, true);
                    target_chara = gattCharacteristic;
                    // 设置数据内容
                    // 往蓝牙模块写入数据
                    // mBluetoothLeService.writeCharacteristic(gattCharacteristic);
                }
                List<BluetoothGattDescriptor> descriptors = gattCharacteristic.getDescriptors();
                for (BluetoothGattDescriptor descriptor : descriptors) {
                    System.out.println("---descriptor UUID:" + descriptor.getUuid());
                    // 获取特征值的描述
                    mBluetoothLeService.getCharacteristicDescriptor(descriptor);
                    // mBluetoothLeService.setCharacteristicNotification(gattCharacteristic,
                    // true);
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
     **/
    public int[] dataSeparate(int len) {
        int[] lens = new int[2];
        lens[0] = len / 20;
        lens[1] = len - 20 * lens[0];
        return lens;
    }

    public void sendMsg(String msg, int num) {
        byte[] buff = msg.getBytes();
        int len = buff.length;
        int[] lens = dataSeparate(len);
        for (int i = 0; i < lens[0]; i++) {
            String str = new String(buff, 20 * i, 20);
            target_chara.setValue(str);//只能一次发送20字节，所以这里要分包发送
            //调用蓝牙服务的写特征值方法实现发送数据
            boolean sendSuc = mBluetoothLeService.writeCharacteristic(target_chara);
            if (sendSuc) {
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        if (lens[1] != 0) {
            String str = new String(buff, 20 * lens[0], lens[1]);
            target_chara.setValue(str);
            //调用蓝牙服务的写特征值方法实现发送数据
            boolean sendSuc = mBluetoothLeService.writeCharacteristic(target_chara);
            if (sendSuc) {//分包的最后一个包发送成功//TODO:

            }
        }
    }

}
