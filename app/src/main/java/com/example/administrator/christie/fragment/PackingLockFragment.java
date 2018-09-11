package com.example.administrator.christie.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
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
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

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
    private LinearLayout                 linear_back;
    private TextView                     mTv_title;
    private ListView                     mLv_lockinfo;
    private List<LockPlateInfo.ListBean> mList;
    private String                       mUserid;
    private LockingInfoAdapter           mAdapter;
    private TextView                     mTv_noinfo;//提示未有停车信息
    private SmartRefreshLayout           mSmt_refresh;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_packing_lock, container, false);
        initView();
        initData();
        return mRootView;
    }

    private void initView() {
        linear_back = (LinearLayout) mRootView.findViewById(R.id.linear_back);
        mTv_title = mRootView.findViewById(R.id.tv_title);
        mTv_noinfo = mRootView.findViewById(R.id.tv_noinfo);
        mSmt_refresh = mRootView.findViewById(R.id.smt_refresh);
        mLv_lockinfo = mRootView.findViewById(R.id.lv_lockinfo);
    }

    private void initData() {
        linear_back.setOnClickListener(this);
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
        mSmt_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                //获取车位锁定状态
                getPlateLockingInfo();
            }
        });
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
                mSmt_refresh.finishRefresh();
                ToastUtils.showToast(getContext(), "网络错误");
            }

            @Override
            public void onSuccess(int code, String resbody) {
                mSmt_refresh.finishRefresh();
                if (code == 200) {
                    if (null == mList) {
                        mList = new ArrayList<>();
                    } else {
                        mList.clear();
                    }
                    Gson gson = new Gson();
                    LockPlateInfo outPlateInfo = gson.fromJson(resbody, LockPlateInfo.class);
                    String result = outPlateInfo.getResult();
                    if ("FAIL".equals(result)) {
                        ToastUtils.showToast(getContext(), "没有车牌信息");
                        return;
                    }
                    List<LockPlateInfo.ListBean> list = outPlateInfo.getList();
                    for (LockPlateInfo.ListBean bean : list) {
                        mList.add(bean);
                    }
                    if (mList.size() > 0) {
                        mTv_noinfo.setVisibility(View.GONE);
                    } else {
                        mTv_noinfo.setVisibility(View.VISIBLE);
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.linear_back:
                getActivity().finish();
                break;
        }
    }
}
