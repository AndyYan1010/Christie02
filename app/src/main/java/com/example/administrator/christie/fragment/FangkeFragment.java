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
import com.example.administrator.christie.activity.InviterecordActivity;
import com.example.administrator.christie.activity.ShenqingActivity;
import com.example.administrator.christie.activity.VisitorActivity;
import com.example.administrator.christie.activity.VisitrecordActivity;
import com.example.administrator.christie.adapter.GridViewAdapter;
import com.example.administrator.christie.entity.MainMenuEntity;
import com.example.administrator.christie.util.Consts;

import java.util.ArrayList;
import java.util.List;

public class FangkeFragment extends Fragment {
    private Context mContext = null;
    private View view;
    private GridView gv_fangke;
    private int[] resArr = new int[]{R.drawable.yaoqing,R.drawable.jilu,R.drawable.laifang};
    private String[] textArr = new String[]{"访客邀请","邀请记录查询","来访记录查询"};
    private List<MainMenuEntity> list;
    private GridViewAdapter adapter;
    private List<String> functionlist = TApplication.user.getFunctionlist();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getContext();
        view = inflater.inflate(R.layout.fragment_fangke, container, false);
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
        gv_fangke = (GridView) view.findViewById(R.id.gv_fangke);
        adapter = new GridViewAdapter(mContext,list);
        gv_fangke.setAdapter(adapter);
    }

    protected void setListeners(){
        gv_fangke.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(mContext,ShenqingActivity.class);
                switch (i){
                    case 0:
                        if (functionlist.contains(Consts.FKYQ)) {
                            startActivity(new Intent(mContext, VisitorActivity.class));
                        }else {
                            intent.putExtra("title",getString(R.string.visitor));
                            intent.putExtra("code",Consts.FKYQ);
                            startActivity(intent);
                        }
                        break;
                    case 1:
                        if (functionlist.contains(Consts.YQJLCX)) {
                            startActivity(new Intent(mContext, InviterecordActivity.class));
                        }else {
                            intent.putExtra("title",getString(R.string.inviterecord));
                            intent.putExtra("code",Consts.YQJLCX);
                            startActivity(intent);
                        }
                        break;
                    case 2:
                        if (functionlist.contains(Consts.LFJLCX)) {
                            startActivity(new Intent(mContext, VisitrecordActivity.class));
                        }else{
                            intent.putExtra("title",getString(R.string.visitrecord));
                            intent.putExtra("code",Consts.LFJLCX);
                            startActivity(intent);
                        }
                        break;
                }
            }
        });
    }
}
