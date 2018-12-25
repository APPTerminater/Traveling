package com.tongji.lisa1225.calendartest.view;

import android.app.ActionBar;
import android.content.Intent;
//import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.prolificinteractive.materialcalendarview.*;
import com.tongji.lisa1225.calendartest.R;


import android.view.*;
import android.widget.Button;
import android.view.View.OnClickListener;


import android.support.annotation.*;

import java.util.Calendar;

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
        Intent get_intent=getIntent();//TODO 传来的昵称 日历标记、侧边栏显示
        get_intent.getStringExtra("nickname");


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
    //几个按钮点击事件
    public void toAdd(View view){
        Intent addIntent =new Intent(MainActivity.this,AddActivity.class);
        //启动
        startActivity(addIntent);
    }
    public void toGaode(View view){
        Intent gaodeIntent =new Intent(MainActivity.this,MapActivity.class);
        //启动
        startActivity(gaodeIntent);
    }
    public void toDiary(View view){
        Intent diaryIntent =new Intent(MainActivity.this,DiaryActivity.class);
        //启动
        startActivity(diaryIntent);
    }
    public void toTrip(View view){
        Intent tripIntent =new Intent(MainActivity.this,TripActivity.class);
        //启动
        startActivity(tripIntent);
    }
    //按钮点击事件结束
}
