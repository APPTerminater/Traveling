package com.tongji.lisa1225.calendartest.model;

public class TripInfo {
    //表名
    public static final String TABLE="TripInfo";
    // 表的各域名
    public static final String KEY_id="id";
    public static final String KEY_nickname="nickname";
    public static final String KEY_destination="destination";
    public static final String KEY_start_time="start_time";
    public static final String KEY_end_time="end_time";
    public static final String KEY_budget="budget";
    public static final String KEY_brief_info="brief_info";
    public static final String KEY_remind="remind";
    public static final String KEY_memo="memo";
    //属性
    public int id;
    public String nickname;
    public String destination;
    public long start_time;
    public long end_time;
    public int budget;
    public String brief_info;
    public String remind;
    public String memo;


    public TripInfo(){

        nickname=null;
        destination=null;
        start_time=0;
        end_time=0;
        budget=0;
        brief_info=null;
        remind=null;
        memo=null;
    }
}
