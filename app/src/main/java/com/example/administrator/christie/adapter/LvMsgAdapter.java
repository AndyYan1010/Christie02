package com.example.administrator.christie.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.christie.R;
import com.example.administrator.christie.modelInfo.MeetingDataInfo;
import com.example.administrator.christie.util.DelHTMLTagUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @创建者 AndyYan
 * @创建时间 2018/4/20 9:14
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class LvMsgAdapter extends BaseAdapter {
    private List<MeetingDataInfo.JsonObjectBean> mList;
    private Context                              mContext;

    public LvMsgAdapter(Context context, List data) {
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
        if (view == null) {
            view = View.inflate(mContext, R.layout.lv_item_msg, null);
            viewHolder = new MyViewHolder();
            viewHolder.liner_msg = view.findViewById(R.id.liner_msg);
            viewHolder.img_head = view.findViewById(R.id.img_head);
            viewHolder.tv_kind = view.findViewById(R.id.tv_kind);
            viewHolder.tv_msg = view.findViewById(R.id.tv_msg);
            viewHolder.tv_time = view.findViewById(R.id.tv_time);
            viewHolder.tv_state = view.findViewById(R.id.tv_state);
            view.setTag(viewHolder);
        } else {
            viewHolder = (MyViewHolder) view.getTag();
        }
        //        if (i % 2 == 0) {
        //            viewHolder.liner_msg.setBackgroundResource(R.color.white);
        //        } else {
        //            viewHolder.liner_msg.setBackgroundResource(R.color.blue_06);
        //        }
        MeetingDataInfo.JsonObjectBean jsonBean = mList.get(i);
        String ftype = jsonBean.getFtype();
        String create_date = jsonBean.getCreate_date();
        String meeting_content = jsonBean.getMeeting_content();
        String fread = jsonBean.getFread();
        String between = judgeTimeByNow(create_date);
        viewHolder.tv_time.setText(between);
        if ("1".equals(ftype)) {
            viewHolder.tv_kind.setText("公告");
            viewHolder.tv_kind.setTextColor(mContext.getResources().getColor(R.color.yellow_kind));
            viewHolder.img_head.setImageResource(R.drawable.notices);
        } else if ("2".equals(ftype)) {
            viewHolder.tv_kind.setText("会议");
            viewHolder.tv_kind.setTextColor(mContext.getResources().getColor(R.color.blue_kind));
            viewHolder.img_head.setImageResource(R.drawable.meeting_icon);
        }
        String content = DelHTMLTagUtil.delHTMLTag(meeting_content);
        viewHolder.tv_msg.setText(content);
        if ("0".equals(fread)) {
            viewHolder.tv_state.setText("未读");
        } else {
            viewHolder.tv_state.setText("已读");
        }

        return view;
    }

    class MyViewHolder {
        LinearLayout liner_msg;
        ImageView    img_head;
        TextView     tv_kind, tv_msg, tv_time, tv_state;
    }

    private String judgeTimeByNow(String date) {
        long millionSeconds = 0;
        String substring = date.substring(0, 19);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            //用户发表的时间/毫秒
            millionSeconds = sdf.parse(substring).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long nowTime = System.currentTimeMillis();
        long intervalTime = nowTime - millionSeconds;
        String spaceTime = getSpaceTime(intervalTime);
        return spaceTime;
    }

    public static String getSpaceTime(long time) {
        //间隔秒
        Long spaceSecond = time / 1000;
        //一分钟之内
        if (spaceSecond >= 0 && spaceSecond < 60) {
            return "刚刚";
        }
        //一小时之内
        else if (spaceSecond / 60 > 0 && spaceSecond / 60 < 60) {
            return spaceSecond / 60 + "分钟之前";
        }
        //一天之内
        else if (spaceSecond / (60 * 60) > 0 && spaceSecond / (60 * 60) < 24) {
            return spaceSecond / (60 * 60) + "小时之前";
        }
        //3天之内
        else if (spaceSecond / (60 * 60 * 24) > 0 && spaceSecond / (60 * 60 * 24) < 3) {
            return spaceSecond / (60 * 60 * 24) + "天之前";
        } else {
            //            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //            Date date = new Date(time);
            //            String dateStr = simpleDateFormat.format(date);
            //            return dateStr;
            return "3天之前";
        }
    }
}
