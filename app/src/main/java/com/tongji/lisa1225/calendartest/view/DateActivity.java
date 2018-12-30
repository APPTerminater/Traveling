package com.tongji.lisa1225.calendartest.view;//todo 需要更换字体、颜色等

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.text.TextPaint;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tongji.lisa1225.calendartest.R;
import com.tongji.lisa1225.calendartest.config.Constant;
import com.tongji.lisa1225.calendartest.controllor.CityController;
import com.tongji.lisa1225.calendartest.dao.DiaryInfoDao;
import com.tongji.lisa1225.calendartest.dao.TripInfoDao;
import com.tongji.lisa1225.calendartest.model.DiaryInfo;
import com.tongji.lisa1225.calendartest.model.TripInfo;
import com.tongji.lisa1225.calendartest.service.StepService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DateActivity extends AppCompatActivity implements Handler.Callback {
    //数据库
    private TripInfoDao tDao;
    private DiaryInfoDao dDao;
    CityController cityController;
    //传来的数据
    String nickname;
    Intent get_intent;
    long selectTime;
    String chosen_date;  //选中日期

    //控件
    private TextView city; //显示城市名
    private TextView text_step;    //显示走的步数
    private TextView content;  //日记内容
    private TextView showdate;//显示日期
    private TextView title;//标题
    private TextView temperature;//温度
    private TextView money;//开销

    //默认信息
    String textFont="msyh";
    String textSize="middle";
    String textColor="black";
    String isBold="no";
    //字体
    Typeface round=null;
    Typeface apple=null;
    Typeface kai=null;
    Typeface msyh=null;

    TextPaint contentPaint;
    //计步相关开始：循环取当前时刻的步数中间的时间间隔
    private long TIME_INTERVAL = 500;


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
                text_step.setText(msg.getData().getInt("step") + "");
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
        setContentView(R.layout.activity_date);
        //字体初始化
        round=Typeface.createFromAsset(getAssets(),"font/round.ttf");
        apple=Typeface.createFromAsset(getAssets(),"font/apple.ttf");
        kai=Typeface.createFromAsset(getAssets(),"font/kai.ttf");
        msyh=Typeface.createFromAsset(getAssets(),"font/msyh.ttf");
        //初始化数据库
        dDao=new DiaryInfoDao(DateActivity.this);
        tDao=new TripInfoDao(DateActivity.this);
        //寻找控件
        city=findViewById(R.id.city);
        showdate=findViewById(R.id.showdate);
        content=(TextView) findViewById(R.id.content);
        contentPaint = content.getPaint();
        title=(TextView) findViewById(R.id.title);
        temperature=(TextView) findViewById(R.id.temperature);
        money=(TextView) findViewById(R.id.money);

        //传来的数据
        get_intent = getIntent();
        Bundle bundle=get_intent.getExtras();
        selectTime=bundle.getLong("selectTime");
        // 传来的昵称
        nickname=get_intent.getStringExtra("nickname");
        //获取日期
        chosen_date=get_intent.getStringExtra("selectdate");
        showdate.setText(chosen_date);
        //显示城市
        List<TripInfo> tripInfoList=new ArrayList<>();
        List<Date> start_timeList=new ArrayList<>();
        List<Date> end_timeList=new ArrayList<>();
        List<String> cityList =new ArrayList<>();

        tripInfoList=tDao.alterData(nickname);
        for (TripInfo tripInfo:tripInfoList)
        {
            Date startDate = new Date(tripInfo.start_time);
            Date endDate = new Date(tripInfo.end_time);
            String city=new String(tripInfo.destination);
            start_timeList.add(startDate);
            end_timeList.add(endDate);
            cityList.add(city);
        }
        cityController=new CityController(start_timeList,end_timeList,cityList);
        if(cityController.inTravel(selectTime)){
            city.setText(cityController.whichCity());
        }
        else city.setText(R.string.notrip);

        //数据库
        DiaryInfo diaryInfo=new DiaryInfo();
        diaryInfo=dDao.alterData(nickname,selectTime);

        if(diaryInfo.title!=null){
            textColor=diaryInfo.textcolor;
            textSize=diaryInfo.textsize;
            textFont=diaryInfo.textfont;
            isBold=diaryInfo.isbold;
            title.setText(diaryInfo.title);
            temperature.setText(String.valueOf(diaryInfo.temperature+"℃"));
            money.setText(String.valueOf(diaryInfo.cost+"元"));
            content.setText(diaryInfo.text);
            changeColor(textColor);
            changeFont(textFont);
            changeSize(textSize);
            changeBond(isBold);
        }
        //改颜色字体大小


        //计步相关开始
        text_step = (TextView) findViewById(R.id.main_text_step);
        delayHandler = new Handler(this);
        //计步相关结束

    }
    public void changeFont(String font){
        switch (font){
            case "round":
                content.setTypeface(round);
                break;
            case "apple":
                content.setTypeface(apple);
                break;
            case "kai":
                content.setTypeface(kai);
                break;
            default:
                content.setTypeface(msyh);
                break;
        }
    }
    public void changeColor(String color){
        switch (color){
            case "black":
                content.setTextColor(getResources().getColor(R.color.black));
                break;
            case "green":
                content.setTextColor(getResources().getColor(R.color.changegreen));
                break;
            case "red":
                content.setTextColor(getResources().getColor(R.color.changered));
                break;
            case "pink":
                content.setTextColor(getResources().getColor(R.color.changepink));
                break;
            case "lowgreen":
                content.setTextColor(getResources().getColor(R.color.changelowgreen));
                break;
            case "orange":
                content.setTextColor(getResources().getColor(R.color.changeorange));
                break;
        }
    }
    public void changeSize(String size){
        switch (size){
            case "small":
                content.setTextSize(TypedValue.COMPLEX_UNIT_DIP,12);
                break;
            case "middle":
                content.setTextSize(TypedValue.COMPLEX_UNIT_DIP,16);
                break;
            case "big":
                content.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20);
                break;
        }
    }
    public void changeBond(String bond){
        switch (bond){
            case "yes":
                contentPaint.setFakeBoldText(true);
                break;
            case "no":
                contentPaint.setFakeBoldText(false);
                break;
        }
    }
    public void back(View view){
        Intent mainintent =new Intent(DateActivity.this,MainActivity.class);
        //启动
        mainintent.putExtra("nickname",nickname);
        startActivity(mainintent);
    }
    public void edit(View view){
        Intent editintent =new Intent(DateActivity.this,EditDiaryActivity.class);
        Bundle b=new Bundle();
        b.putString("name","SWWWWW");
        b.putLong("selectTime",selectTime);
        editintent.putExtras(b);
        editintent.putExtra("nickname",nickname);
        editintent.putExtra("selectdate",chosen_date);
        editintent.putExtra("cityname",city.getText());
        //启动
        startActivity(editintent);
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
}
