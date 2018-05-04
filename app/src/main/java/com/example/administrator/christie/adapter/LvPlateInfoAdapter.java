package com.example.administrator.christie.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.christie.R;
import com.example.administrator.christie.modelInfo.PersonalPlateInfo;

import java.util.List;

/**
 * @创建者 AndyYan
 * @创建时间 2018/5/4 10:16
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class LvPlateInfoAdapter extends BaseAdapter {
    private List<PersonalPlateInfo> mList;
    private Context                 mContext;
    private int                     mSelectItem;

    public LvPlateInfoAdapter(Context context, List list, int selectItem) {
        this.mList = list;
        this.mContext = context;
        this.mSelectItem = selectItem;
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
            view = View.inflate(mContext, R.layout.lv_item_plate, null);
            viewHolder = new ViewHolder();
            viewHolder.linear = view.findViewById(R.id.linear);
            viewHolder.tv_plate = view.findViewById(R.id.tv_plate);
            viewHolder.tv_model = view.findViewById(R.id.tv_model);
            viewHolder.tv_color = view.findViewById(R.id.tv_color);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        PersonalPlateInfo plateInfo = mList.get(i);
        String fplateno = plateInfo.getFplateno();
        String fmodel = plateInfo.getFmodel();
        String fcolor = plateInfo.getFcolor();
        viewHolder.tv_plate.setText(fplateno);
        viewHolder.tv_model.setText(fmodel);
        viewHolder.tv_color.setText(fcolor);
        if (i == mSelectItem) {
            viewHolder.linear.setBackgroundColor(Color.BLUE);
        } else {
            viewHolder.linear.setBackgroundColor(Color.WHITE);
        }
        return view;
    }

    class ViewHolder {
        LinearLayout linear;
        TextView     tv_plate, tv_model, tv_color;
    }
}
