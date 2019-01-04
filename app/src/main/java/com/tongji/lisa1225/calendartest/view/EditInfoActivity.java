package com.tongji.lisa1225.calendartest.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tongji.lisa1225.calendartest.R;
import com.tongji.lisa1225.calendartest.dao.UserInfoDao;

public class EditInfoActivity extends AppCompatActivity {
    private UserInfoDao mDao;
    private TextView nicknameText;
    private EditText birthdayText;
    private EditText walkText;
    private EditText passwordText;
    private EditText password_againText;
    String nickname;
    Intent get_intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editinfor);

        mDao=new UserInfoDao(EditInfoActivity.this);
        get_intent = getIntent();// 传来的昵称
        nickname=get_intent.getStringExtra("nickname");
        nicknameText=(TextView) findViewById(R.id.newname);
        nicknameText.setText(nickname);
        birthdayText=(EditText)findViewById(R.id.newbirthday);
        birthdayText.setText(mDao.alterBirthday(nickname));
        walkText=(EditText) findViewById(R.id.newwalk);
        walkText.setText(String.valueOf(mDao.alterWalk(nickname)));
        password_againText=(EditText)findViewById(R.id.newpassword_again);
        passwordText=(EditText)findViewById(R.id.newpassword);


    }
    public void tomain(View view){
        Intent mainintent =new Intent(EditInfoActivity.this,MainActivity.class);
        //启动
        mainintent.putExtra("nickname",nickname);
        startActivity(mainintent);
    }
    public void update(View view){
        String newbirthday=birthdayText.getText().toString().trim();
        String newpassword=passwordText.getText().toString().trim();
        String newwalk_daliy=walkText.getText().toString().trim();
        String newpasswordAgain=password_againText.getText().toString().trim();
        if(TextUtils.isEmpty(newbirthday)||TextUtils.isEmpty(newwalk_daliy)){
            Toast.makeText(this,"填写不完整",Toast.LENGTH_SHORT).show();
            return;
        }
        else if(!(newpassword.equals(newpasswordAgain))){
            Toast.makeText(this,"两次密码输入需一致！",Toast.LENGTH_SHORT).show();
            return;
        }else {
            int count=mDao.updateData(nickname, newbirthday,newpassword,newwalk_daliy);
            if(count==-1){
                Toast.makeText(this,"更新失败",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this,"数据更新了  "+count+"   行",Toast.LENGTH_SHORT).show();
            }
            Intent mainintent = new Intent(EditInfoActivity.this, MainActivity.class);
            mainintent.putExtra("nickname", nickname);
            //启动
            startActivity(mainintent);
        }
    }
}
