package com.example.administrator.christie.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.christie.R;

import java.util.List;

/**
 * @创建者 AndyYan
 * @创建时间 2018/4/20 9:14
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class LvMsgAdapter extends BaseAdapter {
    private List    mList;
    private Context mContext;

    public LvMsgAdapter(Context context, List data) {
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
            view = View.inflate(mContext, R.layout.lv_item_msg, null);
            viewHolder = new MyViewHolder();
            viewHolder.liner_msg = view.findViewById(R.id.liner_msg);
            viewHolder.img_head = view.findViewById(R.id.img_head);
            viewHolder.tv_kind = view.findViewById(R.id.tv_kind);
            viewHolder.tv_msg = view.findViewById(R.id.tv_msg);
            viewHolder.tv_time = view.findViewById(R.id.tv_time);
            viewHolder.tv_state = view.findViewById(R.id.tv_state);
            view.setTag(viewHolder);
        } else {
            viewHolder = (MyViewHolder) view.getTag();
        }
        if (i % 2 == 0) {
            viewHolder.liner_msg.setBackgroundResource(R.color.white);
        } else {
            viewHolder.liner_msg.setBackgroundResource(R.color.blue_06);
        }

        return view;
    }

    class MyViewHolder {
        LinearLayout liner_msg;
        ImageView    img_head;
        TextView     tv_kind, tv_msg, tv_time, tv_state;
    }
}
