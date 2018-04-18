package com.example.administrator.christie.util;

import android.bluetooth.BluetoothAdapter;

/**
 * @创建者 AndyYan
 * @创建时间 2018/4/18 11:14
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class BluetoothManagerUtils {

    public static BluetoothAdapter bluetoothAdapter;

    /**
     * 当前 Android 设备是否支持 Bluetooth
     * <p>
     *
     * @return true：支持 Bluetooth false：不支持 Bluetooth
     */

    public static boolean isBluetoothSupported() {
        return BluetoothAdapter.getDefaultAdapter() != null ? true : false;
    }

    /**
     * 当前 Android 设备的 bluetooth 是否已经开启
     * <p>
     *
     * @return true：Bluetooth 已经开启 false：Bluetooth 未开启
     */
    public static boolean isBluetoothEnabled() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null) {
            return bluetoothAdapter.isEnabled();
        }
        return false;
    }

    /**
     * 强制开启当前 Android 设备的 Bluetooth
     * <p>
     *
     * @return true：强制打开 Bluetooth　成功　false：强制打开 Bluetooth 失败
     */
    public static boolean turnOnBluetooth() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null) {
            return bluetoothAdapter.enable();
        }
        return false;
    }
}
