package com.example.administrator.christie.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.christie.R;
import com.example.administrator.christie.modelInfo.RequestParamsFM;
import com.example.administrator.christie.util.HttpOkhUtils;
import com.example.administrator.christie.util.RegexUtils;
import com.example.administrator.christie.util.ToastUtils;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Request;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    private EditText mEt_phone_num, mEt_password, mEt_again, mEdit_test_pass;
    private Button mBt_get_test, mBt_register;
    private ImageView mImg_back;
    private TextView  mTv_title;
    private String    mPhone_num;//手机号
    private String    mtest_pass;//验证码
    private String    mPassword, mAgainPassword;//密码和重复密码
    private String markVerification = "-12345678";
    private int    count            = 60;//验证码可重新点击发送时间间隔
    private Handler handler;
    private Context mContext;
    //    private EditText et_mobile_register,et_code_register,et_pwd,et_repeat;
    //    private CountdownButton btn_code_register;
    //    private Button btn_register;
    //    private String code;
    //    public static final int SHOW_RESPONSE = 0;
    //    private CustomProgress dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mContext = RegisterActivity.this;
        setViews();
        setData();
        handler = new Handler();
        //        setListeners();
    }

    protected void setViews() {
        //        et_mobile_register = (EditText)findViewById(R.id.et_mobile_register);
        //        et_code_register = (EditText)findViewById(R.id.et_code_register);
        //        btn_code_register = (CountdownButton)findViewById(R.id.btn_code_register);
        //        btn_register = (Button)findViewById(R.id.btn_register);
        //        et_pwd = (EditText)findViewById(R.id.et_pwd);
        //        et_repeat = (EditText)findViewById(R.id.et_repeat);
        mImg_back = (ImageView) findViewById(R.id.img_back);
        mTv_title = (TextView) findViewById(R.id.tv_title);
        mEt_phone_num = (EditText) findViewById(R.id.et_phone_num);
        mEt_password = (EditText) findViewById(R.id.et_password);
        mEt_again = (EditText) findViewById(R.id.et_again);
        mEdit_test_pass = (EditText) findViewById(R.id.edit_test_pass);
        mBt_get_test = (Button) findViewById(R.id.bt_get_test);
        mBt_register = (Button) findViewById(R.id.bt_register);
    }

    private void setData() {
        mImg_back.setOnClickListener(this);
        mTv_title.setText("注册用户");
        mBt_get_test.setOnClickListener(this);
        mBt_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.bt_get_test:
                //判断一下是否填写手机号
                mPhone_num = String.valueOf(mEt_phone_num.getText()).trim();
                if (mPhone_num.equals("") || mPhone_num.equals("请输入11位手机号")) {
                    ToastUtils.showToast(this, "请输入手机号码");
                } else {
                    // 账号不匹配手机号格式（11位数字且以1开头）
                    if (!RegexUtils.checkMobile(mPhone_num)) {
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
                if (!RegexUtils.checkMobile(mPhone_num)) {
                    ToastUtils.showToast(this, "请输入正确的手机号码");
                    return;
                }
                if (mtest_pass.equals("") || mtest_pass.equals("请输入6位验证码")) {
                    ToastUtils.showToast(this, "请输入验证号码");
                    return;
                }
                boolean isRight = checkVerification();
                if (isRight){
                    //注册
                    sendToRegister();
                }else {
                    ToastUtils.showToast(this, "验证码错误，请从新获取验证码");
                }
                break;
        }
    }

    private void sendToRegister() {
        String urlToRegist="";
        RequestParamsFM params = new RequestParamsFM();
        params.put("mobile",mPhone_num);
        params.put("password",mPassword);
        HttpOkhUtils.getInstance().doPost(urlToRegist, params, new HttpOkhUtils.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                ToastUtils.showToast(mContext, "网络错误");
            }

            @Override
            public void onSuccess(int code, String resbody) {
                if (code!=200){
                    ToastUtils.showToast(mContext,"网络错误");
                    return;
                }
                ToastUtils.showToast(mContext, "注册成功，请登录");
                finish();
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

    private void sendMsgFromIntnet() {
        String urlSendMsg = "";
        RequestParamsFM requestParam = new RequestParamsFM();
        requestParam.put("mobile", mPhone_num);
        HttpOkhUtils.getInstance().doGetWithParams(urlSendMsg, requestParam, new HttpOkhUtils.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                ToastUtils.showToast(mContext, "网络错误");
            }

            @Override
            public void onSuccess(int code, String resbody) {
                if (code!=200){
                    ToastUtils.showToast(mContext,"网络错误");
                    return;
                }
                Gson gson = new Gson();
//                ClubDetailInfo clubDetailInfo = gson.fromJson(response.toString(), ClubDetailInfo.class);
//                boolean valid = clubDetailInfo.getValid();
//                if (valid) {
//                    String validateCode = clubDetailInfo.getValidateCode();
//                    markVerification = validateCode;
//                    ToastUtils.showToast(mContext, "验证码发送成功");
//                    handler.postDelayed(new Runnable() {
//                        public void run() {
//                            handler.postDelayed(this, 1000);//递归执行，一秒执行一次
//                            if (count > 0) {
//                                count--;
//                                mBt_get_test.setText(count + "秒后可重新发送");
//                                mBt_get_test.setClickable(false);
//                            } else {
//                                mBt_get_test.setText("发送验证码");
//                                mBt_get_test.setClickable(true);
//                                handler.removeCallbacks(this);
//                            }
//                        }
//                    }, 1000);    //第一次执行，一秒之后。第一次执行完就没关系了
//                } else {
//                    ToastUtils.showToast(mContext, "验证码发送失败，请重新请求");
//                }
            }
        });
    }

    //    protected void setListeners(){
    //        btn_code_register.setOnClickListener(new View.OnClickListener() {
    //            @Override
    //            public void onClick(View view) {
    //                if(et_mobile_register.getText().toString().equals("")||et_mobile_register.getText().toString().length()<11){
    //                    Toast.makeText(RegisterActivity.this,"请输入正确的手机号！",Toast.LENGTH_SHORT).show();
    //                }else{
    //                    btn_code_register.start();
    //                    //发送短信
    //                    code = SendMsgUtil.getRandomString();
    //                    Log.i("短信验证码:",code+"<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
    //                    new Task(et_mobile_register.getText().toString(),code).execute();
    //                }
    //            }
    //        });
    //        btn_register.setOnClickListener(new View.OnClickListener() {
    //            @Override
    //            public void onClick(View view) {
    //                if(et_mobile_register.getText().toString().equals("")||et_code_register.getText().toString().equals("")){
    //                    Toast.makeText(RegisterActivity.this,"信息填写不完整！",Toast.LENGTH_SHORT).show();
    //                }else{
    //                    //判断验证码是否一致
    //                    if(et_code_register.getText().toString().equals(code)){
    //                        PostThread thread = new PostThread(et_mobile_register.getText().toString(), et_pwd.getText().toString(),UUID.randomUUID().toString());
    //                        thread.start();
    //                    }else{
    //                        Toast.makeText(RegisterActivity.this,"验证码不正确，请重新输入！",Toast.LENGTH_SHORT).show();
    //                    }
    //                }
    //            }
    //        });
    //
    //        et_pwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
    //            @Override
    //            public void onFocusChange(View view, boolean b) {
    //                if(b){
    //
    //                }else{
    //                    if(!et_pwd.getText().toString().equals("")&&!et_repeat.isFocused()){
    //                        et_repeat.setHintTextColor(Color.RED);
    //                        // 新建一个可以添加属性的文本对象
    //                        SpannableString ss = new SpannableString("请重复输入密码！");
    //                        // 新建一个属性对象,设置文字的大小
    //                        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(15,true);
    //                        // 附加属性到文本
    //                        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    //                        et_repeat.setHint(new SpannableString(ss));
    //                    }
    //                }
    //            }
    //        });
    //
    //        et_repeat.setOnFocusChangeListener(new View.OnFocusChangeListener() {
    //            @Override
    //            public void onFocusChange(View view, boolean b) {
    //                if(b){
    //                    if(et_pwd.getText().toString().equals("")){
    ////                        Toast.makeText(PwdModifyActivity.this,"还未输入新密码！",Toast.LENGTH_LONG).show();
    //                        et_pwd.setHintTextColor(Color.RED);
    //                        // 新建一个可以添加属性的文本对象
    //                        SpannableString ss = new SpannableString("请输入密码！");
    //                        // 新建一个属性对象,设置文字的大小
    //                        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(15,true);
    //                        // 附加属性到文本
    //                        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    //                        et_pwd.setHint(new SpannableString(ss));
    //                    }
    //                }else{
    //                    if(!et_pwd.getText().toString().equals(et_repeat.getText().toString())&&!et_repeat.getText().toString().equals("")){
    ////                        Toast.makeText(PwdModifyActivity.this,"两次输入的密码不一致，请重新输入！",Toast.LENGTH_LONG).show();
    //                        et_repeat.setHintTextColor(Color.RED);
    //                        // 新建一个可以添加属性的文本对象
    //                        SpannableString ss = new SpannableString("两次密码不一致，请重新输入！");
    //                        // 新建一个属性对象,设置文字的大小
    //                        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(15,true);
    //                        // 附加属性到文本
    //                        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    //                        et_repeat.setHint(new SpannableString(ss));
    //                        et_repeat.setText("");
    //                    }else if(et_repeat.getText().toString().equals("")&&!et_pwd.getText().toString().equals("")){
    //                        et_repeat.setHintTextColor(Color.RED);
    //                        // 新建一个可以添加属性的文本对象
    //                        SpannableString ss = new SpannableString("请重复输入密码！");
    //                        // 新建一个属性对象,设置文字的大小
    //                        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(15,true);
    //                        // 附加属性到文本
    //                        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    //                        et_repeat.setHint(new SpannableString(ss));
    //                    }
    //                }
    //            }
    //        });
    //    }
    //
    //    private Handler handler = new Handler() {
    //        @Override
    //        public void handleMessage(Message msg) {
    //            super.handleMessage(msg);
    //            switch (msg.what) {
    //                case SHOW_RESPONSE:
    //                    String response = (String) msg.obj;
    //                    String s[] = response.split(",");
    //                    if(s[0].equals("0")){
    //                        Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
    //                        //把登录状态放进我的页面
    //                        TApplication.user.setId(s[2]);
    //                        TApplication.user.setFmobile(s[1]);
    //                        TApplication.user.setFname("");
    //                        TApplication.user.setFunctionlist(new ArrayList<String>());
    //                        TApplication.user.setApplylist(new ArrayList<String>());
    //                        startActivity(new Intent(RegisterActivity.this,ValidateActivity.class));
    //                        finish();
    //                    }else{
    //                        Toast.makeText(RegisterActivity.this,"该手机号已被注册，请更换手机号或直接登录！",Toast.LENGTH_SHORT).show();
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
    //    //子线程：使用POST方法向服务器发送用户名、密码等数据
    //     class PostThread extends Thread {
    //         String mobile;
    //         String password;
    //         String id;
    //
    //        public PostThread(String mobile,String password,String id) {
    //            dialog = CustomProgress.show(RegisterActivity.this,"注册中...",true,null);
    //            this.mobile = mobile;
    //            this.password = password;
    //            this.id = id;
    //        }
    //
    //                 @Override
    //         public void run() {
    //             HttpClient httpClient = new DefaultHttpClient();
    //             String url = Consts.URL+"register";
    //             //第二步：生成使用POST方法的请求对象
    //             HttpPost httpPost = new HttpPost(url);
    //             //NameValuePair对象代表了一个需要发往服务器的键值对
    //             NameValuePair pair1 = new BasicNameValuePair("mobile", mobile);
    //             NameValuePair pair2 = new BasicNameValuePair("password", password);
    //             NameValuePair pair3 = new BasicNameValuePair("id", id);
    //             //将准备好的键值对对象放置在一个List当中
    //             ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
    //             pairs.add(pair1);
    //             pairs.add(pair2);
    //             pairs.add(pair3);
    //             try {
    //                 //创建代表请求体的对象（注意，是请求体）
    //                 HttpEntity requestEntity = new UrlEncodedFormEntity(pairs);
    //                 //将请求体放置在请求对象当中
    //                 httpPost.setEntity(requestEntity);
    //                 //执行请求对象
    //                 try {
    //                     //第三步：执行请求对象，获取服务器发还的相应对象
    //                     HttpResponse response = httpClient.execute(httpPost);
    //                     //第四步：检查相应的状态是否正常：检查状态码的值是200表示正常
    //                     if (response.getStatusLine().getStatusCode() == 200) {
    //                         //第五步：从相应对象当中取出数据，放到entity当中
    //                         HttpEntity entity = response.getEntity();
    //                         BufferedReader reader = new BufferedReader(
    //                                 new InputStreamReader(entity.getContent()));
    //                         String result = reader.readLine();
    //                         Log.d("HTTP", "POST:" + result);
    //                         //在子线程中将Message对象发出去
    //                         Message message = new Message();
    //                         message.what = SHOW_RESPONSE;
    //                         message.obj = result+","+mobile+","+id;
    //                         handler.sendMessage(message);
    //                     }
    //                 } catch (Exception e) {
    //                     e.printStackTrace();
    //                 }
    //             } catch (Exception e) {
    //                 e.printStackTrace();
    //             }
    //        }
    //     }

    //    /*//发送短信
    //    class Task extends AsyncTask<Void, Integer, Integer> {
    //        String mobile;
    //        String code;
    //
    //        Task(String mobile,String code){
    //            this.mobile = mobile;
    //            this.code = code;
    //        }
    //
    //        *//**
    //         * 运行在UI线程中，在调用doInBackground()之前执行
    //         *//*
    //        @Override
    //        protected void onPreExecute() {
    //
    //        }
    //
    //        *//**
    //         * 后台运行的方法，可以运行非UI线程，可以执行耗时的方法
    //         *//*
    //        @Override
    //        protected Integer doInBackground(Void... params) {
    //            try {
    //                String url = Consts.URL + "sendMsg?mobile=" + mobile + "&code=" + code;
    //                HttpResponse response = HttpUtils.GetUtil(url);
    //                if (response.getStatusLine().getStatusCode() == 200) {
    //                    //第五步：从相应对象当中取出数据，放到entity当中
    //                    HttpEntity entity = response.getEntity();
    //                    BufferedReader reader = new BufferedReader(
    //                            new InputStreamReader(entity.getContent()));
    //                    String result = reader.readLine();
    //                    Log.d("HTTP", "POST:" + result);
    //                    return Integer.parseInt(result);
    //                }else{
    //                 return 2;
    //                }
    //            }catch (Exception e){
    //                e.printStackTrace();
    //                return 1;
    //            }
    //        }
    //
    //        *//**
    //         * 运行在ui线程中，在doInBackground()执行完毕后执行
    //         *//*
    //        @Override
    //        protected void onPostExecute(Integer integer) {
    //
    //        }
    //
    //        *//**
    //         * 在publishProgress()被调用以后执行，publishProgress()用于更新进度
    //         *//*
    //        @Override
    //        protected void onProgressUpdate(Integer... values) {
    //
    //        }
    //    }*/
}
