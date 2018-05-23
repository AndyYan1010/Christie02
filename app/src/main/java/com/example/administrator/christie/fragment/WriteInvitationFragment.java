package com.example.administrator.christie.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.administrator.christie.InformationMessege.ProjectMsg;
import com.example.administrator.christie.R;
import com.example.administrator.christie.adapter.BDInfoSpinnerAdapter;
import com.example.administrator.christie.modelInfo.PersonalDataInfo;
import com.example.administrator.christie.modelInfo.RequestParamsFM;
import com.example.administrator.christie.modelInfo.UserInfo;
import com.example.administrator.christie.util.HttpOkhUtils;
import com.example.administrator.christie.util.RegexUtils;
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
    private Context   mContext;
    private View      view;
    private ImageView mImg_back;
    private TextView  mTv_title, mTv_date;
    private Button   mBt_create;
    private EditText mEdit_name, mEdit_phone, mEdit_longtime, mEdit_reason;
    private Spinner              mSpinner_area;
    private List<ProjectMsg>     dataProList;
    private BDInfoSpinnerAdapter mDetailAdapter;//选择小区适配器
    private String               chooseProID;//小区id
    private String               markData;
    private String               mUserid;

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
        mImg_back = (ImageView) view.findViewById(R.id.img_back);
        mTv_title = (TextView) view.findViewById(R.id.tv_title);
        mEdit_name = (EditText) view.findViewById(R.id.edit_name);
        mEdit_phone = (EditText) view.findViewById(R.id.edit_phone);
        mTv_date = view.findViewById(R.id.tv_date);
        mEdit_longtime = (EditText) view.findViewById(R.id.edit_longtime);
        mEdit_reason = (EditText) view.findViewById(R.id.edit_reason);
        mSpinner_area = view.findViewById(R.id.spinner_area);
        mBt_create = (Button) view.findViewById(R.id.bt_create);
    }

    private void setData() {
        mImg_back.setOnClickListener(this);
        mTv_title.setText("访客邀请");
        //设置小区选择器
        dataProList = new ArrayList<>();
        ProjectMsg detailInfo = new ProjectMsg();
        detailInfo.setProject_name("请选择小区");
        dataProList.add(detailInfo);
        mDetailAdapter = new BDInfoSpinnerAdapter(getContext(), dataProList);
        mSpinner_area.setAdapter(mDetailAdapter);
        mSpinner_area.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        UserInfo userinfo = SPref.getObject(getContext(), UserInfo.class, "userinfo");
        mUserid = userinfo.getUserid();
        //从网络获取个人绑定的小区
        getBDProjectID(mUserid);
        mTv_date.setOnClickListener(this);
        mBt_create.setOnClickListener(this);
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
                getActivity().finish();
                break;
            case R.id.tv_date:
                //打开时间选择器
                CustomDatePicker dpk = new CustomDatePicker(getContext(), new CustomDatePicker.ResultHandler() {
                    @Override
                    public void handle(String time) { // 回调接口，获得选中的时间
                        mTv_date.setText(time);
                    }
                }, "2010-01-01 00:00", "2090-12-31 00:00"); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
                dpk.showSpecificTime(true); // 显示时和分
                dpk.setIsLoop(true); // 允许循环滚动
                //获取当前日期
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                String data = simpleDateFormat.format(new Date());
                dpk.show(data);
                break;
            case R.id.bt_create:
                String name = String.valueOf(mEdit_name.getText()).trim();
                String phone = String.valueOf(mEdit_phone.getText()).trim();
                String date = String.valueOf(mTv_date.getText()).trim();
                String longtime = String.valueOf(mEdit_longtime.getText()).trim();
                String reason = String.valueOf(mEdit_reason.getText()).trim();
                if (name.equals("") || name.equals("请输入来访人姓名")) {
                    ToastUtils.showToast(mContext, "来访人姓名不能为空");
                    return;
                }
                if (phone.equals("") || phone.equals("请输入来访人电话")) {
                    ToastUtils.showToast(mContext, "来访人电话不能为空");
                    return;
                } else if (!RegexUtils.checkMobile(phone)) {
                    ToastUtils.showToast(mContext, "电话格式不正确");
                    return;
                }
                if (date.equals("") || date.equals("请选择来访日期")) {
                    ToastUtils.showToast(mContext, "来访日期不能为空");
                    return;
                }
                if (longtime.equals("") || longtime.equals("请输入来访时长")) {
                    ToastUtils.showToast(mContext, "来访时长不能为空");
                    return;
                }
                if (reason.equals("") || reason.equals("请输入来访事由")) {
                    ToastUtils.showToast(mContext, "来访事由不能为空");
                    return;
                }
                if (null == chooseProID || "请选择小区".equals(chooseProID)) {
                    ToastUtils.showToast(mContext, "请选择小区");
                    return;
                }
                //获取邀请二维码数据
                getInvitationQc(name, phone, date, longtime, reason, chooseProID);
                break;
        }
    }

    private void getInvitationQc(String name, String phone, String date, String longtime, String reason, String detail_id) {
        date = date+" 00:00:00";
        String InvitationQcUrl = NetConfig.INVITE;
        RequestParamsFM params = new RequestParamsFM();
        params.put("userid", mUserid);
        params.put("fname", name);
        params.put("fmobile", phone);
        params.put("fdate", date);
        params.put("flength", longtime);
        params.put("freason", reason);
        params.put("project_detail_id", detail_id);
        params.setUseJsonStreamer(true);
        HttpOkhUtils.getInstance().doPost(InvitationQcUrl, params, new HttpOkhUtils.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                ToastUtils.showToast(getContext(), "网络错误");
            }

            @Override
            public void onSuccess(int code, String resbody) {
                if (code == 200) {
                    //获取开始时间 和结束时间，跳转数据结果
                    FragmentTransaction ftt = getFragmentManager().beginTransaction();
                    InvitationQRcodeFragment invitationQRcodeFgt = new InvitationQRcodeFragment();
                    String detailJson = "name:";
                    //                    String detailJson = "name:" + name + "phone:" + phone + "date:" + date + "longtime:" + longtime + "reason:" + reason;
                    //                    invitationQRcodeFgt.setInfoJson(detailJson);
                    //                    ftt.add(R.id.frame_accessdata, invitationQRcodeFgt, "invitationQRcodeFgt");
                    //                    ftt.addToBackStack(null);
                    //                    ftt.commit();
                    ToastUtils.showToast(getContext(), "测试二维码");
                } else {
                    ToastUtils.showToast(getContext(), "数据请求失败");
                }
            }
        });
    }
}
