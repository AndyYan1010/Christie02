package com.example.administrator.christie.activity.usercenter;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.christie.R;
import com.example.administrator.christie.activity.BaseActivity;
import com.example.administrator.christie.modelInfo.GrzxInfo;
import com.example.administrator.christie.modelInfo.RequestParamsFM;
import com.example.administrator.christie.util.HttpOkhUtils;
import com.example.administrator.christie.util.ProgressDialogUtil;
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
 * @创建时间 2019/7/12 15:04
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class WebMoreInfoActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout linear_back;
    private TextView     tv_title;
    private WebView      web_detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_detail);
        initView();
        initData();
    }

    private void initView() {
        linear_back = findViewById(R.id.linear_back);
        tv_title = findViewById(R.id.tv_title);
        web_detail = findViewById(R.id.web_detail);
    }

    private void initData() {
        linear_back.setOnClickListener(this);
        int kind = getIntent().getIntExtra("kind", 0);
        if (1 == kind) {
            tv_title.setText("声明");
        } else if (2 == kind) {
            tv_title.setText("帮助");
        } else if (3 == kind) {
            tv_title.setText("隐私");
        }else {
            ToastUtils.showToast(this,"数据错误");
            finish();
            return;
        }
        //获取页面详情
        getContent(kind);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.linear_back:
                finish();
                break;
        }
    }

    private void getContent(int kind) {
        ProgressDialogUtil.startShow(this, "正在获取内容...");
        RequestParamsFM params = new RequestParamsFM();
        params.put("ftype", "" + kind);
        HttpOkhUtils.getInstance().doGetWithParams(NetConfig.GETGRZX, params, new HttpOkhUtils.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                ProgressDialogUtil.hideDialog();
                ToastUtils.showToast(WebMoreInfoActivity.this, "网络连接错误");
            }

            @Override
            public void onSuccess(int code, String resbody) {
                ProgressDialogUtil.hideDialog();
                if (code != 200) {
                    ToastUtils.showToast(WebMoreInfoActivity.this, "网络错误" + code);
                    return;
                }
                Gson gson = new Gson();
                GrzxInfo grzxInfo = gson.fromJson(resbody, GrzxInfo.class);
                String fcontent = grzxInfo.getGrzx().getFcontent();
                showContent(fcontent);
            }
        });
    }

    private void showContent(String fcontent) {
        web_detail.loadDataWithBaseURL("", getNewContent(fcontent), "text/html", "utf-8", "");
        WebSettings settings = web_detail.getSettings();
        settings.setJavaScriptEnabled(true);
        web_detail.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    private String getNewContent(String htmltext) {
        Document doc = Jsoup.parse(htmltext);
        Elements elements = doc.getElementsByTag("img");
        for (Element element : elements) {
            element.attr("width", "100%").attr("height", "auto");
        }
        return doc.toString();
    }
}
