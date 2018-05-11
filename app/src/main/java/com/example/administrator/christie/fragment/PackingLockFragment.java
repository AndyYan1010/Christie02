package com.example.administrator.christie.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.christie.R;
import com.example.administrator.christie.adapter.LockingInfoAdapter;
import com.example.administrator.christie.modelInfo.LockPlateInfo;
import com.example.administrator.christie.modelInfo.RequestParamsFM;
import com.example.administrator.christie.modelInfo.UserInfo;
import com.example.administrator.christie.util.HttpOkhUtils;
import com.example.administrator.christie.util.SPref;
import com.example.administrator.christie.util.ToastUtils;
import com.example.administrator.christie.websiteUrl.NetConfig;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

/**
 * @创建者 AndyYan
 * @创建时间 2018/5/11 11:23
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class PackingLockFragment extends Fragment implements View.OnClickListener {
    private View                         mRootView;
    private ImageView                    mImg_back;
    private TextView                     mTv_title;
    private ListView                     mLv_lockinfo;
    private List<LockPlateInfo.ListBean> mList;
    private String                       mUserid;
    private LockingInfoAdapter           mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_packing_lock, container, false);
        initView();
        initData();
        return mRootView;
    }

    private void initView() {
        mImg_back = mRootView.findViewById(R.id.img_back);
        mTv_title = mRootView.findViewById(R.id.tv_title);
        mLv_lockinfo = mRootView.findViewById(R.id.lv_lockinfo);
    }

    private void initData() {
        mImg_back.setOnClickListener(this);
        mTv_title.setText("车位锁定");
        mList = new ArrayList();
        mAdapter = new LockingInfoAdapter(getContext(), mList);
        mLv_lockinfo.setAdapter(mAdapter);
        mLv_lockinfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                LockPlateInfo.ListBean bean = mList.get(i);
                String plateno = bean.getPlateno();
                int fstatus = bean.getFstatus();
                if (fstatus == 0) {
                    //加锁
                    changeLockPlate("1", plateno);
                } else {
                    //解锁
                    changeLockPlate("0", plateno);
                }
            }
        });
        UserInfo userinfo = SPref.getObject(getContext(), UserInfo.class, "userinfo");
        mUserid = userinfo.getUserid();
        //获取车位锁定状态
        getPlateLockingInfo();
    }

    private void changeLockPlate(String state, String plate) {
        String changeLockUrl = NetConfig.LOCKPLATE;
        RequestParamsFM params = new RequestParamsFM();
        params.put("userid", mUserid);
        params.put("plateno", plate);
        params.put("fstatus", state);
        HttpOkhUtils.getInstance().doPost(changeLockUrl, params, new HttpOkhUtils.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                ToastUtils.showToast(getContext(), "网络错误");
            }

            @Override
            public void onSuccess(int code, String resbody) {
                if (code == 200) {
                    ToastUtils.showToast(getContext(), "修改成功");
                    //获取车位锁定状态
                    getPlateLockingInfo();
                }
            }
        });
    }

    private void getPlateLockingInfo() {
        String getLockUrl = NetConfig.GETLOCKSTATE;
        RequestParamsFM params = new RequestParamsFM();
        params.put("userid", mUserid);
        HttpOkhUtils.getInstance().doGetWithParams(getLockUrl, params, new HttpOkhUtils.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                ToastUtils.showToast(getContext(), "网络错误");
            }

            @Override
            public void onSuccess(int code, String resbody) {
                if (code == 200) {
                    if (null == mList) {
                        mList = new ArrayList<>();
                    } else {
                        mList.clear();
                    }
                    Gson gson = new Gson();
                    LockPlateInfo outPlateInfo = gson.fromJson(resbody, LockPlateInfo.class);
                    List<LockPlateInfo.ListBean> list = outPlateInfo.getList();
                    for (LockPlateInfo.ListBean bean : list) {
                        mList.add(bean);
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                getActivity().finish();
                break;
        }
    }
}