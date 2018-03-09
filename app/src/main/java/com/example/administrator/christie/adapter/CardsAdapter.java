package com.example.administrator.christie.adapter;

import java.util.List;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.example.administrator.christie.R;
import com.example.administrator.christie.entity.Plate;

public class CardsAdapter extends BaseAdapter {

    private List<Plate> items;
    private final OnClickListener itemButtonClickListener;
    private final Context context;

    public CardsAdapter(Context context, List<Plate> items, OnClickListener itemButtonClickListener) {
        this.context = context;
        this.items = items;
        this.itemButtonClickListener = itemButtonClickListener;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Plate getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_card, null);

            holder = new ViewHolder();
            holder.itemText = (TextView) convertView.findViewById(R.id.list_item_card_text);
            holder.itemButton1 = (Button) convertView.findViewById(R.id.list_item_card_button_1);
            holder.itemButton2 = (Button) convertView.findViewById(R.id.list_item_card_button_2);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(items.get(position).getFisdefault().equals("Y")){
            holder.itemText.setBackgroundColor(Color.parseColor("#FF4081"));
        }else{
            holder.itemText.setBackgroundColor(Color.parseColor("#1296DB"));
        }
        holder.itemText.setText(items.get(position).getFplateno());

        if (itemButtonClickListener != null) {
            holder.itemButton1.setOnClickListener(itemButtonClickListener);
            holder.itemButton2.setOnClickListener(itemButtonClickListener);
        }
        
        return convertView;
    }

    private static class ViewHolder {
        private TextView itemText;
        private Button itemButton1;
        private Button itemButton2;
    }

}
