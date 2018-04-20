package com.example.administrator.christie.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

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
    private List    mList;
    private Context mContext;

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
            view = View.inflate(mContext, R.layout.listview_mjinfo_bl, null);
            viewHolder = new LvBlueTInfoAdapter.MyViewHolder();
            view.setTag(viewHolder);
        } else {
            viewHolder = (LvBlueTInfoAdapter.MyViewHolder) view.getTag();
        }
        return view;
    }

    private class MyViewHolder {

    }
}
