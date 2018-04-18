package com.example.administrator.christie.util;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Vibrator;
import android.widget.Toast;

/**
 * @创建者 AndyYan
 * @创建时间 2018/4/18 9:39
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class ShakeHelper implements SensorEventListener {
    private Context       mContext;
    //传感器管理器
    private SensorManager mSensorManager;
    //传感器
    private Sensor        mSensor;
    //速度阀值
    private int mSpeed    = 3000;
    //时间间隔
    private int mInterval = 100;
    //上一次摇晃的时间
    private long  LastTime;
    //上一次的x、y、z坐标
    private float LastX, LastY, LastZ;
    private static final int SENSOR_SHAKE = 10;
    private int cs;

    public ShakeHelper(Context mContext) {
        this.mContext = mContext;
        Start();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        long NowTime = System.currentTimeMillis();
        if ((NowTime - LastTime) < mInterval) {
            return;
        }
        //将NowTime赋给LastTime
        LastTime = NowTime;
        //获取x,y,z
        float NowX = sensorEvent.values[0];
        float NowY = sensorEvent.values[1];
        float NowZ = sensorEvent.values[2];
        //计算x,y,z变化量
        float DeltaX = NowX - LastX;
        float DeltaY = NowY - LastY;
        float DeltaZ = NowZ - LastZ;
        //赋值
        LastX = NowX;
        LastY = NowY;
        LastZ = NowZ;
        //计算
        double NowSpeed = Math.sqrt(DeltaX * DeltaX + DeltaY * DeltaY + DeltaZ * DeltaZ) / mInterval * 10000;
        //判断
        if (NowSpeed >= mSpeed && cs == 0) {
            cs++;
            Toast.makeText(mContext, "您摇晃了手机！,请2s后再次摇晃", Toast.LENGTH_SHORT).show();
            onVibrator();
            ThreadUtils.runOnSubThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                        cs = 0;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
    //震动
    private void onVibrator() {
        Vibrator vibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null) {
            vibrator.vibrate(100);
        }
    }
    public void Start() {
        mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        if (mSensorManager != null) {
            mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }else {
            ToastUtils.showToast(mContext,"未在手机上发现摇晃传感器，请升级手机！");
        }
        if (mSensor != null) {
            mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_GAME);
        }
    }

    public void Stop() {
        mSensorManager.unregisterListener(this);
    }
}
