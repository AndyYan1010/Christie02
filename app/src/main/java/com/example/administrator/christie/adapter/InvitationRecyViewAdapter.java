package com.example.administrator.christie.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.christie.R;

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
    private List    mList;
    private Context mContext;

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

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);

        }
    }
}
