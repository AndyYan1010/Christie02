package com.example.administrator.christie.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.christie.InformationMessege.ProjectMsg;
import com.example.administrator.christie.R;

import java.util.List;

/**
 * @创建者 AndyYan
 * @创建时间 2018/5/17 17:51
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class PlateSpinnerAdapter extends BaseAdapter {
    private List<ProjectMsg> mList;
    private Context          mContext;

    public PlateSpinnerAdapter(Context context, List list) {
        this.mList = list;
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
        ViewHolder viewHolder;
        if (null == view) {
            viewHolder = new PlateSpinnerAdapter.ViewHolder();
            view = View.inflate(mContext, R.layout.spinner_item_plate, null);
            viewHolder.linear = view.findViewById(R.id.linear);
            viewHolder.tv_name = view.findViewById(R.id.tv_name);
            view.setTag(viewHolder);
        } else {
            viewHolder = (PlateSpinnerAdapter.ViewHolder) view.getTag();
        }
        ProjectMsg projectInfo = mList.get(i);
        String project_name = projectInfo.getProject_name();
        viewHolder.tv_name.setText(project_name);
        if (i == 0) {
            viewHolder.linear.setBackground(null);
            viewHolder.tv_name.setBackground(null);
            viewHolder.tv_name.setTextColor(mContext.getResources().getColor(R.color.vm_black_100));
        } else {
            viewHolder.linear.setBackgroundResource(R.drawable.frame_black_line);
            viewHolder.tv_name.setBackgroundResource(R.color.blue_56);
            viewHolder.tv_name.setTextColor(mContext.getResources().getColor(R.color.white));
        }
        return view;
    }

    private class ViewHolder {
        LinearLayout linear;
        TextView     tv_name;
    }
}
