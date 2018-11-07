package com.example.administrator.christie.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.christie.R;
import com.example.administrator.christie.util.KeyboardUtil;
import com.example.administrator.christie.util.ToastUtils;

/**
 * @创建者 AndyYan
 * @创建时间 2018/11/7 16:16
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class SearchPay4OtherCarsFragment extends Fragment implements View.OnClickListener {
    private View                 mRootView;
    private LinearLayout         linear_back;
    private TextView             tv_title;
    private TextView             tv_submit;
    private EditText             et_carno;
    private KeyboardUtil         keyboardUtil;
    private PlateOutInfoFragment mPlateOutInfoFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_search_others, container, false);
        initView();
        initData();
        return mRootView;
    }

    private void initView() {
        linear_back = mRootView.findViewById(R.id.linear_back);
        tv_title = mRootView.findViewById(R.id.tv_title);
        et_carno = mRootView.findViewById(R.id.et_carno);
        tv_submit = mRootView.findViewById(R.id.tv_submit);
    }

    private void initData() {
        tv_title.setText("绑定车牌");
        linear_back.setOnClickListener(this);
        tv_submit.setOnClickListener(this);
        et_carno.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (keyboardUtil == null) {
                    keyboardUtil = new KeyboardUtil((Activity) getContext(), et_carno);
                    keyboardUtil.hideSoftInputMethod();
                    keyboardUtil.showKeyboard();
                } else {
                    keyboardUtil.showKeyboard();
                }
                return false;
            }
        });
        et_carno.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                //                Log.i("字符变换后", "afterTextChanged");
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //                Log.i("字符变换前", s + "-" + start + "-" + count + "-" + after);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //                Log.i("字符变换中", s + "-" + "-" + start + "-" + before + "-" + count);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.linear_back:
                getFragmentManager().popBackStackImmediate(null, 0);
                break;
            case R.id.tv_submit:
                String plateNo = String.valueOf(et_carno.getText()).trim();
                if ("".equals(plateNo) || "请输入车牌号码".equals(plateNo)) {
                    ToastUtils.showToast(getContext(), "请输入车牌号码");
                    return;
                }
                mPlateOutInfoFragment.setPlatenoAndSearch(plateNo);
                getFragmentManager().popBackStackImmediate(null, 0);
                break;
        }
    }

    private View.OnKeyListener backlistener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View view, int i, KeyEvent keyEvent) {
            if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                if (i == KeyEvent.KEYCODE_BACK) {  //表示按返回键 时的操作
                    if (null != keyboardUtil && keyboardUtil.isShow()) {
                        keyboardUtil.hideKeyboard();
                    } else {
                        getFragmentManager().popBackStackImmediate(null, 0);
                    }
                }
            }
            return false;
        }
    };

    public void setUpFragment(PlateOutInfoFragment plateOutInfoFragment) {
        mPlateOutInfoFragment = plateOutInfoFragment;
    }
}
