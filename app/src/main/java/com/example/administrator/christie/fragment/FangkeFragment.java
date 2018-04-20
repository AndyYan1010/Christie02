package com.example.administrator.christie.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.administrator.christie.R;
import com.example.administrator.christie.TApplication;
import com.example.administrator.christie.adapter.LvMsgAdapter;

import java.util.ArrayList;
import java.util.List;

public class FangkeFragment extends Fragment {
    private Context mContext = null;
    private View view;
    private List<String> functionlist = TApplication.user.getFunctionlist();
    private ListView mLv_messege;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getContext();
        view = inflater.inflate(R.layout.fragment_fangke, container, false);
        setViews();
        setListeners();
        return view;
    }

    protected void setViews() {
        mLv_messege = (ListView) view.findViewById(R.id.lv_messege);
    }

    protected void setListeners() {
        List mData= new ArrayList();
        mData.add("1");
        mData.add("1");
        mData.add("1");
        mData.add("1");
        LvMsgAdapter lvMsgAdapter = new LvMsgAdapter(mContext,mData);
        mLv_messege.setAdapter(lvMsgAdapter);
    }
}
