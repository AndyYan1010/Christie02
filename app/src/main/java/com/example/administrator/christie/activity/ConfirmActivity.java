package com.example.administrator.christie.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.christie.R;
import com.example.administrator.christie.TApplication;
import com.example.administrator.christie.util.SendMsgUtil;
import com.example.administrator.christie.util.Task;
import com.example.administrator.christie.view.CountdownButton;

public class ConfirmActivity extends BaseActivity {
    private EditText et_code_confirm;
    private CountdownButton btn_code_confirm;
    private Button btn_confirm;
    private String code="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);
        setViews();
        setListeners();
    }

    protected void setViews(){
        et_code_confirm = (EditText)findViewById(R.id.et_code_confirm);
        btn_code_confirm = (CountdownButton)findViewById(R.id.btn_code_confirm);
        btn_confirm = (Button)findViewById(R.id.btn_confirm);
    }

    protected void setListeners(){
        btn_code_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //生成随机验证码
                btn_code_confirm.start();
                code = SendMsgUtil.getRandomString();
                Log.i("当前的验证码",code+"<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
                //发送短信
                new Task(TApplication.user.getFmobile(),code).execute();
            }
        });
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if(et_code_confirm.equals("")||code.equals("")){
                Toast.makeText(ConfirmActivity.this,"请先获取验证码并输入",Toast.LENGTH_SHORT).show();
            }else if(et_code_confirm.getText().toString().equals(code)){
                Toast.makeText(ConfirmActivity.this,"验证通过",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ConfirmActivity.this,MobModifyActivity.class));
                finish();
            }
            }
        });
    }
}
