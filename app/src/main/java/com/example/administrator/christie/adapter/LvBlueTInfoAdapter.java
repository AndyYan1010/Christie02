package com.example.administrator.christie.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.christie.InformationMessege.ProjectMsg;
import com.example.administrator.christie.R;

import java.util.List;

/**
 * @创建者 AndyYan
 * @创建时间 2018/4/20 16:22
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class LvBlueTInfoAdapter extends BaseAdapter {
    private List<ProjectMsg> mList;
    private Context          mContext;

    public LvBlueTInfoAdapter(Context context, List mData) {
        this.mList = mData;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        MyViewHolder viewHolder;
        if (view == null) {
            view = View.inflate(mContext, R.layout.listview_bluetinfo, null);
            viewHolder = new LvBlueTInfoAdapter.MyViewHolder();
            viewHolder.tv_btinfo = (TextView) view.findViewById(R.id.tv_btinfo);
            viewHolder.tv_state = (TextView) view.findViewById(R.id.tv_state);
            view.setTag(viewHolder);
        } else {
            viewHolder = (LvBlueTInfoAdapter.MyViewHolder) view.getTag();
        }
        ProjectMsg msg = mList.get(i);
        String project_name = msg.getProject_name();
        viewHolder.tv_btinfo.setText(project_name);
        String toNext = msg.getToNext();
        if ("0".equals(toNext)) {
            viewHolder.tv_btinfo.setTextColor(mContext.getResources().getColor(R.color.vm_black_87));
            viewHolder.tv_state.setText("该设备不在附近");
        } else {
            viewHolder.tv_btinfo.setTextColor(mContext.getResources().getColor(R.color.orange));
            viewHolder.tv_state.setText("该设备可连接");
        }

        //        BluetoothDevice btDevice = (BluetoothDevice) mList.get(i);
        //        String name = btDevice.getName();
        //        if (null == name || "".equals(name) || "null".equals(name)) {
        //            viewHolder.tv_btinfo.setText(btDevice.getAddress());
        //        } else {
        //            viewHolder.tv_btinfo.setText(name);
        //        }
        //        int bondState = btDevice.getBondState();
        //        if (bondState == BluetoothDevice.BOND_BONDED) {
        //            viewHolder.tv_state.setText("已配对过");
        //        } else {
        //            viewHolder.tv_state.setText("未配对过");
        //        }
        return view;
    }

    private class MyViewHolder {
        TextView tv_btinfo, tv_state;
    }
}
