package com.tongji.lisa1225.calendartest.view;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextPaint;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iflytek.cloud.thirdparty.V;
import com.tongji.lisa1225.calendartest.R;

public class EditDiaryActivity extends AppCompatActivity {
    private RelativeLayout decorateLayout;
    private Button decoratedBtn;
    private Button bondedBtn;
    private EditText content;
    TextPaint contentPaint;

    String chosen_date;  //选中日期
    //字体
    Typeface round=null;
    Typeface apple=null;
    Typeface kai=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editdiary);

        round=Typeface.createFromAsset(getAssets(),"font/round.ttf");
        apple=Typeface.createFromAsset(getAssets(),"font/apple.ttf");
        kai=Typeface.createFromAsset(getAssets(),"font/kai.ttf");

        decorateLayout=(RelativeLayout)findViewById(R.id.decorateLayout);

        decoratedBtn=(Button)findViewById(R.id.decoratedButton);
        bondedBtn=(Button)findViewById(R.id.bujiacu);
        content=(EditText)findViewById(R.id.content);
        contentPaint = content.getPaint();
        Intent get_intent=getIntent();
        TextView showdate=findViewById(R.id.showdate);
        chosen_date=get_intent.getStringExtra("selectdate");
        showdate.setText(get_intent.getStringExtra("selectdate"));
    }

    //返回按钮
    public void back(View view){
        Intent dateintent =new Intent(EditDiaryActivity.this,DateActivity.class);
        dateintent.putExtra("selectdate",chosen_date);
        //启动
        startActivity(dateintent);
    }
    //TODO 保存按钮

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
        content.setTypeface(round);
    }
    public void applefont(View view){
        content.setTypeface(apple);
    }
    public void kaifont(View view){
        content.setTypeface(kai);
    }
    //改颜色
    public void color_black(View view){
        content.setTextColor(getResources().getColor(R.color.black));
    }
    public void color_green(View view){
        content.setTextColor(getResources().getColor(R.color.changegreen));
    }
    public void color_red(View view){
        content.setTextColor(getResources().getColor(R.color.changered));
    }
    public void color_pink(View view){
        content.setTextColor(getResources().getColor(R.color.changepink));
    }
    public void color_lowgreen(View view){
        content.setTextColor(getResources().getColor(R.color.changelowgreen));
    }
    public void color_orange(View view){
        content.setTextColor(getResources().getColor(R.color.changeorange));
    }
    //改大小
    public void small(View view){
        content.setTextSize(TypedValue.COMPLEX_UNIT_DIP,12);
    }
    public void middle(View view){
        content.setTextSize(TypedValue.COMPLEX_UNIT_DIP,16);
    }
    public void big(View view){
        content.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20);
    }
    //加粗
    public void bond(View view){

        contentPaint.setFakeBoldText(true);
        bondedBtn.setVisibility(View.VISIBLE);
    }
    public void notbond(View view){

        contentPaint.setFakeBoldText(false);
        bondedBtn.setVisibility(View.INVISIBLE);
    }

}
