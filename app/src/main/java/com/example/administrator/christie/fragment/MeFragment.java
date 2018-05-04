package com.example.administrator.christie.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.christie.R;
import com.example.administrator.christie.TApplication;
import com.example.administrator.christie.activity.CardActivity;
import com.example.administrator.christie.activity.PersonalActivity;
import com.example.administrator.christie.activity.PlateActivity;
import com.example.administrator.christie.activity.SettingsActivity;
import com.example.administrator.christie.util.Consts;
import com.example.administrator.christie.util.HttpUtils;
import com.example.administrator.christie.view.CustomProgress;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import java.io.BufferedReader;
import java.io.InputStreamReader;


public class MeFragment extends Fragment {
    private Context mContext;
    private View view;
    private ImageView iv_settings;
    private LinearLayout ll_geren,ll_kapian,ll_chepai;
//    private Button btn_xiaoxi,btn_huiyi;
    private TextView tv_username;
    private CustomProgress dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getContext();
        view = inflater.inflate(R.layout.fragment_me, container, false);
        setViews();
        setListeners();
        return view;
    }

    protected void setViews(){
        iv_settings = (ImageView)view.findViewById(R.id.iv_settings);
        tv_username = (TextView)view.findViewById(R.id.tv_username);
        //        btn_xiaoxi = (Button)view.findViewById(R.id.btn_xiaoxi);
        //        btn_huiyi = (Button)view.findViewById(R.id.btn_huiyi);
        ll_geren = (LinearLayout)view.findViewById(R.id.ll_geren);
        ll_kapian = (LinearLayout)view.findViewById(R.id.ll_kapian);
        ll_chepai = (LinearLayout)view.findViewById(R.id.ll_chepai);
//        if(!TApplication.user.getFname().equals("")){
//            tv_username.setText(TApplication.user.getFname());
//        }else {
//            tv_username.setText("手机用户"+TApplication.user.getFmobile().substring(7,11));
//        }
    }

    protected void setListeners(){
        tv_username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                final EditText et = new EditText(getActivity());
//                new AlertDialog.Builder(getActivity()).setTitle("修改用户名").setView(
//                        et).setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        new Task(et.getText().toString()).execute();
//                    }
//                })
//                        .setNegativeButton("取消", null).show();
                startActivity(new Intent(mContext,PersonalActivity.class));
            }
        });
        iv_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext,SettingsActivity.class));
            }
        });
//        btn_xiaoxi.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(mContext,MessageActivity.class));
//            }
//        });
//        btn_huiyi.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(mContext,MeettingActivity.class));
//            }
//        });
        ll_geren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext,PersonalActivity.class));
            }
        });
        ll_kapian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext,CardActivity.class));
            }
        });
        ll_chepai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext,PlateActivity.class));
            }
        });
    }

    class Task extends AsyncTask<Void, Integer, Integer> {
        String name;

        Task(String name){
            this.name = name;
        }

        /**
         * 运行在UI线程中，在调用doInBackground()之前执行
         */
        @Override
        protected void onPreExecute() {
            dialog= CustomProgress.show(getActivity(),"提交中...", true, null);
        }

        /**
         * 后台运行的方法，可以运行非UI线程，可以执行耗时的方法
         */
        @Override
        protected Integer doInBackground(Void... params) {
            try{
            String url = Consts.URL+"modifyname?id="+TApplication.user.getId()+"&name="+name;
            HttpResponse response = HttpUtils.GetUtil(url);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(entity.getContent()));
                String result = reader.readLine();
                Log.d("HTTP", "POST:" + result);
                return 1;
            }else {
                return 2;
            }
            }catch (Exception e){
                e.printStackTrace();
                return 3;
            }
        }


        /**
         * 运行在ui线程中，在doInBackground()执行完毕后执行
         */
        @Override
        protected void onPostExecute(Integer integer) {
            dialog.dismiss();
            if(integer==1){
                Toast.makeText(getActivity(),"修改成功",Toast.LENGTH_SHORT).show();
                tv_username.setText(name);
            }else {
                Toast.makeText(getActivity(),"操作失败",Toast.LENGTH_SHORT).show();
            }

        }

        /**
         * 在publishProgress()被调用以后执行，publishProgress()用于更新进度
         */
        @Override
        protected void onProgressUpdate(Integer... values) {

        }
    }
}
