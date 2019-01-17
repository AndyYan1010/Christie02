package com.example.administrator.christie.broadcastReceiver;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.christie.InformationMessege.ProjectMsg;
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
    //private List<BluetoothDevice> mList;
    private List<ProjectMsg>   mList;
    private LvBlueTInfoAdapter mAdapter;
    private boolean isHad = false;
    private TextView         tv_title;
    private ImageView        img_load;
    private List<ProjectMsg> mSumList;
    private String           upperID;

    public SearchBlueThBcr(List mData, LvBlueTInfoAdapter adapter, List<ProjectMsg> sumList) {
        this.mList = mData;
        this.mAdapter = adapter;
        this.mSumList = sumList;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // Toast.makeText(context, "触发广播", Toast.LENGTH_SHORT).show();
        String action = intent.getAction();
        switch (action) {
            case BluetoothDevice.ACTION_FOUND:
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String address = device.getAddress();
                // String name = device.getName();
                if (mAdapter != null) {
                    for (int i = 0; i < mList.size(); i++) {
                        //BluetoothDevice bluetoothDevice = mList.get(i);
                        ProjectMsg bluetoothDevice = mList.get(i);
                        String address1 = bluetoothDevice.getDetail_name();
                        if (address.equals(address1)) {
                            isHad = true;
                        }
                    }
                    if (!isHad) {
                        for (ProjectMsg msg : mSumList) {
                            String project_name = msg.getProject_name();
                            String detail_name = msg.getDetail_name();//蓝牙地址
                            String id = msg.getId();
                            String type = msg.getType();
                            if (detail_name.equals(address)) {
                                ProjectMsg proMsg = new ProjectMsg();
                                proMsg.setProject_name(project_name);
                                proMsg.setDetail_name(address);
                                proMsg.setToNext("1");
                                proMsg.setId(id);
                                proMsg.setType(type);
                                mList.add(proMsg);
                            }
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                    isHad = false;
                }
                break;
            case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                ToastUtils.showToast(context, "连接结束");
                boolean isHave;
                for (ProjectMsg msg : mSumList) {
                    String project_name = msg.getProject_name();
                    String detail_name = msg.getDetail_name();//蓝牙地址
                    String upperID1 = msg.getUpperID();
                    msg.setToNext("0");
                    isHave = false;
                    for (ProjectMsg locMsg : mList) {
                        String project_name1 = locMsg.getProject_name();
                        String detail_name1 = locMsg.getDetail_name();
                        if (project_name.equals(project_name1) && detail_name.equals(detail_name1)) {
                            isHave = true;
                        }
                    }
                    if (!isHave) {
                        if (upperID == null || upperID.equals("") || upperID.equals(upperID1)) {
                            ProjectMsg proMsg = new ProjectMsg();
                            proMsg.setProject_name(project_name);
                            proMsg.setDetail_name(detail_name);
                            proMsg.setToNext("0");
                            mList.add(proMsg);
                        }
                    }
                }
                mAdapter.notifyDataSetChanged();
                AddBluetoothActivity.isSearchBT = false;
                tv_title.setText("开始连接");
                img_load.setVisibility(View.INVISIBLE);
                break;
        }
    }

    public void setUI(TextView tv, ImageView img_load, String upperID) {
        this.tv_title = tv;
        this.img_load = img_load;
        this.upperID = upperID;
    }
}
