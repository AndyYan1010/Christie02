package com.example.administrator.christie.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.christie.R;
import com.example.administrator.christie.activity.usercenter.PlateActivity;
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

public class RcvPlateInfoAdapter extends RecyclerView.Adapter<RcvPlateInfoAdapter.ViewHolder> {
    private List<PersonalPlateInfo> mList;
    private Context                 mContext;

    public RcvPlateInfoAdapter(Context context, List list) {
        this.mList = list;
        this.mContext = context;
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lv_item_plate, parent, false);
        // 实例化viewholder
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        PersonalPlateInfo plateInfo = mList.get(position);
        String fisdefault = plateInfo.getFisdefault();
        final String fplateno = plateInfo.getFplateno();
        String fmodel = plateInfo.getFmodel();
        String fcolor = plateInfo.getFcolor();
        holder.tv_plate.setText(fplateno);
        holder.tv_model.setText(fmodel);
        holder.tv_color.setText(fcolor);
        if ("Y".equals(fisdefault)) {
            holder.cb_default.setVisibility(View.VISIBLE);
            holder.cb_default.setChecked(true);
            holder.cb_default.setEnabled(false);
        } else {
            holder.cb_default.setChecked(false);
            holder.cb_default.setEnabled(true);
            holder.cb_default.setVisibility(View.GONE);
        }
        if (position == PlateActivity.selectItem) {
            holder.linear.setBackgroundColor(mContext.getResources().getColor(R.color.colorAccent));
        } else {
            holder.linear.setBackgroundColor(Color.WHITE);
        }

        holder.linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PlateActivity.selectItem == position) {
                    PlateActivity.selectItem = -1;
                    PlateActivity.selectItem = -1;
                } else {
                    PlateActivity.selectItem = position;
                }
                notifyDataSetChanged();
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox     cb_default;
        LinearLayout linear;
        TextView     tv_plate, tv_model, tv_color;

        public ViewHolder(View itemView) {
            super(itemView);
            cb_default = itemView.findViewById(R.id.cb_default);
            linear = itemView.findViewById(R.id.linear);
            tv_plate = itemView.findViewById(R.id.tv_plate);
            tv_model = itemView.findViewById(R.id.tv_model);
            tv_color = itemView.findViewById(R.id.tv_color);
        }
    }
}
