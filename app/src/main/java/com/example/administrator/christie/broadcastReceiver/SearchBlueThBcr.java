package com.example.administrator.christie.broadcastReceiver;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.example.administrator.christie.util.ToastUtils;

import java.util.List;

/**
 * @创建者 AndyYan
 * @创建时间 2018/4/20 15:42
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class SearchBlueThBcr extends BroadcastReceiver {
    private List        mList;
    private BaseAdapter mAdapter;

    public SearchBlueThBcr(List mData, BaseAdapter adapter) {
        this.mList = mData;
        this.mAdapter = adapter;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "触发广播", Toast.LENGTH_SHORT).show();
        String action = intent.getAction();
        switch (action) {
            case BluetoothDevice.ACTION_FOUND:
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Toast.makeText(context, "找到设备" + device.getName(), Toast.LENGTH_SHORT).show();
                if (mAdapter != null) {
                    mList.add(device);
                    mAdapter.notifyDataSetChanged();
                }
                break;
            case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                ToastUtils.showToast(context,"搜索结束");
                //                mMessageAdapter.addMessage("搜索结束");
                break;
        }
    }
}
