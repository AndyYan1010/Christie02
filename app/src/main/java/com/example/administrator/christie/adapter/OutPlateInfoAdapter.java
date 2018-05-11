package com.example.administrator.christie.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.christie.R;

import java.util.List;

/**
 * @创建者 AndyYan
 * @创建时间 2018/5/11 15:36
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class OutPlateInfoAdapter extends BaseAdapter {
    private List    mList;
    private Context mContext;

    public OutPlateInfoAdapter(Context context, List list) {
        this.mContext = context;
        this.mList = list;
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
        ViewHolder viewHolder;
        if (null == view) {
            view = View.inflate(mContext, R.layout.list_itme_plate_info, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_plate = view.findViewById(R.id.tv_plate);
            viewHolder.tv_enter_time = view.findViewById(R.id.tv_enter_time);
            viewHolder.tv_out_time = view.findViewById(R.id.tv_out_time);
            viewHolder.tv_state = view.findViewById(R.id.tv_state);
            viewHolder.tv_explain = view.findViewById(R.id.tv_state);
            viewHolder.tv_price = view.findViewById(R.id.tv_price);
            viewHolder.tv_submit = view.findViewById(R.id.tv_submit);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }


        return view;
    }

    private class ViewHolder {
        TextView tv_plate, tv_enter_time, tv_out_time, tv_state, tv_explain, tv_price,tv_submit;
    }
}
