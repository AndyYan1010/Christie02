package com.example.administrator.christie.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.christie.R;
import com.example.administrator.christie.activity.homeAct.ReservatParkingActivity;

import java.util.List;

/**
 * @创建者 AndyYan
 * @创建时间 2018/4/19 15:09
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class TimePiontAdapter extends RecyclerView.Adapter<TimePiontAdapter.ViewHolder> {
    private Context      mContext;
    private List<String> mList;
    private int          mSelected;

    public TimePiontAdapter(Context context, List data, int selected) {
        this.mContext = context;
        this.mList = data;
        this.mSelected = selected;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_adapter_time, parent, false);
        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        v.setLayoutParams(params);
        // 实例化viewholder
        TimePiontAdapter.ViewHolder viewHolder = new TimePiontAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (position == mSelected) {
            holder.tv_time_piont.setBackgroundResource(R.color.blue_06);
        } else {
            holder.tv_time_piont.setBackgroundResource(R.color.white);
        }
        holder.tv_time_piont.setText(mList.get(position));
        holder.tv_time_piont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReservatParkingActivity activity = (ReservatParkingActivity) mContext;
                activity.setSelected(position);
                mSelected = position;
                notifyDataSetChanged();
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_time_piont;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_time_piont = (TextView) itemView.findViewById(R.id.tv_time_piont);
        }
    }


}
