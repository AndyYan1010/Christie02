package com.example.administrator.christie.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.example.administrator.christie.R;
import com.example.administrator.christie.activity.notme.TempayActivity;

public class TempayFragment extends Fragment {
    private Context mContext;
    private View view;
    private TextView tv_jine;
    private RadioGroup rb;
    private RadioButton rb_alipay;
    private Button btn_zhifu;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getContext();
        view = inflater.inflate(R.layout.fragment_tempay, container, false);
        setViews();
        setListeners();
        return view;
    }

    protected void setViews(){
        tv_jine = (TextView) view.findViewById(R.id.tv_jine);
        rb = (RadioGroup) view.findViewById(R.id.rb);
        rb_alipay = (RadioButton) view.findViewById(R.id.rb_alipay);
        btn_zhifu = (Button) view.findViewById(R.id.btn_zhifu);
    }

    protected void setListeners(){
        btn_zhifu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"支付成功,请在15分钟内离场",Toast.LENGTH_SHORT).show();
                TempayActivity activity = (TempayActivity)getActivity();
                activity.setFuture(15L);
                FragmentManager fm = activity.getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                if (activity.countdownFragment == null) {
                    activity.countdownFragment = new CountdownFragment();
                }
                ft.replace(R.id.frag_container, activity.countdownFragment);
                ft.commit();
            }
        });
    }

}
