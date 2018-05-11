package com.example.administrator.christie.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.christie.R;
import com.example.administrator.christie.modelInfo.LockPlateInfo;

import java.util.List;

/**
 * @创建者 AndyYan
 * @创建时间 2018/5/11 13:15
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class LockingInfoAdapter extends BaseAdapter {
    private List<LockPlateInfo.ListBean> mList;
    private Context                      mContext;

    public LockingInfoAdapter(Context context, List list) {
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
            view = View.inflate(mContext, R.layout.list_item_locking, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_plate = view.findViewById(R.id.tv_plate);
            viewHolder.img_lock = view.findViewById(R.id.img_lock);
            viewHolder.tv_state = view.findViewById(R.id.tv_state);
            viewHolder.tv_changelock = view.findViewById(R.id.tv_changelock);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        LockPlateInfo.ListBean bean = mList.get(i);
        String plateno = bean.getPlateno();
        int fstatus = bean.getFstatus();
        viewHolder.tv_plate.setText(plateno);
        if (fstatus == 0) {
            viewHolder.img_lock.setImageResource(R.drawable.unlock);
            viewHolder.tv_state.setText("未锁定");
            viewHolder.tv_changelock.setText("点击加锁");
        } else {
            viewHolder.img_lock.setImageResource(R.drawable.lock);
            viewHolder.tv_state.setText("已锁定");
            viewHolder.tv_changelock.setText("点击解锁");
        }
        return view;
    }

    private class ViewHolder {
        ImageView img_lock;
        TextView  tv_plate, tv_state, tv_changelock;
    }
}
