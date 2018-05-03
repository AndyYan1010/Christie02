package com.example.administrator.christie.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.christie.R;
import com.example.administrator.christie.modelInfo.PersonalDataInfo;

import java.util.List;

/**
 * @创建者 AndyYan
 * @创建时间 2018/5/3 14:39
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class BindInfoAdapter extends BaseAdapter {
    private List<PersonalDataInfo.ArrBean.ListProjectBean> mList;
    private Context                                        mContext;

    public BindInfoAdapter(Context context, List data) {
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
        if (null == view) {
            view = View.inflate(mContext, R.layout.list_item_alrbind, null);
            viewHolder = new MyViewHolder();
            viewHolder.tv_company = view.findViewById(R.id.tv_company);
            viewHolder.tv_detail = view.findViewById(R.id.tv_detail);
            view.setTag(viewHolder);
        } else {
            viewHolder = (MyViewHolder) view.getTag();
        }
        PersonalDataInfo.ArrBean.ListProjectBean listProjectBean = mList.get(i);
        String project_name = listProjectBean.getProject_name();
        String fname = listProjectBean.getFname();
        viewHolder.tv_company.setText(project_name);
        viewHolder.tv_detail.setText(fname);
        return view;
    }

    class MyViewHolder {
        TextView tv_company, tv_detail;
    }
}
