package com.tongji.lisa1225.calendartest.dbhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHelper extends SQLiteOpenHelper {
    public MyDBHelper(Context context) {
        /**
         * 参数说明：
         *
         * 第一个参数： 上下文
         * 第二个参数：数据库的名称
         * 第三个参数：null代表的是默认的游标工厂
         * 第四个参数：是数据库的版本号  数据库只能升级,不能降级,版本号只能变大不能变小
         */
        super(context, "test.db", null, 1);

    }
    /**
     * onCreate是在数据库创建的时候调用的，主要用来初始化数据表结构和插入数据初始化的记录
     *
     * 当数据库第一次被创建的时候调用的方法,适合在这个方法里面把数据库的表结构定义出来.
     * 所以只有程序第一次运行的时候才会执行
     * 如果想再看到这个函数执行，必须写在程序然后重新安装这个app
     */

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table UserInfo (nickname varchar(20) primary key, birthday varchar(20), password varchar(20),walk_daliy varchar(20),prefer_mode varchar(20))");
        db.execSQL("create table Tripinfo (id integer primary key autoincrement,nickname varchar(20), destination varchar(20), " +
                "start_time long,end_time long,budget integer,brief_info varchar(50),remind varchar(10),memo varchar(100)," +
                "total_day integer,total_walk integer,total_cost int,rates float,comment varchar(30))");
        db.execSQL("create table DiaryInfo (id integer primary key autoincrement,time long,nickname varchar(20), temperature integer,cost integer," +
                "title varchar(20), text varchar(500),textfont varchar(20),isbold varchar(20),textsize varchar(20),textcolor varchar(20),destination varchar(20),step integer)");

    }


    /**
     * 当数据库更新的时候调用的方法
     * 这个要显示出来得在上面的super语句里面版本号发生改变时才会 打印  （super(context, "itheima.db", null, 2); ）
     * 注意，数据库的版本号只可以变大，不能变小，假设我们当前写的版本号是3，运行，然后又改成1，运行则报错。不能变小
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("alter table userinfo add account varchar(20)");
        //db.execSQL("drop table if exists tripinfo");

    }
}


