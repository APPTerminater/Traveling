package com.tongji.lisa1225.calendartest.model;

public class DiaryInfo {
    //表名
    public static final String TABLE="DiaryInfo";
    // 表的各域名
    public static final String KEY_id="id";
    public static final String KEY_time="time";
    public static final String KEY_nickname="nickname";
    public static final String KEY_temperature="temperature";
    public static final String KEY_cost="cost";
    public static final String KEY_title="title";
    public static final String KEY_text="text";
    public static final String KEY_font="textfont";
    public static final String KEY_bold="isbold";
    public static final String KEY_size="textsize";
    public static final String KEY_color="textcolor";
    public static final String KEY_destination="destination";
    public static final String Key_step="step";
    public static final String Key_background="background";
    public static final String Key_picture="picture";
    //属性
    public int id;
    public long time;
    public String nickname;
    public int temperature;
    public int cost;
    public String title;
    public String text;
    public String textfont;
    public String isbold;
    public String textsize;
    public String textcolor;
    public String destination;
    public int step;
    public int background;
    public int picture;

    public DiaryInfo(){
        id=-1;
        time=0;
        nickname=null;
        temperature=0;
        cost=0;
        title=null;
        text=null;
        textfont=null;
        isbold=null;
        textsize=null;
        textcolor=null;
        destination=null;
        step=0;
        background=0;
        picture=1;
    }
}
