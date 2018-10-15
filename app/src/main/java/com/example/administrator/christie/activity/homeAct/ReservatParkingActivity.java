package com.example.administrator.christie.activity.homeAct;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.administrator.christie.InformationMessege.ProjectMsg;
import com.example.administrator.christie.R;
import com.example.administrator.christie.activity.BaseActivity;
import com.example.administrator.christie.adapter.BDInfoSpinnerAdapter;
import com.example.administrator.christie.adapter.LvOneStringAdapter;
import com.example.administrator.christie.modelInfo.PersonalDataInfo;
import com.example.administrator.christie.modelInfo.RequestParamsFM;
import com.example.administrator.christie.modelInfo.ResultInfo;
import com.example.administrator.christie.modelInfo.UserInfo;
import com.example.administrator.christie.util.HttpOkhUtils;
import com.example.administrator.christie.util.SPref;
import com.example.administrator.christie.util.ToastUtils;
import com.example.administrator.christie.view.CustomDatePicker;
import com.example.administrator.christie.websiteUrl.NetConfig;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Request;

/**
 * @创建者 AndyYan
 * @创建时间 2018/4/19 14:09
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class ReservatParkingActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout linear_back;
    private TextView     mTv_title, mTv_username;
    private Button       mBt_order;
    private LinearLayout linear_time;//选择停车时间
    private TextView     tv_pTime;
    private TextView     tv_pData;
    private String       markData, choosePlate, chooseProID;
    private UserInfo             mUserinfo;
    private List                 mBangList;
    private EditText             et_carno;//填写车牌号
    private Spinner              mSpinner_pro;
    private BDInfoSpinnerAdapter mDetailAdapter;//选择小区适配器
    private List<ProjectMsg>     dataProList;//项目数据
    private List<String>         mTimeData;//时间段数据
    //    private RecyclerView mRecv_stop_time;
    //    private Spinner  mSpinner_plate;
    //    private PlateSpinnerAdapter  mProjAdapter;//选择车牌适配器
    //    private List<ProjectMsg>     dataPlateList;//车牌数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservat_parking);
        initView();
        initData();
    }

    private void initView() {
        linear_back = (LinearLayout) findViewById(R.id.linear_back);
        mTv_username = (TextView) findViewById(R.id.tv_username);
        et_carno = (EditText) findViewById(R.id.et_carno);
        mSpinner_pro = (Spinner) findViewById(R.id.spinner_pro);
        mTv_title = (TextView) findViewById(R.id.tv_title);
        linear_time = (LinearLayout) findViewById(R.id.linear_time);
        tv_pTime = (TextView) findViewById(R.id.tv_pTime);
        tv_pData = (TextView) findViewById(R.id.tv_pData);
        mBt_order = (Button) findViewById(R.id.bt_order);
        //      mRecv_stop_time = (RecyclerView) findViewById(R.id.recv_stop_time);
        //      mSpinner_plate = (Spinner) findViewById(R.id.spinner_plate);
    }

    private void initData() {
        linear_back.setOnClickListener(this);
        mTv_title.setText("车位预约");
        mUserinfo = SPref.getObject(ReservatParkingActivity.this, UserInfo.class, "userinfo");
        String username = mUserinfo.getUsername();
        mTv_username.setText(username);

        //设置小区选择器
        dataProList = new ArrayList<>();
        ProjectMsg detailInfo = new ProjectMsg();
        detailInfo.setProject_name("请选择小区");
        dataProList.add(detailInfo);
        mDetailAdapter = new BDInfoSpinnerAdapter(ReservatParkingActivity.this, dataProList);
        mSpinner_pro.setAdapter(mDetailAdapter);
        mSpinner_pro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ProjectMsg msg = dataProList.get(i);
                String project_name = msg.getProject_name();
                if (!"请选择小区".equals(project_name)) {
                    String detail_id = msg.getId();
                    chooseProID = detail_id;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //设置车牌选择器
        //        dataPlateList = new ArrayList();
        //        ProjectMsg projectInfo = new ProjectMsg();
        //        projectInfo.setProject_name("请选择车牌");
        //        dataPlateList.add(projectInfo);
        //        mProjAdapter = new PlateSpinnerAdapter(ReservatParkingActivity.this, dataPlateList);
        //        mSpinner_plate.setAdapter(mProjAdapter);
        //        mSpinner_plate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        //            @Override
        //            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        //                ProjectMsg msg = dataPlateList.get(i);
        //                String project_name = msg.getProject_name();
        //                if (!"请选择车牌".equals(project_name)) {
        //                    choosePlate = project_name;
        //                }
        //            }
        //
        //            @Override
        //            public void onNothingSelected(AdapterView<?> adapterView) {
        //
        //            }
        //        });
        //获取当前日期
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        String data = simpleDateFormat.format(new Date());
        tv_pData.setText(data.substring(0, 10));
        //        tv_pTime.setText("请选择时间段");

        int subMtime = Integer.parseInt(data.substring(14, 15));
        if (subMtime < 3) {
            tv_pTime.setText(data.substring(11, 13) + ":00 -- " + data.substring(11, 13) + ":30");
        } else {
            int subHTime = Integer.parseInt(data.substring(11, 13));
            if (subHTime == 23) {
                tv_pTime.setText(data.substring(11, 13) + ":30 -- " + "00:00");
            } else {
                if (subHTime >= 9) {
                    tv_pTime.setText(data.substring(11, 13) + ":30 -- " + (subHTime + 1) + ":00");
                } else {
                    tv_pTime.setText(data.substring(11, 13) + ":30 -- 0" + (subHTime + 1) + ":00");
                }
            }
        }

        //选择停车时间
        tv_pData.setOnClickListener(this);
        tv_pTime.setOnClickListener(this);
        //设置选择停车时段数据
        mTimeData = new ArrayList();
        mTimeData.add("00:00 -- 00:30");
        mTimeData.add("00:30 -- 01:00");
        mTimeData.add("01:00 -- 01:30");
        mTimeData.add("01:30 -- 02:00");
        mTimeData.add("02:00 -- 02:30");
        mTimeData.add("02:30 -- 03:00");
        mTimeData.add("03:00 -- 03:30");
        mTimeData.add("03:30 -- 04:00");
        mTimeData.add("04:00 -- 04:30");
        mTimeData.add("04:30 -- 05:00");
        mTimeData.add("05:00 -- 05:30");
        mTimeData.add("05:30 -- 06:00");
        mTimeData.add("06:00 -- 06:30");
        mTimeData.add("06:30 -- 07:00");
        mTimeData.add("07:00 -- 07:30");
        mTimeData.add("07:30 -- 08:00");
        mTimeData.add("08:00 -- 08:30");
        mTimeData.add("08:30 -- 09:00");
        mTimeData.add("09:00 -- 09:30");
        mTimeData.add("09:30 -- 10:00");
        mTimeData.add("10:00 -- 10:30");
        mTimeData.add("10:30 -- 11:00");
        mTimeData.add("11:00 -- 11:30");
        mTimeData.add("11:30 -- 12:00");
        mTimeData.add("12:00 -- 12:30");
        mTimeData.add("12:30 -- 13:00");
        mTimeData.add("13:00 -- 13:30");
        mTimeData.add("13:30 -- 14:00");
        mTimeData.add("14:00 -- 14:30");
        mTimeData.add("14:30 -- 15:00");
        mTimeData.add("15:00 -- 15:30");
        mTimeData.add("15:30 -- 16:00");
        mTimeData.add("16:00 -- 16:30");
        mTimeData.add("16:30 -- 17:00");
        mTimeData.add("17:00 -- 17:30");
        mTimeData.add("17:30 -- 18:00");
        mTimeData.add("18:00 -- 18:30");
        mTimeData.add("18:30 -- 19:00");
        mTimeData.add("19:00 -- 19:30");
        mTimeData.add("19:30 -- 20:00");
        mTimeData.add("20:00 -- 20:30");
        mTimeData.add("20:30 -- 21:00");
        mTimeData.add("21:00 -- 21:30");
        mTimeData.add("21:30 -- 22:00");
        mTimeData.add("22:00 -- 22:30");
        mTimeData.add("22:30 -- 23:00");
        mTimeData.add("23:00 -- 23:30");
        mTimeData.add("23:30 -- 00:00");


        //        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        //        mRecv_stop_time.setLayoutManager(layoutManager);
        //        TimePiontAdapter timePiontAdapter = new TimePiontAdapter(this, mTimeData, mSelected);
        //        mRecv_stop_time.setAdapter(timePiontAdapter);
        //提交预约
        mBt_order.setOnClickListener(this);
        String userid = mUserinfo.getUserid();
        //从网络获取个人绑定的小区
        getBDProjectID(userid);
        //从网络获取个人车牌
        //        choosePlate(userid);
    }

    private PopupWindow popupWindow;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.linear_back:
                finish();
                break;
            case R.id.tv_pData://选择日期
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                String data = simpleDateFormat.format(new Date());
                //打开时间选择器
                CustomDatePicker dpk = new CustomDatePicker(ReservatParkingActivity.this, new CustomDatePicker.ResultHandler() {
                    @Override
                    public void handle(String time) { // 回调接口，获得选中的时间
                        tv_pData.setText(time.substring(0, 10));
                    }
                }, data, "2090-12-31 00:00"); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
                dpk.showSpecificTime(false); // 显示时和分
                dpk.setIsLoop(true); // 允许循环滚动
                dpk.show(data);
                //dpk.show(mTv_park_time.getText().toString());
                break;
            case R.id.tv_pTime://选择时间段
                //从底部弹出popupwindow选择时间段
                openPopupWindow(tv_pTime);
                break;
            case R.id.bt_order:
                String carno = String.valueOf(et_carno.getText()).trim();
                if (null == chooseProID || "".equals(chooseProID)) {
                    ToastUtils.showToast(this, "请选择小区");
                    return;
                }
                if (null == carno || "".equals(carno)) {
                    ToastUtils.showToast(this, "请填写车牌");
                    return;
                }
                choosePlate = carno;
                String fdata = String.valueOf(tv_pData.getText()).trim();
                String ftime = String.valueOf(tv_pTime.getText()).trim();
                //String timeLong = mTimeData.get(mSelected);
                //提交信息
                sendToInt(fdata, ftime);
                break;
        }
    }

    private void openPopupWindow(TextView tv) {
        //防止重复按按钮
        if (popupWindow != null && popupWindow.isShowing()) {
            return;
        }
        //设置PopupWindow的View
        View view = LayoutInflater.from(this).inflate(R.layout.view_popupwindow, null);
        popupWindow = new PopupWindow(view, RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        //设置背景,这个没什么效果，不添加会报错
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //设置点击弹窗外隐藏自身
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        //设置动画
        popupWindow.setAnimationStyle(R.style.PopupWindow);
        //设置位置
        popupWindow.showAtLocation(tv, Gravity.BOTTOM, 0, 0);
        //设置消失监听
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //设置背景色
                setBackgroundAlpha(1.0f);
            }
        });
        //设置PopupWindow的View点击事件
        setOnPopupViewClick(view, tv);
        //设置背景色
        setBackgroundAlpha(0.5f);
    }

    private void setOnPopupViewClick(View view, final TextView tv) {
        ListView lv_time = view.findViewById(R.id.lv_time);
        LvOneStringAdapter oneStringAdapter = new LvOneStringAdapter(this, mTimeData);
        lv_time.setAdapter(oneStringAdapter);
        lv_time.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                tv.setText(mTimeData.get(i));
                popupWindow.dismiss();
            }
        });
    }

    //设置屏幕背景透明效果
    public void setBackgroundAlpha(float alpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = alpha;
        getWindow().setAttributes(lp);
    }

    private void getBDProjectID(String userid) {
        String personalDetailUrl = NetConfig.PERSONALDATA;
        RequestParamsFM params = new RequestParamsFM();
        params.put("userid", userid);
        HttpOkhUtils.getInstance().doGetWithParams(personalDetailUrl, params, new HttpOkhUtils.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                ToastUtils.showToast(ReservatParkingActivity.this, "网络错误");
            }

            @Override
            public void onSuccess(int code, String resbody) {
                if (code != 200) {
                    ToastUtils.showToast(ReservatParkingActivity.this, "网络错误，错误码" + code);
                    return;
                }
                Gson gson = new Gson();
                PersonalDataInfo personalDataInfo = gson.fromJson(resbody, PersonalDataInfo.class);
                PersonalDataInfo.ArrBean arr = personalDataInfo.getArr();
                if (null == dataProList) {
                    dataProList = new ArrayList<>();
                } else {
                    dataProList.clear();
                }
                ProjectMsg detailInfo = new ProjectMsg();
                detailInfo.setProject_name("请选择小区");
                dataProList.add(detailInfo);
                List<PersonalDataInfo.ArrBean.ListProjectBean> listProject = arr.getListProject();
                for (int i = 0; i < listProject.size(); i++) {
                    PersonalDataInfo.ArrBean.ListProjectBean listProjectBean = listProject.get(i);
                    String project_name = listProjectBean.getProject_name();
                    String fname = listProjectBean.getFname();
                    String detail_id = listProjectBean.getProjectdetail_id();
                    ProjectMsg bdInfo = new ProjectMsg();
                    bdInfo.setProject_name(project_name);
                    bdInfo.setDetail_name(fname);
                    bdInfo.setId(detail_id);
                    dataProList.add(bdInfo);
                }
                mDetailAdapter.notifyDataSetChanged();
            }
        });
    }

    private void sendToInt(String fdata, String time) {
        String userid = mUserinfo.getUserid();
        String reserveUrl = NetConfig.PARKRESERVE;
        RequestParamsFM params = new RequestParamsFM();
        params.put("userid", userid);
        params.put("no", choosePlate);
        params.put("time", fdata + " " + time);
        params.put("project_detail_id", chooseProID);
        params.setUseJsonStreamer(true);
        HttpOkhUtils.getInstance().doPost(reserveUrl, params, new HttpOkhUtils.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                ToastUtils.showToast(ReservatParkingActivity.this, "网络错误");
            }

            @Override
            public void onSuccess(int code, String resbody) {
                if (code != 200) {
                    ToastUtils.showToast(ReservatParkingActivity.this, "网络请求失败，错误码" + code);
                    return;
                }
                Gson gson = new Gson();
                ResultInfo resultInfo = gson.fromJson(resbody, ResultInfo.class);
                if ("1".equals(resultInfo.getResult())) {
                    ToastUtils.showToast(ReservatParkingActivity.this, resultInfo.getMessage());
                    finish();
                } else {
                    ToastUtils.showToast(ReservatParkingActivity.this, "预约失败");
                }
            }
        });

    }

    //    private void choosePlate(String userid) {
    //        //访问网络获取个人车牌
    //        String userPlateUrl = NetConfig.GETPLATE;
    //        RequestParamsFM params = new RequestParamsFM();
    //        params.put("userid", userid);
    //        HttpOkhUtils.getInstance().doGetWithParams(userPlateUrl, params, new HttpOkhUtils.HttpCallBack() {
    //            @Override
    //            public void onError(Request request, IOException e) {
    //                ToastUtils.showToast(ReservatParkingActivity.this, "网络错误");
    //            }
    //
    //            @Override
    //            public void onSuccess(int code, String resbody) {
    //                if (code != 200) {
    //                    ToastUtils.showToast(ReservatParkingActivity.this, "网络请求失败，错误码" + code);
    //                    return;
    //                }
    //                if (null == dataPlateList) {
    //                    dataPlateList = new ArrayList();
    //                } else {
    //                    dataPlateList.clear();
    //                }
    //                ProjectMsg projectInfo = new ProjectMsg();
    //                projectInfo.setProject_name("请选择车牌");
    //                dataPlateList.add(projectInfo);
    //                Gson gson = new Gson();
    //                try {
    //                    JSONArray jsonArray = new JSONArray(resbody);
    //                    for (int i = 0; i < jsonArray.length(); i++) {
    //                        PersonalPlateInfo info = gson.fromJson(jsonArray.get(i).toString(), PersonalPlateInfo.class);
    //                        String fplateno = info.getFplateno();
    //                        ProjectMsg plateInfo = new ProjectMsg();
    //                        plateInfo.setProject_name(fplateno);
    //                        dataPlateList.add(plateInfo);
    //                    }
    //                    mProjAdapter.notifyDataSetChanged();
    //                } catch (JSONException e) {
    //                    e.printStackTrace();
    //                }
    //            }
    //        });
    //    }
}
