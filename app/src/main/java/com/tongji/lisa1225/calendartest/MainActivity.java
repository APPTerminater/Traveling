package com.tongji.lisa1225.calendartest;

import android.app.ActionBar;
import android.content.Intent;
//import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.prolificinteractive.materialcalendarview.*;


import android.view.*;
import android.widget.Button;
import android.view.View.OnClickListener;


import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.*;
import android.util.Log;
import android.support.annotation.*;

import java.util.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.*;

public class MainActivity extends AppCompatActivity {
    ActionBar actionBar; //声明ActionBar
    long selectday,selectmonth,selectyear;
    //@BindView(R.id.imcv_tem_mater_calendar_week)
    //MaterialCalendarView imcvTemMaterCalendarWeek=findViewById(R.id.calendarView);

    //MaterialCalendarView imcvTemMaterCalendarWeek=findViewById(R.id.imcv_tem_mater_calendar_week);
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //actionBar = getActionBar(); //得到ActionBar
        //actionBar.hide(); //隐藏ActionBar
        //新建行程按钮监听
        //view层的控件和业务层的控件，靠id关联和映射  给btn1赋值，即设置布局文件中的Button按钮id进行关联
        Button addbtn=(Button)findViewById(R.id.addButton);
        //给btn1绑定监听事件
        addbtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 给bnt1添加点击响应事件
                Intent addintent =new Intent(MainActivity.this,AddActivity.class);
                //启动
                startActivity(addintent);
            }
        });
        //新建行程按钮监听结束

        //高德搜索按钮监听
        //view层的控件和业务层的控件，靠id关联和映射  给btn1赋值，即设置布局文件中的Button按钮id进行关联
        Button btn1=(Button)findViewById(R.id.searchButton);
        //给btn1绑定监听事件
        btn1.setOnClickListener(new OnClickListener() {
            @Override
             public void onClick(View v) {
                // 给bnt1添加点击响应事件
                Intent intent =new Intent(MainActivity.this,MapActivity.class);
                //启动
                startActivity(intent);
            }
        });
        //高德搜索按钮监听结束

        //处理日历
        //MaterialCalendarView imcvTemMaterCalendarWeek=findViewById(R.id.imcv_tem_mater_calendar_week);
        MaterialCalendarView imcvTemMaterCalendarWeek=findViewById(R.id.liView);
        //imcvTemMaterCalendarWeek.state().edit() .setFirstDayOfWeek(Calendar.MONDAY) .setCalendarDisplayMode(CalendarMode.WEEKS) .commit();
        imcvTemMaterCalendarWeek.state().edit() .setFirstDayOfWeek(Calendar.MONDAY).commit();

        //imcvTemMaterCalendarWeek.setTopbarVisible(true);//隐藏标题栏和两边的箭头
        Calendar calendar = Calendar.getInstance();
        imcvTemMaterCalendarWeek.setSelectedDate(calendar.getTime());//当日选中
        // 设置选中日期颜色。
        imcvTemMaterCalendarWeek.setSelectionColor(getResources().getColor(R.color.ControlNormal));
        //设置日期选中时的点击事件。
        imcvTemMaterCalendarWeek.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) { //在这个方法中处理选中事件。
                // dealWithData(date);
                // 给bnt1添加点击响应事件
                selectday=date.getDay();
                selectmonth=date.getMonth()+1;
                selectyear=date.getYear();
                //Date nowdate = new Date(System.currentTimeMillis());
                Intent dateintent =new Intent(MainActivity.this,DateActivity.class);
                //if(nowdate.)
                dateintent.putExtra("selectdate",String.valueOf(selectyear)+"年"+String.valueOf(selectmonth)+"月"+String.valueOf(selectday)+"日");
                //启动
                startActivity(dateintent);
            }
            });
        //处理日历结束

    }
}
