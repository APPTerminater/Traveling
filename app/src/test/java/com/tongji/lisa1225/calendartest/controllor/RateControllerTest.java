package com.tongji.lisa1225.calendartest.controllor;

import com.tongji.lisa1225.calendartest.model.TripInfo;

import org.junit.Test;

import static org.junit.Assert.*;

public class RateControllerTest {

    @Test
    public void getRate() {
        TripInfo tripInfo = new TripInfo();

        tripInfo.budget = 60000;
        tripInfo.total_cost = 13000;
        tripInfo.total_day = 10;
        tripInfo.total_walk = 100000;
        //tripInfo.rates = cursor.getFloat(11);
        //tripInfo.comment = cursor.getString(12);
        RateController rateController = new RateController(tripInfo,-4000,-1);
        tripInfo = rateController.getRate();
        System.out.print(tripInfo.rates);
        System.out.print(tripInfo.comment);
    }
}