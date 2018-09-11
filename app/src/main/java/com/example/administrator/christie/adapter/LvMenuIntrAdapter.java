package com.example.administrator.christie.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.christie.R;
import com.example.administrator.christie.modelInfo.GoodsListInfo;
import com.example.administrator.christie.modelInfo.LikeMenuInfo;
import com.example.administrator.christie.modelInfo.RequestParamsFM;
import com.example.administrator.christie.modelInfo.UserInfo;
import com.example.administrator.christie.util.GlideLoaderUtil;
import com.example.administrator.christie.util.HttpOkhUtils;
import com.example.administrator.christie.util.SPref;
import com.example.administrator.christie.util.ToastUtils;
import com.example.administrator.christie.websiteUrl.NetConfig;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.Request;

/**
 * @创建者 AndyYan
 * @创建时间 2018/5/3 15:09
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class LvMenuIntrAdapter extends BaseAdapter {
    private List<GoodsListInfo.ArrBean> mList;
    private Context                     mContext;

    public LvMenuIntrAdapter(Context context, List data) {
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final MyViewHolder viewHolder;
        if (null == view) {
            view = View.inflate(mContext, R.layout.list_item_menu, null);
            viewHolder = new MyViewHolder();
            viewHolder.img_food = view.findViewById(R.id.img_food);
            viewHolder.img_zan = view.findViewById(R.id.img_zan);
            viewHolder.img_cai = view.findViewById(R.id.img_cai);
            viewHolder.tv_zan = view.findViewById(R.id.tv_zan);
            viewHolder.tv_cai = view.findViewById(R.id.tv_cai);
            viewHolder.tv_name = view.findViewById(R.id.tv_name);
            viewHolder.tv_intro = view.findViewById(R.id.tv_intro);
            viewHolder.tv_price = view.findViewById(R.id.tv_price);
            view.setTag(viewHolder);
        } else {
            viewHolder = (MyViewHolder) view.getTag();
        }
        final GoodsListInfo.ArrBean arrBean = mList.get(i);
        String good_newpic = arrBean.getNewpic();
        String good_name = arrBean.getGood_name();
        String good_introduce = arrBean.getGood_introduce();
        double good_price = arrBean.getGood_price();
        GlideLoaderUtil.showImageView(mContext, good_newpic, viewHolder.img_food);
        viewHolder.tv_name.setText(good_name);
        viewHolder.tv_intro.setText(good_introduce);
        viewHolder.tv_price.setText("¥" + good_price);

        String type = arrBean.getType();
        viewHolder.tv_zan.setText("" + arrBean.getLike());
        viewHolder.tv_cai.setText("" + arrBean.getDislike());
        if ("0".equals(type)) {
            viewHolder.img_zan.setImageDrawable(mContext.getResources().getDrawable(R.drawable.zan_norm));
            viewHolder.img_cai.setImageDrawable(mContext.getResources().getDrawable(R.drawable.cai_norm));
            viewHolder.img_zan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    GoodsListInfo.ArrBean arrBean1 = mList.get(i);
                    //点赞
                    sendLikeInfo(arrBean1.getId(), "1", viewHolder.img_zan, viewHolder.tv_zan, arrBean1.getLike());//1是喜欢，2是不喜欢
                }
            });
            viewHolder.img_cai.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    GoodsListInfo.ArrBean arrBean1 = mList.get(i);
                    //踩
                    sendLikeInfo(arrBean1.getId(), "2", viewHolder.img_cai, viewHolder.tv_cai, arrBean1.getDislike());
                }
            });
        } else {
            if ("1".equals(type)) {
                viewHolder.img_zan.setImageDrawable(mContext.getResources().getDrawable(R.drawable.zan_on));
                viewHolder.img_cai.setImageDrawable(mContext.getResources().getDrawable(R.drawable.cai_norm));
            } else {
                viewHolder.img_zan.setImageDrawable(mContext.getResources().getDrawable(R.drawable.zan_norm));
                viewHolder.img_cai.setImageDrawable(mContext.getResources().getDrawable(R.drawable.cai_on));
            }
            viewHolder.img_zan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ToastUtils.showToast(mContext, "您已经评价过了，每个用户只能评价一次");
                }
            });
            viewHolder.img_cai.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ToastUtils.showToast(mContext, "您已经评价过了，每个用户只能评价一次");
                }
            });
        }
        return view;
    }

    private void sendLikeInfo(String orderID, final String type, final ImageView img, final TextView tv, final int num) {
        UserInfo userinfo = SPref.getObject(mContext, UserInfo.class, "userinfo");
        String userid = userinfo.getUserid();
        String likeUrl = NetConfig.USERLIKEGOOD;
        RequestParamsFM params = new RequestParamsFM();
        params.put("userid", userid);
        params.put("goodid", orderID);
        if ("1".equals(type)) {
            params.put("type", "1");
        } else {
            params.put("type", "2");
        }
        params.setUseJsonStreamer(true);
        HttpOkhUtils.getInstance().doPost(likeUrl, params, new HttpOkhUtils.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                ToastUtils.showToast(mContext, "网络连接错误");
            }

            @Override
            public void onSuccess(int code, String resbody) {
                if (code != 200) {
                    ToastUtils.showToast(mContext, "网络错误");
                    return;
                }
                Gson gson = new Gson();
                LikeMenuInfo likeMenuInfo = gson.fromJson(resbody, LikeMenuInfo.class);
                String result = likeMenuInfo.getResult();
                String message = likeMenuInfo.getMessage();
                if ("1".equals(result)) {
                    if ("1".equals(type)) {
                        img.setImageDrawable(mContext.getResources().getDrawable(R.drawable.zan_on));
                        tv.setText("" + (num + 1));
                    } else {
                        img.setImageDrawable(mContext.getResources().getDrawable(R.drawable.cai_on));
                        tv.setText("" + (num + 1));
                    }
                }
                ToastUtils.showToast(mContext, message);
            }
        });
    }

    class MyViewHolder {
        ImageView img_food, img_zan, img_cai;
        TextView tv_name, tv_intro, tv_price, tv_zan, tv_cai;
    }
}
