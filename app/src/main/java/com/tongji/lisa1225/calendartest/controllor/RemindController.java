package com.tongji.lisa1225.calendartest.controllor;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RemindController {
    private List<Date> startDates;
    private List<String> reminds;
    private int num;

    public RemindController(List<Date> startDate,List<String> remind) {
        this.startDates = startDate;
        this.reminds = remind;
        num = 0;
    }

    public boolean shouldRemind() {
        Calendar calendar = Calendar.getInstance(); //获取calendar对象
        calendar.getTime().getTime();

        long currentTime = calendar.getTime().getTime();
        Date[] startArray = new Date[startDates.size()];
        startDates.toArray(startArray);
        String[] remindArray = new String[reminds.size()];
        reminds.toArray(remindArray);

        for (int i = 0; i < startArray.length; i++) {
            if (remindArray[i].equals("yes") && currentTime >= (startArray[i].getTime() - 1000 * 60 * 60 * 24L)
                    //开始的一天减掉一天跳出提示
                    && currentTime < startArray[i].getTime()) {
                num = i;
                return true;
            }

        }
        return false;
    }

    public String remindText(List<String> memoList) {
        String[] memoArray = new String[memoList.size()];
        memoList.toArray(memoArray);
        return memoArray[num];
    }

    public Date getstart_dates() {
        Date[] startArray = new Date[startDates.size()];
        startDates.toArray(startArray);
        return startArray[num];
    }
}
