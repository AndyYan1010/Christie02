package com.example.administrator.christie.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.christie.R;
import com.example.administrator.christie.modelInfo.FkRecordInfo;

import java.util.List;

/**
 * @创建者 AndyYan
 * @创建时间 2018/4/19 13:28
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class InvitationRecyViewAdapter extends RecyclerView.Adapter<InvitationRecyViewAdapter.ViewHolder> {
    private List<FkRecordInfo.ListBean> mList;
    private Context                     mContext;

    public InvitationRecyViewAdapter(Context context, List mData) {
        this.mList = mData;
        this.mContext = context;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyview_result, parent, false);
        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        v.setLayoutParams(params);
        // 实例化viewholder
        InvitationRecyViewAdapter.ViewHolder viewHolder = new InvitationRecyViewAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FkRecordInfo.ListBean bean = mList.get(position);
        String fdate = bean.getFdate();
        String subdata = fdate.substring(0, 10);
        String fname = bean.getFname();
        String fmobile = bean.getFmobile();
        String freason = bean.getFreason();
        holder.tv_data.setText(subdata);
        holder.tv_visitor_name.setText(fname);
        holder.tv_visit_time.setText(fdate);
        holder.tv_phone.setText(fmobile);
        holder.tv_reason.setText(freason);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_data, tv_visitor_name, tv_phone, tv_visit_time, tv_reason;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_data = itemView.findViewById(R.id.tv_data);
            tv_visitor_name = itemView.findViewById(R.id.tv_visitor_name);
            tv_phone = itemView.findViewById(R.id.tv_phone);
            tv_visit_time = itemView.findViewById(R.id.tv_visit_time);
            tv_reason = itemView.findViewById(R.id.tv_reason);
        }
    }
}
