package com.example.administrator.christie.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.christie.R;

import java.util.List;

/**
 * @创建者 AndyYan
 * @创建时间 2018/4/19 10:02
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class LVAccessInfoAdapter extends BaseAdapter {
    private List    mList;
    private Context mContext;

    public LVAccessInfoAdapter(Context context, List data) {
        this.mList = data;
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
            view = View.inflate(mContext, R.layout.listview_mjinfo_bl, null);
            viewHolder = new MyViewHolder();
            viewHolder.relative_item = (RelativeLayout) view.findViewById(R.id.relative_item);
            viewHolder.tv_date = (TextView) view.findViewById(R.id.tv_date);
            viewHolder.tv_door_name = (TextView) view.findViewById(R.id.tv_door_name);
            viewHolder.tv_area = (TextView) view.findViewById(R.id.tv_area);
            view.setTag(viewHolder);
        } else {
            viewHolder = (MyViewHolder) view.getTag();
        }
        if (i % 2 == 0) {
            viewHolder.relative_item.setBackgroundResource(R.color.blue_06);
        } else {
            viewHolder.relative_item.setBackgroundResource(R.color.white);
        }
        if (i==0){
            viewHolder.tv_date.setText("打卡日期");
            viewHolder.tv_door_name.setText("门名称");
            viewHolder.tv_area.setText("打卡区域");
        }else {
            viewHolder.tv_date.setText("2017-02-01");
            viewHolder.tv_door_name.setText("大厅刷卡出门");
            viewHolder.tv_area.setText("W1-A");
        }
        return view;
    }

    class MyViewHolder {
        RelativeLayout relative_item;
        TextView       tv_date, tv_door_name, tv_area;
    }
}
