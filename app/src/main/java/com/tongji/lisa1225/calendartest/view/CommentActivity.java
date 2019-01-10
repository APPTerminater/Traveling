package com.tongji.lisa1225.calendartest.view;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.io.ByteArrayOutputStream;
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


import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tongji.lisa1225.calendartest.R;
import com.tongji.lisa1225.calendartest.dao.TripInfoDao;
import com.tongji.lisa1225.calendartest.model.TripInfo;

public class CommentActivity extends AppCompatActivity {
    TripInfoDao tDao;
    List<TripInfo> tripInfoList;
    String nickname;
    Intent get_intent;
    int position;
//eee

    private static final int THUMB_SIZE = 120;

    private static final String SDCARD_ROOT = Environment.getExternalStorageDirectory().getAbsolutePath();
    private  static final String sdCardPath = Environment.getExternalStorageDirectory().getPath();
    private static final String APP_ID = "wx61684d30a8561d7c";
    private static final String tag = "CommentActivity";
    IWXAPI api = null;
    private SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commment);

        //初始化
        TextView destination=(TextView)findViewById(R.id.destination);
        TextView starttime=(TextView)findViewById(R.id.starttime);
        TextView endtime=(TextView)findViewById(R.id.endtime);
        TextView step=(TextView)findViewById(R.id.step);
        TextView cost=(TextView)findViewById(R.id.cost);
        RatingBar stars=(RatingBar)findViewById(R.id.ratingBar);
        TextView comment=(TextView)findViewById(R.id.comment);

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

        Button btn = (Button) findViewById(R.id.share);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share();
            }
        });

       // IWXAPI mWxApi = WXAPIFactory.createWXAPI(this, "wx61684d30a8561d7c", true);
        //mWxApi.registerApp("wx61684d30a8561d7c");
        api = WXAPIFactory.createWXAPI(this, APP_ID, true);
        api.registerApp(APP_ID);

    }

    private void sendImg() {
        String imagePath = sdCardPath + File.separator + "screenshot.png";
        WXImageObject imgObj = new WXImageObject();
        imgObj.setImagePath(imagePath);

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;
        msg.description="my travel";

        Bitmap bmp = BitmapFactory.decodeFile(imagePath);
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
        bmp.recycle();
        msg.thumbData = bmpToByteArray(thumbBmp, true);
        msg.title="abc-title";
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = "img"+String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
        api.sendReq(req);
    }
//eee

    public static byte[] bmpToByteArray(final Bitmap bmp,
                                        final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 80, output);
        if (needRecycle) {
            bmp.recycle();
        }
        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
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
        sendImg();
    }

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
