package com.example.administrator.christie.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.christie.R;
import com.example.administrator.christie.modelInfo.LoginInfo;
import com.example.administrator.christie.modelInfo.PersonalPlateInfo;
import com.example.administrator.christie.modelInfo.RequestParamsFM;
import com.example.administrator.christie.modelInfo.UserInfo;
import com.example.administrator.christie.util.HttpOkhUtils;
import com.example.administrator.christie.util.ToastUtils;
import com.example.administrator.christie.websiteUrl.NetConfig;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.Request;

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
    private int                     mSelectItem;
    private UserInfo                mUserInfo;
    private FloatingActionButton    mDefaultButton;

    public RcvPlateInfoAdapter(Context context, List list, int selectItem, UserInfo userinfo, FloatingActionButton fab_default) {
        this.mList = list;
        this.mContext = context;
        this.mSelectItem = selectItem;
        this.mUserInfo = userinfo;
        this.mDefaultButton = fab_default;
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
            holder.cb_default.setChecked(true);
        } else {
            holder.cb_default.setChecked(false);
        }
        if (position == mSelectItem) {
            holder.linear.setBackgroundColor(mContext.getResources().getColor(R.color.colorAccent));
        } else {
            holder.linear.setBackgroundColor(Color.WHITE);
        }

        holder.linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSelectItem == position) {
                    mSelectItem = -1;
                } else {
                    mSelectItem = position;
                }
                notifyDataSetChanged();
            }
        });
        holder.cb_default.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checked = holder.cb_default.isChecked();
                if (checked) {
                    ToastUtils.showToast(mContext, "请先设置其他车牌为默认车牌");
                    holder.cb_default.setChecked(true);
                } else {
                    //修改默认车牌
                    changeDefault(fplateno);
                }
            }
        });
    }

    private void changeDefault(String fplateno) {
        String changePlateUrl = NetConfig.CHANGEPLATE;
        RequestParamsFM params = new RequestParamsFM();
        params.put("id", fplateno);
        params.put("oldid", mUserInfo.getPhone());
        HttpOkhUtils.getInstance().doPost(changePlateUrl, params, new HttpOkhUtils.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                ToastUtils.showToast(mContext, "网络错误");
            }

            @Override
            public void onSuccess(int code, String resbody) {
                if (code == 200) {
                    Gson gson = new Gson();
                    LoginInfo loginInfo = gson.fromJson(resbody, LoginInfo.class);
                    String result = loginInfo.getResult();
                    String message = loginInfo.getMessage();
                    if ("1".equals(result)) {
                        //更改默认车牌成功
                        mDefaultButton.performClick();
                    }
                    ToastUtils.showToast(mContext, message);
                } else {
                    ToastUtils.showToast(mContext, "网络错误");
                }
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
