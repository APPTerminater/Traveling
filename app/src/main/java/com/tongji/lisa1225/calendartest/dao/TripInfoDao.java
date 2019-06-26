package com.tongji.lisa1225.calendartest.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.tongji.lisa1225.calendartest.dbhelper.MyDBHelper;
import com.tongji.lisa1225.calendartest.model.TripInfo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TripInfoDao {
    private MyDBHelper myDbHelper;

    /*
     * dao类需要实例化数据库Help类,只有得到帮助类的对象我们才可以实例化 SQLiteDatabase
     * @param context
     */
    public TripInfoDao(Context context) {
        myDbHelper = new MyDBHelper(context);
    }

    // 将数据库打开帮帮助类实例化，然后利用这个对象
    // 调用谷歌的api去进行增删改查

    // 增加的方法，返回的的是一个long值
    public long addData(TripInfo tripInfo) {
        // 增删改查每一个方法都要得到数据库，然后操作完成后一定要关闭
        // getWritableDatabase(); 执行后数据库文件才会生成
        // 数据库文件利用DDMS可以查看，在 data/data/包名/databases 目录下即可查看

        ContentValues contentValues = new ContentValues();

        contentValues.put(TripInfo.KEY_nickname,tripInfo.nickname);
        contentValues.put(TripInfo.KEY_destination,tripInfo.destination);
        contentValues.put(TripInfo.KEY_start_time,tripInfo.start_time);
        contentValues.put(TripInfo.KEY_end_time,tripInfo.end_time);
        contentValues.put(TripInfo.KEY_budget,tripInfo.budget);
        contentValues.put(TripInfo.KEY_brief_info,tripInfo.brief_info);
        contentValues.put(TripInfo.KEY_remind,tripInfo.remind);
        if (tripInfo.total_day > 0) {
            contentValues.put(TripInfo.KEY_total_day,tripInfo.total_day);
        }

        if (!TextUtils.isEmpty(tripInfo.destination)) {
            contentValues.put(TripInfo.KEY_memo, tripInfo.memo);
        }
        // 返回,显示数据添加在第几行
        // 加了现在连续添加了3行数据,突然删掉第三行,然后再添加一条数据返回的是4不是3
        // 因为自增长
        SQLiteDatabase sqLiteDatabase =  myDbHelper.getWritableDatabase();
        long rowid = sqLiteDatabase.insert(TripInfo.TABLE,null,contentValues);

        sqLiteDatabase.close();
        return rowid;
    }

    /*
     * 修改的方法（明明用过了）
     * @param nickname
     * @param starttime
     * @return
    */
    public int updateRemind(String nickname, Date starttime) {
        SQLiteDatabase sqLiteDatabase = myDbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TripInfo.KEY_remind, "no");
        int updateResult = sqLiteDatabase.update(TripInfo.TABLE, contentValues, "nickname=? and start_time=?",
                new String[]{nickname,String.valueOf(starttime.getTime())});
        sqLiteDatabase.close();
        return updateResult;
    }

    public int updateRate(TripInfo tripInfo) {
        SQLiteDatabase sqLiteDatabase = myDbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TripInfo.KEY_comment, tripInfo.comment);
        contentValues.put(TripInfo.KEY_rates, tripInfo.rates);
        contentValues.put(TripInfo.KEY_walk,tripInfo.total_walk);
        contentValues.put(TripInfo.KEY_cost,tripInfo.total_cost);

        int updateResult = sqLiteDatabase.update(TripInfo.TABLE, contentValues, "id=?",
                new String[]{String.valueOf(tripInfo.id)});
        sqLiteDatabase.close();
        return updateResult;
    }



    /*
     * 查询的方法
     * @param nickname
     * @return
     */
    public List<TripInfo> alterData(String nickname) {
        List<TripInfo> tripInfoList = new ArrayList<>();

        int count = 0;
        SQLiteDatabase readableDatabase = myDbHelper.getReadableDatabase();
        // 查询比较特别,涉及到 cursor
        Cursor cursor = readableDatabase.query(TripInfo.TABLE,
                new String[]{TripInfo.KEY_id,TripInfo.KEY_destination,TripInfo.KEY_start_time,
                    TripInfo.KEY_end_time,TripInfo.KEY_budget,TripInfo.KEY_brief_info,TripInfo.KEY_remind,
                    TripInfo.KEY_memo, TripInfo.KEY_cost,TripInfo.KEY_total_day,TripInfo.KEY_walk,
                    TripInfo.KEY_rates,TripInfo.KEY_comment},
                "nickname=?", new String[]{nickname}, null, null, null);
        while (cursor.moveToNext()) {
            TripInfo tripInfo = new TripInfo();
            tripInfo.nickname = nickname;
            tripInfo.id = cursor.getInt(0); //获取第一列的值,第一列的索引从0开始
            tripInfo.destination = cursor.getString(1);//获取第二列的值
            tripInfo.start_time = cursor.getLong(2);
            tripInfo.end_time = cursor.getLong(3);
            tripInfo.budget = cursor.getInt(4);
            tripInfo.brief_info = cursor.getString(5);
            tripInfo.remind = cursor.getString(6);
            tripInfo.memo = cursor.getString(7);
            tripInfo.total_cost = cursor.getInt(8);
            tripInfo.total_day = cursor.getInt(9);
            tripInfo.total_walk = cursor.getInt(10);
            tripInfo.rates = cursor.getFloat(11);
            tripInfo.comment = cursor.getString(12);
            tripInfoList.add(tripInfo);
            count++;
        }
        cursor.close(); // 记得关闭 corsor
        readableDatabase.close(); // 关闭数据库
        return tripInfoList;
    }

}
