package com.example.administrator.christie.activity.menushowpackage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.administrator.christie.InformationMessege.ProjectMsg;
import com.example.administrator.christie.R;
import com.example.administrator.christie.activity.BaseActivity;
import com.example.administrator.christie.adapter.LvMenuIntrAdapter;
import com.example.administrator.christie.adapter.ProSpinnerAdapter;
import com.example.administrator.christie.modelInfo.GoodsListInfo;
import com.example.administrator.christie.modelInfo.ProjectByTelInfo;
import com.example.administrator.christie.modelInfo.RequestParamsFM;
import com.example.administrator.christie.modelInfo.UserInfo;
import com.example.administrator.christie.util.HttpOkhUtils;
import com.example.administrator.christie.util.SPref;
import com.example.administrator.christie.util.ToastUtils;
import com.example.administrator.christie.websiteUrl.NetConfig;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

/**
 * @创建者 AndyYan
 * @创建时间 2018/5/3 15:04
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class MenuIntroduceActivity extends BaseActivity implements View.OnClickListener {
    private Context                     mContext;
    private LinearLayout                linear_back;
    private ListView                    mLv_menu;
    private List<GoodsListInfo.ArrBean> mData;
    private Spinner                     mSpinner_id;
    private String menuId = "4d2881e8636152a2016361840fd50043";
    private LvMenuIntrAdapter mMenuAdapter;
    private TextView          mTv_title;
    private List<ProjectMsg>  mListProId;
    private ProSpinnerAdapter mProjAdapter;
    private String            userid;//用户id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_introduce);
        mContext = MenuIntroduceActivity.this;
        initView();
        initData();
    }

    private void initView() {
        linear_back = (LinearLayout) findViewById(R.id.linear_back);
        mTv_title = (TextView) findViewById(R.id.tv_title);
        mSpinner_id = (Spinner) findViewById(R.id.spinner_id);
        mLv_menu = (ListView) findViewById(R.id.lv_menu);
    }

    private void initData() {
        linear_back.setOnClickListener(this);
        UserInfo userinfo = SPref.getObject(MenuIntroduceActivity.this, UserInfo.class, "userinfo");
        userid = userinfo.getUserid();
        mTv_title.setText("菜单");
        if (null == mData) {
            mData = new ArrayList();
        } else {
            mData.clear();
        }
        mMenuAdapter = new LvMenuIntrAdapter(MenuIntroduceActivity.this, mData);
        mLv_menu.setAdapter(mMenuAdapter);
        if (null == mListProId) {
            mListProId = new ArrayList();
        } else {
            mListProId.clear();
        }
        ProjectMsg projectInfo = new ProjectMsg();
        projectInfo.setProject_name("请选择公司");
        mListProId.add(projectInfo);
        mProjAdapter = new ProSpinnerAdapter(mContext, mListProId);
        mSpinner_id.setAdapter(mProjAdapter);
        mSpinner_id.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ProjectMsg msg = mListProId.get(i);
                String project_name = msg.getProject_name();
                if (!project_name.equals("请选择公司")) {
                    String id = msg.getId();
                    //访问网络获取菜品
                    getMenuFromIntnet(id);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //访问网络获取菜品
        //        getMenuFromIntnet(menuId);
        //获取个人绑定项目id
        getProjectId();
        mLv_menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                GoodsListInfo.ArrBean arrBean = mData.get(i);
                String id = arrBean.getId();
                Intent intent = new Intent(MenuIntroduceActivity.this, MenuDetailShowActivity.class);
                intent.putExtra("menuId", id);
                startActivity(intent);
            }
        });
    }

    private void getProjectId() {
        String proId = NetConfig.PROJECTBYTEL;
        RequestParamsFM params = new RequestParamsFM();
        params.put("userid", userid);
        HttpOkhUtils.getInstance().doGetWithParams(proId, params, new HttpOkhUtils.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                ToastUtils.showToast(MenuIntroduceActivity.this, "网络错误");
            }

            @Override
            public void onSuccess(int code, String resbody) {
                if (code == 200) {
                    Gson gson = new Gson();
                    ProjectByTelInfo projectByTelInfo = gson.fromJson(resbody, ProjectByTelInfo.class);
                    List<ProjectByTelInfo.ProjectlistBean> projectlist = projectByTelInfo.getProjectlist();
                    for (ProjectByTelInfo.ProjectlistBean bean : projectlist) {
                        String project_id = bean.getId();
                        String project_name = bean.getProject_name();
                        ProjectMsg projectInfo = new ProjectMsg();
                        projectInfo.setProject_name(project_name);
                        projectInfo.setId(project_id);
                        mListProId.add(projectInfo);
                    }
                    mProjAdapter.notifyDataSetChanged();
                    if (projectlist.size() > 0) {
                        ProjectByTelInfo.ProjectlistBean bean = projectlist.get(0);
                        String project_id = bean.getId();
                        menuId = project_id;
                    }
                } else {
                    ToastUtils.showToast(MenuIntroduceActivity.this, "获取个人项目id失败");
                }
                //访问网络获取菜品
                getMenuFromIntnet(menuId);
            }
        });

    }

    private void getMenuFromIntnet(String projectID) {
        String goodsIdUrl = NetConfig.GOODSLIST;
        RequestParamsFM params = new RequestParamsFM();
        params.put("project_id", projectID);
        params.put("userid", userid);
        HttpOkhUtils.getInstance().doGetWithParams(goodsIdUrl, params, new HttpOkhUtils.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                ToastUtils.showToast(MenuIntroduceActivity.this, "网络错误");
            }

            @Override
            public void onSuccess(int code, String resbody) {
                if (code != 200) {
                    ToastUtils.showToast(mContext, "网络错误，错误码" + code);
                    return;
                }
                Gson gson = new Gson();
                GoodsListInfo goodsListInfo = gson.fromJson(resbody, GoodsListInfo.class);
                List<GoodsListInfo.ArrBean> arr = goodsListInfo.getArr();
                if (null != mData)
                    mData.clear();
                for (int i = 0; i < arr.size(); i++) {
                    mData.add(arr.get(i));
                }
                mMenuAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.linear_back:
                finish();
                break;
        }
    }
}
