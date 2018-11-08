package com.example.administrator.christie.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;

import java.util.Date;

/**
 * @创建者 AndyYan
 * @创建时间 2018/3/2 16:33
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */
public class TimeTextView extends android.support.v7.widget.AppCompatTextView {

    public TimeTextView(Context context) {
        super(context);
    }

    public TimeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TimeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private long Time;
    private boolean run     = true; // 是否启动了
    @SuppressLint("NewApi")
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (run) {
                        long mTime = Time;
                        if (mTime >= 0) {
                            setTextString(mTime);
                            //                            TimeTextView.this.setText((Time / 1000) + "s");
                            Time = Time - 1000;
                            handler.sendEmptyMessageDelayed(0, 1000);
                        } else {
                            TimeTextView.this.setText("请离场");
                        }
                    } else {
                        TimeTextView.this.setText("请离场");
                    }
                    break;
            }
        }
    };

    private void setTextString(long mTime) {
        long mBetween = mTime / 1000;
        if (mBetween > 0) {
            //            int day1 = (int) (mBetween / (24 * 3600));
            int hour1 = (int) (mBetween % (24 * 3600) / 3600);
            String hourtime = "";
            if (hour1 <= 9) {
                hourtime = "0" + String.valueOf(hour1);
            } else {
                hourtime = String.valueOf(hour1);
            }
            int minute1 = (int) (mBetween % (24 * 3600) % 3600 / 60);
            String minTime = "";
            if (minute1 <= 9) {
                minTime = "0" + String.valueOf(minute1);
            } else {
                minTime = String.valueOf(minute1);
            }
            int second1 = (int) (mBetween % (24 * 3600) % 3600 % 60 % 60);
            String sceTime = "";
            if (second1 <= 9) {
                sceTime = "0" + String.valueOf(second1);
            } else {
                sceTime = String.valueOf(second1);
            }
            //            String time = day1 + "天" + hourtime + ":" + minTime + ":" + sceTime;
            String time = hourtime + ":" + minTime + ":" + sceTime;
            TimeTextView.this.setText(time);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        // 在控件被销毁时移除消息
        handler.removeMessages(0);
    }

    @SuppressLint("NewApi")
    public void setTimes(long mT) {
        // 标示已经启动
        Date date = new Date();
        long t2 = date.getTime();
        //        Time = mT - t2;
        Time = mT;
        date = null;

        if (Time > 0) {
            handler.removeMessages(0);
            handler.sendEmptyMessage(0);
            start();
        } else {
            TimeTextView.this.setText("请离场");
            stop();
        }
    }

    public void start() {
        run = true;
    }

    public void stop() {
        run = false;
    }
}
