package com.example.administrator.christie.activity.usercenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.christie.R;
import com.example.administrator.christie.activity.BaseActivity;
import com.example.administrator.christie.adapter.BindInfoAdapter;
import com.example.administrator.christie.modelInfo.PersonalDataInfo;
import com.example.administrator.christie.modelInfo.RequestParamsFM;
import com.example.administrator.christie.modelInfo.UserInfo;
import com.example.administrator.christie.util.HttpOkhUtils;
import com.example.administrator.christie.util.ProgressDialogUtil;
import com.example.administrator.christie.util.SPref;
import com.example.administrator.christie.util.ToastUtils;
import com.example.administrator.christie.websiteUrl.NetConfig;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

public class PersonalActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout ll_yhm, ll_sjh, ll_bd;
    private TextView tv_username, tv_mob, tv_address;
    public static final int SHOW_RESPONSE = 0;
    private Context                                        mContext;
    private List<PersonalDataInfo.ArrBean.ListProjectBean> mBangList;
    private ListView                                       mLv_bang;
    private BindInfoAdapter                                mInfoAdapter;
    private ImageView                                      mImg_back;
    private TextView                                       mTv_title;
    private SmartRefreshLayout                             mSmt_refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        mContext = PersonalActivity.this;
        setViews();
        setListeners();
    }

    protected void setViews() {
        mImg_back = (ImageView) findViewById(R.id.img_back);
        mTv_title = (TextView) findViewById(R.id.tv_title);
        mSmt_refresh = (SmartRefreshLayout) findViewById(R.id.smt_refresh);
        ll_yhm = (LinearLayout) findViewById(R.id.ll_yhm);
        ll_sjh = (LinearLayout) findViewById(R.id.ll_sjh);
        //        ll_xb = (LinearLayout) findViewById(R.id.ll_xb);
        //        tv_gender = (TextView) findViewById(R.id.tv_gender);
        ll_bd = (LinearLayout) findViewById(R.id.ll_bd);
        tv_username = (TextView) findViewById(R.id.tv_username);
        tv_mob = (TextView) findViewById(R.id.tv_mob);
        tv_address = (TextView) findViewById(R.id.tv_address);
        //        GetThread thread = new GetThread(TApplication.user.getId());
        //        thread.start();
        mLv_bang = (ListView) findViewById(R.id.lv_bang);
        mImg_back.setOnClickListener(this);
        mTv_title.setText(R.string.person);
        if (null == mBangList) {
            mBangList = new ArrayList();
        } else {
            mBangList.clear();
        }
        //访问网络获取个人资料
        getPersonalData();
        mInfoAdapter = new BindInfoAdapter(mContext, mBangList);
        mLv_bang.setAdapter(mInfoAdapter);
        mSmt_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                //访问网络获取个人资料
                getPersonalData();
            }
        });
    }

    private void getPersonalData() {
        ProgressDialogUtil.startShow(PersonalActivity.this, "正在加载，请稍后...");
        UserInfo userinfo = SPref.getObject(mContext, UserInfo.class, "userinfo");
        final String phone = userinfo.getPhone();
        String userid = userinfo.getUserid();
        String personalDetailUrl = NetConfig.PERSONALDATA;
        RequestParamsFM params = new RequestParamsFM();
        params.put("userid", userid);
        HttpOkhUtils.getInstance().doGetWithParams(personalDetailUrl, params, new HttpOkhUtils.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                mSmt_refresh.finishRefresh();
                ProgressDialogUtil.hideDialog();
                ToastUtils.showToast(mContext, "网络错误");
            }

            @Override
            public void onSuccess(int code, String resbody) {
                mSmt_refresh.finishRefresh();
                ProgressDialogUtil.hideDialog();
                if (code != 200) {
                    ToastUtils.showToast(mContext, "网络错误，错误码" + code);
                    return;
                }
                Gson gson = new Gson();
                PersonalDataInfo personalDataInfo = gson.fromJson(resbody, PersonalDataInfo.class);
                PersonalDataInfo.ArrBean arr = personalDataInfo.getArr();
                //                String telephone = arr.getTelephone();
                String faddress = arr.getFaddress();
                String user_name = arr.getUser_name();
                tv_username.setText(user_name);
                tv_mob.setText(phone);
                tv_address.setText(faddress);
                if (null == mBangList) {
                    mBangList = new ArrayList<>();
                } else {
                    mBangList.clear();
                }
                List<PersonalDataInfo.ArrBean.ListProjectBean> listProject = arr.getListProject();
                for (int i = 0; i < listProject.size(); i++) {
                    mBangList.add(listProject.get(i));
                }
                mInfoAdapter.notifyDataSetChanged();
            }
        });
    }

    protected void setListeners() {
        ll_yhm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //                final EditText et = new EditText(PersonalActivity.this);
                //                new AlertDialog.Builder(PersonalActivity.this).setTitle("用户名").setView(
                //                        et).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                //                    @Override
                //                    public void onClick(DialogInterface dialog, int which) {
                //                        //                        Modify thread = new Modify(TApplication.user.getId(), et.getText().toString(), "0");
                //                        //                        thread.start();
                //                        //提交修改的用户名
                //                    }
                //                }).setNegativeButton("取消", null).show();
            }
        });
        ll_sjh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //                startActivity(new Intent(PersonalActivity.this, ConfirmActivity.class));
            }
        });
        //        ll_xb.setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View view) {
        //                AlertDialog.Builder builder = new AlertDialog.Builder(PersonalActivity.this);
        //                builder.setTitle("修改性别");
        //                //    指定下拉列表的显示数据
        //                final String[] genders = {"男", "女"};
        //                //    设置一个下拉的列表选择项
        //                builder.setItems(genders, new DialogInterface.OnClickListener() {
        //                    @Override
        //                    public void onClick(DialogInterface dialog, int which) {
        //                        String gender = null;
        //                        switch (which) {
        //                            case 0:
        //                                gender = "0";
        //                                break;
        //                            case 1:
        //                                gender = "1";
        //                                break;
        //                        }
        //                        if (!gender.equals(null)) {
        //                            Modify thread = new Modify(TApplication.user.getId(), gender, "2");
        //                            thread.start();
        //                        }
        //                    }
        //                });
        //                builder.show();
        //            }
        //        });
        ll_bd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PersonalActivity.this, BindProjectActivity.class);
                startActivity(intent);
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

    //    //子线程：使用POST方法向服务器发送数据
    //    class GetThread extends Thread {
    //        String id;
    //
    //        public GetThread(String id) {
    //            dialog = CustomProgress.show(PersonalActivity.this, "加载中...", true, null);
    //            this.id = id;
    //        }
    //
    //        @Override
    //        public void run() {
    //            HttpClient httpClient = new DefaultHttpClient();
    //            String url = Consts.URL + "detail?id=" + id;
    //            Log.i("当前请求的URL", url + "  <<<<<<<<<<<<<<<<<<<<<<");
    //            //第二步：生成使用POST方法的请求对象
    //            HttpGet httpGet = new HttpGet(url);
    //            try {
    //                try {
    //                    //第三步：执行请求对象，获取服务器发还的相应对象
    //                    HttpResponse response = httpClient.execute(httpGet);
    //                    //第四步：检查相应的状态是否正常：检查状态码的值是200表示正常
    //                    if (response.getStatusLine().getStatusCode() == 200) {
    //                        //第五步：从相应对象当中取出数据，放到entity当中
    //                        HttpEntity entity = response.getEntity();
    //                        BufferedReader reader = new BufferedReader(
    //                                new InputStreamReader(entity.getContent()));
    //                        String result = reader.readLine();
    //                        Log.d("HTTP", "POST:" + result);
    //                        //在子线程中将Message对象发出去
    //                        Message message = new Message();
    //                        message.what = SHOW_RESPONSE;
    //                        message.obj = result;
    //                        handler.sendMessage(message);
    //                    }
    //                } catch (Exception e) {
    //                    e.printStackTrace();
    //                }
    //            } catch (Exception e) {
    //                e.printStackTrace();
    //            }
    //        }
    //    }
    //
    //
    //    private Handler handler = new Handler() {
    //        @Override
    //        public void handleMessage(Message msg) {
    //            super.handleMessage(msg);
    //            switch (msg.what) {
    //                case SHOW_RESPONSE:
    //                    String response = (String) msg.obj;
    //                    try {
    //                        JSONObject json = new JSONObject(response);
    //                        String username = json.getString("username");
    //                        String address = json.getString("address");
    //                        String gender = json.getString("gender");
    //                        if (username.equals("null")) {
    //                            tv_username.setText("未填写");
    //                        } else {
    //                            tv_username.setText(username);
    //                        }
    //                        tv_mob.setText(TApplication.user.getFmobile());
    //                        if (address.equals("null")) {
    //                            tv_address.setText("未填写");
    //                        } else {
    //                            tv_address.setText(address);
    //                        }
    //                        //                        tv_gender.setText(gender);
    //                    } catch (Exception e) {
    //                        e.printStackTrace();
    //                    }
    //                    dialog.dismiss();
    //                    break;
    //                default:
    //                    break;
    //            }
    //        }
    //    };
    //
    //
    //    //修改资料
    //    class Modify extends Thread {
    //        String id;
    //        String str;
    //        String code;
    //
    //        public Modify(String id, String str, String code) {
    //            dialog = CustomProgress.show(PersonalActivity.this, "提交中...", true, null);
    //            this.id = id;
    //            this.str = str;
    //            this.code = code;
    //        }
    //
    //        @Override
    //        public void run() {
    //            HttpClient httpClient = new DefaultHttpClient();
    //            String url = Consts.URL + "modifydetail?id=" + id + "&str=" + str + "&code=" + code;
    //            Log.i("当前请求的URL", url + "  <<<<<<<<<<<<<<<<<<<<<<");
    //            //第二步：生成使用POST方法的请求对象
    //            HttpGet httpGet = new HttpGet(url);
    //            try {
    //                try {
    //                    //第三步：执行请求对象，获取服务器发还的相应对象
    //                    HttpResponse response = httpClient.execute(httpGet);
    //                    //第四步：检查相应的状态是否正常：检查状态码的值是200表示正常
    //                    if (response.getStatusLine().getStatusCode() == 200) {
    //                        //第五步：从相应对象当中取出数据，放到entity当中
    //                        HttpEntity entity = response.getEntity();
    //                        BufferedReader reader = new BufferedReader(
    //                                new InputStreamReader(entity.getContent()));
    //                        String result = reader.readLine();
    //                        Log.d("HTTP", "POST:" + result);
    //                        //在子线程中将Message对象发出去
    //                        Message message = new Message();
    //                        message.what = SHOW_RESPONSE;
    //                        message.obj = result;
    //                        handler1.sendMessage(message);
    //                    }
    //                } catch (Exception e) {
    //                    e.printStackTrace();
    //                }
    //            } catch (Exception e) {
    //                e.printStackTrace();
    //            }
    //        }
    //    }
    //
    //
    //    private Handler handler1 = new Handler() {
    //        @Override
    //        public void handleMessage(Message msg) {
    //            super.handleMessage(msg);
    //            switch (msg.what) {
    //                case SHOW_RESPONSE:
    //                    String response = (String) msg.obj;
    //                    if (!response.equals("")) {
    //                        Toast.makeText(PersonalActivity.this, "用户名修改成功", Toast.LENGTH_SHORT).show();
    //                        tv_username.setText(response);
    //                    } else if (response.equals("3")) {
    //                        Toast.makeText(PersonalActivity.this, "手机号修改成功", Toast.LENGTH_SHORT).show();
    //                    } else if (response.equals("0")) {
    //                        Toast.makeText(PersonalActivity.this, "性别修改成功", Toast.LENGTH_SHORT).show();
    //                        //                        tv_gender.setText("男");
    //                    } else if (response.equals("1")) {
    //                        Toast.makeText(PersonalActivity.this, "性别修改成功", Toast.LENGTH_SHORT).show();
    //                        //                        tv_gender.setText("女");
    //                    } else if (response.equals("4")) {
    //                        Toast.makeText(PersonalActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
    //                    }
    //                    dialog.dismiss();
    //                    break;
    //                default:
    //                    break;
    //            }
    //        }
    //    };
}
