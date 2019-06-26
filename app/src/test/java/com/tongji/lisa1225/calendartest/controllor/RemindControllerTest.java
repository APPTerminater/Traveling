package com.tongji.lisa1225.calendartest.controllor;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class RemindControllerTest {

    @Test
    public void shouldRemind() {
        List<Date> start_timeList = new ArrayList<>();
        List<String> remindList = new ArrayList<>();
        Date startDate = new Date(1561466844178L);
        String remind= "yes";
        start_timeList.add(startDate);
        remindList.add(remind);
        RemindController remindController = new RemindController(start_timeList,remindList);
        if(remindController.shouldRemind()){
            System.out.print("yes");
        } else {
            System.out.print("no");
        }
    }

    @Test
    public void remindText() {
        List<Date> start_timeList = new ArrayList<>();
        List<String> remindList = new ArrayList<>();
        List<String> memoList = new ArrayList<>();
        Date startDate = new Date(400000000);
        String remind= "yes";
        String memo = "";
        start_timeList.add(startDate);
        remindList.add(remind);
        memoList.add(memo);
        RemindController remindController = new RemindController(start_timeList,remindList);
        System.out.print(remindController.remindText(memoList));
    }

    @Test
    public void getstart_dates() {
        List<Date> start_timeList = new ArrayList<>();
        List<String> remindList = new ArrayList<>();
        Date startDate = new Date();
        String remind= "yes";
        start_timeList.add(startDate);
        remindList.add(remind);
        RemindController remindController = new RemindController(start_timeList,remindList);
        System.out.print(remindController.getstart_dates());
    }
}