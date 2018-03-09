package com.example.administrator.christie.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.christie.R;
import com.example.administrator.christie.activity.TempayActivity;
import com.example.administrator.christie.util.AllCapTransformationMethod;
import com.example.administrator.christie.util.Consts;
import com.example.administrator.christie.util.HttpUtils;
import com.example.administrator.christie.view.CustomProgress;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class SearchFragment extends Fragment {
    private Context mContext;
    private View view;
    private EditText et_plate;
    private Button btn_chaxun;
    private CustomProgress dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getContext();
        view = inflater.inflate(R.layout.fragment_search, container, false);
        setViews();
        setListeners();
        return view;
    }

    protected void setViews(){
        et_plate = (EditText) view.findViewById(R.id.et_plate);
        btn_chaxun = (Button) view.findViewById(R.id.btn_chaxun);
        et_plate.setTransformationMethod(new AllCapTransformationMethod());
    }

    protected void setListeners(){
        btn_chaxun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et_plate.getText().toString().equals("")){
                    Toast.makeText(mContext,"请输入当前车牌号",Toast.LENGTH_SHORT).show();
                }else{
                    new Task(et_plate.getText().toString()).execute();
                }
            }
        });
    }

    class Task extends AsyncTask<Void,String,String>{
        String fplateno;

        Task(String fplateno){
            this.fplateno = fplateno;
        }

        @Override
        protected void onPreExecute() {
            dialog = CustomProgress.show(mContext,"查询中",true,null);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {
            try{
                String url = Consts.URL+"searchPlateno?fplateno="+fplateno;
                HttpResponse response = HttpUtils.GetUtil(url);
                HttpEntity entity = response.getEntity();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(entity.getContent()));
                String result = reader.readLine();
                Log.d("HTTP", "POST:" + result);
                return result;
            }catch (Exception e){
                e.printStackTrace();
                return "1";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            dialog.dismiss();
            if("1".equals(s)){
                Toast.makeText(mContext,"查找失败",Toast.LENGTH_SHORT).show();
            }else if(s==null){
                Toast.makeText(mContext,"查无此车牌，请检查车牌输入无误",Toast.LENGTH_SHORT).show();
            }else{
                //查出入场时间
                TempayActivity activity = (TempayActivity)getActivity();
                FragmentManager fm = activity.getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                if (activity.tempayFragment == null) {
                    activity.tempayFragment = new TempayFragment();
                }
                ft.replace(R.id.frag_container, activity.tempayFragment);
                ft.commit();
            }
            super.onPostExecute(s);
        }
    }
}
