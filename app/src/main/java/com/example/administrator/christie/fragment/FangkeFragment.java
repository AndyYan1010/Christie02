package com.example.administrator.christie.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.administrator.christie.R;
import com.example.administrator.christie.TApplication;
import com.example.administrator.christie.activity.msgAct.MsgDetailActivity;
import com.example.administrator.christie.adapter.LvMsgAdapter;
import com.example.administrator.christie.modelInfo.MeetingDataInfo;
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

public class FangkeFragment extends Fragment {
    private Context mContext = null;
    private View view;
    private List<String> functionlist = TApplication.user.getFunctionlist();
    private ListView                             mLv_messege;
    private List<MeetingDataInfo.JsonObjectBean> mData;
    private LvMsgAdapter                         mLvMsgAdapter;
    private SmartRefreshLayout                   mSmt_refresh;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getContext();
        view = inflater.inflate(R.layout.fragment_fangke, container, false);
        setViews();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setData();
        setListeners();
    }

    private void setData() {
        if (null == mData) {
            mData = new ArrayList<>();
        } else {
            mData.clear();
        }
        mLvMsgAdapter = new LvMsgAdapter(mContext, mData);
        mLv_messege.setAdapter(mLvMsgAdapter);
        //获取公告会议内容
        getNoticeAndMeeting();
        mLv_messege.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MeetingDataInfo.JsonObjectBean jsonObjectBean = mData.get(i);
                String msgId = jsonObjectBean.getId();
                String ftype = jsonObjectBean.getFtype();
                String fread = jsonObjectBean.getFread();
                Intent intent = new Intent(getContext(), MsgDetailActivity.class);
                intent.putExtra("msgid", msgId);
                intent.putExtra("kind",ftype);
                intent.putExtra("fread",fread);
                startActivity(intent);
            }
        });
    }

    private void getNoticeAndMeeting() {
        UserInfo userinfo = SPref.getObject(getActivity(), UserInfo.class, "userinfo");
        String userid = userinfo.getUserid();
        String meetingUrl = NetConfig.MEETINGSEARCH;
        RequestParamsFM params = new RequestParamsFM();
        params.put("userid", userid);
        HttpOkhUtils.getInstance().doPost(meetingUrl, params, new HttpOkhUtils.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                mSmt_refresh.finishRefresh();
                ToastUtils.showToast(getActivity(), "网络错误");
            }

            @Override
            public void onSuccess(int code, String resbody) {
                mSmt_refresh.finishRefresh();
                if (code == 200) {
                    Gson gson = new Gson();
                    MeetingDataInfo meetingInfo = gson.fromJson(resbody, MeetingDataInfo.class);
                    String message = meetingInfo.getMessage();
                    if ("查找成功".equals(message)) {
                        if (null == mData) {
                            mData = new ArrayList<>();
                        } else {
                            mData.clear();
                        }
                        List<MeetingDataInfo.JsonObjectBean> jsonObject = meetingInfo.getJsonObject();
                        for (int i = 0; i < jsonObject.size(); i++) {
                            MeetingDataInfo.JsonObjectBean jsonBean = jsonObject.get(i);
                            mData.add(jsonBean);
                        }
                        mLvMsgAdapter.notifyDataSetChanged();
                    } else {
                        ToastUtils.showToast(getActivity(), "数据未查找到");
                    }
                } else {
                    ToastUtils.showToast(getActivity(), "网络错误");
                }
            }
        });
    }

    protected void setViews() {
        mSmt_refresh = (SmartRefreshLayout) this.view.findViewById(R.id.smt_refresh);
        mLv_messege = (ListView) this.view.findViewById(R.id.lv_messege);
    }

    protected void setListeners() {
        mSmt_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                //获取公告会议内容
                getNoticeAndMeeting();
            }
        });
        //        mSmt_refresh.setOnLoadMoreListener(new OnLoadMoreListener() {
        //            @Override
        //            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        //                //上拉加载更多
        //
        //            }
        //        });
    }
}
