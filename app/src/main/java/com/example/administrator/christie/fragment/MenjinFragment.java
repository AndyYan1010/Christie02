package com.example.administrator.christie.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.christie.InformationMessege.IconListInfo;
import com.example.administrator.christie.R;
import com.example.administrator.christie.activity.LoginActivity;
import com.example.administrator.christie.adapter.IconAdapter;
import com.example.administrator.christie.entity.MainMenuEntity;
import com.example.administrator.christie.modelInfo.BannerListInfo;
import com.example.administrator.christie.modelInfo.LoginInfo;
import com.example.administrator.christie.modelInfo.RequestParamsFM;
import com.example.administrator.christie.modelInfo.UserInfo;
import com.example.administrator.christie.util.GlideLoaderUtil;
import com.example.administrator.christie.util.HttpOkhUtils;
import com.example.administrator.christie.util.MD5Util;
import com.example.administrator.christie.util.ProgressDialogUtils;
import com.example.administrator.christie.util.SPref;
import com.example.administrator.christie.util.SpUtils;
import com.example.administrator.christie.util.ToastUtils;
import com.example.administrator.christie.view.bannerview.AbSlidingPlayView;
import com.example.administrator.christie.websiteUrl.NetConfig;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.Request;

public class MenjinFragment extends Fragment implements View.OnClickListener {
    private Context mContext;
    private View    view;
    private int[]    resArr       = new int[]{R.drawable.code, R.drawable.lanyamen, R.drawable.menjin, R.drawable.addmore};
    private String[] textArr      = new String[]{"二维码开门", "蓝牙开门", "门禁数据", "添加"};
    private int[]    resArrTotal  = new int[]{R.drawable.code, R.drawable.lanyamen, R.drawable.menjin, R.drawable.addmore, R.drawable.fangkeyaoqing, R.drawable.fangkejilu, R.drawable.cheweiyuyue, R.drawable.tingchejiaofei, R.drawable.jiaofeijilu, R.drawable.cheweisuoding, R.drawable.caidan};
    private String[] textArrTotal = new String[]{"二维码开门", "蓝牙开门", "门禁数据", "添加", "访客邀请", "邀请记录", "车位预约", "停车缴费", "缴费记录", "车位锁定", "菜单"};
    private List<MainMenuEntity> list, listMore;
    private RecyclerView      mRecyclerView_icon01;
    private IconAdapter       mIconAdapter;
    private AbSlidingPlayView mVp_banner;
    private ArrayList<View>   allListView;//存储首页轮播的界面
    private AlertDialog       mAlertDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getContext();
        view = inflater.inflate(R.layout.fragment_menjin, container, false);
        //初始化列表数据
        initIconData();
        setViews();
        //获取轮播图图片地址
        getBannerUrl();
        //查看用户是否认证过
        checkIsAuthentication();
        return view;
    }

    private List<String> imgUrlList;

    private void getBannerUrl() {
        if (null == imgUrlList) {
            imgUrlList = new ArrayList<>();
        } else {
            imgUrlList.clear();
        }
        String bannerUrl = NetConfig.BANNERURL;
        ProgressDialogUtils.getInstance().show(getActivity(), "正在加载...");
        HttpOkhUtils.getInstance().doGet(bannerUrl, new HttpOkhUtils.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                ToastUtils.showToast(getContext(), "网络错误");
            }

            @Override
            public void onSuccess(int code, String resbody) {
                ProgressDialogUtils.getInstance().dismiss();
                if (code != 200) {
                    ToastUtils.showToast(mContext, "请求异常");
                    return;
                }
                Gson gson = new Gson();
                final BannerListInfo bannerInfo = gson.fromJson(resbody, BannerListInfo.class);
                List<BannerListInfo.ArrBean> arr = bannerInfo.getArr();
                for (BannerListInfo.ArrBean bean : arr) {
                    imgUrlList.add(bean.getNewpic());
                }
                //初始化轮播图
                setIntData(imgUrlList);
            }
        });
    }

    private void checkIsAuthentication() {
        UserInfo userinfo = SPref.getObject(mContext, UserInfo.class, "userinfo");
        if (null == userinfo) {
            //为空让用户重新登录，获取登录信息
            ToastUtils.showToast(mContext, "请重新登录，获取账号信息");
            Intent intent = new Intent(mContext, LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        } else {
            boolean fstatus = userinfo.getFstatus();
            if (!fstatus) {
                //弹一个dailog提示
                View view = View.inflate(mContext, R.layout.dialog_remind_bd, null);
                TextView tv_cancel = view.findViewById(R.id.tv_cancel);
                TextView tv_refresh = view.findViewById(R.id.tv_refresh);
                TextView tv_ok = view.findViewById(R.id.tv_ok);
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                mAlertDialog = builder.setView(view).create();
                mAlertDialog.show();
                tv_cancel.setOnClickListener(this);
                tv_refresh.setOnClickListener(this);
                tv_ok.setOnClickListener(this);
            }
        }
    }

    private void initIconData() {
        list = new ArrayList<MainMenuEntity>();
        for (int i = 0; i < resArr.length; i++) {
            MainMenuEntity data = new MainMenuEntity();
            data.setResId(resArr[i]);
            data.setText(textArr[i]);
            list.add(data);
        }
        String listStr = SpUtils.getString(getContext(), "listStr");
        Gson gson = new Gson();
        if (null == listStr) {
            String str = gson.toJson(list);
            SpUtils.putString(getContext(), "listStr", str);
        } else if (listStr.equals("")) {
            String str = gson.toJson(list);
            SpUtils.putString(getContext(), "listStr", str);
        } else {
            ArrayList<IconListInfo> iconBeanList = new ArrayList<>();
            //Json的解析类对象
            JsonParser parser = new JsonParser();
            //将JSON的String 转成一个JsonArray对象
            JsonArray jsonArray = parser.parse(listStr).getAsJsonArray();
            for (JsonElement user : jsonArray) {
                IconListInfo userBean = gson.fromJson(user, IconListInfo.class);
                iconBeanList.add(userBean);
            }
            list.clear();
            for (int i = 0; i < iconBeanList.size(); i++) {
                int someOne = 0;
                IconListInfo iconListInfo = iconBeanList.get(i);
                String text = iconListInfo.getText();
                for (int n = 0; n < textArrTotal.length; n++) {
                    if (textArrTotal[n].equals(text)) {
                        someOne = n;
                        break;
                    }
                }
                MainMenuEntity data = new MainMenuEntity();
                data.setResId(resArrTotal[someOne]);
                data.setText(text);
                list.add(data);
            }
        }
        listMore = new ArrayList<MainMenuEntity>();
        for (int i = 0; i < textArrTotal.length; i++) {
            MainMenuEntity data = new MainMenuEntity();
            data.setResId(resArrTotal[i]);
            data.setText(textArrTotal[i]);
            listMore.add(data);
        }
        Iterator<MainMenuEntity> iter = listMore.iterator();
        while (iter.hasNext()) {
            String text = iter.next().getText();
            for (int i = 0; i < list.size(); i++) {
                String text1 = list.get(i).getText();
                if (text1.equals(text)) {
                    iter.remove();
                }
            }
        }
    }

    protected void setViews() {
        mVp_banner = view.findViewById(R.id.vp_banner);
        mRecyclerView_icon01 = view.findViewById(R.id.recyclerView_icon01);
        GridLayoutManager mGridManager = new GridLayoutManager(getContext(), 3);
        mRecyclerView_icon01.setLayoutManager(mGridManager);
        mIconAdapter = new IconAdapter(getContext(), list, listMore, 0);
        mRecyclerView_icon01.setAdapter(mIconAdapter);
    }

    //初始化轮播图
    private void setIntData(List<String> imgUrlList) {
        if (null == allListView) {
            allListView = new ArrayList<>();
        } else {
            allListView.clear();
        }
        for (int i = 0; i < imgUrlList.size(); i++) {
            //导入ViewPager的布局
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.banner_item, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.img_banner);
            //            Glide.with(mContext).load(imgUrlList.get(i)).into(imageView);
            GlideLoaderUtil.showImgWithIcon(getContext(), imgUrlList.get(i), R.drawable.lunbo, R.drawable.lunbo, imageView);
            allListView.add(view);
        }
        //设置播放方式为顺序播放
        mVp_banner.setPlayType(1);
        //设置播放间隔时间
        mVp_banner.setSleepTime(3000);
        mVp_banner.addViews(allListView);
        //开始轮播
        mVp_banner.startPlay();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                if (null != mAlertDialog) {
                    mAlertDialog.dismiss();
                }
                break;
            case R.id.tv_refresh:
                //后台登录重新获取，认证状态
                refreshFstatus();
                break;
            case R.id.tv_ok:
                if (null != mAlertDialog) {
                    mAlertDialog.dismiss();
                }
                break;
        }
    }

    private void refreshFstatus() {
        UserInfo userinfo = SPref.getObject(mContext, UserInfo.class, "userinfo");
        if (null != userinfo) {
            String phone = userinfo.getPhone();
            String psw = userinfo.getPsw();
            //隐形登录
            loginToSeverce(phone, psw);
        } else {
            if (null != mAlertDialog) {
                mAlertDialog.dismiss();
            }
            ToastUtils.showToast(mContext, "请重新登录");
            Intent intent = new Intent(mContext, LoginActivity.class);
            intent.putExtra("autoNext", 0);
            startActivity(intent);
            getActivity().finish();
        }
    }

    private void loginToSeverce(String phone, final String password) {
        ProgressDialogUtils.getInstance().show(getActivity(), "正在刷新请稍后");
        String MD5 = MD5Util.MD5Encode(password, "utf-8", false);
        String url = NetConfig.LOGINURL;
        RequestParamsFM params = new RequestParamsFM();
        params.put("telephone", phone);
        params.put("password", MD5);
        HttpOkhUtils.getInstance().doPost(url, params, new HttpOkhUtils.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                ToastUtils.showToast(mContext, "网络异常,刷新失败");
            }

            @Override
            public void onSuccess(int code, String resbody) {
                ProgressDialogUtils.getInstance().dismiss();
                if (code != 200) {
                    ToastUtils.showToast(mContext, "刷新异常");
                    return;
                }
                Gson gson = new Gson();
                final LoginInfo mLoginInfo = gson.fromJson(resbody, LoginInfo.class);
                String result = mLoginInfo.getResult();
                if ("2".equals(result)) {
                    if (null != mAlertDialog) {
                        mAlertDialog.dismiss();
                    }
                    UserInfo userInfo = new UserInfo();
                    userInfo.setPhone(mLoginInfo.getTelephone());
                    userInfo.setPsw(password);
                    userInfo.setUsername(mLoginInfo.getUsername());
                    userInfo.setUserid(mLoginInfo.getUserid());
                    String fstatus = mLoginInfo.getFstatus();
                    if (null == fstatus || "".equals(fstatus) || "0".equals(fstatus)) {
                        userInfo.setFstatus(false);
                    } else {
                        userInfo.setFstatus(true);
                    }
                    SPref.setObject(mContext, UserInfo.class, "userinfo", userInfo);
                } else {
                    ToastUtils.showToast(mContext, "刷新失败");
                }
            }
        });
    }
}
