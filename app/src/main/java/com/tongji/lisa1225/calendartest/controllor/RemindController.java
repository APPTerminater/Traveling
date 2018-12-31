package com.tongji.lisa1225.calendartest.controllor;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RemindController {
    private List<Date> start_dates;
    private List<String> reminds;
    private int num;

    //public RemindController() {}
    public RemindController(List<Date> start_date,List<String> remind) {
        this.start_dates = start_date;
        this.reminds=remind;
        num=0;
    }

    public boolean shouldRemind() {
        Calendar calendar = Calendar.getInstance();
        calendar.getTime().getTime();

        long currentTime = calendar.getTime().getTime();
        Date[] startArray = new Date[start_dates.size()];
        start_dates.toArray(startArray);
        String[] remindArray=new String[reminds.size()];
        reminds.toArray(remindArray);

        for (int i = 0; i < startArray.length; i++) {
            if (remindArray[i].equals("yes")&&currentTime >= (startArray[i].getTime() - 1000 * 60 * 60 * 24L)) {
                num=i;
                return true;
            }

        }
        return false;
    }

    public String remindText(List<String>memoList){
        String[] memoArray=new String[memoList.size()];
        memoList.toArray(memoArray);
        return memoArray[num];
    }

    public int getNum() {
        return num;
    }
    public Date getstart_dates(){
        Date[] startArray = new Date[start_dates.size()];
        start_dates.toArray(startArray);
        return startArray[num];
    }
}
