package com.example.administrator.christie.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.christie.R;
import com.example.administrator.christie.entity.MainMenuEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/1/3 0003.
 */

public class GridViewAdapter extends BaseAdapter{
    private Context mContext = null;
    private List<MainMenuEntity> list;
    private LayoutInflater inflater;

    public GridViewAdapter(Context context,List<MainMenuEntity> list){
        this.mContext = context;
        this.list = list;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder;
        if(view==null){
            view = inflater.inflate(R.layout.item_gridview,null);
            holder = new Holder();
            holder.ll = (LinearLayout) view.findViewById(R.id.mydada_item_ll);
            holder.iv_icon = (ImageView) view.findViewById(R.id.mydada_icon_img);
            holder.img_delete_icon = (ImageView) view.findViewById(R.id.img_delete_icon);
            holder.img_add_icon = (ImageView) view.findViewById(R.id.img_add_icon);
            holder.tv_text = (TextView) view.findViewById(R.id.mydada_text_tv);
            view.setTag(holder);
        }else{
            holder = (Holder)view.getTag();
        }
        holder.iv_icon.setImageResource(list.get(i).getResId());
        String title = list.get(i).getText();
        if (title.equals("添加")){
            holder.img_delete_icon.setVisibility(View.GONE);
        }
        holder.tv_text.setText(list.get(i).getText());

//        WindowManager wm = (WindowManager) mContext.getApplicationContext()
//                .getSystemService(Context.WINDOW_SERVICE);
//        int width = (wm.getDefaultDisplay().getWidth() - 2) / 3;
//        int height = width;
//        AbsListView.LayoutParams params = new AbsListView.LayoutParams(width,
//                height);
//        holder.ll.setLayoutParams(params);
        return view;
    }

    class Holder{
        LinearLayout ll;
        ImageView iv_icon,img_delete_icon,img_add_icon;
        TextView tv_text;
    }
}
