package com.tongji.lisa1225.calendartest.view;

import android.app.ActionBar;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.prolificinteractive.materialcalendarview.*;
import com.tongji.lisa1225.calendartest.R;
import com.tongji.lisa1225.calendartest.dao.UserInfoDao;


import android.support.v7.widget.Toolbar;
import android.view.*;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.LinearLayout.LayoutParams;
import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.LinearLayout;

import android.support.annotation.*;
import android.widget.TextView;
//import android.widget.Toolbar;


import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private UserInfoDao mDao;
    long selectday, selectmonth, selectyear;
    Toolbar topbar;
    //侧边栏部分
    private LinearLayout infoLayout;
    private TextView nicknameTextview;
    private TextView birthdayTextview;
    private TextView walkTextview;
    private CheckBox modechange;
    String nickname;
    Intent get_intent;
    //@BindView(R.id.imcv_tem_mater_calendar_week)
    //MaterialCalendarView imcvTemMaterCalendarWeek=findViewById(R.id.calendarView);

    //MaterialCalendarView imcvTemMaterCalendarWeek=findViewById(R.id.imcv_tem_mater_calendar_week);
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDao=new UserInfoDao(MainActivity.this);

        infoLayout=(LinearLayout)findViewById(R.id.userinfo);
        nicknameTextview=(TextView)findViewById(R.id.nickname);
        birthdayTextview=(TextView)findViewById(R.id.birthday);
        walkTextview=(TextView)findViewById(R.id.walk_daily);
        modechange=(CheckBox)findViewById(R.id.changemode);

        topbar=(Toolbar)findViewById(R.id.activity_toolbar);

        get_intent = getIntent();//TODO 传来的昵称 日历标记、侧边栏显示
        nickname=get_intent.getStringExtra("nickname");
        whichMode();

        //处理日历
        //MaterialCalendarView imcvTemMaterCalendarWeek=findViewById(R.id.imcv_tem_mater_calendar_week);
        MaterialCalendarView imcvTemMaterCalendarWeek = findViewById(R.id.liView);
        //imcvTemMaterCalendarWeek.state().edit() .setFirstDayOfWeek(Calendar.MONDAY) .setCalendarDisplayMode(CalendarMode.WEEKS) .commit();
        imcvTemMaterCalendarWeek.state().edit().setFirstDayOfWeek(Calendar.MONDAY).commit();

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
                selectday = date.getDay();
                selectmonth = date.getMonth() + 1;
                selectyear = date.getYear();
                //Date nowdate = new Date(System.currentTimeMillis());
                Intent dateintent = new Intent(MainActivity.this, DateActivity.class);
                //if(nowdate.)
                dateintent.putExtra("selectdate", String.valueOf(selectyear) + "年" + String.valueOf(selectmonth) + "月" + String.valueOf(selectday) + "日");
                //启动
                startActivity(dateintent);
            }
        });
        //处理日历结束
    }

    //几个按钮点击事件
    public void toAdd(View view) {
        Intent addIntent = new Intent(MainActivity.this, AddActivity.class);
        //启动
        startActivity(addIntent);
    }

    public void toGaode(View view) {
        Intent gaodeIntent = new Intent(MainActivity.this, MapActivity.class);
        //启动
        startActivity(gaodeIntent);
    }

    public void toDiary(View view) {
        Intent diaryIntent = new Intent(MainActivity.this, DiaryActivity.class);
        //启动
        startActivity(diaryIntent);
    }

    public void toTrip(View view) {
        Intent tripIntent = new Intent(MainActivity.this, TripActivity.class);
        //启动
        startActivity(tripIntent);
    }
    public void seemore(View view){
        infoLayout.setVisibility(View.VISIBLE);
        nicknameTextview.setText(nickname);
        birthdayTextview.setText(mDao.alterBirthday(nickname));
        walkTextview.setText(mDao.alterWalk(nickname)+"步");
    }
    public void seeless(View view){
        infoLayout.setVisibility(View.INVISIBLE);
    }
    public void toEditInfo(View view){
        Intent editinfoIntent = new Intent(MainActivity.this, EditInfoActivity.class);
        editinfoIntent.putExtra("nickname",nickname);
        //启动
        startActivity(editinfoIntent);
    }
    public void changeMode(View view){
        switch (mDao.alterMode(nickname)){
            case "day":
                mDao.updateMode(nickname,"night");
                topbar.setBackgroundColor(getResources().getColor(R.color.black));
                break;
            case "night":
                mDao.updateMode(nickname,"day");
                topbar.setBackgroundColor(getResources().getColor(R.color.tool_bar));
                break;
        }

    }
    //按钮点击事件结束
    public void whichMode(){
        switch (mDao.alterMode(nickname)){
            case "day":
                break;
            case "night":
                topbar.setBackgroundColor(getResources().getColor(R.color.black));
                infoLayout.setBackgroundColor(getResources().getColor(R.color.black));
                modechange.setChecked(true);
                break;
        }
    }

}