package com.example.administrator.christie.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.administrator.christie.adapter.BDInfoSpinnerAdapter;
import com.example.administrator.christie.adapter.LvOneStringAdapter;
import com.example.administrator.christie.modelInfo.InvoteFkResultInfo;
import com.example.administrator.christie.modelInfo.PersonalDataInfo;
import com.example.administrator.christie.modelInfo.RequestParamsFM;
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
 * @创建时间 2018/4/23 15:00
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class WriteInvitationFragment extends Fragment implements View.OnClickListener {
    private Context      mContext;
    private View         view;
    private LinearLayout linear_back;
    private TextView     mTv_title;
    private TextView     tv_pData;
    private TextView     tv_time1;
    private TextView     tv_time2;
    private Button       mBt_create;
    private EditText     mEdit_name, mEdit_phone, mEdit_reason;
    private EditText             et_arrTime;//到达时间
    private Spinner              mSpinner_area;
    private List<ProjectMsg>     dataProList;
    private BDInfoSpinnerAdapter mDetailAdapter;//选择小区适配器
    private String               chooseProID;//小区id
    private String               chooseProDetailID;//小区详细id
    private String               mFcode;//小区code
    private String               markData;
    private String               mUserid;
    private String               mUserName;
    private String               stime2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getContext();
        view = inflater.inflate(R.layout.fragment_write_invitation, container, false);
        mContext = getContext();
        setViews();
        setData();
        return view;
    }

    private void setViews() {
        linear_back = (LinearLayout) view.findViewById(R.id.linear_back);
        mTv_title = (TextView) view.findViewById(R.id.tv_title);
        mEdit_name = (EditText) view.findViewById(R.id.edit_name);
        mEdit_phone = (EditText) view.findViewById(R.id.edit_phone);
        tv_pData = (TextView) view.findViewById(R.id.tv_pData);
        tv_time1 = (TextView) view.findViewById(R.id.tv_time1);
        tv_time2 = (TextView) view.findViewById(R.id.tv_time2);
        //        et_arrTime = (EditText) view.findViewById(R.id.et_arrTime);
        mEdit_reason = (EditText) view.findViewById(R.id.edit_reason);
        mSpinner_area = view.findViewById(R.id.spinner_area);
        mBt_create = (Button) view.findViewById(R.id.bt_create);
    }

    private void setData() {
        linear_back.setOnClickListener(this);
        mTv_title.setText("访客邀请");
        //设置小区选择器
        dataProList = new ArrayList<>();
        ProjectMsg detailInfo = new ProjectMsg();
        detailInfo.setProject_name("请选择项目");
        dataProList.add(detailInfo);
        mDetailAdapter = new BDInfoSpinnerAdapter(getContext(), dataProList);
        mSpinner_area.setAdapter(mDetailAdapter);
        mSpinner_area.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ProjectMsg msg = dataProList.get(i);
                String project_name = msg.getProject_name();
                if (!"请选择项目".equals(project_name)) {
                    String project_id = msg.getUpperID();
                    String detail_id = msg.getId();
                    String fcode = msg.getType();
                    chooseProID = project_id;
                    chooseProDetailID = detail_id;
                    mFcode = fcode;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        UserInfo userinfo = SPref.getObject(getContext(), UserInfo.class, "userinfo");
        mUserid = userinfo.getUserid();
        mUserName = userinfo.getUsername();
        //从网络获取个人绑定的小区
        getBDProjectID(mUserid);
        //选择时间数据设置
        setTimeList();
        tv_pData.setOnClickListener(this);
        tv_time1.setOnClickListener(this);
        tv_time2.setOnClickListener(this);
        mBt_create.setOnClickListener(this);
    }

    private void setTimeList() {
        mTimeData = new ArrayList();
        //        mTimeData.add("00:00");
        //        mTimeData.add("00:30");
        //        mTimeData.add("01:00");
        //        mTimeData.add("01:30");
        //        mTimeData.add("02:00");
        //        mTimeData.add("02:30");
        //        mTimeData.add("03:00");
        //        mTimeData.add("03:30");
        //        mTimeData.add("04:00");
        //        mTimeData.add("04:30");
        //        mTimeData.add("05:00");
        //        mTimeData.add("05:30");
        mTimeData.add("06:00");
        mTimeData.add("06:30");
        mTimeData.add("07:00");
        mTimeData.add("07:30");
        mTimeData.add("08:00");
        mTimeData.add("08:30");
        mTimeData.add("09:00");
        mTimeData.add("09:30");
        mTimeData.add("10:00");
        mTimeData.add("10:30");
        mTimeData.add("11:00");
        mTimeData.add("11:30");
        mTimeData.add("12:00");
        mTimeData.add("12:30");
        mTimeData.add("13:00");
        mTimeData.add("13:30");
        mTimeData.add("14:00");
        mTimeData.add("14:30");
        mTimeData.add("15:00");
        mTimeData.add("15:30");
        mTimeData.add("16:00");
        mTimeData.add("16:30");
        mTimeData.add("17:00");
        mTimeData.add("17:30");
        mTimeData.add("18:00");
        mTimeData.add("18:30");
        mTimeData.add("19:00");
        mTimeData.add("19:30");
        mTimeData.add("20:00");
        mTimeData.add("20:30");
        mTimeData.add("21:00");
        mTimeData.add("21:30");
        mTimeData.add("22:00");
        mTimeData.add("22:30");
        mTimeData.add("23:00");
        mTimeData.add("23:30");
        mTimeData.add("24:00");
    }

    private void getBDProjectID(String userid) {
        String personalDetailUrl = NetConfig.PERSONALDATA;
        RequestParamsFM params = new RequestParamsFM();
        params.put("userid", userid);
        HttpOkhUtils.getInstance().doGetWithParams(personalDetailUrl, params, new HttpOkhUtils.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                ToastUtils.showToast(getContext(), "网络错误");
            }

            @Override
            public void onSuccess(int code, String resbody) {
                if (code != 200) {
                    ToastUtils.showToast(getContext(), "网络错误，错误码" + code);
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
                detailInfo.setProject_name("请选择项目");
                dataProList.add(detailInfo);
                List<PersonalDataInfo.ArrBean.ListProjectBean> listProject = arr.getListProject();
                for (int i = 0; i < listProject.size(); i++) {
                    PersonalDataInfo.ArrBean.ListProjectBean listProjectBean = listProject.get(i);
                    String project_name = listProjectBean.getProject_name();
                    String project_id = listProjectBean.getProject_id();
                    String fname = listProjectBean.getFname();
                    String detail_id = listProjectBean.getProjectdetail_id();
                    String fcode = listProjectBean.getFcode();
                    ProjectMsg bdInfo = new ProjectMsg();
                    bdInfo.setProject_name(project_name);
                    bdInfo.setUpperID(project_id);
                    bdInfo.setDetail_name(fname);
                    bdInfo.setId(detail_id);
                    bdInfo.setType(fcode);
                    dataProList.add(bdInfo);
                }
                mDetailAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.linear_back:
                getActivity().finish();
                break;
            case R.id.tv_pData:
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                String data = simpleDateFormat.format(new Date());
                //打开时间选择器
                CustomDatePicker dpk = new CustomDatePicker(getContext(), new CustomDatePicker.ResultHandler() {
                    @Override
                    public void handle(String time) { // 回调接口，获得选中的时间
                        tv_pData.setText(time.substring(0, 10));
                    }
                }, data, "2090-12-31 00:00"); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
                dpk.showSpecificTime(false); // 显示时和分
                dpk.setIsLoop(true); // 允许循环滚动
                dpk.show(data);
                break;
            case R.id.tv_time1:
                //从底部弹出popupwindow选择时间段
                openPopupWindow(tv_time1);
                break;
            case R.id.tv_time2:
                //从底部弹出popupwindow选择时间段
                openPopupWindow(tv_time2);
                break;
            case R.id.bt_create:
                String name = String.valueOf(mEdit_name.getText()).trim();
                String phone = String.valueOf(mEdit_phone.getText()).trim();
                String date = String.valueOf(tv_pData.getText()).trim();
                String stime1 = String.valueOf(tv_time1.getText()).trim();
                stime2 = String.valueOf(tv_time2.getText()).trim();

                //                String longtime = String.valueOf(mEdit_longtime.getText()).trim();
                String reason = String.valueOf(mEdit_reason.getText()).trim();
                if (name.equals("") || name.equals("请输入来访人姓名")) {
                    ToastUtils.showToast(mContext, "来访人姓名不能为空");
                    return;
                }
                if (phone.equals("") || phone.equals("请输入来访人电话")) {
                    ToastUtils.showToast(mContext, "来访人电话不能为空");
                    return;
                } else if (phone.length() != 11) {
                    ToastUtils.showToast(mContext, "电话格式不正确");
                    return;
                }
                if (date.equals("") || date.equals("请选择来访日期")) {
                    ToastUtils.showToast(mContext, "来访日期不能为空");
                    return;
                }
                if ("".equals(stime1) ) {
                    stime1="06:00";
                }
                if ("".equals(stime2)){
                    stime2="24:00";
                }
                //                if (longtime.equals("") || longtime.equals("请输入来访时长")) {
                //                    ToastUtils.showToast(mContext, "来访时长不能为空");
                //                    return;
                //                }
                if (reason.equals("") || reason.equals("请输入来访事由")) {
                    ToastUtils.showToast(mContext, "来访事由不能为空");
                    return;
                }
                if (null == chooseProDetailID || "请选择项目".equals(chooseProDetailID)) {
                    ToastUtils.showToast(mContext, "请选择项目");
                    return;
                }
                //获取邀请二维码数据
                getInvitationQc(name, phone, date + " " + stime1, reason);
                break;
        }
    }

    private PopupWindow  popupWindow;
    private List<String> mTimeData;//时间段数据

    private void openPopupWindow(TextView tv) {
        //防止重复按按钮
        if (popupWindow != null && popupWindow.isShowing()) {
            return;
        }
        //设置PopupWindow的View
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_popupwindow, null);
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
        LvOneStringAdapter oneStringAdapter = new LvOneStringAdapter(getContext(), mTimeData);
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
        WindowManager.LayoutParams lp = ((Activity) getContext()).getWindow().getAttributes();
        lp.alpha = alpha;
        ((Activity) getContext()).getWindow().setAttributes(lp);
    }

    private void getInvitationQc(String name, String phone, String date, String reason) {
        String InvitationQcUrl = NetConfig.INVITE;
        RequestParamsFM params = new RequestParamsFM();
        params.put("userid", mUserid);
        params.put("username", mUserName);
        params.put("fname", name);
        params.put("fmobile", phone);
        params.put("fdate", date);
        params.put("fdate2", stime2);
        params.put("fcode", mFcode);
        params.put("freason", reason);
        params.put("project_id", chooseProID);
        params.put("project_detail_id", chooseProDetailID);
        params.setUseJsonStreamer(true);
        HttpOkhUtils.getInstance().doPost(InvitationQcUrl, params, new HttpOkhUtils.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                ToastUtils.showToast(getContext(), "网络错误");
            }

            @Override
            public void onSuccess(int code, String resbody) {
                if (code == 200) {
                    Gson gson = new Gson();
                    InvoteFkResultInfo invoteFkResultInfo = gson.fromJson(resbody, InvoteFkResultInfo.class);
                    String result = invoteFkResultInfo.getResult();
                    if ("1".equals(result)) {
                        String code1 = invoteFkResultInfo.getCode();
                        //获取开始时间 和结束时间，跳转数据结果
                        FragmentTransaction ftt = getFragmentManager().beginTransaction();
                        InvitationQRcodeFragment invitationQRcodeFgt = new InvitationQRcodeFragment();
                        invitationQRcodeFgt.setInfoJson(code1);
                        ftt.add(R.id.frame_accessdata, invitationQRcodeFgt, "invitationQRcodeFgt");
                        ftt.addToBackStack(null);
                        ftt.commit();
                    } else {
                        ToastUtils.showToast(getContext(), "提交失败");
                    }
                } else {
                    ToastUtils.showToast(getContext(), "数据请求失败");
                }
            }
        });
    }
}
