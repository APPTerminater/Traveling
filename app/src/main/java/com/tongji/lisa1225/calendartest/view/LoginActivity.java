package com.tongji.lisa1225.calendartest.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tongji.lisa1225.calendartest.R;
import com.tongji.lisa1225.calendartest.dao.UserInfoDao;

public class LoginActivity extends AppCompatActivity {
    private EditText mEtName;
    private EditText mEtPassword;
    private UserInfoDao mDao;
    Button loginbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginbtn=(Button)findViewById(R.id.loginButton);
        mDao=new UserInfoDao(LoginActivity.this);
        mEtName= (EditText) findViewById(R.id.name);
        mEtPassword=(EditText)findViewById(R.id.password);
    }
    public void login(View view){
        loginbtn.setBackgroundColor(getResources().getColor(R.color.danxiaqu));
        String name=mEtName.getText().toString().trim();
        String password=mEtPassword.getText().toString().trim();
        if(TextUtils.isEmpty(name)||TextUtils.isEmpty(password)){
            Toast.makeText(this,"填写不完整",Toast.LENGTH_SHORT).show();
            loginbtn.setBackgroundColor(getResources().getColor(R.color.tool_bar));
            return;
        }
        else{
            String result = mDao.alterPassword(name);
            if(result==null||!(password.equals(result))){
                Toast.makeText(this,"昵称或密码错误！",Toast.LENGTH_SHORT).show();
                loginbtn.setBackgroundColor(getResources().getColor(R.color.tool_bar));
                return;
            }else{
                Toast.makeText(this,"登录成功！",Toast.LENGTH_SHORT).show();
            }

        }
        Intent mainintent =new Intent(LoginActivity.this,MainActivity.class);
        mainintent.putExtra("nickname",name);
        //启动
        startActivity(mainintent);
    }
    public void jump_register(View view)
    {
        Intent loginintent =new Intent(LoginActivity.this,RegisterActivity.class);
        //启动
        startActivity(loginintent);
    }
}
