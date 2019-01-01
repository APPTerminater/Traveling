package com.tongji.lisa1225.calendartest.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.tongji.lisa1225.calendartest.R;
import com.tongji.lisa1225.calendartest.dao.TripInfoDao;
import com.tongji.lisa1225.calendartest.model.TripInfo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddActivity extends AppCompatActivity implements View.OnClickListener, OnDateSetListener{
    String nickname;
    Intent get_intent;
    private TripInfoDao mDao;

    EditText destination;
    EditText money;
    EditText info;
    EditText memo;
    private CheckBox isremind,isremind2;
    private RelativeLayout addTripLayout,addReminderLayout;
    private Button tripBtn,reminderBtn;
    //private RelativeLayout selectDate, selectTime,selectDate2, selectTime2;
    private TextView currentDate, currentTime,currentDate2, currentTime2;

    private TimePickerDialog mDialogAll,mDialogAll2,mDialogYearMonthDay;

    long tenYears = 10L * 365 * 1000 * 60 * 60 * 24L;

    //测试日期有效性
    long starttime,backtime;

    SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd HH:mm");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        get_intent = getIntent();
        nickname=get_intent.getStringExtra("nickname");

        mDao=new TripInfoDao(AddActivity.this);

        destination=(EditText)findViewById(R.id.destination);
        money=(EditText)findViewById(R.id.money);
        info=(EditText)findViewById(R.id.info);
        memo=(EditText)findViewById(R.id.memo);
        isremind=(CheckBox)findViewById(R.id.isremind);
        isremind2=(CheckBox)findViewById(R.id.isremind2);
        currentTime = (TextView) findViewById(R.id.goTime);
        currentTime.setOnClickListener(this);
        currentTime2 = (TextView) findViewById(R.id.backTime);
        currentTime2.setOnClickListener(this);
        addTripLayout=(RelativeLayout)findViewById(R.id.addTripLayout);
        addReminderLayout=(RelativeLayout)findViewById(R.id.addReminderLayout);
        tripBtn=(Button)findViewById(R.id.tripButton);
        reminderBtn=(Button)findViewById(R.id.reminderButton);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.goTime:
                mDialogAll = new TimePickerDialog.Builder()
                        .setCallBack(this)
                        .setCancelStringId("取消")
                        .setSureStringId("确定")
                        .setTitleStringId("出发时间")
                        .setYearText("年")
                        .setMonthText("月")
                        .setDayText("日")
                        .setHourText("时")
                        .setMinuteText("分")
                        .setCyclic(false)
                        .setMinMillseconds(System.currentTimeMillis() - tenYears)
                        .setMaxMillseconds(System.currentTimeMillis() + tenYears)
                        .setCurrentMillseconds(System.currentTimeMillis())
                        .setThemeColor(getResources().getColor(R.color.colorPrimary))
                        .setType(Type.ALL)
                        .setWheelItemTextNormalColor(getResources().getColor(R.color.colorPrimary))
                        .setWheelItemTextSelectorColor(getResources().getColor(R.color.colorPrimary))
                        .setWheelItemTextSize(12)
                        .build();

                mDialogAll.show(getSupportFragmentManager(), "time");
                break;

            case R.id.backTime:
                mDialogAll2 = new TimePickerDialog.Builder()
                        .setCallBack(new OnDateSetListener() {
                            @Override
                            public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
                                String text = getDateToString(millseconds);

                                backtime=millseconds;
                                if(!dateWarning(2))
                                {
                                    currentTime2.setText(text);
                                }
                            }
                        })
                        .setCancelStringId("取消")
                        .setSureStringId("确定")
                        .setTitleStringId("回程时间")
                        .setYearText("年")
                        .setMonthText("月")
                        .setDayText("日")
                        .setHourText("时")
                        .setMinuteText("分")
                        .setCyclic(false)
                        .setMinMillseconds(System.currentTimeMillis() - tenYears)
                        .setMaxMillseconds(System.currentTimeMillis() + tenYears)
                        .setCurrentMillseconds(System.currentTimeMillis())
                        .setThemeColor(getResources().getColor(R.color.colorPrimary))
                        .setType(Type.ALL)
                        .setWheelItemTextNormalColor(getResources().getColor(R.color.colorPrimary))
                        .setWheelItemTextSelectorColor(getResources().getColor(R.color.colorPrimary))
                        .setWheelItemTextSize(12)
                        .build();

                mDialogAll2.show(getSupportFragmentManager(), "time");
                break;
        }
    }

    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
        String text = getDateToString(millseconds);

        starttime=millseconds;
        if(!dateWarning(1))
        {
            currentTime.setText(text);
        }

    }

    public String getDateToString(long time) {
        Date d = new Date(time);
        return sf.format(d);
    }
    //判断出发时间是否晚于回程时间
    public boolean dateWarning(int num){
        if(starttime*backtime!=0)
        {
            if(backtime<starttime)
            {
                switch (num)
                {
                    case 1:
                        starttime=0;
                        break;
                    case 2:
                        backtime=0;
                        break;
                }
                AlertDialog.Builder alertdialogbuilder=new AlertDialog.Builder(this);
                alertdialogbuilder.setMessage("出发时间不能晚于回程时间！请重新选择！");
                alertdialogbuilder.setPositiveButton("确定", okclick);
                AlertDialog alertdialog1=alertdialogbuilder.create();
                alertdialog1.show();
                return true;
            }
        }
        return false;
    }
    private DialogInterface.OnClickListener okclick=new DialogInterface.OnClickListener()
    {
        @Override
        public void onClick(DialogInterface arg0,int arg1)
        {
            arg0.cancel();
        }
    };
    //按钮点击事件
    public void tomain(View view){
        Intent mainintent =new Intent(AddActivity.this,MainActivity.class);
        mainintent.putExtra("nickname",nickname);
        //启动
        startActivity(mainintent);
    }
    public void toReminder(View view){
        addTripLayout.setVisibility(View.GONE);
        addReminderLayout.setVisibility(View.VISIBLE);
        tripBtn.setTextColor(getResources().getColor(R.color.danxiaqu));
        reminderBtn.setTextColor(getResources().getColor(R.color.PureWhite));
    }
    public void toTrip(View view){
        addReminderLayout.setVisibility(View.GONE);
        addTripLayout.setVisibility(View.VISIBLE);
        reminderBtn.setTextColor(getResources().getColor(R.color.danxiaqu));
        tripBtn.setTextColor(getResources().getColor(R.color.PureWhite));
    }
    public void submit(View view){
        TripInfo tripInfo=new TripInfo();
        tripInfo.nickname=nickname;
        tripInfo.destination=destination.getText().toString().trim();
        tripInfo.start_time=starttime;
        tripInfo.end_time=backtime;
        if(!TextUtils.isEmpty(money.getText().toString()))
        tripInfo.budget=Integer.parseInt(money.getText().toString());
        tripInfo.brief_info=info.getText().toString().trim();
        if(isremind.isChecked())
        tripInfo.remind="yes";
        else tripInfo.remind="no";
        tripInfo.memo=memo.getText().toString().trim();
        tripInfo.total_day=countDay();

        if(TextUtils.isEmpty(tripInfo.destination)||TextUtils.isEmpty(money.getText().toString().trim())
                ||TextUtils.isEmpty(tripInfo.brief_info)||tripInfo.start_time*tripInfo.end_time==0){
            Toast.makeText(this,"填写不完整",Toast.LENGTH_SHORT).show();
            return;
        }else{
            long addLong = mDao.addData(tripInfo);
            if(addLong==-1){
                Toast.makeText(this,"添加失败",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this,"数据添加在第  "+addLong+"   行",Toast.LENGTH_SHORT).show();
            }

        }

        Intent mainintent =new Intent(AddActivity.this,MainActivity.class);
        mainintent.putExtra("nickname",nickname);
        //启动
        startActivity(mainintent);
    }
    public int countDay(){
        Date startDate=new Date(starttime);
        Date endDate=new Date(backtime);
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.setTime(startDate);
        fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
        fromCalendar.set(Calendar.MINUTE, 0);
        fromCalendar.set(Calendar.SECOND, 0);
        fromCalendar.set(Calendar.MILLISECOND, 0);

        Calendar toCalendar = Calendar.getInstance();
        toCalendar.setTime(endDate);
        toCalendar.set(Calendar.HOUR_OF_DAY, 0);
        toCalendar.set(Calendar.MINUTE, 0);
        toCalendar.set(Calendar.SECOND, 0);
        toCalendar.set(Calendar.MILLISECOND, 0);

        return (int) ((toCalendar.getTime().getTime() - fromCalendar.getTime().getTime()) / (1000 * 60 * 60 * 24)+1);
    }
    //两个按钮的联动
    public void remindBtn(View view){
        if (isremind.isChecked())
            isremind2.setChecked(true);
        else isremind2.setChecked(false);
    }
    public void remindBtn2(View view){
        if(isremind2.isChecked())
            isremind.setChecked(true);
        else isremind.setChecked(false);
    }

}



