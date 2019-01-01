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
    public static final String KEY_walk="total_walk";
    public static final String KEY_cost="total_cost";
    public static final String KEY_rates="rates";
    public static final String KEY_comment="comment";
    public static final String KEY_total_day="total_day";
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
    public int total_walk;
    public int total_cost;
    public float rates;
    public String comment;
    public int total_day;

    public TripInfo(){
        id=-1;
        nickname=null;
        destination=null;
        start_time=0;
        end_time=0;
        budget=0;
        brief_info=null;
        remind=null;
        memo=null;
        comment=null;
        total_cost=0;
        total_walk=0;
        rates=0;
        total_day=1;
    }
}
