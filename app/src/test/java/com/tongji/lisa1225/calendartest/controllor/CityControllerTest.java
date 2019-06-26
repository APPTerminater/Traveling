package com.tongji.lisa1225.calendartest.controllor;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class CityControllerTest {

    @Test
    public void inTravel() {
        List<Date> start_timeList = new ArrayList<>();
        List<Date> end_timeList = new ArrayList<>();
        List<String> cityList = new ArrayList<>();
        Date startDate = new Date(400000000);
        Date endDate = new Date(700000000);
        String city = "shanghai";
        start_timeList.add(startDate);
        end_timeList.add(endDate);
        cityList.add(city);
        CityController cityController = new CityController(start_timeList,end_timeList,cityList);
        if(cityController.inTravel(200000000)){
            System.out.print("yes");
        } else {
            System.out.print("no");
        }
    }

    @Test
    public void whichCity() {
        List<Date> start_timeList = new ArrayList<>();
        List<Date> end_timeList = new ArrayList<>();
        List<String> cityList = new ArrayList<>();
        Date startDate = new Date(400000000);
        Date endDate = new Date(700000000);
        String city = "";
        start_timeList.add(startDate);
        end_timeList.add(endDate);
        cityList.add(city);
        CityController cityController = new CityController(start_timeList,end_timeList,cityList);
        System.out.print(cityController.whichCity());
    }
}