package com.tongji.lisa1225.calendartest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.tongji.lisa1225.calendartest.dao.UserInfoDao;


public class RegisterActivity extends AppCompatActivity {
    private EditText mEtName;
    private EditText mEtBirthday;
    private EditText mEtPlaceNow;
    private EditText mEtWalkDaily;
    private UserInfoDao mDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mDao=new UserInfoDao(RegisterActivity.this);
        mEtName= (EditText) findViewById(R.id.mEtName);
        mEtBirthday= (EditText) findViewById(R.id.mEtBirthday);
        mEtPlaceNow=(EditText)findViewById(R.id.mEtPlaceNow);
        mEtWalkDaily=(EditText)findViewById(R.id.mEtWalkDaily);

    }
    public void add(View view){

        String name=mEtName.getText().toString().trim();
        String birthday=mEtBirthday.getText().toString().trim();
        String place_now=mEtPlaceNow.getText().toString().trim();
        String walk_daliy=mEtWalkDaily.getText().toString().trim();
        if(TextUtils.isEmpty(name)||TextUtils.isEmpty(birthday)||TextUtils.isEmpty(place_now)||TextUtils.isEmpty(walk_daliy)){
            Toast.makeText(this,"填写不完整",Toast.LENGTH_SHORT).show();
            return;
        }else{
            long addLong = mDao.addData(name, birthday,place_now,walk_daliy);
            if(addLong==-1){
                Toast.makeText(this,"添加失败",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this,"数据添加在第  "+addLong+"   行",Toast.LENGTH_SHORT).show();
            }

        }
    }

    public void delete(View view){
        String name=mEtName.getText().toString().trim();


        if(TextUtils.isEmpty(name)){
            Toast.makeText(this,"填写不完整",Toast.LENGTH_SHORT).show();
            return;
        }else{
            int deleteDate = mDao.deleteData(name);
            if(deleteDate==-1){
                Toast.makeText(this,"删除失败",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this,"成功删除  "+deleteDate+"   条数据",Toast.LENGTH_SHORT).show();
            }

        }

    }


    public void update(View view){

        String name=mEtName.getText().toString().trim();
        String walk_daliy=mEtWalkDaily.getText().toString().trim();
        if(TextUtils.isEmpty(name)||TextUtils.isEmpty(walk_daliy)){
            Toast.makeText(this,"填写不完整",Toast.LENGTH_SHORT).show();
            return;
        }else{
            int count=mDao.updateData(name, walk_daliy);
            if(count==-1){
                Toast.makeText(this,"更新失败",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this,"数据更新了  "+count+"   行",Toast.LENGTH_SHORT).show();
            }

        }
    }


    public void query(View view){

        String name=mEtName.getText().toString().trim();

        if(TextUtils.isEmpty(name)){
            Toast.makeText(this,"填写不完整",Toast.LENGTH_SHORT).show();
            return;
        }else{
            String result = mDao.alterData(name);

            Toast.makeText(this,"日均步数为:    "+result,Toast.LENGTH_SHORT).show();


        }
    }
}