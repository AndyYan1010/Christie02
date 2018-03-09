package com.example.administrator.christie.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.administrator.christie.R;
import com.example.administrator.christie.TApplication;
import com.example.administrator.christie.activity.ApplyActivity;
import com.example.administrator.christie.activity.CarlockActivity;
import com.example.administrator.christie.activity.MoonpayActivity;
import com.example.administrator.christie.activity.PayrecordActivity;
import com.example.administrator.christie.activity.ShenqingActivity;
import com.example.administrator.christie.activity.TempayActivity;
import com.example.administrator.christie.adapter.GridViewAdapter;
import com.example.administrator.christie.entity.MainMenuEntity;
import com.example.administrator.christie.util.Consts;

import java.util.ArrayList;
import java.util.List;

public class ParkFragment extends Fragment {
    private Context mContext = null;
    private View view;
    private GridView gv_park;
    private int[] resArr = new int[]{R.drawable.yuyue,R.drawable.jiaofei,R.drawable.linshi,R.drawable.suoding,R.drawable.jfjl,0
    };
    private String[] textArr = new String[]{"车位预约","月卡缴费","临时缴费","车辆锁定","缴费记录查询",""
    };
    private List<MainMenuEntity> list;
    private GridViewAdapter adapter;
    private List<String> functionlist = TApplication.user.getFunctionlist();
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getContext();
        view = inflater.inflate(R.layout.fragment_park, container, false);
        list = new ArrayList<MainMenuEntity>();
        for (int i = 0; i < resArr.length; i++) {
            MainMenuEntity data = new MainMenuEntity();
            data.setResId(resArr[i]);
            data.setText(textArr[i]);
            list.add(data);
        }
        setViews();
        setListeners();
        return view;
    }


    protected void setViews(){
        gv_park = (GridView) view.findViewById(R.id.gv_park);
        adapter = new GridViewAdapter(mContext,list);
        gv_park.setAdapter(adapter);
    }

    protected void setListeners(){
        gv_park.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(mContext,ShenqingActivity.class);
                switch (i){
                    case 0:
                        if (functionlist.contains(Consts.CWYY)) {
                            startActivity(new Intent(mContext, ApplyActivity.class));
                        }else {
                            intent.putExtra("title",getString(R.string.apply));
                            intent.putExtra("code",Consts.CWYY);
                            startActivity(intent);
                        }
                        break;
                    case 1:
                        if (functionlist.contains(Consts.YKJF)) {
                            startActivity(new Intent(mContext, MoonpayActivity.class));
                        }else {
                            intent.putExtra("title",getString(R.string.moon));
                            intent.putExtra("code",Consts.YKJF);
                            startActivity(intent);
                        }
                        break;
                    case 2:
                        if(functionlist.contains(Consts.LSJF)) {
                            startActivity(new Intent(mContext, TempayActivity.class));
                        }else {
                            intent.putExtra("title",getString(R.string.tem));
                            intent.putExtra("code",Consts.LSJF);
                            startActivity(intent);
                        }
                        break;
                    case 3:
                        if (functionlist.contains(Consts.CLSD)) {
                            startActivity(new Intent(mContext, CarlockActivity.class));
                        }else {
                            intent.putExtra("title",getString(R.string.lock));
                            intent.putExtra("code",Consts.CLSD);
                            startActivity(intent);
                        }
                        break;
                    case 4:
                        if (functionlist.contains(Consts.JFJLCX)) {
                            startActivity(new Intent(mContext, PayrecordActivity.class));
                        }else {
                            intent.putExtra("title",getString(R.string.pay));
                            intent.putExtra("code",Consts.JFJLCX);
                            startActivity(intent);
                        }
                        break;
                }
            }
        });
    }
}
