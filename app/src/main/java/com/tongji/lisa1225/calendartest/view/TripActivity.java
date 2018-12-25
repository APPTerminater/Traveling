package com.tongji.lisa1225.calendartest.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.tongji.lisa1225.calendartest.R;

public class TripActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);
    }
    //几个按钮点击事件
    public void tomain(View view) {
        Intent mainintent =new Intent(TripActivity.this,MainActivity.class);
        startActivity(mainintent);
    }
    public void toGaode(View view){
        Intent gaodeIntent =new Intent(TripActivity.this,MapActivity.class);
        startActivity(gaodeIntent);
    }
    public void toAdd(View view){
        Intent addIntent =new Intent(TripActivity.this,AddActivity.class);
        startActivity(addIntent);
    }
    public void toDiary(View view){
        Intent diaryIntent =new Intent(TripActivity.this,DiaryActivity.class);
        startActivity(diaryIntent);
    }
    //按钮点击事件结束
}
