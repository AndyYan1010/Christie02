package com.example.administrator.christie.activity.msgAct;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.christie.R;
import com.example.administrator.christie.activity.BaseActivity;
import com.example.administrator.christie.modelInfo.MsgDetailInfo;
import com.example.administrator.christie.modelInfo.RequestParamsFM;
import com.example.administrator.christie.modelInfo.UserInfo;
import com.example.administrator.christie.util.HttpOkhUtils;
import com.example.administrator.christie.util.ProgressDialogUtil;
import com.example.administrator.christie.util.SPref;
import com.example.administrator.christie.util.ToastUtils;
import com.example.administrator.christie.websiteUrl.NetConfig;
import com.google.gson.Gson;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import okhttp3.Request;

/**
 * @创建者 AndyYan
 * @创建时间 2018/5/15 10:31
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class MsgDetailActivity extends BaseActivity implements View.OnClickListener {

    private TextView  mTv_kind;
    private TextView  mTv_time;
    private String    mMsgid;
    private ImageView mImg_back, mImg_type, mImg_nonet;
    private TextView mTv_title;
    private String   mKind;
    private String   mFread;
    private WebView  web_detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_detail);
        Intent intent = getIntent();
        mMsgid = intent.getStringExtra("msgid");
        mKind = intent.getStringExtra("kind");
        mFread = intent.getStringExtra("fread");
        initView();
        initData();
    }

    private void initView() {
        mImg_nonet = (ImageView) findViewById(R.id.img_nonet);
        mImg_back = (ImageView) findViewById(R.id.img_back);
        mImg_type = (ImageView) findViewById(R.id.img_type);
        mTv_title = (TextView) findViewById(R.id.tv_title);
        mTv_kind = (TextView) findViewById(R.id.tv_kind);
        mTv_time = (TextView) findViewById(R.id.tv_time);
        web_detail = (WebView) findViewById(R.id.web_detail);
    }

    private void initData() {
        mImg_back.setOnClickListener(this);
        if ("1".equals(mKind)) {
            mTv_title.setText("公告详情");
            mTv_kind.setTextColor(getResources().getColor(R.color.yellow_kind));
            mImg_type.setImageResource(R.drawable.notices);
        } else if ("2".equals(mKind)) {
            mTv_title.setText("会议详情");
            mTv_kind.setTextColor(getResources().getColor(R.color.blue_kind));
            mImg_type.setImageResource(R.drawable.meeting_icon);
        }
        //获取消息详情
        getMsgDetail();
    }

    private void getMsgDetail() {
        ProgressDialogUtil.startShow(MsgDetailActivity.this, "正在加载,请稍后。。。");
        UserInfo userinfo = SPref.getObject(MsgDetailActivity.this, UserInfo.class, "userinfo");
        String msgDetUrl = NetConfig.MEETINGDETAIL;
        RequestParamsFM params = new RequestParamsFM();
        params.put("userid", userinfo.getUserid());
        params.put("fmeeting_id", mMsgid);
        params.put("fread", mFread);
        HttpOkhUtils.getInstance().doPost(msgDetUrl, params, new HttpOkhUtils.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                ProgressDialogUtil.hideDialog();
                ToastUtils.showToast(MsgDetailActivity.this, "网络错误");
            }

            @Override
            public void onSuccess(int code, String resbody) {
                ProgressDialogUtil.hideDialog();
                if (code == 200) {
                    mImg_nonet.setVisibility(View.GONE);
                    Gson gson = new Gson();
                    MsgDetailInfo msgDetailInfo = gson.fromJson(resbody, MsgDetailInfo.class);
                    int code1 = msgDetailInfo.getCode();
                    String message = msgDetailInfo.getMessage();
                    if (code1 == 1) {
                        MsgDetailInfo.DetailBean detail = msgDetailInfo.getDetail();
                        String create_date = detail.getCreate_date();
                        String ftype = detail.getFtype();
                        String meeting_content = detail.getMeeting_content();
                        if ("1".equals(ftype)) {
                            mTv_kind.setText("公告");
                        } else {
                            mTv_kind.setText("会议");
                        }
                        mTv_time.setText(create_date);
                        web_detail.loadDataWithBaseURL("", getNewContent(meeting_content), "text/html", "utf-8", "");
                        //启用支持javascript
                        WebSettings settings = web_detail.getSettings();
                        settings.setJavaScriptEnabled(true);
                        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
                        web_detail.setWebViewClient(new WebViewClient() {
                            @Override
                            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                // TODO Auto-generated method stub
                                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                                view.loadUrl(url);
                                return true;
                            }
                        });
                    } else {
                        ToastUtils.showToast(MsgDetailActivity.this, message);
                    }
                }
            }
        });
    }

    private String getNewContent(String htmltext) {
        Document doc = Jsoup.parse(htmltext);
        Elements elements = doc.getElementsByTag("img");
        for (Element element : elements) {
            element.attr("width", "100%").attr("height", "auto");
        }
        Log.d("VACK", doc.toString());
        return doc.toString();
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
