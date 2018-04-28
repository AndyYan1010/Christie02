package com.example.administrator.christie.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.administrator.christie.InformationMessege.IconListInfo;
import com.example.administrator.christie.R;
import com.example.administrator.christie.TApplication;
import com.example.administrator.christie.adapter.GridViewAdapter;
import com.example.administrator.christie.adapter.IconAdapter;
import com.example.administrator.christie.entity.MainMenuEntity;
import com.example.administrator.christie.util.SpUtils;
import com.example.administrator.christie.view.bannerview.AbSlidingPlayView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MenjinFragment extends Fragment {
    private Context mContext = null;
    private View view;
    //    private GridView gv_menjin, more_icon;
    private int[]    resArr       = new int[]{R.drawable.code, R.drawable.bluetooth, R.drawable.menjin, R.drawable.menjin};
    private String[] textArr      = new String[]{"二维码开门", "蓝牙开门", "门禁数据", "添加"};
    private int[]    resArrTotal  = new int[]{R.drawable.code, R.drawable.bluetooth, R.drawable.menjin, R.drawable.menjin, R.drawable.code, R.drawable.bluetooth, R.drawable.menjin, R.drawable.menjin, R.drawable.bluetooth, R.drawable.bluetooth, R.drawable.bluetooth};
    private String[] textArrTotal = new String[]{"二维码开门", "蓝牙开门", "门禁数据", "添加", "访客邀请", "邀请记录", "车位预约", "停车缴费", "缴费记录", "车位锁定", "菜单"};
    private List<MainMenuEntity> list, listMore;
    private GridViewAdapter adapter, more_iconAdapter;
    private List<String> functionlist = TApplication.user.getFunctionlist();
    private RecyclerView      mRecyclerView_icon01;
    private IconAdapter       mIconAdapter;
    private AbSlidingPlayView mVp_banner;
    private ArrayList<View>   allListView;//存储首页轮播的界面

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getContext();
        view = inflater.inflate(R.layout.fragment_menjin, container, false);
        //初始化列表数据
        initIconData();
        setViews();
        setIntData();
        setListeners();
        return view;
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
        //        gv_menjin = (GridView) view.findViewById(R.id.gv_menjin);
        //        adapter = new GridViewAdapter(mContext, list);
        //        gv_menjin.setAdapter(adapter);
        mVp_banner = view.findViewById(R.id.vp_banner);
        mRecyclerView_icon01 = view.findViewById(R.id.recyclerView_icon01);
        GridLayoutManager mGridManager = new GridLayoutManager(getContext(), 3);
        mRecyclerView_icon01.setLayoutManager(mGridManager);
        mIconAdapter = new IconAdapter(getContext(), list, listMore, 0);
        mRecyclerView_icon01.setAdapter(mIconAdapter);
    }

    private void setIntData() {
        if (null == allListView) {
            allListView = new ArrayList<>();
        } else {
            allListView.clear();
        }
        for (int i = 0; i < 3; i++) {
            //导入ViewPager的布局
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.banner_item, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.img_banner);
            Glide.with(mContext).load("http://220.248.107.62:8084/upFiles/upload/files/20180321/banner1.jpg").into(imageView);
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

    protected void setListeners() {
        //        gv_menjin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        //            @Override
        //            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //                TextView tv_grid_name = (TextView) view.findViewById(R.id.mydada_text_tv);
        //                String name = String.valueOf(tv_grid_name.getText());
        //                //                Intent intent = new Intent(mContext, ShenqingActivity.class);
        //                if ("二维码开门".equals(name)) {
        //                    //                    if (functionlist.contains(Consts.QRKM)) {
        //                    startActivity(new Intent(mContext, QrcodeActivity.class));
        //                    //                    } else {
        //                    //                        intent.putExtra("title", getString(R.string.qrcode));
        //                    //                        intent.putExtra("code", Consts.QRKM);
        //                    //                        startActivity(intent);
        //                    //                    }
        //                }
        //                if ("蓝牙开门".equals(name)) {
        //                    //                    if (functionlist.contains(Consts.LYKM)) {
        //                    startActivity(new Intent(mContext, BluetoothActivity.class));
        //                    //                    } else {
        //                    //                        intent.putExtra("title", getString(R.string.bluetooth));
        //                    //                        intent.putExtra("code", Consts.LYKM);
        //                    //                        startActivity(intent);
        //                    //                    }
        //                }
        //                if ("门禁数据查询".equals(name)) {
        //                    //                    if (functionlist.contains(Consts.MJSJCX)) {
        //                    startActivity(new Intent(mContext, AccessdataActivity.class));
        //                    //                    } else {
        //                    //                        intent.putExtra("title", getString(R.string.accessdata));
        //                    //                        intent.putExtra("code", Consts.MJSJCX);
        //                    //                        startActivity(intent);
        //                    //                    }
        //                }
        //                if ("访客邀请".equals(name)) {
        //                    ToastUtils.showToast(getContext(), "正在开发");
        //                }
        //                if ("邀请记录".equals(name)) {
        //                    ToastUtils.showToast(getContext(), "正在开发");
        //                }
        //                if ("车位预约".equals(name)) {
        //                    ToastUtils.showToast(getContext(), "正在开发");
        //                }
        //                if ("停车缴费".equals(name)) {
        //                    ToastUtils.showToast(getContext(), "正在开发");
        //                }
        //                if ("缴费记录".equals(name)) {
        //                    ToastUtils.showToast(getContext(), "正在开发");
        //                }
        //                if ("添加".equals(name)) {
        //                    if (listMore.size() == 0) {
        //                        ToastUtils.showToast(getContext(), "没有可添加的内容了");
        //                    } else {
        //                        //弹出一个popupwindow让用户选择添加icon
        //                        showMoreIcon(tv_grid_name);
        //                    }
        //                }
        //                //                switch (i){
        //                //                    case 0:
        //                //                        if(functionlist.contains(Consts.QRKM)) {
        //                //                            startActivity(new Intent(mContext, QrcodeActivity.class));
        //                //                        }else{
        //                //                            intent.putExtra("title",getString(R.string.qrcode));
        //                //                            intent.putExtra("code",Consts.QRKM);
        //                //                            startActivity(intent);
        //                //                        }
        //                //                        break;
        //                //                    case 1:
        //                //                        if(functionlist.contains(Consts.LYKM)){
        //                //                            startActivity(new Intent(mContext,BluetoothActivity.class));
        //                //                        }else{
        //                //                            intent.putExtra("title",getString(R.string.bluetooth));
        //                //                            intent.putExtra("code",Consts.LYKM);
        //                //                            startActivity(intent);
        //                //                        }
        //                //                        break;
        //                //                    case 2:
        //                //                        if(functionlist.contains(Consts.MJSJCX)) {
        //                //                            startActivity(new Intent(mContext, AccessdataActivity.class));
        //                //                        }else {
        //                //                            intent.putExtra("title",getString(R.string.accessdata));
        //                //                            intent.putExtra("code",Consts.MJSJCX);
        //                //                            startActivity(intent);
        //                //                        }
        //                //                        break;
        //                //                }
        //            }
        //        });
        //        gv_menjin.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
        //            @Override
        //            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
        //                TextView tv_grid_name = (TextView) view.findViewById(R.id.mydada_text_tv);
        //                final String name = String.valueOf(tv_grid_name.getText());
        //                final ImageView img_delete = view.findViewById(R.id.img_delete_icon);
        //                if (name.equals("二维码开门") || name.equals("蓝牙开门") || name.equals("添加")) {
        //                    ToastUtils.showToast(getContext(), "该图标不可删除");
        //                    return true;
        //                }
        //                //记录是对应的是否已删除
        //                if (img_delete.getVisibility() == View.VISIBLE) {
        //                    img_delete.setVisibility(View.GONE);
        //                } else {
        //                    img_delete.setVisibility(View.VISIBLE);
        //                    img_delete.setOnClickListener(new View.OnClickListener() {
        //                        @Override
        //                        public void onClick(View view) {
        //                            View parent = (View) img_delete.getParent();
        //                            img_delete.setVisibility(View.GONE);
        //                            TextView title_name = (TextView) parent.findViewById(R.id.mydada_text_tv);
        //                            String min_title = String.valueOf(title_name.getText());
        //                            int whichOne = 0;
        //                            for (int i = 0; i < list.size(); i++) {
        //                                MainMenuEntity menuEntity = list.get(i);
        //                                String text = menuEntity.getText();
        //                                if (min_title.equals(text)) {
        //                                    whichOne = i;
        //                                    break;
        //                                }
        //                            }
        //                            listMore.add(list.get(whichOne));
        //                            list.remove(whichOne);
        //                            adapter.notifyDataSetChanged();
        //                            ThreadUtils.runOnSubThread(new Runnable() {
        //                                @Override
        //                                public void run() {
        //                                    Gson gson = new Gson();
        //                                    String str = gson.toJson(list);
        //                                    SpUtils.putString(getContext(), "listStr", str);
        //                                }
        //                            });
        //                        }
        //                    });
        //                }
        //                return true;
        //            }
        //        });
    }
    //    private static int REQUEST_ENABLE = 4000;
    //    @Override
    //    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    //        super.onActivityResult(requestCode, resultCode, data);
    //        if (requestCode == REQUEST_ENABLE) {
    //            switch (resultCode) {
    //                // 点击确认按钮
    //                case Activity.RESULT_OK: {
    //                    //用户选择开启 Bluetooth，Bluetooth 会被开启
    //                    ToastUtils.showToast(mContext,"蓝牙开启了");
    //                    //跳转摇一摇界面
    //                    Intent intent = new Intent(mContext, BluetoothActivity.class);
    //                    mContext.startActivity(intent);
    //                }
    //                break;
    //                // 点击取消按钮或点击返回键
    //                case Activity.RESULT_CANCELED: {
    //                    //用户拒绝打开 Bluetooth, Bluetooth 不会被开启
    //                    ToastUtils.showToast(mContext,"开启蓝牙功能，才能搜索");
    //                }
    //                break;
    //                default:
    //                    break;
    //            }
    //        }
    //    }
}
