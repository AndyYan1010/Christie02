package com.example.administrator.christie.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.christie.R;
import com.example.administrator.christie.util.ToastUtils;

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
    private TextView  mTv_title;
    private Button    mBt_create;
    private EditText  mEdit_name, mEdit_phone, mEdit_date, mEdit_longtime, mEdit_reason;

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
        mEdit_date = (EditText) view.findViewById(R.id.edit_date);
        mEdit_longtime = (EditText) view.findViewById(R.id.edit_longtime);
        mEdit_reason = (EditText) view.findViewById(R.id.edit_reason);
        mBt_create = (Button) view.findViewById(R.id.bt_create);
    }

    private void setData() {
        mImg_back.setOnClickListener(this);
        mTv_title.setText("访客邀请");
        mBt_create.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                getActivity().finish();
                break;
            case R.id.bt_create:
                String name = String.valueOf(mEdit_name.getText()).trim();
                String phone = String.valueOf(mEdit_phone.getText()).trim();
                String date = String.valueOf(mEdit_date.getText()).trim();
                String longtime = String.valueOf(mEdit_longtime.getText()).trim();
                String reason = String.valueOf(mEdit_reason.getText()).trim();
                if (name.equals("") || name.equals("请输入来访人姓名")) {
                    ToastUtils.showToast(mContext, "来访人姓名不能为空");
                    return;
                }
                if (phone.equals("") || phone.equals("请输入来访人电话")) {
                    ToastUtils.showToast(mContext, "来访人电话不能为空");
                    return;
                }
                if (date.equals("") || date.equals("请输入来访日期")) {
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
                //获取开始时间 和结束时间，跳转数据结果
                FragmentTransaction ftt = getFragmentManager().beginTransaction();
                InvitationQRcodeFragment invitationQRcodeFgt = new InvitationQRcodeFragment();
                String detailJson = "name:" + name + "phone:" + phone + "date:" + date + "longtime:" + longtime + "reason:" + reason;
                invitationQRcodeFgt.setInfoJson(detailJson);
                ftt.add(R.id.frame_accessdata, invitationQRcodeFgt, "invitationQRcodeFgt");
                ftt.addToBackStack(null);
                ftt.commit();
                break;
        }
    }
}
