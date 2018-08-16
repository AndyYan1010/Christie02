package com.example.administrator.christie.broadcastReceiver;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.christie.activity.homeAct.AddBluetoothActivity;
import com.example.administrator.christie.adapter.LvBlueTInfoAdapter;
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
    private List<BluetoothDevice> mList;
    private LvBlueTInfoAdapter    mAdapter;
    private boolean isHad = false;
    private TextView  tv_title;
    private ImageView img_load;

    public SearchBlueThBcr(List mData, LvBlueTInfoAdapter adapter) {
        this.mList = mData;
        this.mAdapter = adapter;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //        Toast.makeText(context, "触发广播", Toast.LENGTH_SHORT).show();
        String action = intent.getAction();
        switch (action) {
            case BluetoothDevice.ACTION_FOUND:
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String address = device.getAddress();
                //                Toast.makeText(context, "找到设备" + device.getName(), Toast.LENGTH_SHORT).show();
                if (mAdapter != null) {
                    for (int i = 0; i < mList.size(); i++) {
                        BluetoothDevice bluetoothDevice = mList.get(i);
                        String address1 = bluetoothDevice.getAddress();
                        if (address.equals(address1)) {
                            isHad = true;
                        }
                    }
                    if (!isHad) {
                        mList.add(device);
                        mAdapter.notifyDataSetChanged();
                    }
                    isHad = false;
                }
                break;
            case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                ToastUtils.showToast(context, "搜索结束");
                AddBluetoothActivity.isSearchBT = false;
                tv_title.setText("开始搜索");
                img_load.setVisibility(View.INVISIBLE);
                break;
        }
    }

    public void setUI(TextView tv, ImageView img_load) {
        this.tv_title = tv;
        this.img_load = img_load;
    }
}
