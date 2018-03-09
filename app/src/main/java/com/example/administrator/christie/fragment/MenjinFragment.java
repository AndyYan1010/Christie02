package com.example.administrator.christie.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import com.example.administrator.christie.R;
import com.example.administrator.christie.TApplication;
import com.example.administrator.christie.activity.AccessdataActivity;
import com.example.administrator.christie.activity.BluetoothActivity;
import com.example.administrator.christie.activity.QrcodeActivity;
import com.example.administrator.christie.activity.ShenqingActivity;
import com.example.administrator.christie.adapter.GridViewAdapter;
import com.example.administrator.christie.entity.MainMenuEntity;
import com.example.administrator.christie.util.Consts;

import java.util.ArrayList;
import java.util.List;

public class MenjinFragment extends Fragment {
    private Context mContext = null;
    private View view;
    private GridView gv_menjin;
    private int[] resArr = new int[]{R.drawable.code, R.drawable.bluetooth,R.drawable.menjin};
    private String[] textArr = new String[]{"二维码开门", "蓝牙开门","门禁数据查询"};
    private List<MainMenuEntity> list;
    private GridViewAdapter adapter;
    private List<String> functionlist = TApplication.user.getFunctionlist();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getContext();
        view = inflater.inflate(R.layout.fragment_menjin, container, false);
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
        gv_menjin = (GridView)view.findViewById(R.id.gv_menjin);
        adapter = new GridViewAdapter(mContext,list);
        gv_menjin.setAdapter(adapter);
    }

    protected void setListeners(){
        gv_menjin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(mContext,ShenqingActivity.class);
                switch (i){
                    case 0:
                        if(functionlist.contains(Consts.QRKM)) {
                            startActivity(new Intent(mContext, QrcodeActivity.class));
                        }else{
                            intent.putExtra("title",getString(R.string.qrcode));
                            intent.putExtra("code",Consts.QRKM);
                            startActivity(intent);
                        }
                        break;
                    case 1:
                        if(functionlist.contains(Consts.LYKM)){
                            startActivity(new Intent(mContext,BluetoothActivity.class));
                        }else{
                            intent.putExtra("title",getString(R.string.bluetooth));
                            intent.putExtra("code",Consts.LYKM);
                            startActivity(intent);
                        }
                        break;
                    case 2:
                        if(functionlist.contains(Consts.MJSJCX)) {
                            startActivity(new Intent(mContext, AccessdataActivity.class));
                        }else {
                            intent.putExtra("title",getString(R.string.accessdata));
                            intent.putExtra("code",Consts.MJSJCX);
                            startActivity(intent);
                        }
                        break;
                }
            }
        });
    }
}
