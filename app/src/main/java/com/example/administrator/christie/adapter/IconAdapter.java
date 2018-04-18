package com.example.administrator.christie.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.administrator.christie.R;
import com.example.administrator.christie.activity.AccessdataActivity;
import com.example.administrator.christie.activity.BluetoothActivity;
import com.example.administrator.christie.activity.QrcodeActivity;
import com.example.administrator.christie.entity.MainMenuEntity;
import com.example.administrator.christie.util.SpUtils;
import com.example.administrator.christie.util.ThreadUtils;
import com.example.administrator.christie.util.ToastUtils;
import com.google.gson.Gson;

import java.util.List;

/**
 * @创建者 AndyYan
 * @创建时间 2018/4/18 16:13
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class IconAdapter extends RecyclerView.Adapter<IconAdapter.ViewHolder> {
    private Context              mContext;
    private List<MainMenuEntity> mData;
    private List<MainMenuEntity> mOtherdata;
    private int                  mKind;
    private boolean needShow = false;

    public IconAdapter(Context context, List<MainMenuEntity> maindata, List<MainMenuEntity> otherdata, int kind) {
        this.mContext = context;
        this.mData = maindata;
        this.mOtherdata = otherdata;
        this.mKind = kind;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gridview, parent, false);
        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        v.setLayoutParams(params);
        // 实例化viewholder
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.iv_icon.setImageResource(mData.get(position).getResId());
        holder.tv_text.setText(mData.get(position).getText());
        if (mKind == 0) {
            if (needShow){
                holder.img_delete_icon.setVisibility(View.VISIBLE);
                if (position == 0 || position == 1 || position == (mData.size() - 1)) {
                    holder.img_delete_icon.setVisibility(View.GONE);
                }
            }else {
                holder.img_delete_icon.setVisibility(View.GONE);
            }
        }
        if (mKind == 0) {
            holder.iv_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mKind == 0) {
                        MainMenuEntity menuEntity = mData.get(position);
                        String title = menuEntity.getText();
                        //判断点击跳转事件
                        startWhichAct(title, holder.iv_icon);
                    }
                }
            });
            holder.iv_icon.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    needShow = !needShow;
                    notifyDataSetChanged();
                    if (position == 0 || position == 1 || position == (mData.size() - 1)) {
                        ToastUtils.showToast(mContext, "该图标不可删除");
                        return true;
                    }
//                    if (holder.img_delete_icon.getVisibility() == View.VISIBLE) {
//                        holder.img_delete_icon.setVisibility(View.GONE);
//                    } else {
//                        holder.img_delete_icon.setVisibility(View.VISIBLE);
//                    }
                    return true;
                }
            });
        }
        if (mKind == 1) {
            holder.iv_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOtherdata.add(mOtherdata.size() - 1, mData.get(position));
                    mData.remove(position);
                    notifyDataSetChanged();
                    ThreadUtils.runOnSubThread(new Runnable() {
                        @Override
                        public void run() {
                            Gson gson = new Gson();
                            String str = gson.toJson(mOtherdata);
                            SpUtils.putString(mContext, "listStr", str);
                        }
                    });
                }
            });
        }
        holder.ll.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                needShow = !needShow;
                notifyDataSetChanged();
                if (mKind == 0) {
                    if (position == 0 || position == 1 || position == (mData.size() - 1)) {
                        ToastUtils.showToast(mContext, "该图标不可删除");
                        return true;
                    }
                }
                return true;
            }
        });
        if (holder.img_delete_icon != null) {
            holder.img_delete_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOtherdata.add(mData.get(position));
                    mData.remove(position);
                    notifyDataSetChanged();
                    ThreadUtils.runOnSubThread(new Runnable() {
                        @Override
                        public void run() {
                            Gson gson = new Gson();
                            String str = gson.toJson(mData);
                            SpUtils.putString(mContext, "listStr", str);
                        }
                    });
                }
            });
        }
        //        if (holder.img_add_icon != null) {
        //            holder.img_add_icon.setOnClickListener(new View.OnClickListener() {
        //                @Override
        //                public void onClick(View view) {
        //                    mOtherdata.add(mOtherdata.size() - 1, mData.get(position));
        //                    mData.remove(position);
        //                    notifyDataSetChanged();
        //                    ThreadUtils.runOnSubThread(new Runnable() {
        //                        @Override
        //                        public void run() {
        //                            Gson gson = new Gson();
        //                            String str = gson.toJson(mOtherdata);
        //                            SpUtils.putString(mContext, "listStr", str);
        //                        }
        //                    });
        //                }
        //            });
        //        }
    }

    private void startWhichAct(String title, ImageView icon) {
        if ("二维码开门".equals(title)) {
            //                    if (functionlist.contains(Consts.QRKM)) {
            mContext.startActivity(new Intent(mContext, QrcodeActivity.class));
            //                    } else {
            //                        intent.putExtra("title", getString(R.string.qrcode));
            //                        intent.putExtra("code", Consts.QRKM);
            //                        startActivity(intent);
            //                    }
        }
        if ("蓝牙开门".equals(title)) {
            //                    if (functionlist.contains(Consts.LYKM)) {
            mContext.startActivity(new Intent(mContext, BluetoothActivity.class));
            //                    } else {
            //                        intent.putExtra("title", getString(R.string.bluetooth));
            //                        intent.putExtra("code", Consts.LYKM);
            //                        startActivity(intent);
            //                    }
        }
        if ("门禁数据查询".equals(title)) {
            //                    if (functionlist.contains(Consts.MJSJCX)) {
            mContext.startActivity(new Intent(mContext, AccessdataActivity.class));
            //                    } else {
            //                        intent.putExtra("title", getString(R.string.accessdata));
            //                        intent.putExtra("code", Consts.MJSJCX);
            //                        startActivity(intent);
            //                    }
        }
        if ("访客邀请".equals(title)) {
            ToastUtils.showToast(mContext, "正在开发");
        }
        if ("邀请记录".equals(title)) {
            ToastUtils.showToast(mContext, "正在开发");
        }
        if ("车位预约".equals(title)) {
            ToastUtils.showToast(mContext, "正在开发");
        }
        if ("停车缴费".equals(title)) {
            ToastUtils.showToast(mContext, "正在开发");
        }
        if ("缴费记录".equals(title)) {
            ToastUtils.showToast(mContext, "正在开发");
        }
        if ("添加".equals(title)) {
            if (mOtherdata.size() == 0) {
                ToastUtils.showToast(mContext, "没有可添加的内容了");
            } else {
                //弹出一个popupwindow让用户选择添加icon
                showMoreIcon(icon);
            }
        }
    }

    private void showMoreIcon(ImageView icon) {
        PopupWindow popupWindow = popupWindow = new PopupWindow(mContext);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        View v = LayoutInflater.from(mContext).inflate(R.layout.popup_more_icon, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(10, 0, 10, 0);
        v.setLayoutParams(params);
        popupWindow.setContentView(v);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(true);
        //显示popupwindow,指定位置
        popupWindow.showAtLocation(icon, Gravity.CENTER, 0, 0);

        //找到展示条目
        RecyclerView recyclerView02 = popupWindow.getContentView().findViewById(R.id.recyclerView_icon02);
        GridLayoutManager mGridManager = new GridLayoutManager(mContext, 3);
        recyclerView02.setLayoutManager(mGridManager);
        IconAdapter iconAdapter = new IconAdapter(mContext, mOtherdata, mData, 1);
        recyclerView02.setAdapter(iconAdapter);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                notifyDataSetChanged();
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll;
        ImageView    iv_icon, img_delete_icon, img_add_icon;
        TextView tv_text;

        public ViewHolder(View itemView) {
            super(itemView);
            ll = itemView.findViewById(R.id.mydada_item_ll);
            iv_icon = (ImageView) itemView.findViewById(R.id.mydada_icon_img);
            img_delete_icon = (ImageView) itemView.findViewById(R.id.img_delete_icon);
            img_add_icon = (ImageView) itemView.findViewById(R.id.img_add_icon);
            tv_text = (TextView) itemView.findViewById(R.id.mydada_text_tv);
        }
    }
}
