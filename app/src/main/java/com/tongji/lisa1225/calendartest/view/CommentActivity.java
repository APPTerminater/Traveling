package com.tongji.lisa1225.calendartest.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.graphics.Bitmap;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;


import com.tongji.lisa1225.calendartest.R;
import com.tongji.lisa1225.calendartest.dao.TripInfoDao;
import com.tongji.lisa1225.calendartest.model.TripInfo;

public class CommentActivity extends AppCompatActivity {
    TripInfoDao tDao;
    List<TripInfo> tripInfoList;
    String nickname;
    Intent get_intent;
    int position;

    private SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd HH:mm");


    private TextView destination;
    private TextView starttime;
    private TextView endtime;
    private TextView step;
    private TextView cost;
    private RatingBar stars;
    private TextView comment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commment);

        destination=(TextView)findViewById(R.id.destination);
        starttime=(TextView)findViewById(R.id.starttime);
        endtime=(TextView)findViewById(R.id.endtime);
        step=(TextView)findViewById(R.id.step);
        cost=(TextView)findViewById(R.id.cost);
        stars=(RatingBar)findViewById(R.id.ratingBar);
        comment=(TextView)findViewById(R.id.comment);

        get_intent = getIntent();//传来的昵称
        nickname=get_intent.getStringExtra("nickname");
        position=get_intent.getIntExtra("position",0);
        tDao=new TripInfoDao(CommentActivity.this);
        tripInfoList = tDao.alterData(nickname);
        TripInfo[] tripInfoArray=new TripInfo[tripInfoList.size()];
        tripInfoList.toArray(tripInfoArray);

        destination.setText("本次"+tripInfoArray[position].destination+"之行：");
        starttime.setText("启程于： "+getDateToString(tripInfoArray[position].start_time));
        endtime.setText("回程于： "+getDateToString(tripInfoArray[position].end_time));
        step.setText(String.valueOf(tripInfoArray[position].total_walk)+"步");
        cost.setText(String.valueOf(tripInfoArray[position].total_cost)+"元");
        stars.setRating(tripInfoArray[position].rates);
        comment.setText(tripInfoArray[position].comment);
        //tripInfoList[position];


       Button btn = (Button) findViewById(R.id.share);
       btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                share();
            }
        });
    }
    //几个按钮点击事件
    public void share() {
        View dView = getWindow().getDecorView();
        dView.setDrawingCacheEnabled(true);
        dView.buildDrawingCache();
        Bitmap bmp = dView.getDrawingCache();
        if (bmp != null)
        {
            try {
                // 获取内置SD卡路径
                String sdCardPath = Environment.getExternalStorageDirectory().getPath();
                // 图片文件路径
                String filePath = sdCardPath + File.separator + "screenshot.png";

                File file = new File(filePath);
                FileOutputStream os = new FileOutputStream(file);
                bmp.compress(Bitmap.CompressFormat.PNG, 100, os);
                os.flush();
                os.close();
            } catch (Exception e) {
                Toast tot = Toast.makeText(
                        CommentActivity.this,
                        "异常",
                        Toast.LENGTH_LONG);
                tot.show();
            }
        }
    }
   /* public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_commment, menu);
        return true;
    }*/
   public void toTrip(View view) {
       Intent tripIntent = new Intent(CommentActivity.this, TripActivity.class);
       tripIntent.putExtra("nickname",nickname);
       //启动
       startActivity(tripIntent);
   }

    public String getDateToString(long time) {
        Date d = new Date(time);
        return sf.format(d);
    }
}
