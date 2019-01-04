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


public class RegisterActivity extends AppCompatActivity {
    private EditText mEtName;
    private EditText mEtBirthday;
    private EditText mEtPassword;
    private EditText mEtPasswordAgain;
    private EditText mEtWalkDaily;
    private UserInfoDao mDao;
    Button registerbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerbtn=(Button)findViewById(R.id.registerButton);
        mDao=new UserInfoDao(RegisterActivity.this);
        mEtName= (EditText) findViewById(R.id.name);
        mEtBirthday= (EditText) findViewById(R.id.born);
        mEtPassword=(EditText)findViewById(R.id.password);
        mEtPasswordAgain=(EditText)findViewById(R.id.password_again);
        mEtWalkDaily=(EditText)findViewById(R.id.step);

    }
    public void add(View view){
        registerbtn.setBackgroundColor(getResources().getColor(R.color.danxiaqu));
        String name=mEtName.getText().toString().trim();
        String birthday=mEtBirthday.getText().toString().trim();
        String password=mEtPassword.getText().toString().trim();
        String walk_daliy=mEtWalkDaily.getText().toString().trim();
        String passwordAgain=mEtPasswordAgain.getText().toString().trim();
        if(TextUtils.isEmpty(name)||TextUtils.isEmpty(birthday)||TextUtils.isEmpty(password)||TextUtils.isEmpty(walk_daliy)){
            Toast.makeText(this,"填写不完整",Toast.LENGTH_SHORT).show();
            registerbtn.setBackgroundColor(getResources().getColor(R.color.tool_bar));
            return;
        }
        else if(TextUtils.isEmpty(passwordAgain)||!(password.equals(passwordAgain))){
            Toast.makeText(this,"两次密码输入需一致！",Toast.LENGTH_SHORT).show();
            registerbtn.setBackgroundColor(getResources().getColor(R.color.tool_bar));
            return;
        }else{
            long addLong = mDao.addData(name, birthday,password,walk_daliy);
            if(addLong==-1){
                Toast.makeText(this,"添加失败",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this,"数据添加在第  "+addLong+"   行",Toast.LENGTH_SHORT).show();
            }

        }
        Intent mainintent =new Intent(RegisterActivity.this,MainActivity.class);
        mainintent.putExtra("nickname",name);
        //启动
        startActivity(mainintent);
    }
    public void jump_login(View view)
    {
        Intent loginintent =new Intent(RegisterActivity.this,LoginActivity.class);
        //启动
        startActivity(loginintent);
    }
}