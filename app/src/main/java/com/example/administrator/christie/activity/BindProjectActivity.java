package com.example.administrator.christie.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.administrator.christie.InformationMessege.ProjectMsg;
import com.example.administrator.christie.R;
import com.example.administrator.christie.adapter.ProSpinnerAdapter;
import com.example.administrator.christie.modelInfo.ProjectInfo;
import com.example.administrator.christie.modelInfo.RequestParamsFM;
import com.example.administrator.christie.modelInfo.UpDataInfo;
import com.example.administrator.christie.modelInfo.UserInfo;
import com.example.administrator.christie.util.HttpOkhUtils;
import com.example.administrator.christie.util.ProgressDialogUtils;
import com.example.administrator.christie.util.SPref;
import com.example.administrator.christie.util.ToastUtils;
import com.example.administrator.christie.websiteUrl.NetConfig;
import com.google.gson.Gson;
import com.nanchen.compresshelper.CompressHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

/**
 * @创建者 AndyYan
 * @创建时间 2018/4/27 9:33
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class BindProjectActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mImg_back, mImg_id_pic;
    private TextView mTv_title;
    private Spinner  mSpinner_village, mSpinner_unit;
    private List<ProjectMsg> dataProList, dataInfoList;
    private ProSpinnerAdapter mProjAdapter, mDetailAdapter;
    private Button mBt_submit, mBt_insert_pic;
    private String mProjectID, mDetailID;
    private EditText mEt_place, mEt_relation;
    private String mPlace, mRelation;
    private static final int IMAGE = 1; //调用系统相册-选择图片
    private String mImg_onSer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_projcet);
        setViews();
        setData();
    }

    private void setViews() {
        mImg_back = (ImageView) findViewById(R.id.img_back);
        mImg_id_pic = (ImageView) findViewById(R.id.img_id_pic);
        mTv_title = (TextView) findViewById(R.id.tv_title);
        mSpinner_village = (Spinner) findViewById(R.id.spinner_village);
        mSpinner_unit = (Spinner) findViewById(R.id.spinner_unit);
        mEt_place = (EditText) findViewById(R.id.et_place);
        mEt_relation = (EditText) findViewById(R.id.et_relation);
        mBt_insert_pic = (Button) findViewById(R.id.bt_insert_pic);
        mBt_submit = (Button) findViewById(R.id.bt_submit);
    }

    private void setData() {
        mTv_title.setText("绑定项目");
        mImg_back.setOnClickListener(this);
        dataProList = new ArrayList<>();
        ProjectMsg projectInfo = new ProjectMsg();
        projectInfo.setProject_name("请选择公司");
        dataProList.add(projectInfo);
        dataInfoList = new ArrayList<>();
        dataInfoList.add(projectInfo);
        mProjAdapter = new ProSpinnerAdapter(BindProjectActivity.this, dataProList);
        mSpinner_village.setAdapter(mProjAdapter);
        //从网络获取项目
        getProjectFromServer("1");
        mSpinner_village.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //获取点击条目ID，给spinner2设置数据
                ProjectMsg projectInfo = dataProList.get(i);
                String project_name = projectInfo.getProject_name();
                if (!project_name.equals("请选择公司")) {
                    mProjectID = projectInfo.getId();
                    //to intnet
                    getDetailFromInt(mProjectID);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mDetailAdapter = new ProSpinnerAdapter(BindProjectActivity.this, dataInfoList);
        mSpinner_unit.setAdapter(mDetailAdapter);
        mSpinner_unit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ProjectMsg projectInfo = dataInfoList.get(i);
                String project_name = projectInfo.getProject_name();
                if (!project_name.equals("请选择公司")) {
                    mDetailID = projectInfo.getId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mBt_insert_pic.setOnClickListener(this);
        mBt_submit.setOnClickListener(this);
    }

    private void getDetailFromInt(String id) {
        String url = NetConfig.PROJECT + "?id=" + id;
        HttpOkhUtils.getInstance().doGet(url, new HttpOkhUtils.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                ToastUtils.showToast(BindProjectActivity.this, "网络请求失败");
            }

            @Override
            public void onSuccess(int code, String resbody) {
                if (code != 200) {
                    ToastUtils.showToast(BindProjectActivity.this, "网络错误");
                    return;
                }
                if (null == dataInfoList) {
                    dataInfoList = new ArrayList<>();
                } else {
                    dataInfoList.clear();
                }
                ProjectMsg projectMsg = new ProjectMsg();
                projectMsg.setProject_name("请选择公司");
                dataInfoList.add(projectMsg);
                Gson gson = new Gson();
                ProjectInfo info = gson.fromJson(resbody, ProjectInfo.class);
                List<ProjectInfo.ProjectdetalilistBean> projectdetalilist = info.getProjectdetalilist();
                for (int i = 0; i < projectdetalilist.size(); i++) {
                    ProjectInfo.ProjectdetalilistBean detalilistBean = projectdetalilist.get(i);
                    ProjectMsg projectMsgN = new ProjectMsg();
                    projectMsgN.setProject_name(detalilistBean.getFname());
                    projectMsgN.setId(detalilistBean.getId());
                    dataInfoList.add(projectMsgN);
                }
                mDetailAdapter.notifyDataSetChanged();
            }
        });
    }

    private void getProjectFromServer(String i) {
        String url = NetConfig.PROJECT + "?id=" + i;
        HttpOkhUtils.getInstance().doGet(url, new HttpOkhUtils.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                ToastUtils.showToast(BindProjectActivity.this, "网络错误");
            }

            @Override
            public void onSuccess(int code, String resbody) {
                if (code != 200) {
                    ToastUtils.showToast(BindProjectActivity.this, "网络获取失败");
                    return;
                }
                if (null == dataProList) {
                    dataProList = new ArrayList<>();
                } else {
                    dataProList.clear();
                }
                ProjectMsg projectMsg = new ProjectMsg();
                projectMsg.setProject_name("请选择公司");
                dataProList.add(projectMsg);
                Gson gson = new Gson();
                ProjectInfo info = gson.fromJson(resbody, ProjectInfo.class);
                List<ProjectInfo.ProjectlistBean> projectlist = info.getProjectlist();
                for (int i = 0; i < projectlist.size(); i++) {
                    ProjectInfo.ProjectlistBean projectlistBean = projectlist.get(i);
                    ProjectMsg projectMsgN = new ProjectMsg();
                    projectMsgN.setProject_name(projectlistBean.getProject_name());
                    projectMsgN.setId(projectlistBean.getId());
                    dataProList.add(projectMsgN);
                }
                mProjAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.bt_submit:
                mPlace = String.valueOf(mEt_place.getText()).trim();
                mRelation = String.valueOf(mEt_relation.getText()).trim();
                if ("".equals(mPlace) || "请输入房屋具体地址".equals(mPlace)) {
                    ToastUtils.showToast(BindProjectActivity.this, "请输入地址");
                    return;
                }
                if ("".equals(mRelation) || "请输入和房屋的关系".equals(mRelation)) {
                    ToastUtils.showToast(BindProjectActivity.this, "请输入关系");
                    return;
                }
                if (null == mProjectID || "".equals(mProjectID)) {
                    ToastUtils.showToast(BindProjectActivity.this, "请选择公司");
                    return;
                }
                if (null == mDetailID || "".equals(mDetailID)) {
                    ToastUtils.showToast(BindProjectActivity.this, "请选择公司详细信息");
                    return;
                }
                if (null == mImg_onSer || "".equals(mImg_onSer)) {
                    ToastUtils.showToast(BindProjectActivity.this, "图片上传失败，请重新选择图片");
                    return;
                }
                //提交网络
                sendToIntnet();
                break;
            case R.id.bt_insert_pic:
                Boolean aBoolean = existsSdcard();
                if (!aBoolean) {
                    ToastUtils.showToast(BindProjectActivity.this, "手机SD卡不可用");
                    return;
                }
                verifyStoragePermissions(BindProjectActivity.this);
                //调用相册
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, IMAGE);
                break;
            default:
                break;
        }
    }

    private void sendToIntnet() {
        ProgressDialogUtils.getInstance().show(BindProjectActivity.this, "正在提交，请稍等");
        UserInfo userinfo = SPref.getObject(BindProjectActivity.this, UserInfo.class, "userinfo");
        String phone = userinfo.getPhone();
        String psw = userinfo.getPsw();
        String url = NetConfig.AUTHENTICATION;
        RequestParamsFM requestParams = new RequestParamsFM();
        requestParams.put("mobile", phone);
        requestParams.put("password", psw);
        requestParams.put("project_id", mProjectID);
        requestParams.put("projectdetail_id", mDetailID);
        requestParams.put("relation", mRelation);
        requestParams.put("faddress", mPlace);
        requestParams.put("img", mImg_onSer);
        requestParams.setUseJsonStreamer(true);
        HttpOkhUtils.getInstance().doPost(url, requestParams, new HttpOkhUtils.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                ProgressDialogUtils.getInstance().dismiss();
                ToastUtils.showToast(BindProjectActivity.this, "网络错误");
            }

            @Override
            public void onSuccess(int code, String resbody) {
                ProgressDialogUtils.getInstance();
                if (code != 200) {
                    ToastUtils.showToast(BindProjectActivity.this, "网络错误");
                    return;
                }
                ToastUtils.showToast(BindProjectActivity.this, "提交成功，等待审核");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //获取图片路径
        if (requestCode == IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            String imagePath = c.getString(columnIndex);
            showImage(imagePath);
            c.close();
        }
    }

    //加载图片
    private void showImage(String imgPath) {
        //        Bitmap bm = BitmapFactory.decodeFile(imgPath);
        //压缩图片
        File file = new File(imgPath);
        File newFile = new CompressHelper.Builder(this)
                .setMaxWidth(720)  // 默认最大宽度为720
                .setMaxHeight(960) // 默认最大高度为960
                .setQuality(100)    // 默认压缩质量为80
                .setFileName("sendPic") // 设置你需要修改的文件名
                .setCompressFormat(Bitmap.CompressFormat.JPEG) // 设置默认压缩为jpg格式
                .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES).getAbsolutePath())
                .build()
                .compressToFile(file);
        Bitmap bm = BitmapFactory.decodeFile(newFile.getPath());
        mImg_id_pic.setImageBitmap(bm);
        mImg_onSer = "";
        //上传图片
        sendImgToService(bm);
    }

    private void sendImgToService(Bitmap bm) {
        ProgressDialogUtils.getInstance().show(BindProjectActivity.this, "正在加载");
        String upImgUrl = NetConfig.UPLOADBASE64;
        String strByBase64 = Bitmap2StrByBase64(bm);
        RequestParamsFM params = new RequestParamsFM();
        params.put("imgStr", strByBase64);
        HttpOkhUtils.getInstance().doGetWithParams(upImgUrl, params, new HttpOkhUtils.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                ProgressDialogUtils.getInstance().dismiss();
                ToastUtils.showToast(BindProjectActivity.this, "网络错误，图片上传失败");
            }

            @Override
            public void onSuccess(int code, String resbody) {
                ProgressDialogUtils.getInstance().dismiss();
                if (code != 200) {
                    ToastUtils.showToast(BindProjectActivity.this, "图片上传失败,错误代码" + code);
                    return;
                }
                Gson gson = new Gson();
                UpDataInfo upDataInfo = gson.fromJson(resbody, UpDataInfo.class);
                int result = upDataInfo.getResult();
                if (result != 1) {
                    ToastUtils.showToast(BindProjectActivity.this, "图片上传失败,返回值" + result);
                    return;
                }
                String fileName = upDataInfo.getFileName();
                mImg_onSer = fileName;
                ToastUtils.showToast(BindProjectActivity.this, "上传成功");
            }
        });
    }

    /**
     * 通过Base32将Bitmap转换成Base64字符串
     *
     * @param bit
     * @return
     */
    public String Bitmap2StrByBase64(Bitmap bit) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.JPEG, 40, bos);//参数100表示不压缩
        byte[] bytes = bos.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    /**
     * 判断外置sdcard是否可以正常使用
     *
     * @return
     */
    public static Boolean existsSdcard() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable();
    }

    private static final int      REQUEST_EXTERNAL_STORAGE = 1;
    private static       String[] PERMISSIONS_STORAGE      = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};

    public static void verifyStoragePermissions(Activity activity) {

        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
