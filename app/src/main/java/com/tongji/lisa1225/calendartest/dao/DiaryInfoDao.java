package com.tongji.lisa1225.calendartest.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tongji.lisa1225.calendartest.dbhelper.MyDBHelper;
import com.tongji.lisa1225.calendartest.model.DiaryInfo;
import com.tongji.lisa1225.calendartest.model.DiaryInfo;

import java.util.ArrayList;
import java.util.List;

public class DiaryInfoDao {
    private MyDBHelper mMyDBHelper;

    /**
     * dao类需要实例化数据库Help类,只有得到帮助类的对象我们才可以实例化 SQLiteDatabase
     * @param context
     */
    public DiaryInfoDao(Context context) {
        mMyDBHelper=new MyDBHelper(context);
    }

    // 将数据库打开帮帮助类实例化，然后利用这个对象
    // 调用谷歌的api去进行增删改查

    // 增加的方法，返回的的是一个long值
    public long addData(DiaryInfo diaryInfo){
        // 增删改查每一个方法都要得到数据库，然后操作完成后一定要关闭
        // getWritableDatabase(); 执行后数据库文件才会生成
        // 数据库文件利用DDMS可以查看，在 data/data/包名/databases 目录下即可查看
        SQLiteDatabase sqLiteDatabase =  mMyDBHelper.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(DiaryInfo.KEY_time,diaryInfo.time);
        contentValues.put(DiaryInfo.KEY_nickname,diaryInfo.nickname);
        contentValues.put(DiaryInfo.KEY_temperature,diaryInfo.temperature);
        contentValues.put(DiaryInfo.KEY_cost,diaryInfo.cost);
        contentValues.put(DiaryInfo.KEY_title,diaryInfo.title);
        contentValues.put(DiaryInfo.KEY_text,diaryInfo.text);
        contentValues.put(DiaryInfo.KEY_font,diaryInfo.textfont);
        contentValues.put(DiaryInfo.KEY_bold,diaryInfo.isbold);
        contentValues.put(DiaryInfo.KEY_size,diaryInfo.textsize);
        contentValues.put(DiaryInfo.KEY_color,diaryInfo.textcolor);
        contentValues.put(DiaryInfo.KEY_destination,diaryInfo.destination);

        // 返回,显示数据添加在第几行
        // 加了现在连续添加了3行数据,突然删掉第三行,然后再添加一条数据返回的是4不是3
        // 因为自增长
        long rowid=sqLiteDatabase.insert(DiaryInfo.TABLE,null,contentValues);

        sqLiteDatabase.close();
        return rowid;
    }

    //没写日记时加步数
    public long insertStep(long time,String nickname,int step){
        SQLiteDatabase sqLiteDatabase =  mMyDBHelper.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(DiaryInfo.KEY_time,time);
        contentValues.put(DiaryInfo.KEY_nickname,nickname);
        contentValues.put(DiaryInfo.Key_step,step);
        long rowid=sqLiteDatabase.insert(DiaryInfo.TABLE,null,contentValues);

        sqLiteDatabase.close();
        return rowid;
    }
    public int updateStep(long time,String nickname,int step){
        SQLiteDatabase sqLiteDatabase =  mMyDBHelper.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(DiaryInfo.KEY_time,time);
        contentValues.put(DiaryInfo.KEY_nickname,nickname);
        contentValues.put(DiaryInfo.Key_step,step);
        int updateResult = sqLiteDatabase.update(DiaryInfo.TABLE, contentValues, "time=? and nickname=?", new String[]{String.valueOf(time),nickname});
        sqLiteDatabase.close();
        return updateResult;
    }

    // 删除的方法，返回值是int
    public int deleteData(int id,String nickname){
        SQLiteDatabase sqLiteDatabase = mMyDBHelper.getWritableDatabase();
        int deleteResult = sqLiteDatabase.delete(DiaryInfo.TABLE, "id=? and nickname=?", new String[]{String.valueOf(id),nickname});
        sqLiteDatabase.close();
        return deleteResult;
    }

    /**
     * 修改的方法
     * @param id
     * @param diaryInfo
     * @return
     * */

    public int updateData(int id,DiaryInfo diaryInfo){
        SQLiteDatabase sqLiteDatabase = mMyDBHelper.getWritableDatabase();
        ContentValues contentValues =new ContentValues();
        contentValues.put(DiaryInfo.KEY_temperature,diaryInfo.temperature);
        contentValues.put(DiaryInfo.KEY_cost,diaryInfo.cost);
        contentValues.put(DiaryInfo.KEY_title,diaryInfo.title);
        contentValues.put(DiaryInfo.KEY_text,diaryInfo.text);
        contentValues.put(DiaryInfo.KEY_font,diaryInfo.textfont);
        contentValues.put(DiaryInfo.KEY_bold,diaryInfo.isbold);
        contentValues.put(DiaryInfo.KEY_size,diaryInfo.textsize);
        contentValues.put(DiaryInfo.KEY_color,diaryInfo.textcolor);
        contentValues.put(DiaryInfo.Key_step,diaryInfo.step);
        contentValues.put(DiaryInfo.KEY_destination,diaryInfo.destination);
        int updateResult = sqLiteDatabase.update(DiaryInfo.TABLE, contentValues, "id=?", new String[]{String.valueOf(id)});
        sqLiteDatabase.close();
        return updateResult;
    }


    /**
     * 查询的方法
     * @param nickname
     * @return
     */
    public DiaryInfo alterData(String nickname,long time){
        DiaryInfo diaryInfo = new DiaryInfo();
        int count=0;
        SQLiteDatabase readableDatabase = mMyDBHelper.getReadableDatabase();
        // 查询比较特别,涉及到 cursor
        Cursor cursor = readableDatabase.query(DiaryInfo.TABLE, new String[]{DiaryInfo.KEY_id,DiaryInfo.KEY_temperature,DiaryInfo.KEY_cost,
                DiaryInfo.KEY_title,DiaryInfo.KEY_text,DiaryInfo.KEY_font,DiaryInfo.KEY_bold,DiaryInfo.KEY_size,DiaryInfo.KEY_color,
                        DiaryInfo.Key_step,DiaryInfo.KEY_destination},
                "nickname=? and time=?", new String[]{nickname,String.valueOf(time)}, null, null, null);
        if(cursor.moveToNext()) {
            diaryInfo.nickname=nickname;
            diaryInfo.time=time;
            diaryInfo.id = cursor.getInt(0); //获取第一列的值,第一列的索引从0开始
            diaryInfo.temperature = cursor.getInt(1);//获取第二列的值
            diaryInfo.cost = cursor.getInt(2);
            diaryInfo.title=cursor.getString(3);
            diaryInfo.text=cursor.getString(4);
            diaryInfo.textfont=cursor.getString(5);
            diaryInfo.isbold=cursor.getString(6);
            diaryInfo.textsize=cursor.getString(7);
            diaryInfo.textcolor=cursor.getString(8);
            diaryInfo.step=cursor.getInt(9);
            diaryInfo.destination=cursor.getString(10);
        }
        cursor.close(); // 记得关闭 cursor
        readableDatabase.close(); // 关闭数据库
        return diaryInfo;
    }
    //recyclerview 使用的查询
    public List<DiaryInfo> alterData(String nickname){
        List<DiaryInfo> diaryInfoList=new ArrayList<>();
        SQLiteDatabase readableDatabase = mMyDBHelper.getReadableDatabase();
        // 查询比较特别,涉及到 cursor
        Cursor cursor = readableDatabase.query(DiaryInfo.TABLE, new String[]{DiaryInfo.KEY_id,DiaryInfo.KEY_temperature,DiaryInfo.KEY_cost,
                        DiaryInfo.KEY_title,DiaryInfo.KEY_text,DiaryInfo.KEY_font,DiaryInfo.KEY_bold,DiaryInfo.KEY_size,
                        DiaryInfo.KEY_color,DiaryInfo.KEY_time,DiaryInfo.KEY_destination,DiaryInfo.Key_step},
                "nickname=?", new String[]{nickname}, null, null, null);
        while (cursor.moveToNext()) {
            if(cursor.getInt(0)!=-1&&cursor.getString(3)!=null) {
                DiaryInfo diaryInfo = new DiaryInfo();
                diaryInfo.nickname = nickname;
                diaryInfo.id = cursor.getInt(0); //获取第一列的值,第一列的索引从0开始
                diaryInfo.temperature = cursor.getInt(1);//获取第二列的值
                diaryInfo.cost = cursor.getInt(2);
                diaryInfo.title = cursor.getString(3);
                diaryInfo.text = cursor.getString(4);
                diaryInfo.textfont = cursor.getString(5);
                diaryInfo.isbold = cursor.getString(6);
                diaryInfo.textsize = cursor.getString(7);
                diaryInfo.textcolor = cursor.getString(8);
                diaryInfo.time = cursor.getLong(9);
                diaryInfo.destination = cursor.getString(10);
                diaryInfo.step=cursor.getInt(11);
                diaryInfoList.add(diaryInfo);
            }
        }
        cursor.close(); // 记得关闭 cursor
        readableDatabase.close(); // 关闭数据库
        return diaryInfoList;
    }

}
