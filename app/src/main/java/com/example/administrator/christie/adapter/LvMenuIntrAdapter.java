package com.example.administrator.christie.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.christie.R;
import com.example.administrator.christie.modelInfo.GoodsListInfo;
import com.example.administrator.christie.util.GlideLoaderUtil;

import java.util.List;

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
    public View getView(int i, View view, ViewGroup viewGroup) {
        MyViewHolder viewHolder;
        if (null == view) {
            view = View.inflate(mContext, R.layout.list_item_menu, null);
            viewHolder = new MyViewHolder();
            viewHolder.img_food = view.findViewById(R.id.img_food);
            viewHolder.tv_name = view.findViewById(R.id.tv_name);
            viewHolder.tv_intro = view.findViewById(R.id.tv_intro);
            viewHolder.tv_price = view.findViewById(R.id.tv_price);
            view.setTag(viewHolder);
        } else {
            viewHolder = (MyViewHolder) view.getTag();
        }
        GoodsListInfo.ArrBean arrBean = mList.get(i);
        String good_pic = arrBean.getGood_pic();
        String good_name = arrBean.getGood_name();
        String good_introduce = arrBean.getGood_introduce();
        int good_price = arrBean.getGood_price();
        GlideLoaderUtil.showImageView(mContext,good_pic,viewHolder.img_food);
        viewHolder.tv_name.setText(good_name);
        viewHolder.tv_intro.setText(good_introduce);
        viewHolder.tv_price.setText("¥"+good_price);
        return view;
    }

    class MyViewHolder {
        ImageView img_food;
        TextView tv_name,tv_intro,tv_price;
    }
}
