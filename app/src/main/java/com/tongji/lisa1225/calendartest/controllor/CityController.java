package com.tongji.lisa1225.calendartest.controllor;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CityController {
    private List<Date> start_dates;
    private List<Date> end_dates;
    private List<String> destinations;
    private int num;

    //public RemindController() {}
    public CityController(List<Date> start_date,List<Date>end_date,List<String> destination) {
        this.start_dates = start_date;
        this.end_dates=end_date;
        this.destinations=destination;
        num=0;
    }

    public boolean inTravel(long currentTime) {
        Date[] startArray=new Date[start_dates.size()];
        start_dates.toArray(startArray);
        Date[] endArray=new Date[end_dates.size()];
        end_dates.toArray(endArray);
        for(int i=0;i<startArray.length;i++)
        {
            if(currentTime > (startArray[i].getTime()-1000 * 60 * 60 * 24L)
                    && currentTime <= endArray[i].getTime()){
                num=i;
                return true;
            }

        }
        return false;
    }
    public String whichCity()
    {
        String[] dest_Array=new String[destinations.size()];
        destinations.toArray(dest_Array);
        if(dest_Array[num].isEmpty()){
            return null;
        }
        return dest_Array[num];
    }
    public int getNum(){
        return num;
    }

}
