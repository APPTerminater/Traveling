package com.tongji.lisa1225.calendartest.view;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
//import android.support.annotation.*;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bugtags.library.Bugtags;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

//import com.prolificinteractive.materialcalendarview.*;
import com.tongji.lisa1225.calendartest.R;
import com.tongji.lisa1225.calendartest.config.Constant;
import com.tongji.lisa1225.calendartest.controllor.RemindController;
import com.tongji.lisa1225.calendartest.dao.DiaryInfoDao;
import com.tongji.lisa1225.calendartest.dao.TripInfoDao;
import com.tongji.lisa1225.calendartest.dao.UserInfoDao;
import com.tongji.lisa1225.calendartest.decorator.DayModeDecorator;
import com.tongji.lisa1225.calendartest.decorator.EventDecorator;
import com.tongji.lisa1225.calendartest.decorator.NightModeDecorator;
import com.tongji.lisa1225.calendartest.model.DiaryInfo;
import com.tongji.lisa1225.calendartest.model.TripInfo;
import com.tongji.lisa1225.calendartest.service.StepService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity implements Handler.Callback {
    //日历控件
    MaterialCalendarView imcvTemMaterCalendarWeek;
    //数据库
    private UserInfoDao userDao;
    private TripInfoDao tripDao;
    private DiaryInfoDao diaryDao;
    //时间部分
    SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
    Date todayZero;//今天零点
    long today0;
    long selectday,selectmonth
            , selectyear;
    Toolbar topbar;
    //侧边栏部分
    private RelativeLayout layout;
    private LinearLayout infoLayout;
    private TextView nicknameTextview;
    private TextView birthdayTextview;
    private TextView walkTextview;
    private CheckBox modechange;
    //底边栏部分
    private Toolbar bottombar;
    private Button addBtn;
    private Button homepage;
    private Button searchBtn;
    //提醒
    RemindController remindController;
    //传来的数据
    String nickname;
    Intent get_intent;
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    //计步相关开始：循环取当前时刻的步数中间的时间间隔
    private long TIME_INTERVAL = 5000;

    private Messenger messenger;
    private Messenger mGetReplyMessenger = new Messenger(new Handler(this));
    private Handler delayHandler;

    //以bind形式开启service，故有ServiceConnection接收回调
    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                messenger = new Messenger(service);
                Message msg = Message.obtain(null, Constant.MSG_FROM_CLIENT);
                msg.replyTo = mGetReplyMessenger;
                messenger.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {}
    };

    //接收从服务端回调的步数
    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case Constant.MSG_FROM_SERVER:
                //更新步数
                DiaryInfo diaryInfo=new DiaryInfo();
                today0= todayZero.getTime()- todayZero.getTime()%1000;
                //today0=today0-172800000;
                diaryInfo= diaryDao.alterData(nickname,today0);

                if(diaryInfo.title==null&&diaryInfo.id==-1){
                    long addLong= diaryDao.insertStep(today0,nickname,msg.getData().getInt("step"));
                    if(addLong==-1){
                        Toast.makeText(this,"添加失败",Toast.LENGTH_SHORT).show();
                    }else{
                        //Toast.makeText(this,"数据添加在第  "+addLong+"   行",Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    int count;
                    if (diaryInfo.step<msg.getData().getInt("step")) {
                        count = diaryDao.updateStep(today0, nickname, msg.getData().getInt("step"));
                        if (count == -1) {
                            Toast.makeText(this, "更新失败", Toast.LENGTH_SHORT).show();
                        } else {
                            //Toast.makeText(this, "数据更新了  " + count + "   行", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                delayHandler.sendEmptyMessageDelayed(Constant.REQUEST_SERVER, TIME_INTERVAL);
                break;
            case Constant.REQUEST_SERVER:
                try {
                    Message msgl = Message.obtain(null, Constant.MSG_FROM_CLIENT);
                    msgl.replyTo = mGetReplyMessenger;
                    messenger.send(msgl);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
        }
        return false;
    }
    //计步相关结束
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化
        userDao =new UserInfoDao(MainActivity.this);
        tripDao =new TripInfoDao(MainActivity.this);
        diaryDao =new DiaryInfoDao(MainActivity.this);

        layout=(RelativeLayout)findViewById(R.id.layout);
        infoLayout=(LinearLayout)findViewById(R.id.userinfo);
        nicknameTextview=(TextView)findViewById(R.id.nickname);
        birthdayTextview=(TextView)findViewById(R.id.birthday);
        walkTextview=(TextView)findViewById(R.id.walk_daily);
        modechange=(CheckBox)findViewById(R.id.changemode);
        addBtn=(Button)findViewById(R.id.addButton);
        homepage=(Button)findViewById(R.id.shouye);
        searchBtn=(Button)findViewById(R.id.searchButton);

        topbar=(Toolbar)findViewById(R.id.activity_toolbar);
        bottombar=(Toolbar)findViewById(R.id.bottom_tool_bar);

        get_intent = getIntent();
        nickname=get_intent.getStringExtra("nickname");
        //获取今天零点的date
        TimeZone curTimeZone = TimeZone.getTimeZone("GMT+8");
        Calendar c = Calendar.getInstance(curTimeZone);
        Date d = new Date(System.currentTimeMillis());
        c.setTime(d);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        todayZero = c.getTime();

        //查询数据库
        List<TripInfo> tripInfoList=new ArrayList<>();
        List<Date> start_timeList=new ArrayList<>();
        List<Date> end_timeList=new ArrayList<>();
        List<String> remindList =new ArrayList<>();
        List<String> memoList=new ArrayList<>();

        tripInfoList= tripDao.alterData(nickname);
        for (TripInfo tripInfo:tripInfoList)
        {
            Date startDate = new Date(tripInfo.start_time);
            Date endDate = new Date(tripInfo.end_time);
            String remind=tripInfo.remind;
            String memo=tripInfo.memo;
            start_timeList.add(startDate);
            end_timeList.add(endDate);
            remindList.add(remind);
            memoList.add(memo);
        }
        //提醒功能
        remindController=new RemindController(start_timeList,remindList);
        if(remindController.shouldRemind())
        {
            AlertDialog.Builder alertdialogbuilder=new AlertDialog.Builder(this);
            if(!remindController.remindText(memoList).isEmpty())
                alertdialogbuilder.setMessage(remindController.remindText(memoList));
            else alertdialogbuilder.setMessage("您在一天内有出行计划哦！请做好准备！");
            alertdialogbuilder.setPositiveButton("再次提醒",againclick);
            alertdialogbuilder.setNegativeButton("确定", okclick);
            AlertDialog alertdialog=alertdialogbuilder.create();
            alertdialog.show();
        }

        //在日历上显示出行的日子
        EventDecorator eventDecorator=new EventDecorator(start_timeList,end_timeList);
        //处理日历
        imcvTemMaterCalendarWeek = findViewById(R.id.liView);
        imcvTemMaterCalendarWeek.state().edit().setFirstDayOfWeek(Calendar.MONDAY).commit();
        imcvTemMaterCalendarWeek.setArrowColor(R.color.black);
        imcvTemMaterCalendarWeek.addDecorator(eventDecorator);
        Calendar calendar = Calendar.getInstance();
        imcvTemMaterCalendarWeek.setSelectedDate(calendar.getTime());//当日选中
        // 设置选中日期颜色。
        imcvTemMaterCalendarWeek.setSelectionColor(getResources().getColor(R.color.ControlNormal));
        //设置日期选中时的点击事件。
        imcvTemMaterCalendarWeek.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) { //在这个方法中处理选中事件。
                long selectTime=date.getDate().getTime();
                selectday = date.getDay();
                selectmonth = date.getMonth() + 1;
                selectyear = date.getYear();
                Intent dateintent = new Intent(MainActivity.this, DateActivity.class);
                Bundle b=new Bundle();
                b.putString("name","SWWWWW");
                b.putLong("selectTime",selectTime);
                dateintent.putExtras(b);

                dateintent.putExtra("nickname",nickname);
                dateintent.putExtra("selectdate", String.valueOf(selectyear) + "年" + String.valueOf(selectmonth) + "月" + String.valueOf(selectday) + "日");
                //启动
                startActivity(dateintent);
            }
        });
        //处理日历结束
        whichMode();
        //计步相关开始
        delayHandler = new Handler(this);
        //计步相关结束
    }
    //提醒确定按钮
    private DialogInterface.OnClickListener okclick=new DialogInterface.OnClickListener()
    {
        @Override
        public void onClick(DialogInterface arg0,int arg1)
        {
            tripDao.updateRemind(nickname,remindController.getstart_dates());
            arg0.cancel();
        }
    };
    //再次提醒按钮
    private DialogInterface.OnClickListener againclick=new DialogInterface.OnClickListener()
    {
        @Override
        public void onClick(DialogInterface arg0,int arg1)
        {
            arg0.cancel();
        }
    };
    //几个按钮点击事件
    public void toAdd(View view) {
        Intent addIntent = new Intent(MainActivity.this, AddActivity.class);
        addIntent.putExtra("nickname",nickname);
        //启动
        startActivity(addIntent);
    }

    public void toGaode(View view) {
        Intent gaodeIntent = new Intent(MainActivity.this, MapActivity.class);
        gaodeIntent.putExtra("nickname",nickname);
        //启动
        startActivity(gaodeIntent);
    }

    public void toDiary(View view) {
        Intent diaryIntent = new Intent(MainActivity.this, DiaryActivity.class);
        diaryIntent.putExtra("nickname",nickname);
        //启动
        startActivity(diaryIntent);
    }

    public void toTrip(View view) {
        Intent tripIntent = new Intent(MainActivity.this, TripActivity.class);
        tripIntent.putExtra("nickname",nickname);
        //启动
        startActivity(tripIntent);
    }
    public void seemore(View view){
        infoLayout.setVisibility(View.VISIBLE);
        nicknameTextview.setText(nickname);
        birthdayTextview.setText(userDao.alterBirthday(nickname));
        walkTextview.setText(userDao.alterWalk(nickname)+"步");
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
        switch (userDao.alterMode(nickname)){
            case "day":
                userDao.updateMode(nickname,"night");
                whichMode();
                break;
            case "night":
                userDao.updateMode(nickname,"day");
                layout.setBackgroundColor(getResources().getColor(R.color.white));
                topbar.setBackgroundColor(getResources().getColor(R.color.tool_bar));
                infoLayout.setBackgroundColor(getResources().getColor(R.color.tool_bar));
                addBtn.setBackground(getResources().getDrawable(R.drawable.addst));
                homepage.setTextColor(getResources().getColor(R.color.tool_bar));
                searchBtn.setTextColor(getResources().getColor(R.color.danxiaqu2));
                bottombar.setBackgroundColor(getResources().getColor(R.color.zi));
                imcvTemMaterCalendarWeek.addDecorator(new DayModeDecorator());
                break;
        }

    }
    //按钮点击事件结束
    public void whichMode(){
        if(userDao.alterMode(nickname).equals("night")) {
            layout.setBackground(getResources().getDrawable(R.drawable.bg_xk));
            topbar.setBackgroundColor(getResources().getColor(R.color.night_toolbar));
            infoLayout.setBackgroundColor(getResources().getColor(R.color.night_toolbar));
            addBtn.setBackground(getResources().getDrawable(R.drawable.addst1));
            homepage.setTextColor(getResources().getColor(R.color.night_toolbar));
            searchBtn.setTextColor(getResources().getColor(R.color.night_danxiaqu));
            bottombar.setBackgroundColor(getResources().getColor(R.color.night_buttombar));
            imcvTemMaterCalendarWeek.addDecorator(new NightModeDecorator());
            modechange.setChecked(true);
        }
    }
    //计步相关开始
    @Override
    public void onStart() {
        super.onStart();
        setupService();
    }
    /**
     * 开启服务
     */
    private void setupService() {
        Intent intent = new Intent(this, StepService.class);
        bindService(intent, conn, Context.BIND_AUTO_CREATE);
        startService(intent);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        //取消服务绑定
        unbindService(conn);
        super.onDestroy();
    }
    //计步相关结束
    @Override
    protected void onResume() {
        super.onResume();
        //注：回调 1
        Bugtags.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //注：回调 2
        Bugtags.onPause(this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        //注：回调 3
        Bugtags.onDispatchTouchEvent(this, event);
        return super.dispatchTouchEvent(event);
    }
}