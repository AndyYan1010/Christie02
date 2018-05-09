package com.example.administrator.christie.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.administrator.christie.InformationMessege.ProjectMsg;
import com.example.administrator.christie.R;
import com.example.administrator.christie.adapter.BDInfoSpinnerAdapter;
import com.example.administrator.christie.adapter.ProSpinnerAdapter;
import com.example.administrator.christie.adapter.TimePiontAdapter;
import com.example.administrator.christie.modelInfo.PersonalDataInfo;
import com.example.administrator.christie.modelInfo.PersonalPlateInfo;
import com.example.administrator.christie.modelInfo.RequestParamsFM;
import com.example.administrator.christie.modelInfo.UserInfo;
import com.example.administrator.christie.util.HttpOkhUtils;
import com.example.administrator.christie.util.SPref;
import com.example.administrator.christie.util.ToastUtils;
import com.example.administrator.christie.websiteUrl.NetConfig;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
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

    private ImageView mImg_back;
    private TextView  mTv_title, mTv_park_time, mTv_username;
    private RecyclerView mRecv_stop_time;
    private Button       mBt_order;
    private int mSelected = 0;
    private List<String> mTimeData;
    private String       markData, choosePlate, chooseProID;
    private UserInfo mUserinfo;
    private List     mBangList;
    private Spinner  mSpinner_plate, mSpinner_pro;
    private ProSpinnerAdapter    mProjAdapter;//选择车牌适配器
    private BDInfoSpinnerAdapter mDetailAdapter;//选择小区适配器
    private List<ProjectMsg>     dataPlateList, dataProList;//车牌数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservat_parking);
        initView();
        initData();
    }

    private void initView() {
        mImg_back = (ImageView) findViewById(R.id.img_back);
        mTv_username = (TextView) findViewById(R.id.tv_username);
        mSpinner_plate = (Spinner) findViewById(R.id.spinner_plate);
        mSpinner_pro = (Spinner) findViewById(R.id.spinner_pro);
        mTv_title = (TextView) findViewById(R.id.tv_title);
        mTv_park_time = (TextView) findViewById(R.id.tv_park_time);
        mRecv_stop_time = (RecyclerView) findViewById(R.id.recv_stop_time);
        mBt_order = (Button) findViewById(R.id.bt_order);
    }

    private void initData() {
        mImg_back.setOnClickListener(this);
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
        dataPlateList = new ArrayList();
        ProjectMsg projectInfo = new ProjectMsg();
        projectInfo.setProject_name("请选择车牌");
        dataPlateList.add(projectInfo);
        mProjAdapter = new ProSpinnerAdapter(ReservatParkingActivity.this, dataPlateList);
        mSpinner_plate.setAdapter(mProjAdapter);
        mSpinner_plate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ProjectMsg msg = dataPlateList.get(i);
                String project_name = msg.getProject_name();
                if (!"请选择车牌".equals(project_name)) {
                    choosePlate = project_name;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //选择停车时间
        mTv_park_time.setOnClickListener(this);
        //设置选择停车时长数据
        mTimeData = new ArrayList();
        mTimeData.add("30分钟");
        mTimeData.add("1小时");
        mTimeData.add("2小时");
        mTimeData.add("2.5小时");
        mTimeData.add("3小时");
        mTimeData.add("3.5小时");
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecv_stop_time.setLayoutManager(layoutManager);
        TimePiontAdapter timePiontAdapter = new TimePiontAdapter(this, mTimeData, mSelected);
        mRecv_stop_time.setAdapter(timePiontAdapter);
        //提交预约
        mBt_order.setOnClickListener(this);
        String userid = mUserinfo.getUserid();
        //从网络获取个人绑定的小区
        getBDProjectID(userid);
        //从网络获取个人车牌
        choosePlate(userid);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_park_time:
                //打开时间选择器
                Calendar calendar = Calendar.getInstance();
                showDatePickerDialog(calendar, 2);
                break;
            case R.id.bt_order:
                if (null == chooseProID || "".equals(chooseProID)) {
                    ToastUtils.showToast(this, "请选择小区");
                    return;
                }
                if (null == choosePlate || "".equals(choosePlate)) {
                    ToastUtils.showToast(this, "请选择车牌");
                    return;
                }
                String time = String.valueOf(mTv_park_time.getText()).trim();
                String timeLong = mTimeData.get(mSelected);
                //提交信息
                sendToInt(time, timeLong);
                break;
        }
    }

    private void sendToInt(String time, String timeData) {
        String userid = mUserinfo.getUserid();
        String reserveUrl = NetConfig.PARKRESERVE;
        RequestParamsFM params = new RequestParamsFM();
        params.put("userid", userid);
        params.put("no", choosePlate);
        params.put("time", time);
        params.put("length", timeData);
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
                ToastUtils.showToast(ReservatParkingActivity.this, "提交成功");
            }
        });

    }

    private void choosePlate(String userid) {
        //访问网络获取个人车牌
        String userPlateUrl = NetConfig.GETPLATE;
        RequestParamsFM params = new RequestParamsFM();
        params.put("userid", userid);
        HttpOkhUtils.getInstance().doGetWithParams(userPlateUrl, params, new HttpOkhUtils.HttpCallBack() {
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
                if (null == dataPlateList) {
                    dataPlateList = new ArrayList();
                } else {
                    dataPlateList.clear();
                }
                ProjectMsg projectInfo = new ProjectMsg();
                projectInfo.setProject_name("请选择车牌");
                dataPlateList.add(projectInfo);
                Gson gson = new Gson();
                try {
                    JSONArray jsonArray = new JSONArray(resbody);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        PersonalPlateInfo info = gson.fromJson(jsonArray.get(i).toString(), PersonalPlateInfo.class);
                        String fplateno = info.getFplateno();
                        ProjectMsg plateInfo = new ProjectMsg();
                        plateInfo.setProject_name(fplateno);
                        dataPlateList.add(plateInfo);
                    }
                    mProjAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void setSelected(int num) {
        this.mSelected = num;
    }

    private void showDatePickerDialog(Calendar calendar, int themeResId) {
        new DatePickerDialog(this, themeResId, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                String yue = "";
                String ri = "";
                i1 = i1 + 1;
                if (i1 < 10) {
                    yue = "0" + i1;
                } else {
                    yue = "" + i1;
                }
                if (i2 < 10) {
                    ri = "0" + i2;
                } else {
                    ri = "" + i2;
                }
                markData = i + "-" + yue + "-" + ri;
                Calendar calendar = Calendar.getInstance();
                showTimePickerDialog(calendar, 2);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void showTimePickerDialog(Calendar calendar, int themeResId) {
        new TimePickerDialog(this, themeResId, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                String xs = "";
                if (i < 10) {
                    xs = "0" + i;
                } else {
                    xs = "" + i;
                }
                String fz = "" + i1;
                if (i1 < 10) {
                    fz = "0" + i1;
                }
                mTv_park_time.setText(markData + " " + i + ":" + fz);
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
    }
}
