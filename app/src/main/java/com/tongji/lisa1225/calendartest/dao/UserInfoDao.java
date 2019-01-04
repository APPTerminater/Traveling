package com.tongji.lisa1225.calendartest.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tongji.lisa1225.calendartest.dbhelper.MyDBHelper;
import com.tongji.lisa1225.calendartest.model.UserInfo;

public class UserInfoDao {
    private MyDBHelper mMyDBHelper;

    /**
     * dao类需要实例化数据库Help类,只有得到帮助类的对象我们才可以实例化 SQLiteDatabase
     * @param context
     */
    public UserInfoDao(Context context) {
        mMyDBHelper=new MyDBHelper(context);
    }

    // 将数据库打开帮帮助类实例化，然后利用这个对象
    // 调用谷歌的api去进行增删改查
    
    // 增加的方法，返回的的是一个long值
    public long addData(String nickname,String birthday,String password,String walk_daliy){
        // 增删改查每一个方法都要得到数据库，然后操作完成后一定要关闭
        // getWritableDatabase(); 执行后数据库文件才会生成
        // 数据库文件利用DDMS可以查看，在 data/data/包名/databases 目录下即可查看
        SQLiteDatabase sqLiteDatabase =  mMyDBHelper.getWritableDatabase();
        ContentValues contentValues=new ContentValues();

        contentValues.put(UserInfo.KEY_nickname,nickname);
        contentValues.put(UserInfo.KEY_birthday,birthday );
        contentValues.put(UserInfo.KEY_password,password);
        contentValues.put(UserInfo.KEY_walk,Integer.valueOf(walk_daliy));
        contentValues.put(UserInfo.KEY_mode,"day");
        // 返回,显示数据添加在第几行
        // 加了现在连续添加了3行数据,突然删掉第三行,然后再添加一条数据返回的是4不是3
        // 因为自增长
        long rowid=sqLiteDatabase.insert(UserInfo.TABLE,null,contentValues);

        sqLiteDatabase.close();
        return rowid;
    }

    /**
     * 修改的方法
     * @param nickname
     * @param newBirthday
     * @param newPassword
     * @param newWalk
     * @return
     */
    public int updateData(String nickname,String newBirthday,String newPassword,String newWalk){
        SQLiteDatabase sqLiteDatabase = mMyDBHelper.getWritableDatabase();
        ContentValues contentValues =new ContentValues();
        contentValues.put(UserInfo.KEY_birthday,newBirthday);
        if (!newPassword.isEmpty()) {
            contentValues.put(UserInfo.KEY_password, newPassword);
        }
        contentValues.put(UserInfo.KEY_walk, newWalk);
        int updateResult = sqLiteDatabase.update(UserInfo.TABLE, contentValues, "nickname=?", new String[]{nickname});
        sqLiteDatabase.close();
        return updateResult;
    }
    /**
     * 修改的方法（修改模式）
     * @param nickname
     * @param mode
     * @return
     */
    public int updateMode(String nickname,String mode){
        SQLiteDatabase sqLiteDatabase = mMyDBHelper.getWritableDatabase();
        ContentValues contentValues =new ContentValues();
        contentValues.put(UserInfo.KEY_mode, mode);
        int updateResult = sqLiteDatabase.update(UserInfo.TABLE, contentValues, "nickname=?", new String[]{nickname});
        sqLiteDatabase.close();
        return updateResult;
    }
    /**
     * 查询的方法（查找模式）
     * @param nickname
     * @return
     */
    public String alterMode(String nickname){
        String mode = null;

        SQLiteDatabase readableDatabase = mMyDBHelper.getReadableDatabase();
        // 查询比较特别,涉及到 cursor

        Cursor cursor = readableDatabase.query(UserInfo.TABLE, new String[]{"prefer_mode"}, "nickname=?", new String[]{nickname}, null, null, null);
        if(cursor.moveToNext()){
            mode=cursor.getString(0);
        }

        cursor.close(); // 记得关闭 cursor
        readableDatabase.close(); // 关闭数据库
        return mode;
    }
    /**
     * 查询的方法（查找密码）
     * @param nickname
     * @return
     */
    public String alterPassword(String nickname){
        String password = null;

        SQLiteDatabase readableDatabase = mMyDBHelper.getReadableDatabase();
        // 查询比较特别,涉及到 cursor

        Cursor cursor = readableDatabase.query(UserInfo.TABLE, new String[]{"password"}, "nickname=?", new String[]{nickname}, null, null, null);
        if(cursor.moveToNext()){
            password=cursor.getString(0);
        }
        cursor.close(); // 记得关闭 cursor
        readableDatabase.close(); // 关闭数据库
        return password;
    }
    /**
     * 查询的方法（查找生日）
     * @param nickname
     * @return
     */
    public String alterBirthday(String nickname){
        String birthday = null;

        SQLiteDatabase readableDatabase = mMyDBHelper.getReadableDatabase();
        // 查询比较特别,涉及到 cursor

        Cursor cursor = readableDatabase.query(UserInfo.TABLE, new String[]{"birthday"}, "nickname=?", new String[]{nickname}, null, null, null);
        if(cursor.moveToNext()){
            birthday=cursor.getString(0);
        }
        cursor.close(); // 记得关闭 cursor
        readableDatabase.close(); // 关闭数据库
        return birthday;
    }
    /**
     * 查询的方法（查找步数）
     * @param nickname
     * @return
     */
    public int alterWalk(String nickname){
        int walk_daliy = 0;

        SQLiteDatabase readableDatabase = mMyDBHelper.getReadableDatabase();
        // 查询比较特别,涉及到 cursor

        Cursor cursor = readableDatabase.query(UserInfo.TABLE, new String[]{"walk_daliy"}, "nickname=?", new String[]{nickname}, null, null, null);
        if(cursor.moveToNext()){
            walk_daliy=cursor.getInt(0);
        }
        cursor.close(); // 记得关闭 cursor
        readableDatabase.close(); // 关闭数据库
        return walk_daliy;
    }

}
