package com.example.administrator.christie.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.administrator.christie.R;
import com.example.administrator.christie.TApplication;
import com.example.administrator.christie.modelInfo.LoginInfo;
import com.example.administrator.christie.modelInfo.RequestParamsFM;
import com.example.administrator.christie.modelInfo.UserInfo;
import com.example.administrator.christie.util.HttpOkhUtils;
import com.example.administrator.christie.util.ProgressDialogUtils;
import com.example.administrator.christie.util.RegexUtils;
import com.example.administrator.christie.util.SPref;
import com.example.administrator.christie.util.ToastUtils;
import com.example.administrator.christie.websiteUrl.NetConfig;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Request;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private EditText mEt_acct_num;
    private EditText mEt_password;
    private TextView mTv_register;
    private TextView mTv_forgot;
    private Button   mBt_login;
    private String   mPhone;
    private String   mPassword;
    //    protected static final String TAG = "LoginActivity";
    //    private EditText et_user, et_pwd;//用户名,密码
    //    private Button          btn_log;//登录按钮
    //    private LinearLayout    mLoginLinearLayout; // 登录内容的容器
    //    private LinearLayout    mUserIdLinearLayout; // 将下拉弹出窗口在此容器下方显示
    //    private Animation       mTranslate; // 位移动画
    //    private ImageView       mMoreUser; // 下拉图标
    //    private ImageView       mLoginMoreUserView; // 弹出下拉弹出窗的按钮
    //    private String          mIdString;
    //    private String          mPwdString;
    //    private ArrayList<User> mUsers; // 用户列表
    //    private ListView        mUserIdListView; // 下拉弹出窗显示的ListView对象
    //    private MyAapter        mAdapter; // ListView的监听器
    //    private PopupWindow     mPop; // 下拉弹出窗
    //    private CustomProgress  dialog;
    //    private TextView        tv_pwd_change, tv_register;
    //    public static final int  SHOW_RESPONSE = 0;
    //    public static final int  ERROR         = 1;
    //    private             long exitTime      = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TApplication.flag = 0;
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        int autoNext = intent.getIntExtra("autoNext",1);
        //创建界面前先判断手机之前是否登录过，登录过跳过登录阶段，直接显示主页面
        startMainAct(autoNext);
        setContentView(R.layout.activity_login);
        setViews();
        setData();
        //        mLoginLinearLayout.startAnimation(mTranslate); // Y轴水平移动
        //
        //		/* 获取已经保存好的用户密码 */
        //        mUsers = UserUtil.getUserList(LoginActivity.this);
        //
        //        if (mUsers.size() > 0) {
        //            /* 将列表中的第一个user显示在编辑框 */
        //            et_user.setText(mUsers.get(0).getId());
        //            et_pwd.setText(mUsers.get(0).getPwd());
        //        }
        //
        //        LinearLayout parent = (LinearLayout) getLayoutInflater().inflate(
        //                R.layout.userifo_listview, null);
        //        mUserIdListView = (ListView) parent.findViewById(android.R.id.list);
        //        parent.removeView(mUserIdListView); // 必须脱离父子关系,不然会报错
        //        mUserIdListView.setOnItemClickListener(this); // 设置点击事
        //        mAdapter = new MyAapter(mUsers);
        //        mUserIdListView.setAdapter(mAdapter);
    }

    private void startMainAct(int autoNext) {
        UserInfo userinfo = SPref.getObject(LoginActivity.this, UserInfo.class, "userinfo");
        if (null != userinfo) {
            String phone = userinfo.getPhone();
            if (null != phone && !"".equals(phone) && 1 == autoNext) {
                //跳转mainactivity
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    private void setViews() {
        mEt_acct_num = (EditText) findViewById(R.id.et_account_num);
        mEt_password = (EditText) findViewById(R.id.et_password);
        mTv_register = (TextView) findViewById(R.id.tv_register);
        mTv_forgot = (TextView) findViewById(R.id.tv_forgot);
        mBt_login = (Button) findViewById(R.id.bt_login);
    }

    private void setData() {
        mTv_register.setOnClickListener(this);
        mTv_forgot.setOnClickListener(this);
        mBt_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_register:
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_forgot:
                Intent intent2 = new Intent(this, PwdModifyActivity.class);
                startActivity(intent2);
                break;
            case R.id.bt_login:
                mPhone = String.valueOf(mEt_acct_num.getText()).trim();
                mPassword = String.valueOf(mEt_password.getText()).trim();
                if (!RegexUtils.checkMobile(mPhone)) {
                    ToastUtils.showToast(LoginActivity.this, "请输入正确的手机号码");
                    return;
                }
                if ("".equals(mPassword) || "请输入密码".equals(mPassword)) {
                    ToastUtils.showToast(LoginActivity.this, "请输入密码");
                    return;
                }
                //登录
                loginToSeverce(mPhone, mPassword);
                break;
        }
    }

    private void loginToSeverce(String phone, String password) {
        ProgressDialogUtils.getInstance().show(LoginActivity.this, "正在登录请稍后");
        String url = NetConfig.LOGINURL;
        RequestParamsFM params = new RequestParamsFM();
        params.put("telephone", phone);
        params.put("password", password);
        HttpOkhUtils.getInstance().doPost(url, params, new HttpOkhUtils.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                ToastUtils.showToast(LoginActivity.this, "网络异常");
            }

            @Override
            public void onSuccess(int code, String resbody) {
                ProgressDialogUtils.getInstance().dismiss();
                if (code != 200) {
                    ToastUtils.showToast(LoginActivity.this, "登录异常");
                    return;
                }
                Gson gson = new Gson();
                final LoginInfo mLoginInfo = gson.fromJson(resbody, LoginInfo.class);
                String result = mLoginInfo.getResult();
                if ("2".equals(result)) {
                    ToastUtils.showToast(LoginActivity.this, mLoginInfo.getMessage());
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    UserInfo userInfo = new UserInfo();
                    userInfo.setPhone(mLoginInfo.getTelephone());
                    userInfo.setPsw(mPassword);
                    String fstatus = mLoginInfo.getFstatus();
                    if (null == fstatus || "".equals(fstatus) || "0".equals(fstatus)) {
                        userInfo.setFstatus(false);
                    } else {
                        userInfo.setFstatus(true);
                    }
                    SPref.setObject(LoginActivity.this, UserInfo.class, "userinfo", userInfo);
                    finish();
                }
            }
        });
    }

    //
    //    public void setViews() {
    //        et_user = (EditText) findViewById(R.id.login_edtId);
    //        et_pwd = (EditText) findViewById(R.id.login_edtPwd);
    //        btn_log = (Button) findViewById(R.id.login_btnLogin);
    //        mMoreUser = (ImageView) findViewById(R.id.login_more_user);
    //        mLoginMoreUserView = (ImageView) findViewById(R.id.login_more_user);
    //        mLoginLinearLayout = (LinearLayout) findViewById(R.id.login_linearLayout);
    //        mUserIdLinearLayout = (LinearLayout) findViewById(R.id.userId_LinearLayout);
    //        mTranslate = AnimationUtils.loadAnimation(this, R.anim.my_translate); // 初始化动画对象
    //        tv_pwd_change = (TextView) findViewById(R.id.tv_pwd_change);
    //        tv_register = (TextView) findViewById(R.id.tv_register);
    //    }
    //
    //
    //    public void initPop() {
    //        int width = mUserIdLinearLayout.getWidth() - 4;
    //        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
    //        mPop = new PopupWindow(mUserIdListView, width, height, true);
    //        mPop.setOnDismissListener(this);// 设置弹出窗口消失时监听器
    //
    //        // 注意要加这句代码，点击弹出窗口其它区域才会让窗口消失
    //        mPop.setBackgroundDrawable(new ColorDrawable(0xffffffff));
    //
    //    }
    //
    //    public void setListeners() {
    //        mMoreUser.setOnClickListener(new View.OnClickListener() {
    //            @Override
    //            public void onClick(View view) {
    //                if (mPop == null) {
    //                    initPop();
    //                }
    //                if (!mPop.isShowing() && mUsers.size() > 0) {
    //                    // Log.i(TAG, "切换为角向上图标");
    //                    mMoreUser.setImageResource(R.drawable.login_more_down); // 切换图标
    //                    mPop.showAsDropDown(mUserIdLinearLayout, 2, 1); // 显示弹出窗口
    //                }
    //            }
    //        });
    //
    //        btn_log.setOnClickListener(new View.OnClickListener() {
    //            @Override
    //            public void onClick(View view) {
    //                // 启动登录
    //                Log.i(TAG, mIdString + "  " + mPwdString);
    //                if (mIdString == null || mIdString.equals("")) { // 账号为空时
    //                    Toast.makeText(LoginActivity.this, "请输入账号", Toast.LENGTH_SHORT)
    //                            .show();
    //                } else if (mPwdString == null || mPwdString.equals("")) {// 密码为空时
    //                    Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT)
    //                            .show();
    //                } else {// 账号和密码都不为空时
    //                    if (NetWorkUtils.isNetworkConnected(LoginActivity.this)) {
    //                        PostThread thread = new PostThread(mIdString, mPwdString);
    //                        thread.start();
    //                    } else {
    //                        Toast.makeText(LoginActivity.this, "网络不可用", Toast.LENGTH_SHORT).show();
    //                    }
    //                    boolean mIsSave = true;
    //                    try {
    //                        Log.i(TAG, "保存用户列表");
    //                        for (User user : mUsers) { // 判断本地文档是否有此ID用户
    //                            if (user.getId().equals(mIdString)) {
    //                                mIsSave = false;
    //                                break;
    //                            }
    //                        }
    //                        if (mIsSave) { // 将新用户加入users
    //                            User user = new User(mIdString, mPwdString);
    //                            mUsers.add(user);
    //                        }
    //
    //                    } catch (Exception e) {
    //                        e.printStackTrace();
    //                    }
    //
    //                }
    //            }
    //        });
    //
    //        et_user.addTextChangedListener(new TextWatcher() {
    //
    //            public void onTextChanged(CharSequence s, int start, int before,
    //                                      int count) {
    //                mIdString = s.toString();
    //            }
    //
    //            public void beforeTextChanged(CharSequence s, int start, int count,
    //                                          int after) {
    //            }
    //
    //            public void afterTextChanged(Editable s) {
    //            }
    //        });
    //        et_pwd.addTextChangedListener(new TextWatcher() {
    //
    //            public void onTextChanged(CharSequence s, int start, int before,
    //                                      int count) {
    //                mPwdString = s.toString();
    //            }
    //
    //            public void beforeTextChanged(CharSequence s, int start, int count,
    //                                          int after) {
    //            }
    //
    //            public void afterTextChanged(Editable s) {
    //            }
    //        });
    //
    //        tv_pwd_change.setOnClickListener(new View.OnClickListener() {
    //            @Override
    //            public void onClick(View view) {
    //                startActivity(new Intent(LoginActivity.this, PwdModifyActivity.class));
    //            }
    //        });
    //
    //        tv_register.setOnClickListener(new View.OnClickListener() {
    //            @Override
    //            public void onClick(View view) {
    //                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    //            }
    //        });
    //    }
    //
    //    @Override
    //    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
    //        et_user.setText(mUsers.get(i).getId());
    //        et_pwd.setText(mUsers.get(i).getPwd());
    //        mPop.dismiss();
    //    }
    //
    //    /* PopupWindow对象dismiss时的事件 */
    //    @Override
    //    public void onDismiss() {
    //        // Log.i(TAG, "切换为角向下图标");
    //        mMoreUser.setImageResource(R.drawable.login_more_up);
    //    }
    //
    //    /* ListView的适配器 */
    //    class MyAapter extends ArrayAdapter<User> {
    //
    //        public MyAapter(ArrayList<User> users) {
    //            super(LoginActivity.this, 0, users);
    //        }
    //
    //        public View getView(final int position, View convertView,
    //                            ViewGroup parent) {
    //            if (convertView == null) {
    //                convertView = getLayoutInflater().inflate(
    //                        R.layout.listview_item, null);
    //            }
    //
    //            TextView userIdText = (TextView) convertView
    //                    .findViewById(R.id.listview_userid);
    //            userIdText.setText(getItem(position).getId());
    //
    //            ImageView deleteUser = (ImageView) convertView
    //                    .findViewById(R.id.login_delete_user);
    //            deleteUser.setOnClickListener(new View.OnClickListener() {
    //                // 点击删除deleteUser时,在mUsers中删除选中的元素
    //                @Override
    //                public void onClick(View v) {
    //
    //                    if (getItem(position).getId().equals(mIdString)) {
    //                        // 如果要删除的用户Id和Id编辑框当前值相等，则清空
    //                        mIdString = "";
    //                        mPwdString = "";
    //                        et_user.setText(mIdString);
    //                        et_pwd.setText(mPwdString);
    //                    }
    //                    mUsers.remove(getItem(position));
    //                    mAdapter.notifyDataSetChanged(); // 更新ListView
    //                }
    //            });
    //            return convertView;
    //        }
    //
    //    }
    //
    //
    //    /* 退出此Activity时保存users */
    //    @Override
    //    public void onPause() {
    //        super.onPause();
    //        try {
    //            UserUtil.saveUserList(LoginActivity.this, mUsers);
    //        } catch (Exception e) {
    //            e.printStackTrace();
    //        }
    //    }
    //
    //    @Override
    //    public void onBackPressed() {
    //        super.onBackPressed();
    //        finish();
    //    }
    //
    //    private Handler handler = new Handler() {
    //        @Override
    //        public void handleMessage(Message msg) {
    //            super.handleMessage(msg);
    //            switch (msg.what) {
    //                case SHOW_RESPONSE:
    //                    try {
    //                        String response = (String) msg.obj;
    //                        System.out.println(response);
    //                        String s[] = response.split("/");
    //                        if (s[0].equals("null")) {
    //                            Toast.makeText(LoginActivity.this, "用户不存在", Toast.LENGTH_SHORT).show();
    //                        } else {
    //                            System.out.println(s[0]);
    //                            JSONObject json = new JSONObject(s[0]);
    //                            String id = json.getString("id");
    //                            if (!id.equals("1")) {
    //                                String fcheck = json.getString("fcheck");
    //                                String fname = json.getString("fname");
    //                                JSONArray array = json.getJSONArray("functionlist");
    //                                List<String> functionlist = new ArrayList<>();
    //                                for (int i = 0; i < array.length(); i++) {
    //                                    String function = (String) array.get(i);
    //                                    functionlist.add(function);
    //                                }
    //                                JSONArray array1 = json.getJSONArray("applylist");
    //                                List<String> applylist = new ArrayList<>();
    //                                for (int j = 0; j < array1.length(); j++) {
    //                                    String apply = (String) array1.get(j);
    //                                    applylist.add(apply);
    //                                }
    //                                TApplication.user.setId(id);
    //                                TApplication.user.setFmobile(s[1]);
    //                                TApplication.user.setFcheck(fcheck);
    //                                TApplication.user.setFname(fname);
    //                                TApplication.user.setFunctionlist(functionlist);
    //                                TApplication.user.setApplylist(applylist);
    //                                System.out.println(TApplication.user);
    //                                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
    //                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
    //                                finish();
    //                            } else {
    //                                Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
    //                            }
    //                        }
    //                    } catch (Exception e) {
    //                        e.printStackTrace();
    //                    }
    //                    dialog.dismiss();
    //                    break;
    //                case ERROR:
    //                    dialog.dismiss();
    //                    Toast.makeText(LoginActivity.this, "服务器故障，请稍后再试", Toast.LENGTH_SHORT).show();
    //                    break;
    //                default:
    //                    break;
    //            }
    //        }
    //    };
    //
    //
    //    //子线程：使用POST方法向服务器发送用户名、密码等数据
    //    class PostThread extends Thread {
    //        String mobile;
    //        String password;
    //
    //        public PostThread(String mobile, String password) {
    //            dialog = CustomProgress.show(LoginActivity.this, "登录中...", true, null);
    //            this.mobile = mobile;
    //            this.password = password;
    //        }
    //
    //        @Override
    //        public void run() {
    //            String url = Consts.URL + "login";
    //            //NameValuePair对象代表了一个需要发往服务器的键值对
    //            NameValuePair pair1 = new BasicNameValuePair("mobile", mobile);
    //            NameValuePair pair2 = new BasicNameValuePair("password", password);
    //            Log.i("输入的用户名和密码", mobile + "<<<<<<<<<<" + password);
    //            //将准备好的键值对对象放置在一个List当中
    //            ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
    //            pairs.add(pair1);
    //            pairs.add(pair2);
    //            try {
    //                HttpResponse response = HttpUtils.PostUtil(url, pairs);
    //                Log.i("获取的服务器响应", response.getStatusLine().getStatusCode() + "");
    //                //第四步：检查相应的状态是否正常：检查状态码的值是200表示正常
    //                if (response.getStatusLine().getStatusCode() == 200) {
    //                    //第五步：从相应对象当中取出数据，放到entity当中
    //                    HttpEntity entity = response.getEntity();
    //                    BufferedReader reader = new BufferedReader(
    //                            new InputStreamReader(entity.getContent()));
    //                    String result = reader.readLine();
    //                    Log.d("HTTP", "POST:" + result);
    //                    //在子线程中将Message对象发出去
    //                    Message message = new Message();
    //                    message.what = SHOW_RESPONSE;
    //                    message.obj = result + "/" + mobile;
    //                    handler.sendMessage(message);
    //                }
    //            } catch (Exception e) {
    //                e.printStackTrace();
    //                Message message = new Message();
    //                message.what = ERROR;
    //                handler.sendMessage(message);
    //            }
    //        }
    //    }
}
