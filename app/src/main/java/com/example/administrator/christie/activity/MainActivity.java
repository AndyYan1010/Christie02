package com.example.administrator.christie.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.administrator.christie.R;
import com.example.administrator.christie.TApplication;
import com.example.administrator.christie.fragment.FangkeFragment;
import com.example.administrator.christie.fragment.MeFragment;
import com.example.administrator.christie.fragment.MenjinFragment;
import com.example.administrator.christie.fragment.ParkFragment;

public class MainActivity extends FragmentActivity {
    MenjinFragment menjin;
    FangkeFragment fangke;
    ParkFragment   park;
    MeFragment     me;
    Button[] btns = new Button[4];
    //    Fragment[] fragments = null;
    private long exitTime = 0;
    /**
     * 当前显示的fragment
     */
    int currentIndex = 0;
    /**
     * 选中的button,显示下一个fragment
     */
    int selectedIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TApplication.listActivity.add(this);
        setViews();
        setListeners();
    }

    protected void setViews() {
        btns[0] = (Button) findViewById(R.id.btn_menjin);//一卡通
        btns[1] = (Button) findViewById(R.id.btn_fangke);//消息
        btns[2] = (Button) findViewById(R.id.btn_park);//菜单
        btns[3] = (Button) findViewById(R.id.btn_me);//个人中心
        btns[0].setSelected(true);

        //        menjin = new MenjinFragment();
        //        fangke = new FangkeFragment();
        //        park = new ParkFragment();
        //        me = new MeFragment();
        //        fragments = new Fragment[]{menjin, fangke, park, me};

        // 一开始，显示第一个fragment
        // 初始化默认显示的界面
        if (menjin == null) {
            menjin = new MenjinFragment();
            addFragment(menjin);
            showFragment(menjin);
        } else {
            showFragment(menjin);
        }
        //        FragmentManager fragmentManager = getSupportFragmentManager();
        //        FragmentTransaction transaction = fragmentManager.beginTransaction();
        //        transaction.add(R.id.fragment_container, menjin);
        //        transaction.show(menjin);
        //        transaction.commit();
    }

    protected void setListeners() {
        for (int i = 0; i < btns.length; i++) {
            btns[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //                    switch (view.getId()) {
                    //                        case R.id.btn_menjin:
                    //                            selectedIndex = 0;
                    //                            break;
                    //                        case R.id.btn_fangke:
                    //                            selectedIndex = 1;
                    //                            break;
                    //                        case R.id.btn_park:
                    //                            selectedIndex = 2;
                    //                            break;
                    //                        case R.id.btn_me:
                    //                            selectedIndex = 3;
                    //                            break;
                    //                    }
                    //                    // 判断单击是不是当前的
                    //                    if (selectedIndex != currentIndex) {
                    //                        // 不是当前的
                    //                        FragmentTransaction transaction = getSupportFragmentManager()
                    //                                .beginTransaction();
                    //                        // 当前hide
                    //                        transaction.hide(fragments[currentIndex]);
                    //                        // show你选中
                    //
                    //                        if (!fragments[selectedIndex].isAdded()) {
                    //                            // 以前没添加过
                    //                            transaction.add(R.id.fragment_container,
                    //                                    fragments[selectedIndex]);
                    //                        }
                    //                        // 事务
                    //                        transaction.show(fragments[selectedIndex]);
                    //                        transaction.commit();
                    //
                    //                        btns[currentIndex].setSelected(false);
                    //                        btns[selectedIndex].setSelected(true);
                    //                        currentIndex = selectedIndex;
                    //                    }
                    switch (view.getId()) {
                        case R.id.btn_menjin:
                            // 主界面
                            if (menjin == null) {
                                menjin = new MenjinFragment();
                                // 判断当前界面是否隐藏，如果隐藏就进行添加显示，false表示显示，true表示当前界面隐藏
                                addFragment(menjin);
                                showFragment(menjin);
                            } else {
                                if (menjin.isHidden()) {
                                    showFragment(menjin);
                                }
                            }
                            showBtImg(btns[0]);
                            break;
                        case R.id.btn_fangke:
                            if (fangke == null) {
                                fangke = new FangkeFragment();
                                // 判断当前界面是否隐藏，如果隐藏就进行添加显示，false表示显示，true表示当前界面隐藏
                                if (!fangke.isHidden()) {
                                    addFragment(fangke);
                                    showFragment(fangke);
                                }
                            } else {
                                if (fangke.isHidden()) {
                                    showFragment(fangke);
                                }
                            }
                            showBtImg(btns[1]);
                            break;
                        case R.id.btn_park:
                            if (park == null) {
                                park = new ParkFragment();
                                // 判断当前界面是否隐藏，如果隐藏就进行添加显示，false表示显示，true表示当前界面隐藏
                                if (!park.isHidden()) {
                                    addFragment(park);
                                    showFragment(park);
                                }
                            } else {
                                if (park.isHidden()) {
                                    showFragment(park);
                                }
                            }
                            showBtImg(btns[2]);
                            break;
                        case R.id.btn_me:
                            if (me == null) {
                                me = new MeFragment();
                                // 判断当前界面是否隐藏，如果隐藏就进行添加显示，false表示显示，true表示当前界面隐藏
                                if (!me.isHidden()) {
                                    addFragment(me);
                                    showFragment(me);
                                }
                            } else {
                                if (me.isHidden()) {
                                    showFragment(me);
                                }
                            }
                            showBtImg(btns[3]);
                            break;
                    }
                }
            });
        }
    }

    private void showBtImg(Button btn) {
        for (int i = 0; i < btns.length; i++) {
            btns[i].setSelected(false);
            if (btns[i] == btn) {
                btns[i].setSelected(true);
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
     * 删除Fragment
     **/
    public void removeFragment(Fragment fragment) {
        FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
        ft.remove(fragment);
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
        if (park != null) {
            ft.hide(park);
        }
        if (me != null) {
            ft.hide(me);
        }
        ft.show(fragment);
        ft.commitAllowingStateLoss();

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
}
