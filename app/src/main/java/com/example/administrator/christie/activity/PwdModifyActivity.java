package com.example.administrator.christie.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.christie.R;
import com.example.administrator.christie.modelInfo.RequestParamsFM;
import com.example.administrator.christie.modelInfo.UpDataInfo;
import com.example.administrator.christie.util.HttpOkhUtils;
import com.example.administrator.christie.util.MD5Util;
import com.example.administrator.christie.util.ToastUtils;
import com.example.administrator.christie.websiteUrl.NetConfig;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Request;

/**
 * 密码重置
 */
public class PwdModifyActivity extends BaseActivity implements View.OnClickListener {
    //    private EditText et_mobile, et_newpwd, et_repeatpwd, et_code;
    //    private CountdownButton btn_code;
    //    private Button          btn_next;
    //    private CustomProgress  dialog;
    //    private String          code;
    private Context      mContext;
    private LinearLayout linear_back;
    private TextView     mTv_title;
    private EditText     mEt_phone_num, mEt_password, mEt_again, mEdit_test_pass;
    private Button mBt_get_test, mBt_register;
    private String mPhone_num;//手机号
    private String mtest_pass;//验证码
    private String mPassword, mAgainPassword;//密码和重复密码
    private String markVerification = "-12345678";
    private Handler handler;
    private int count = 60;//验证码可重新点击发送时间间隔

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pwd_modify);
        mContext = PwdModifyActivity.this;
        setViews();
        setData();
        handler = new Handler();
        //        setListeners();
    }

    protected void setViews() {
        //        et_mobile = (EditText)findViewById(R.id.et_mobile);
        //        et_newpwd = (EditText)findViewById(R.id.et_newpwd);
        //        et_repeatpwd = (EditText)findViewById(R.id.et_repeatpwd);
        //        et_code = (EditText)findViewById(R.id.et_code);
        //        btn_code = (CountdownButton) findViewById(R.id.btn_code);
        //        btn_next = (Button)findViewById(R.id.btn_next);
        linear_back = (LinearLayout) findViewById(R.id.linear_back);
        mTv_title = (TextView) findViewById(R.id.tv_title);
        mEt_phone_num = (EditText) findViewById(R.id.et_phone_num);
        mEt_password = (EditText) findViewById(R.id.et_password);
        mEt_again = (EditText) findViewById(R.id.et_again);
        mEdit_test_pass = (EditText) findViewById(R.id.edit_test_pass);
        mBt_get_test = (Button) findViewById(R.id.bt_get_test);
        mBt_register = (Button) findViewById(R.id.bt_register);
    }

    private void setData() {
        linear_back.setOnClickListener(this);
        mTv_title.setText("忘记密码");
        mBt_get_test.setOnClickListener(this);
        mBt_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.linear_back:
                finish();
                break;
            case R.id.bt_get_test:
                //判断一下是否填写手机号
                mPhone_num = String.valueOf(mEt_phone_num.getText()).trim();
                if (mPhone_num.equals("") || mPhone_num.equals("请输入11位手机号")) {
                    ToastUtils.showToast(this, "请输入手机号码");
                } else {
                    // 账号不匹配手机号格式（11位数字且以1开头）
                    if (mPhone_num.length() != 11) {
                        ToastUtils.showToast(this, "手机号码格式不正确");
                    } else {
                        //发送验证码
                        sendMsgFromIntnet();
                    }
                }
                break;
            case R.id.bt_register:
                mPassword = String.valueOf(mEt_password.getText()).trim();
                mAgainPassword = String.valueOf(mEt_again.getText()).trim();
                //判断两次密码是否一样
                boolean b = compareTowPass(mPassword, mAgainPassword);
                if (!b) {
                    ToastUtils.showToast(this, "两次密码不一致，请重新输入");
                    return;
                }
                mPhone_num = String.valueOf(mEt_phone_num.getText()).trim();
                mtest_pass = String.valueOf(mEdit_test_pass.getText()).trim();
                if (mPhone_num.equals("") || mPhone_num.equals("请输入11位手机号")) {
                    ToastUtils.showToast(this, "请输入手机号码");
                    return;
                }
                if (mPhone_num.length() != 11) {
                    ToastUtils.showToast(this, "请输入正确的手机号码");
                    return;
                }
                if (mtest_pass.equals("") || mtest_pass.equals("请输入6位验证码")) {
                    ToastUtils.showToast(this, "请输入验证号码");
                    return;
                }
                boolean isRight = checkVerification();
                if (isRight) {
                    //注册
                    sendToRegister();
                } else {
                    ToastUtils.showToast(this, "验证码错误，请从新获取验证码");
                }
                break;
        }
    }

    private void sendToRegister() {
        String MD5 = MD5Util.MD5Encode(mPassword, "utf-8", false);
        String urlChangePass = NetConfig.MODIFYPSW;
        RequestParamsFM params = new RequestParamsFM();
        params.put("telephone", mPhone_num);
        params.put("new_psw", MD5);
        HttpOkhUtils.getInstance().doPost(urlChangePass, params, new HttpOkhUtils.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                ToastUtils.showToast(mContext, "网络错误");
            }

            @Override
            public void onSuccess(int code, String resbody) {
                if (code != 200) {
                    ToastUtils.showToast(mContext, "网络错误");
                    return;
                }
                ToastUtils.showToast(mContext, "修改成功，请登录");
                finish();
            }
        });
    }

    private void sendMsgFromIntnet() {
        String urlSendMsg = NetConfig.SENDMSG;
        RequestParamsFM requestParam = new RequestParamsFM();
        requestParam.put("mobile", mPhone_num);
        HttpOkhUtils.getInstance().doGetWithParams(urlSendMsg, requestParam, new HttpOkhUtils.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                ToastUtils.showToast(mContext, "网络错误");
            }

            @Override
            public void onSuccess(int code, String resbody) {
                if (code != 200) {
                    ToastUtils.showToast(mContext, "网络错误");
                    return;
                }
                Gson gson = new Gson();
                UpDataInfo sendMsgInfo = gson.fromJson(resbody, UpDataInfo.class);
                boolean valid = sendMsgInfo.isValid();
                if (valid) {
                    String validateCode = sendMsgInfo.getValidateCode();
                    markVerification = validateCode;
                    ToastUtils.showToast(mContext, "验证码发送成功");
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            handler.postDelayed(this, 1000);//递归执行，一秒执行一次
                            if (count > 0) {
                                count--;
                                mBt_get_test.setBackground(getResources().getDrawable(R.drawable.bg_round_gray));
                                mBt_get_test.setText(count + "秒后可重新发送");
                                mBt_get_test.setClickable(false);
                            } else {
                                count = 60;
                                mBt_get_test.setBackground(getResources().getDrawable(R.drawable.bg_round_blue_mormal));
                                mBt_get_test.setText("发送验证码");
                                mBt_get_test.setClickable(true);
                                handler.removeCallbacks(this);
                            }
                        }
                    }, 1000);    //第一次执行，一秒之后。第一次执行完就没关系了
                } else {
                    ToastUtils.showToast(mContext, "验证码发送失败，请重新请求");
                }
            }
        });
    }

    private boolean compareTowPass(String password, String againPassword) {
        return password.equals(againPassword) ? true : false;
    }

    private boolean checkVerification() {
        if (!mtest_pass.equals(markVerification)) {
            return false;
        } else {
            return true;
        }
    }

    //    protected void setListeners(){
    //        btn_code.setOnClickListener(new View.OnClickListener() {
    //            @Override
    //            public void onClick(View view) {
    //                if(et_mobile.getText().toString().equals("")||et_mobile.getText().toString().length()<11){
    //                    Toast.makeText(PwdModifyActivity.this,"请填写正确的手机号！",Toast.LENGTH_SHORT).show();
    //                }else{
    //                    btn_code.start();
    //                    //发送短信
    //                    code = SendMsgUtil.getRandomString();
    //                    Log.i("短信验证码:",code+"<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
    //                    new com.example.administrator.christie.util.Task(et_mobile.getText().toString(),code).execute();
    //                }
    //            }
    //        });
    //
    //        btn_next.setOnClickListener(new View.OnClickListener() {
    //            @Override
    //            public void onClick(View view) {
    //                if(et_mobile.getText().toString().equals("")||et_newpwd.getText().toString().equals("")
    //                        ||et_repeatpwd.getText().toString().equals("")||et_code.getText().toString().equals("")){
    //                    Toast.makeText(PwdModifyActivity.this,"请填写完整信息",Toast.LENGTH_SHORT).show();
    //                }else if(!et_code.getText().toString().equals(code)) {
    //                    Toast.makeText(PwdModifyActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
    //                }else {
    //                    btn_next.setEnabled(true);
    //                    new Task(et_mobile.getText().toString(),et_newpwd.getText().toString()).execute();
    //                    //将登录状态放入我的页面
    //
    //                }
    //            }
    //        });
    //
    //        et_newpwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
    //            @Override
    //            public void onFocusChange(View view, boolean b) {
    //                if(b){
    //
    //                }else{
    //                    if(!et_newpwd.getText().toString().equals("")&&!et_repeatpwd.isFocused()){
    //                        et_repeatpwd.setHintTextColor(Color.RED);
    //                        // 新建一个可以添加属性的文本对象
    //                        SpannableString ss = new SpannableString("请重复输入密码！");
    //                        // 新建一个属性对象,设置文字的大小
    //                        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(15,true);
    //                        // 附加属性到文本
    //                        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    //                        et_repeatpwd.setHint(new SpannableString(ss));
    //                    }
    //                }
    //            }
    //        });
    //
    //        et_repeatpwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
    //            @Override
    //            public void onFocusChange(View view, boolean b) {
    //                if(b){
    //                    if(et_newpwd.getText().toString().equals("")){
    ////                        Toast.makeText(PwdModifyActivity.this,"还未输入新密码！",Toast.LENGTH_LONG).show();
    //                        et_newpwd.setHintTextColor(Color.RED);
    //                        // 新建一个可以添加属性的文本对象
    //                        SpannableString ss = new SpannableString("请输入密码！");
    //                        // 新建一个属性对象,设置文字的大小
    //                        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(15,true);
    //                        // 附加属性到文本
    //                        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    //                        et_newpwd.setHint(new SpannableString(ss));
    //                    }
    //                }else{
    //                    if(!et_newpwd.getText().toString().equals(et_repeatpwd.getText().toString())&&!et_repeatpwd.getText().toString().equals("")){
    ////                        Toast.makeText(PwdModifyActivity.this,"两次输入的密码不一致，请重新输入！",Toast.LENGTH_LONG).show();
    //                        et_repeatpwd.setHintTextColor(Color.RED);
    //                        // 新建一个可以添加属性的文本对象
    //                        SpannableString ss = new SpannableString("两次密码不一致，请重新输入！");
    //                        // 新建一个属性对象,设置文字的大小
    //                        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(15,true);
    //                        // 附加属性到文本
    //                        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    //                        et_repeatpwd.setHint(new SpannableString(ss));
    //                        et_repeatpwd.setText("");
    //                    }else if(et_repeatpwd.getText().toString().equals("")&&!et_newpwd.getText().toString().equals("")){
    //                        et_repeatpwd.setHintTextColor(Color.RED);
    //                        // 新建一个可以添加属性的文本对象
    //                        SpannableString ss = new SpannableString("请重复输入密码！");
    //                        // 新建一个属性对象,设置文字的大小
    //                        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(15,true);
    //                        // 附加属性到文本
    //                        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    //                        et_repeatpwd.setHint(new SpannableString(ss));
    //                    }
    //                }
    //            }
    //        });
    //    }
    //
    //    class Task extends AsyncTask<Void, Integer, Integer> {
    //        String mobile;
    //        String password;
    //
    //        Task(String mobile, String password){
    //            this.mobile = mobile;
    //            this.password = password;
    //        }
    //        /**
    //         * 运行在UI线程中，在调用doInBackground()之前执行
    //         */
    //        @Override
    //        protected void onPreExecute() {
    //            dialog= CustomProgress.show(PwdModifyActivity.this,"提交中...", true, null);
    //        }
    //
    //        /**
    //         * 后台运行的方法，可以运行非UI线程，可以执行耗时的方法
    //         */
    //        @Override
    //        protected Integer doInBackground(Void... params) {
    //            try {
    //                String url = Consts.URL + "updatepassword";
    //                NameValuePair pair1 = new BasicNameValuePair("mobile", mobile);
    //                NameValuePair pair2 = new BasicNameValuePair("newpwd", password);
    //                Log.i("输入的用户名和密码", mobile + "<<<<<<<<<<" + password);
    //                //将准备好的键值对对象放置在一个List当中
    //                ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
    //                pairs.add(pair1);
    //                pairs.add(pair2);
    //                HttpResponse response = HttpUtils.PostUtil(url, pairs);
    //                if (response.getStatusLine().getStatusCode() == 200) {
    //                    //第五步：从相应对象当中取出数据，放到entity当中
    //                    HttpEntity entity = response.getEntity();
    //                    BufferedReader reader = new BufferedReader(
    //                            new InputStreamReader(entity.getContent()));
    //                    String result = reader.readLine();
    //                    Log.d("HTTP", "POST:" + result);
    //                    JSONObject json = new JSONObject(result);
    //                    String id = json.getString("id");
    //                    String fcheck = json.getString("fcheck");
    //                    String fname = json.getString("fname");
    //                    JSONArray array = json.getJSONArray("functionlist");
    //                    List<String> functionlist = new ArrayList<>();
    //                    for(int i=0;i<array.length();i++){
    //                        String function = (String) array.get(i);
    //                        functionlist.add(function);
    //                    }
    //                    JSONArray array1 = json.getJSONArray("applylist");
    //                    List<String> applylist = new ArrayList<>();
    //                    for (int j=0;j<array1.length();j++){
    //                        String apply = (String) array1.get(j);
    //                        applylist.add(apply);
    //                    }
    //                    TApplication.user.setId(id);
    //                    TApplication.user.setFmobile(mobile);
    //                    TApplication.user.setFcheck(fcheck);
    //                    TApplication.user.setFname(fname);
    //                    TApplication.user.setFunctionlist(functionlist);
    //                    TApplication.user.setApplylist(applylist);
    //                    System.out.println(TApplication.user);
    //                    return 1;
    //                }else {
    //                    return 3;
    //                }
    //            }catch (Exception e){
    //                e.printStackTrace();
    //                return 2;
    //            }
    //        }
    //
    //        /**
    //         * 运行在ui线程中，在doInBackground()执行完毕后执行
    //         */
    //        @Override
    //        protected void onPostExecute(Integer integer) {
    //            dialog.dismiss();
    //            if(integer==1) {
    //                Toast.makeText(PwdModifyActivity.this,"重置成功",Toast.LENGTH_SHORT).show();
    //                startActivity(new Intent(PwdModifyActivity.this,MainActivity.class));
    //                finish();
    //            }else{
    //                Toast.makeText(PwdModifyActivity.this,"操作失败，请重试",Toast.LENGTH_SHORT).show();
    //            }
    //        }
    //
    //        /**
    //         * 在publishProgress()被调用以后执行，publishProgress()用于更新进度
    //         */
    //        @Override
    //        protected void onProgressUpdate(Integer... values) {
    //
    //        }
    //    }
}
