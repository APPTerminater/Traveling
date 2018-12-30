package com.tongji.lisa1225.calendartest.view;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.cloud.thirdparty.V;
import com.tongji.lisa1225.calendartest.R;
import com.tongji.lisa1225.calendartest.dao.DiaryInfoDao;
import com.tongji.lisa1225.calendartest.dao.TripInfoDao;
import com.tongji.lisa1225.calendartest.model.DiaryInfo;

public class EditDiaryActivity extends AppCompatActivity {
    private RelativeLayout decorateLayout;
    private Button decoratedBtn;
    private Button bondedBtn;
    private EditText content;  //日记内容
    private TextView showdate;//显示日期
    private TextView city; //显示城市名
    private EditText title;//标题
    private EditText temperature;//温度
    private EditText money;//开销

    TextPaint contentPaint;

    //数据库
    private DiaryInfoDao dDao;

    //保存当页信息
    String textFont="msyh";
    String textSize="middle";
    String textColor="black";
    String isBold="no";

    Intent get_intent;
    String nickname;
    String chosen_date;  //选中日期
    String cityname;
    long selectTime;
    //字体
    Typeface round=null;
    Typeface apple=null;
    Typeface kai=null;
    Typeface msyh=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editdiary);

        round=Typeface.createFromAsset(getAssets(),"font/round.ttf");
        apple=Typeface.createFromAsset(getAssets(),"font/apple.ttf");
        kai=Typeface.createFromAsset(getAssets(),"font/kai.ttf");
        msyh=Typeface.createFromAsset(getAssets(),"font/msyh.ttf");

        //寻找控件
        decorateLayout=(RelativeLayout)findViewById(R.id.decorateLayout);
        decoratedBtn=(Button)findViewById(R.id.decoratedButton);
        bondedBtn=(Button)findViewById(R.id.bujiacu);
        content=(EditText)findViewById(R.id.content);
        contentPaint = content.getPaint();
        showdate=findViewById(R.id.showdate);
        city=findViewById(R.id.city);
        title=(EditText)findViewById(R.id.title);
        temperature=(EditText)findViewById(R.id.temperature);
        money=(EditText)findViewById(R.id.money);

        //从上一个界面传来的数据
        get_intent=getIntent();
        Bundle bundle=get_intent.getExtras();
        selectTime=bundle.getLong("selectTime");
        nickname=get_intent.getStringExtra("nickname");
        chosen_date=get_intent.getStringExtra("selectdate");
        showdate.setText(chosen_date);
        cityname=get_intent.getStringExtra("cityname");
        city.setText(cityname);

        //数据库
        dDao=new DiaryInfoDao(EditDiaryActivity.this);
        DiaryInfo diaryInfo=dDao.alterData(nickname,selectTime);
        if(diaryInfo.title!=null){
            textColor=diaryInfo.textcolor;
            textSize=diaryInfo.textsize;
            textFont=diaryInfo.textfont;
            isBold=diaryInfo.isbold;
            title.setText(diaryInfo.title);
            temperature.setText(String.valueOf(diaryInfo.temperature));
            money.setText(String.valueOf(diaryInfo.cost));
            content.setText(diaryInfo.text);
        }

        //改颜色字体大小
        changeColor(textColor);
        changeFont(textFont);
        changeSize(textSize);
        changeBond(isBold);
    }

    //返回按钮
    public void back(View view){
        backToDate();
    }

    public void save(View view){
        DiaryInfo diaryInfo=new DiaryInfo();
        diaryInfo.nickname=nickname;
        diaryInfo.time=selectTime;
        diaryInfo.textcolor=textColor;
        diaryInfo.textsize=textSize;
        diaryInfo.textfont=textFont;
        diaryInfo.isbold=isBold;
        diaryInfo.title=title.getText().toString().trim();
        diaryInfo.text=content.getText().toString().trim();
        diaryInfo.temperature= Integer.parseInt(temperature.getText().toString());
        diaryInfo.cost=Integer.parseInt(money.getText().toString());
        //添加
        if(dDao.alterData(nickname,selectTime).title==null) {
            if (TextUtils.isEmpty(diaryInfo.title) || TextUtils.isEmpty(money.getText().toString().trim())
                    || TextUtils.isEmpty(temperature.getText().toString().trim()) || TextUtils.isEmpty(diaryInfo.text)) {
                Toast.makeText(this, "填写不完整", Toast.LENGTH_SHORT).show();
                return;
            } else {
                long addLong = dDao.addData(diaryInfo);
                if (addLong == -1) {
                    Toast.makeText(this, "添加失败", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "数据添加在第  " + addLong + "   行", Toast.LENGTH_SHORT).show();
                    backToDate();
                }
            }
        }
        //todo 修改
        else {
            if (TextUtils.isEmpty(diaryInfo.title) || TextUtils.isEmpty(money.getText().toString().trim())
                    || TextUtils.isEmpty(temperature.getText().toString().trim()) || TextUtils.isEmpty(diaryInfo.text)) {
                Toast.makeText(this, "填写不完整", Toast.LENGTH_SHORT).show();
                return;
            }
            else {
                int count = dDao.updateData(dDao.alterData(nickname,selectTime).id,diaryInfo);
                if (count == -1) {
                    Toast.makeText(this, "更新失败", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "数据更新了  " + count + "   行", Toast.LENGTH_SHORT).show();
                    backToDate();
                }
            }
        }

    }

    public void decorate(View view){
        decoratedBtn.setVisibility(View.VISIBLE);
        decorateLayout.setVisibility(View.VISIBLE);
    }
    public void decorated(View view){
        decoratedBtn.setVisibility(View.INVISIBLE);
        decorateLayout.setVisibility(View.INVISIBLE);
    }
    //改字体
    public void roundfont(View view){
        changeFont("round");
    }
    public void applefont(View view){ changeFont("apple"); }
    public void kaifont(View view){
        changeFont("kai");
    }
    public void changeFont(String font){
        switch (font){
            case "round":
                content.setTypeface(round);
                textFont="round";
                break;
            case "apple":
                content.setTypeface(apple);
                textFont="apple";
                break;
            case "kai":
                content.setTypeface(kai);
                textFont="kai";
                break;
            default:
                content.setTypeface(msyh);
                break;
        }
    }
    //改颜色
    public void color_black(View view){ changeColor("black"); }
    public void color_green(View view){ changeColor("green"); }
    public void color_red(View view){ changeColor("red"); }
    public void color_pink(View view){ changeColor("pink"); }
    public void color_lowgreen(View view){ changeColor("lowgreen"); }
    public void color_orange(View view){ changeColor("orange"); }
    public void changeColor(String color){
        switch (color){
            case "black":
                content.setTextColor(getResources().getColor(R.color.black));
                textColor="black";
                break;
            case "green":
                content.setTextColor(getResources().getColor(R.color.changegreen));
                textColor="green";
                break;
            case "red":
                content.setTextColor(getResources().getColor(R.color.changered));
                textColor="red";
                break;
            case "pink":
                content.setTextColor(getResources().getColor(R.color.changepink));
                textColor="pink";
                break;
            case "lowgreen":
                content.setTextColor(getResources().getColor(R.color.changelowgreen));
                textColor="lowgreen";
                break;
            case "orange":
                content.setTextColor(getResources().getColor(R.color.changeorange));
                textColor="orange";
                break;
        }
    }
    //改大小
    public void small(View view){
        changeSize("small");
    }
    public void middle(View view){ changeSize("middle"); }
    public void big(View view){ changeSize("big");}
    public void changeSize(String size){
        switch (size){
            case "small":
                content.setTextSize(TypedValue.COMPLEX_UNIT_DIP,12);
                textSize="small";
                break;
            case "middle":
                content.setTextSize(TypedValue.COMPLEX_UNIT_DIP,16);
                textSize="middle";
                break;
            case "big":
                content.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20);
                textSize="big";
                break;
        }
    }
    //加粗
    public void bond(View view){ changeBond("yes");}
    public void notbond(View view){ changeBond("no");}
    public void changeBond(String bond){
        switch (bond){
            case "yes":
                contentPaint.setFakeBoldText(true);
                bondedBtn.setVisibility(View.VISIBLE);
                isBold="yes";
                break;
            case "no":
                contentPaint.setFakeBoldText(false);
                bondedBtn.setVisibility(View.INVISIBLE);
                isBold="no";
                break;
        }
    }
    public void backToDate()
    {
        Intent dateintent =new Intent(EditDiaryActivity.this,DateActivity.class);
        Bundle b=new Bundle();
        b.putString("name","SWWWWW");
        b.putLong("selectTime",selectTime);
        dateintent.putExtras(b);
        dateintent.putExtra("selectdate",chosen_date);
        dateintent.putExtra("nickname",nickname);
        //启动
        startActivity(dateintent);
    }
}
