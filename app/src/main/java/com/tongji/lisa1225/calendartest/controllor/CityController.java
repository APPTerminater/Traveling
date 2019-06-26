package com.tongji.lisa1225.calendartest.controllor;

import java.util.Date;
import java.util.List;

public class CityController {
    private List<Date> startDates;
    private List<Date> endDates;
    private List<String> destinations;
    private int num;

    public CityController(List<Date> startDate,List<Date> endDate,List<String> destination) {
        this.startDates = startDate;
        this.endDates = endDate;
        this.destinations = destination;
        num = 0;
    }

    public boolean inTravel(long currentTime) {
        Date[] startArray = new Date[startDates.size()];
        startDates.toArray(startArray);
        Date[] endArray = new Date[endDates.size()];
        endDates.toArray(endArray);
        for (int i = 0;i < startArray.length;i++) {
            if (currentTime > (startArray[i].getTime() - 1000 * 60 * 60 * 24L)
                    && currentTime <= endArray[i].getTime()) {
                num = i;
                return true;
            }

        }
        return false;
    }

    public String whichCity() {
        String[] destArray = new String[destinations.size()];
        destinations.toArray(destArray);
        if (destArray[num].isEmpty()) {
            return null;
        }
        return destArray[num];
    }

}
