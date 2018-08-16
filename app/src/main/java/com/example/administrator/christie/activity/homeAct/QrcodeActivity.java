package com.example.administrator.christie.activity.homeAct;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.christie.InformationMessege.ProjectMsg;
import com.example.administrator.christie.R;
import com.example.administrator.christie.activity.BaseActivity;
import com.example.administrator.christie.adapter.BDInfoSpinnerAdapter;
import com.example.administrator.christie.modelInfo.LoginInfo;
import com.example.administrator.christie.modelInfo.RequestParamsFM;
import com.example.administrator.christie.modelInfo.UserInfo;
import com.example.administrator.christie.util.HttpOkhUtils;
import com.example.administrator.christie.util.SPref;
import com.example.administrator.christie.util.ToastUtils;
import com.example.administrator.christie.util.ZxingUtils;
import com.example.administrator.christie.websiteUrl.NetConfig;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.Request;

public class QrcodeActivity extends BaseActivity implements View.OnClickListener {
    private ImageView iv_code, img_back;
    private Thread thread;
    private boolean isAlive = true;
    private TextView         mTv_title;
    //    private Spinner          mSpinner_village;//项目
    //    private Spinner          mSpinner_unit;//小区
    //    private Spinner          mSpinner_men;//大门
    private Button           mBt_submit;
    private List<ProjectMsg> dataInfoList, dataMenList;
    private BDInfoSpinnerAdapter mDetailAdapter;
    private String               mProjectID, mDetailID;
    private String mUserid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        setViews();
        setData();
        //        thread = new Thread(new MyThread());
        //        thread.start();
    }

    protected void setViews() {
        img_back = (ImageView) findViewById(R.id.img_back);
        mTv_title = (TextView) findViewById(R.id.tv_title);
        //        mSpinner_unit = (Spinner) findViewById(R.id.spinner_unit);
        //        mSpinner_men = (Spinner) findViewById(R.id.spinner_men);
        iv_code = (ImageView) findViewById(R.id.iv_code);
        mBt_submit = (Button) findViewById(R.id.bt_submit);
    }

    private void setData() {
        img_back.setOnClickListener(this);
        mTv_title.setText("二维码开门");
        //        dataInfoList = new ArrayList<>();
        //        ProjectMsg detailInfo = new ProjectMsg();
        //        detailInfo.setProject_name("请选择小区");
        //        dataInfoList.add(detailInfo);
        //        mDetailAdapter = new BDInfoSpinnerAdapter(QrcodeActivity.this, dataInfoList);
        //        mSpinner_unit.setAdapter(mDetailAdapter);
        UserInfo userinfo = SPref.getObject(QrcodeActivity.this, UserInfo.class, "userinfo");
        mUserid = userinfo.getUserid();
        //从网络获取用户绑定的小区
        //        getBDProjectID(mUserid);
        //        mSpinner_unit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        //            @Override
        //            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        //                ProjectMsg projectInfo = dataInfoList.get(i);
        //                String project_name = projectInfo.getProject_name();
        //                if (!project_name.equals("请选择小区")) {
        //                    mDetailID = projectInfo.getId();
        //                    //                    getMenInfoFromInt(mDetailID);
        //                }
        //            }
        //
        //            @Override
        //            public void onNothingSelected(AdapterView<?> adapterView) {
        //
        //            }
        //        });
        //        ProjectMsg menInfo = new ProjectMsg();
        //        menInfo.setProject_name("请选择大门");
        //        dataMenList = new ArrayList<>();
        //        dataMenList.add(menInfo);
        //        mSpinner_men.setAdapter();
        mBt_submit.setOnClickListener(this);
    }

    //    private void getBDProjectID(String userID) {
    //        String personalDetailUrl = NetConfig.PERSONALDATA;
    //        RequestParamsFM params = new RequestParamsFM();
    //        params.put("userid", userID);
    //        HttpOkhUtils.getInstance().doGetWithParams(personalDetailUrl, params, new HttpOkhUtils.HttpCallBack() {
    //            @Override
    //            public void onError(Request request, IOException e) {
    //                ToastUtils.showToast(QrcodeActivity.this, "网络错误");
    //            }
    //
    //            @Override
    //            public void onSuccess(int code, String resbody) {
    //                if (code != 200) {
    //                    ToastUtils.showToast(QrcodeActivity.this, "网络错误，错误码" + code);
    //                    return;
    //                }
    //                Gson gson = new Gson();
    //                PersonalDataInfo personalDataInfo = gson.fromJson(resbody, PersonalDataInfo.class);
    //                PersonalDataInfo.ArrBean arr = personalDataInfo.getArr();
    //                if (null == dataInfoList) {
    //                    dataInfoList = new ArrayList<>();
    //                } else {
    //                    dataInfoList.clear();
    //                }
    //                ProjectMsg detailInfo = new ProjectMsg();
    //                detailInfo.setProject_name("请选择小区");
    //                dataInfoList.add(detailInfo);
    //                List<PersonalDataInfo.ArrBean.ListProjectBean> listProject = arr.getListProject();
    //                for (int i = 0; i < listProject.size(); i++) {
    //                    PersonalDataInfo.ArrBean.ListProjectBean listProjectBean = listProject.get(i);
    //                    String project_name = listProjectBean.getProject_name();
    //                    String fname = listProjectBean.getFname();
    //                    String detail_id = listProjectBean.getProjectdetail_id();
    //                    ProjectMsg bdInfo = new ProjectMsg();
    //                    bdInfo.setProject_name(project_name);
    //                    bdInfo.setDetail_name(fname);
    //                    bdInfo.setId(detail_id);
    //                    dataInfoList.add(bdInfo);
    //                }
    //                mDetailAdapter.notifyDataSetChanged();
    //            }
    //        });
    //    }

    private void getMenInfoFromInt(String detailID) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.bt_submit:
                //用户选择完后，提交服务器获取数据
                getQrcode();
                break;
        }
    }

    private void getQrcode() {
        String getQcUrl = NetConfig.QRCODE;
        RequestParamsFM params = new RequestParamsFM();
        params.put("id", mUserid);
        params.put("type", "0");
        HttpOkhUtils.getInstance().doGetWithParams(getQcUrl, params, new HttpOkhUtils.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                ToastUtils.showToast(QrcodeActivity.this, "网络错误");
            }

            @Override
            public void onSuccess(int code, String resbody) {
                if (code == 200) {
                    Gson gson = new Gson();
                    LoginInfo loginInfo = gson.fromJson(resbody, LoginInfo.class);
                    String qrcode = loginInfo.getQrcode();
                    //生成二维码
                    generateQr(qrcode);
                } else {
                    ToastUtils.showToast(QrcodeActivity.this, "获取数据失败");
                }
            }
        });
    }

    private void generateQr(String qrcode) {
        Bitmap bitmap = ZxingUtils.createQRImage(qrcode, 200, 200);
        System.out.println(bitmap);
        if (null != bitmap) {
            iv_code.setImageBitmap(bitmap);
        }
    }
    //    Bitmap encodeAsBitmap(String str) {
    //        Bitmap bitmap = null;
    //        BitMatrix result = null;
    //        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
    //        try {
    //            result = multiFormatWriter.encode(str, BarcodeFormat.QR_CODE, 300, 300);
    //            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
    //            bitmap = barcodeEncoder.createBitmap(result);
    //        } catch (WriterException e) {
    //            e.printStackTrace();
    //        } catch (IllegalArgumentException iae) {
    //            return null;
    //        }
    //        return bitmap;
    //    }
    //
    //    @Override
    //    protected void onDestroy() {
    //        isAlive = false;
    //        super.onDestroy();
    //    }
    //
    //    Handler handler = new Handler() {
    //        public void handleMessage(Message msg) {
    //            switch (msg.what) {
    //                case 1:
    //                    //                    generateQr(msg.obj.toString());
    //                    generateQr("哈哈，猜猜我是什么内容？");
    //                    //                    iv_code.setImageBitmap(encodeAsBitmap(msg.obj.toString()));
    //                    break;
    //            }
    //            super.handleMessage(msg);
    //        }
    //    };
    //
    //
    //    class MyThread implements Runnable {
    //        @Override
    //        public void run() {
    //            // TODO Auto-generated method stub
    //            while (isAlive) {
    //                try {
    //                    String url = Consts.URL + "qrcode?userid=" + TApplication.user.getId();
    //                    HttpResponse response = HttpUtils.GetUtil(url);
    //                    HttpEntity entity = response.getEntity();
    //                    BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
    //                    String result = reader.readLine();
    //                    Log.d("HTTP", "POST:" + result);
    //                    Message message = new Message();
    //                    message.what = 1;
    //                    message.obj = result;
    //                    handler.sendMessage(message);// 发送消息
    //                    Thread.sleep(5 * 60 * 1000);// 线程暂停5分钟，单位毫秒
    //                } catch (Exception e) {
    //                    // TODO Auto-generated catch block
    //                    e.printStackTrace();
    //                }
    //            }
    //        }
    //    }
    //
    //    private void generateQr(String data) {
    //        Bitmap bitmap = ZxingUtils.createQRImage(data, 200, 200);
    //        System.out.println(bitmap);
    //        if (null != bitmap) {
    //            iv_code.setImageBitmap(bitmap);
    //        }
    //    }
}
