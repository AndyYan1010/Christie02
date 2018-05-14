package com.example.administrator.christie.activity.menushowpackage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.christie.R;
import com.example.administrator.christie.activity.BaseActivity;
import com.example.administrator.christie.modelInfo.GoodsDetailInfo;
import com.example.administrator.christie.modelInfo.RequestParamsFM;
import com.example.administrator.christie.util.GlideLoaderUtil;
import com.example.administrator.christie.util.HttpOkhUtils;
import com.example.administrator.christie.util.ToastUtils;
import com.example.administrator.christie.websiteUrl.NetConfig;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Request;

/**
 * @创建者 AndyYan
 * @创建时间 2018/5/14 9:32
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class MenuDetailShowActivity extends BaseActivity implements View.OnClickListener {


    private ImageView mImg_back, mImg_nonet;
    private TextView  mTv_title;
    private ImageView mImg_food;
    private TextView  mTv_name, mTv_price, mTv_intro;
    private String mMenuId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_detail);
        Intent intent = getIntent();
        mMenuId = intent.getStringExtra("menuId");
        initView();
        initData();
    }

    private void initView() {
        mImg_back = (ImageView) findViewById(R.id.img_back);
        mImg_nonet = (ImageView) findViewById(R.id.img_nonet);
        mTv_title = (TextView) findViewById(R.id.tv_title);
        mImg_food = (ImageView) findViewById(R.id.img_food);
        mTv_name = (TextView) findViewById(R.id.tv_name);
        mTv_price = (TextView) findViewById(R.id.tv_price);
        mTv_intro = (TextView) findViewById(R.id.tv_intro);
    }

    private void initData() {
        if (null == mMenuId || "".equals(mMenuId)) {
            ToastUtils.showToast(MenuDetailShowActivity.this, "请传入菜品id");
            finish();
        }
        mImg_back.setOnClickListener(this);
        mTv_title.setText("菜品详细");
        //获取菜品详情
        getMenuDetail();
    }

    private void getMenuDetail() {
        String menuDetailUrl = NetConfig.GOODSDETAIL;
        RequestParamsFM params = new RequestParamsFM();
        params.put("goodsId", mMenuId);
        HttpOkhUtils.getInstance().doGetWithParams(menuDetailUrl, params, new HttpOkhUtils.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                ToastUtils.showToast(MenuDetailShowActivity.this, "网络错误");
            }

            @Override
            public void onSuccess(int code, String resbody) {
                if (code == 200) {
                    mImg_nonet.setVisibility(View.GONE);
                    Gson gson = new Gson();
                    GoodsDetailInfo goodsDetailInfo = gson.fromJson(resbody, GoodsDetailInfo.class);
                    String good_pic = goodsDetailInfo.getGood_pic();
                    String good_name = goodsDetailInfo.getGood_name();
                    double good_price = goodsDetailInfo.getGood_price();
                    String good_introduce = goodsDetailInfo.getGood_introduce();
                    GlideLoaderUtil.showImageView(MenuDetailShowActivity.this, good_pic, mImg_food);
                    mTv_name.setText(good_name);
                    mTv_price.setText("¥" + good_price);
                    mTv_intro.setText(good_introduce);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
        }
    }
}
