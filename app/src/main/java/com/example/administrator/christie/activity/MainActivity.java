package com.example.administrator.christie.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.administrator.christie.R;
import com.example.administrator.christie.TApplication;
import com.example.administrator.christie.fragment.FangkeFragment;
import com.example.administrator.christie.fragment.MeFragment;
import com.example.administrator.christie.fragment.MenjinFragment;
import com.example.administrator.christie.modelInfo.LoginInfo;
import com.example.administrator.christie.modelInfo.RequestParamsFM;
import com.example.administrator.christie.modelInfo.UserInfo;
import com.example.administrator.christie.util.HttpOkhUtils;
import com.example.administrator.christie.util.ProgressDialogUtils;
import com.example.administrator.christie.util.SPref;
import com.example.administrator.christie.util.ToastUtils;
import com.example.administrator.christie.websiteUrl.NetConfig;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Request;

public class MainActivity extends FragmentActivity implements View.OnClickListener {
    MenjinFragment menjin;
    FangkeFragment fangke;
    //    ParkFragment   park;//菜单
    MeFragment     me;
    //    Button[] btns = new Button[3];
    //    Fragment[] fragments = null;
    private long exitTime = 0;
    private LinearLayout linear0;
    private LinearLayout linear1;
    private LinearLayout linear2;
    // 界面底部的菜单按钮
    private ImageView[] bt_menu    = new ImageView[3];
    // 界面底部的菜单按钮id
    private int[]       bt_menu_id = {R.id.iv_menu_0, R.id.iv_menu_1, R.id.iv_menu_2};
    // 界面底部的选中菜单按钮资源
    private int[]       select_on  = {R.drawable.menjin_dark2, R.drawable.park_dark2, R.drawable.fangke_dark2};
    // 界面底部的未选中菜单按钮资源
    private int[]       select_off = {R.drawable.menjin_light2, R.drawable.park_light2, R.drawable.fangke_light2};
    //底部布局按钮的id
    private int[]       linear_id  = {R.id.linear0, R.id.linear1, R.id.linear2};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TApplication.flag = 0;
        super.onCreate(savedInstanceState);
        //        setSystemBarTransparent();
        setContentView(R.layout.activity_main);
        TApplication.listActivity.add(this);
        //        setListeners();
        setViews();
        setData();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    protected void setViews() {
        linear0 = (LinearLayout) findViewById(R.id.linear0);//主页
        linear1 = (LinearLayout) findViewById(R.id.linear1);//消息
        linear2 = (LinearLayout) findViewById(R.id.linear2);//个人中心
        //        btns[0] = (Button) findViewById(R.id.btn_menjin);//一卡通
        //        btns[1] = (Button) findViewById(R.id.btn_fangke);//消息
        //        btns[2] = (Button) findViewById(R.id.btn_park);//菜单
        //        btns[2] = (Button) findViewById(R.id.btn_me);//个人中心
        //        btns[0].setSelected(true);
        //        menjin = new MenjinFragment();
        //        fangke = new FangkeFragment();
        //        park = new ParkFragment();
        //        me = new MeFragment();
        //        fragments = new Fragment[]{menjin, fangke, park, me};

        // 一开始，显示第一个fragment
        // 初始化默认显示的界面
        //        if (menjin == null) {
        //            menjin = new MenjinFragment();
        //            addFragment(menjin);
        //            showFragment(menjin);
        //        } else {
        //            showFragment(menjin);
        //        }
        //        FragmentManager fragmentManager = getSupportFragmentManager();
        //        FragmentTransaction transaction = fragmentManager.beginTransaction();
        //        transaction.add(R.id.fragment_container, menjin);
        //        transaction.show(menjin);
        //        transaction.commit();
    }

    private void setData() {
        // 找到底部菜单的按钮并设置监听
        for (int i = 0; i < bt_menu.length; i++) {
            bt_menu[i] = (ImageView) findViewById(bt_menu_id[i]);
        }
        linear0.setOnClickListener(this);
        linear1.setOnClickListener(this);
        linear2.setOnClickListener(this);
        // 初始化默认显示的界面
        if (menjin == null) {
            menjin = new MenjinFragment();
            addFragment(menjin);
            showFragment(menjin);
        } else {
            showFragment(menjin);
        }
        // 设置默认首页为点击时的图片
        bt_menu[0].setImageResource(select_on[0]);
    }

    //    protected void setListeners() {
    //        for (int i = 0; i < btns.length; i++) {
    //            btns[i].setOnClickListener(new View.OnClickListener() {
    //                @Override
    //                public void onClick(View view) {
    //                    //                    switch (view.getId()) {
    //                    //                        case R.id.btn_menjin:
    //                    //                            selectedIndex = 0;
    //                    //                            break;
    //                    //                        case R.id.btn_fangke:
    //                    //                            selectedIndex = 1;
    //                    //                            break;
    //                    //                        case R.id.btn_park:
    //                    //                            selectedIndex = 2;
    //                    //                            break;
    //                    //                        case R.id.btn_me:
    //                    //                            selectedIndex = 3;
    //                    //                            break;
    //                    //                    }
    //                    //                    // 判断单击是不是当前的
    //                    //                    if (selectedIndex != currentIndex) {
    //                    //                        // 不是当前的
    //                    //                        FragmentTransaction transaction = getSupportFragmentManager()
    //                    //                                .beginTransaction();
    //                    //                        // 当前hide
    //                    //                        transaction.hide(fragments[currentIndex]);
    //                    //                        // show你选中
    //                    //
    //                    //                        if (!fragments[selectedIndex].isAdded()) {
    //                    //                            // 以前没添加过
    //                    //                            transaction.add(R.id.fragment_container,
    //                    //                                    fragments[selectedIndex]);
    //                    //                        }
    //                    //                        // 事务
    //                    //                        transaction.show(fragments[selectedIndex]);
    //                    //                        transaction.commit();
    //                    //
    //                    //                        btns[currentIndex].setSelected(false);
    //                    //                        btns[selectedIndex].setSelected(true);
    //                    //                        currentIndex = selectedIndex;
    //                    //                    }
    //                    switch (view.getId()) {
    //                        case R.id.btn_menjin:
    //                            // 主界面
    //                            if (menjin == null) {
    //                                menjin = new MenjinFragment();
    //                                // 判断当前界面是否隐藏，如果隐藏就进行添加显示，false表示显示，true表示当前界面隐藏
    //                                addFragment(menjin);
    //                                showFragment(menjin);
    //                            } else {
    //                                //                                if (menjin.isHidden()) {
    //                                showFragment(menjin);
    //                                //                                }
    //                            }
    //                            showBtImg(btns[0]);
    //                            break;
    //                        case R.id.btn_fangke:
    //                            if (fangke == null) {
    //                                fangke = new FangkeFragment();
    //                                // 判断当前界面是否隐藏，如果隐藏就进行添加显示，false表示显示，true表示当前界面隐藏
    //                                if (!fangke.isHidden()) {
    //                                    addFragment(fangke);
    //                                    showFragment(fangke);
    //                                }
    //                            } else {
    //                                //                                if (fangke.isHidden()) {
    //                                showFragment(fangke);
    //                                //                                }
    //                            }
    //                            showBtImg(btns[1]);
    //                            break;
    //                        //                        case R.id.btn_park:
    //                        //                            if (park == null) {
    //                        //                                park = new ParkFragment();
    //                        //                                // 判断当前界面是否隐藏，如果隐藏就进行添加显示，false表示显示，true表示当前界面隐藏
    //                        //                                if (!park.isHidden()) {
    //                        //                                    addFragment(park);
    //                        //                                    showFragment(park);
    //                        //                                }
    //                        //                            } else {
    //                        //                                if (park.isHidden()) {
    //                        //                                    showFragment(park);
    //                        //                                }
    //                        //                            }
    //                        //                            showBtImg(btns[2]);
    //                        //                            break;
    //                        case R.id.btn_me:
    //                            if (me == null) {
    //                                me = new MeFragment();
    //                                // 判断当前界面是否隐藏，如果隐藏就进行添加显示，false表示显示，true表示当前界面隐藏
    //                                if (!me.isHidden()) {
    //                                    addFragment(me);
    //                                    showFragment(me);
    //                                }
    //                            } else {
    //                                //                                if (me.isHidden()) {
    //                                showFragment(me);
    //                                //                                }
    //                            }
    //                            showBtImg(btns[2]);
    //                            break;
    //                    }
    //                }
    //            });
    //        }
    //    }

    //    private void showBtImg(Button btn) {
    //        for (int i = 0; i < btns.length; i++) {
    //            btns[i].setSelected(false);
    //            if (btns[i] == btn) {
    //                btns[i].setSelected(true);
    //            }
    //        }
    //    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.linear0:
                // 主界面
                if (menjin == null) {
                    menjin = new MenjinFragment();
                    // 判断当前界面是否隐藏，如果隐藏就进行添加显示，false表示显示，true表示当前界面隐藏
                    addFragment(menjin);
                    showFragment(menjin);
//                    ToastUtils.showToast(MainActivity.this, "为空，a添加");
                } else {
                    if (menjin.isHidden()) {
                        showFragment(menjin);
//                        ToastUtils.showToast(MainActivity.this, "不为空，a已隐藏");
                    } else {
//                        ToastUtils.showToast(MainActivity.this, "不为空, a未隐藏");
                    }
                }
                break;
            case R.id.linear1:
                // 消息界面
                if (fangke == null) {
                    fangke = new FangkeFragment();
                    // 判断当前界面是否隐藏，如果隐藏就进行添加显示，false表示显示，true表示当前界面隐藏
                    addFragment(fangke);
                    showFragment(fangke);
//                    ToastUtils.showToast(MainActivity.this, "为空，b添加");
                } else {

                    if (fangke.isHidden()) {
                        showFragment(fangke);
//                        ToastUtils.showToast(MainActivity.this, "不为空，b已隐藏");
                    } else {
//                        ToastUtils.showToast(MainActivity.this, "不为空, b未隐藏");
                    }
                }
                break;
            case R.id.linear2:
                // 个人中心界面
                if (me == null) {
                    me = new MeFragment();
                    // 判断当前界面是否隐藏，如果隐藏就进行添加显示，false表示显示，true表示当前界面隐藏
                    addFragment(me);
                    showFragment(me);
                } else {
                    if (me.isHidden()) {
                        showFragment(me);
                    }
                }
                break;
        }
        // 设置按钮的选中和未选中资源
        for (int i = 0; i < bt_menu.length; i++) {
            bt_menu[i].setImageResource(select_off[i]);
            if (view.getId() == linear_id[i]) {
                bt_menu[i].setImageResource(select_on[i]);
            }
        }
    }

    /**
     * 添加Fragment
     **/
    public void addFragment(Fragment fragment) {
        FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fragment_container, fragment);
        ft.commit();
    }

    /**
     * 显示Fragment
     **/
    public void showFragment(Fragment fragment) {
        FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
        //        // 设置Fragment的切换动画
        //        ft.setCustomAnimations(R.anim.cu_push_right_in, R.anim.cu_push_left_out);

        // 判断页面是否已经创建，如果已经创建，那么就隐藏掉
        if (menjin != null) {
            ft.hide(menjin);
        }
        if (fangke != null) {
            ft.hide(fangke);
        }
        if (me != null) {
            ft.hide(me);
        }
        ft.show(fragment);
        ft.commitAllowingStateLoss();
    }

    /**
     * 删除Fragment
     **/
    public void removeFragment(Fragment fragment) {
        FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
        ft.remove(fragment);
        ft.commit();
    }

    /**
     * 设置沉浸式状态栏
     */
    private void setSystemBarTransparent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // 5.0 LOLLIPOP解决方案
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 4.4 KITKAT解决方案
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次退出应用",
                    Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            TApplication.exit();
        }
    }

    private void refreshFstatus() {
        UserInfo userinfo = SPref.getObject(MainActivity.this, UserInfo.class, "userinfo");
        if (null != userinfo) {
            String phone = userinfo.getPhone();
            String psw = userinfo.getPsw();
            //隐形登录
            loginToSeverce(phone, psw);
        } else {
            ToastUtils.showToast(MainActivity.this, "请重新登录");
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.putExtra("autoNext", 0);
            startActivity(intent);
            finish();
        }
    }

    private void loginToSeverce(String phone, final String password) {
        ProgressDialogUtils.getInstance().show(MainActivity.this, "正在刷新请稍后");
        String url = NetConfig.LOGINURL;
        RequestParamsFM params = new RequestParamsFM();
        params.put("telephone", phone);
        params.put("password", password);
        HttpOkhUtils.getInstance().doPost(url, params, new HttpOkhUtils.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                ToastUtils.showToast(MainActivity.this, "网络异常,刷新失败");
            }

            @Override
            public void onSuccess(int code, String resbody) {
                ProgressDialogUtils.getInstance().dismiss();
                if (code != 200) {
                    ToastUtils.showToast(MainActivity.this, "刷新异常");
                    return;
                }
                Gson gson = new Gson();
                final LoginInfo mLoginInfo = gson.fromJson(resbody, LoginInfo.class);
                String result = mLoginInfo.getResult();
                if ("2".equals(result)) {
                    UserInfo userInfo = new UserInfo();
                    userInfo.setPhone(mLoginInfo.getTelephone());
                    userInfo.setPsw(password);
                    String fstatus = mLoginInfo.getFstatus();
                    if (null == fstatus || "".equals(fstatus) || "0".equals(fstatus)) {
                        userInfo.setFstatus(false);
                    } else {
                        userInfo.setFstatus(true);
                    }
                    SPref.setObject(MainActivity.this, UserInfo.class, "userinfo", userInfo);
                } else {
                    ToastUtils.showToast(MainActivity.this, "刷新失败");
                }
            }
        });
    }
}
