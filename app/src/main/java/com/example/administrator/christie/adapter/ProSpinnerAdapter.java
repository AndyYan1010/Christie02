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
 * @创建时间 2018/4/27 13:45
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class ProSpinnerAdapter extends BaseAdapter {
    private List<ProjectMsg> mList;
    private Context          mContext;

    public ProSpinnerAdapter(Context context, List list) {
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
            viewHolder = new ViewHolder();
            view = View.inflate(mContext, R.layout.spinner_item_name, null);
            viewHolder.tv_name = view.findViewById(R.id.tv_name);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        ProjectMsg projectInfo = mList.get(i);
        String project_name = projectInfo.getProject_name();
        viewHolder.tv_name.setText(project_name);
        return view;
    }

    class ViewHolder {
        TextView tv_name;
    }
}
