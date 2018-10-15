package com.example.administrator.christie.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.christie.R;
import com.example.administrator.christie.modelInfo.PersonalPlateInfo;
import com.example.administrator.christie.modelInfo.RequestParamsFM;
import com.example.administrator.christie.modelInfo.UserInfo;
import com.example.administrator.christie.util.HttpOkhUtils;
import com.example.administrator.christie.util.SPref;
import com.example.administrator.christie.util.ToastUtils;
import com.example.administrator.christie.websiteUrl.NetConfig;

import java.io.IOException;
import java.util.List;

import okhttp3.Request;

/**
 * @创建者 AndyYan
 * @创建时间 2018/10/12 13:12
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class LvCarNoAdapter extends BaseAdapter {
    private Context                 mContext;
    private List<PersonalPlateInfo> mList;
    private int                     selectItem;

    public LvCarNoAdapter(Context context, List list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public int getCount() {
        return null == mList ? 0 : mList.size();
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        MyViewHolder viewHolder;
        if (null == view) {
            view = View.inflate(mContext, R.layout.lv_item_carno, null);
            viewHolder = new MyViewHolder();
            viewHolder.tv_carno = view.findViewById(R.id.tv_carno);
            viewHolder.tv_carnorm = view.findViewById(R.id.tv_carnorm);
            viewHolder.img_del = view.findViewById(R.id.img_del);
            view.setTag(viewHolder);
        } else {
            viewHolder = (MyViewHolder) view.getTag();
        }

        PersonalPlateInfo personalPlateInfo = mList.get(i);
        String fisdefault = personalPlateInfo.getFisdefault();
        viewHolder.tv_carno.setText(personalPlateInfo.getFplateno());
        if ("Y".equals(fisdefault)) {
            selectItem = i;
            viewHolder.tv_carnorm.setText("默认车辆");
            viewHolder.tv_carnorm.setBackgroundResource(R.drawable.bg_round_white);
            viewHolder.tv_carnorm.setTextColor(mContext.getResources().getColor(R.color.blue_sky));
            viewHolder.tv_carnorm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ToastUtils.showToast(mContext, "此车牌是默认车辆，无需修改");
                }
            });
        } else {
            viewHolder.tv_carnorm.setText("设为默认");
            viewHolder.tv_carnorm.setTextColor(Color.WHITE);
            viewHolder.tv_carnorm.setBackgroundResource(R.drawable.bg_round_white_frame);
            viewHolder.tv_carnorm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //设置为默认车辆
                    setDefaultPlate(i);
                }
            });
        }
        //删除车牌
        viewHolder.img_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ("Y".equals(mList.get(i).getFisdefault())) {
                    ToastUtils.showToast(mContext, "此车牌是默认车辆，不能删除");
                } else {
                    //删除车牌
                    deletPlate(i);
                }
            }
        });
        return view;
    }

    private void deletPlate(final int position) {
        UserInfo userinfo = SPref.getObject(mContext, UserInfo.class, "userinfo");
        //删除选择的车牌
        String delPlateUrl = NetConfig.DELPLATE;
        RequestParamsFM params = new RequestParamsFM();
        params.put("id", mList.get(position).getId());
        params.put("userid", userinfo.getUserid());
        HttpOkhUtils.getInstance().doPost(delPlateUrl, params, new HttpOkhUtils.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                ToastUtils.showToast(mContext, "网络错误");
            }

            @Override
            public void onSuccess(int code, String resbody) {
                if (code == 200) {
                    if ("1".equals(resbody)) {
                        ToastUtils.showToast(mContext, "删除成功");
                        mList.remove(position);
                        notifyDataSetChanged();
                    } else {
                        ToastUtils.showToast(mContext, "删除失败");
                    }
                } else {
                    ToastUtils.showToast(mContext, "网络请求失败");
                }
            }
        });
    }

    private void setDefaultPlate(final int position) {
        UserInfo userinfo = SPref.getObject(mContext, UserInfo.class, "userinfo");
        String setDefPlate = NetConfig.CHANGEPLATE;
        RequestParamsFM params = new RequestParamsFM();
        params.put("id", mList.get(position).getId());
        params.put("userid", userinfo.getUserid());
        HttpOkhUtils.getInstance().doPost(setDefPlate, params, new HttpOkhUtils.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                ToastUtils.showToast(mContext, "网络错误");
            }

            @Override
            public void onSuccess(int code, String resbody) {
                if (code == 200) {
                    ToastUtils.showToast(mContext, "设置成功");
                    //访问网络获取已有车牌信息
                    mList.get(selectItem).setFisdefault("N");
                    mList.get(position).setFisdefault("Y");
                    notifyDataSetChanged();
                } else {
                    ToastUtils.showToast(mContext, "设置失败");
                }
            }
        });
    }

    private class MyViewHolder {
        TextView  tv_carno;
        TextView  tv_carnorm;
        ImageView img_del;
    }
}
