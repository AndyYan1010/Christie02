package com.example.administrator.christie.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.christie.R;
import com.example.administrator.christie.activity.TempayActivity;
import com.example.administrator.christie.view.CountDownView;

public class CountdownFragment extends Fragment{
    private Context mContext;
    private View view;
    private CountDownView countDownView;
    private TempayActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getContext();
        view = inflater.inflate(R.layout.fragment_countdown, container, false);
        activity = (TempayActivity)getActivity();
        countDownView = (CountDownView) view.findViewById(R.id.tv_timer);
        countDownView.initTime(0,activity.getFuture(),0);
        countDownView.start();
        countDownView.setOnTimeCompleteListener(new CountDownView.OnTimeCompleteListener() {
            @Override
            public void onTimeComplete() {
                Toast.makeText(mContext, "开始重新计费", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }


}
