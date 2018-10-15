package com.example.administrator.christie.activity.usercenter;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.christie.R;
import com.example.administrator.christie.activity.BaseActivity;
import com.example.administrator.christie.modelInfo.RequestParamsFM;
import com.example.administrator.christie.modelInfo.UserInfo;
import com.example.administrator.christie.util.HttpOkhUtils;
import com.example.administrator.christie.util.KeyboardUtil;
import com.example.administrator.christie.util.SPref;
import com.example.administrator.christie.util.ToastUtils;
import com.example.administrator.christie.websiteUrl.NetConfig;

import java.io.IOException;

import okhttp3.Request;

/**
 * @创建者 AndyYan
 * @创建时间 2018/10/12 15:35
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class AddCarNoActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout linear_back;
    private TextView     tv_title;
    private EditText     et_carno;
    private TextView     tv_submit;
    private KeyboardUtil keyboardUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_carno);
        initView();
        initData();
    }

    private void initView() {
        linear_back = (LinearLayout) findViewById(R.id.linear_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        et_carno = (EditText) findViewById(R.id.et_carno);
        tv_submit = (TextView) findViewById(R.id.tv_submit);
    }

    private void initData() {
        tv_title.setText("绑定车牌");
        linear_back.setOnClickListener(this);
        tv_submit.setOnClickListener(this);
        et_carno.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (keyboardUtil == null) {
                    keyboardUtil = new KeyboardUtil(AddCarNoActivity.this, et_carno);
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (null != keyboardUtil && keyboardUtil.isShow()) {
                keyboardUtil.hideKeyboard();
            } else {
                finish();
            }
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.linear_back:
                finish();
                break;
            case R.id.tv_submit:
                //添加新的车牌
                addNewCarNo();
                break;
        }
    }

    private void addNewCarNo() {
        String carno = String.valueOf(et_carno.getText());
        if ("".equals(carno) || "请输入车牌号码".equals(carno)) {
            ToastUtils.showToast(this, "请输入车牌号码");
            return;
        }
        addPersonalCar(carno);
    }

    private void addPersonalCar(String carno) {
        UserInfo userinfo = SPref.getObject(this, UserInfo.class, "userinfo");
        String userid = userinfo.getUserid();
        String addCarUrl = NetConfig.ADDPLATE;
        RequestParamsFM params = new RequestParamsFM();
        params.put("user_id", userid);
        params.put("plateno", carno);
        params.setUseJsonStreamer(true);
        HttpOkhUtils.getInstance().doPost(addCarUrl, params, new HttpOkhUtils.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                ToastUtils.showToast(AddCarNoActivity.this, "网络错误");
            }

            @Override
            public void onSuccess(int code, String resbody) {
                if (code != 200) {
                    ToastUtils.showToast(AddCarNoActivity.this, "网络请求失败");
                } else {
                    ToastUtils.showToast(AddCarNoActivity.this, "添加成功");
                    finish();
                }
            }
        });
    }
}
