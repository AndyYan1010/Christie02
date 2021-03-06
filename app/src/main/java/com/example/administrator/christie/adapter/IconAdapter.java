package com.example.administrator.christie.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.administrator.christie.R;
import com.example.administrator.christie.activity.homeAct.AccessdataActivity;
import com.example.administrator.christie.activity.homeAct.AddBluetoothActivity;
import com.example.administrator.christie.activity.homeAct.InvitationRecordActivity;
import com.example.administrator.christie.activity.homeAct.ParkinglockActivity;
import com.example.administrator.christie.activity.homeAct.PayForParkingActivity;
import com.example.administrator.christie.activity.homeAct.PaymentRecordActivity;
import com.example.administrator.christie.activity.homeAct.QrcodeActivity;
import com.example.administrator.christie.activity.homeAct.ReservatParkingActivity;
import com.example.administrator.christie.activity.homeAct.VisitorInvitationActivity;
import com.example.administrator.christie.activity.menushowpackage.MenuIntroduceActivity;
import com.example.administrator.christie.entity.MainMenuEntity;
import com.example.administrator.christie.modelInfo.UserInfo;
import com.example.administrator.christie.util.SPref;
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
            if (needShow) {
                holder.img_delete_icon.setVisibility(View.VISIBLE);
                if (position == 0 || position == 1 || position == (mData.size() - 1)) {
                    holder.img_delete_icon.setVisibility(View.GONE);
                }
            } else {
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
                    return true;
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
    }

    private void startWhichAct(String title, ImageView icon) {
        if ("添加".equals(title)) {
            if (mOtherdata.size() == 0) {
                ToastUtils.showToast(mContext, "没有可添加的内容了");
            } else {
                //弹出一个popupwindow让用户选择添加icon
                showMoreIcon(icon);
            }
        }
        UserInfo userinfo = SPref.getObject(mContext, UserInfo.class, "userinfo");
        if (null == userinfo) {
            ToastUtils.showToast(mContext, "读取账号信息失败，请退出重新登录");
            return;
        } else {
            boolean fstatus = userinfo.getFstatus();
            if (!fstatus) {
                ToastUtils.showToast(mContext, "您还没有绑定，部分功能无法使用。请立即去个人中心绑定");
                return;
            }
        }
        if ("二维码开门".equals(title)) {
            mContext.startActivity(new Intent(mContext, QrcodeActivity.class));
        }
        if ("蓝牙开门".equals(title)) {
            Intent intent = new Intent(mContext, AddBluetoothActivity.class);
            mContext.startActivity(intent);
        }
        if ("门禁数据".equals(title)) {
            mContext.startActivity(new Intent(mContext, AccessdataActivity.class));
        }
        if ("访客邀请".equals(title)) {
            Intent intent = new Intent(mContext, VisitorInvitationActivity.class);
            mContext.startActivity(intent);
        }
        if ("邀请记录".equals(title)) {
            Intent intent = new Intent(mContext, InvitationRecordActivity.class);
            mContext.startActivity(intent);
        }
        if ("车位预约".equals(title)) {
            Intent intent = new Intent(mContext, ReservatParkingActivity.class);
            mContext.startActivity(intent);
        }
        if ("停车缴费".equals(title)) {
            Intent intent = new Intent(mContext, PayForParkingActivity.class);
            mContext.startActivity(intent);
        }
        if ("缴费记录".equals(title)) {
            Intent intent = new Intent(mContext, PaymentRecordActivity.class);
            mContext.startActivity(intent);
        }
        if ("车位锁定".equals(title)) {
            Intent intent = new Intent(mContext, ParkinglockActivity.class);
            mContext.startActivity(intent);
        }
        if ("菜单".equals(title)) {
            Intent intent = new Intent(mContext, MenuIntroduceActivity.class);
            mContext.startActivity(intent);
        }
    }

    //    private void requestBluetooth() {
    //        boolean bluetoothSupported = BluetoothManagerUtils.isBluetoothSupported();
    //        if (bluetoothSupported) {
    //            boolean bluetoothEnabled = BluetoothManagerUtils.isBluetoothEnabled();
    //            if (!bluetoothEnabled) {
    //                //弹出对话框提示用户是后打开
    //                Intent enabler = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
    //                // 设置 Bluetooth 设备可以被其它 Bluetooth 设备扫描到
    //                enabler.setAction(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
    //                // 设置 Bluetooth 设备可见时间
    //                enabler.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, BLUETOOTH_DISCOVERABLE_DURATION);
    //                Activity activity = (Activity) mContext;
    //                activity.startActivityForResult(enabler, REQUEST_ENABLE);
    //            } else {
    //                //蓝牙打开可以跳转
    //                Intent intent = new Intent(mContext, BluetoothActivity.class);
    //                mContext.startActivity(intent);
    //            }
    //        } else {
    //            ToastUtils.showToast(mContext, "当前设备不支持蓝牙功能");
    //        }
    //    }

    private GridView        more_icon;
    private GridViewAdapter more_iconAdapter;

    private void showMoreIcon(ImageView icon) {
        final PopupWindow popupWindow = new PopupWindow(mContext);
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setContentView(LayoutInflater.from(mContext).inflate(R.layout.popup_more_icon, null));
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(true);
        //显示popupwindow,指定位置
        popupWindow.showAtLocation(icon, Gravity.CENTER, 0, 0);

        //找到展示条目
        more_icon = (GridView) popupWindow.getContentView().findViewById(R.id.gridview_more);
        more_iconAdapter = new GridViewAdapter(mContext, mOtherdata);
        more_icon.setAdapter(more_iconAdapter);
        more_icon.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                mData.add(mData.size() - 1, mOtherdata.get(position));
                mOtherdata.remove(position);
                more_iconAdapter.notifyDataSetChanged();
                notifyDataSetChanged();
                if (mOtherdata.size() == 0) {
                    popupWindow.dismiss();
                }
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
        //        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
        //            @Override
        //            public void onDismiss() {
        //                notifyDataSetChanged();
        //            }
        //        });
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
