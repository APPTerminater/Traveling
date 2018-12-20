package com.tongji.lisa1225.calendartest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddActivity extends AppCompatActivity implements View.OnClickListener, OnDateSetListener{

    private RelativeLayout selectDate, selectTime,selectDate2, selectTime2;
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

        selectTime = (RelativeLayout) findViewById(R.id.selectTime);
        selectTime.setOnClickListener(this);
        // selectDate = (RelativeLayout) findViewById(R.id.selectDate);
        // selectDate.setOnClickListener(this);
        // currentDate = (TextView) findViewById(R.id.currentDate);
        currentTime = (TextView) findViewById(R.id.currentTime);
        selectTime2 = (RelativeLayout) findViewById(R.id.selectTime2);
        selectTime2.setOnClickListener(this);
        // selectDate2 = (RelativeLayout) findViewById(R.id.selectDate2);
        // selectDate2.setOnClickListener(this);
        // currentDate2 = (TextView) findViewById(R.id.currentDate2);
        currentTime2 = (TextView) findViewById(R.id.currentTime2);

        //返回按钮监听
        //view层的控件和业务层的控件，靠id关联和映射  给btn赋值，即设置布局文件中的Button按钮id进行关联
        Button backbtn=(Button)findViewById(R.id.backButton);
        //给btn1绑定监听事件
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 给bnt1添加点击响应事件
                Intent addintent =new Intent(AddActivity.this,MainActivity.class);
                //启动
                startActivity(addintent);
            }
        });
        //返回按钮监听结束
        //提交按钮监听
        //view层的控件和业务层的控件，靠id关联和映射  给btn赋值，即设置布局文件中的Button按钮id进行关联
        Button submitbtn=(Button)findViewById(R.id.submitButton);
        //给btn1绑定监听事件
        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 给bnt1添加点击响应事件
                Intent addintent =new Intent(AddActivity.this,MainActivity.class);
                //启动
                startActivity(addintent);
            }
        });
        //提交按钮监听结束
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            /*case R.id.selectDate:
                mDialogYearMonthDay = new TimePickerDialog.Builder()
                        .setType(Type.YEAR_MONTH_DAY)
                        .setCallBack(new OnDateSetListener() {
                            @Override
                            public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
                                String text = getDateToString(millseconds);
                                currentDate.setText(text);
                            }
                        })
                        .setCancelStringId("取消")
                        .setSureStringId("确定")
                        .setTitleStringId("出发日期")
                        .setYearText("年")
                        .setMonthText("月")
                        .setDayText("日")
                        .setCyclic(false)
                        .setMinMillseconds(System.currentTimeMillis())
                        .setMaxMillseconds(System.currentTimeMillis() + tenYears)
                        .setCurrentMillseconds(System.currentTimeMillis())
                        .setThemeColor(getResources().getColor(R.color.colorAdd))
                        .setWheelItemTextNormalColor(getResources().getColor(R.color.colorAdd))
                        .setWheelItemTextSelectorColor(getResources().getColor(R.color.colorAdd))
                        .setWheelItemTextSize(12)
                        .build();

                mDialogYearMonthDay.show(getSupportFragmentManager(),"date");
                break;*/

            case R.id.selectTime:
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
                        .setThemeColor(getResources().getColor(R.color.colorAdd))
                        .setType(Type.ALL)
                        .setWheelItemTextNormalColor(getResources().getColor(R.color.colorAdd))
                        .setWheelItemTextSelectorColor(getResources().getColor(R.color.colorAdd))
                        .setWheelItemTextSize(12)
                        .build();

                mDialogAll.show(getSupportFragmentManager(), "time");
                break;

            case R.id.selectTime2:
                mDialogAll2 = new TimePickerDialog.Builder()
                        .setCallBack(new OnDateSetListener() {
                            @Override
                            public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
                                String text = getDateToString(millseconds);

                                backtime=millseconds;
                                if(!DateWarning(2))
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
                        .setThemeColor(getResources().getColor(R.color.colorAdd))
                        .setType(Type.ALL)
                        .setWheelItemTextNormalColor(getResources().getColor(R.color.colorAdd))
                        .setWheelItemTextSelectorColor(getResources().getColor(R.color.colorAdd))
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
        if(!DateWarning(1))
        {
            currentTime.setText(text);
        }

    }

    public String getDateToString(long time) {
        Date d = new Date(time);
        return sf.format(d);
    }
    //判断出发时间是否晚于回程时间
    public boolean DateWarning(int num){
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

}



