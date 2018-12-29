package com.tongji.lisa1225.calendartest.decorator;


import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Date;
import java.util.List;

public class EventDecorator implements DayViewDecorator {
    private List<Date> start_dates;
    private List<Date> end_dates;

    public EventDecorator() {}
    public EventDecorator(List<Date> start_date,List<Date>end_date) {
        this.start_dates = start_date;
        this.end_dates=end_date;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        long currentTime=day.getDate().getTime();
        Date[] startArray=new Date[start_dates.size()];
        start_dates.toArray(startArray);
        Date[] endArray=new Date[end_dates.size()];
        end_dates.toArray(endArray);
        for(int i=0;i<startArray.length;i++)
        {
            if(currentTime > (startArray[i].getTime()-1000 * 60 * 60 * 24L)
                    && currentTime <= endArray[i].getTime()){
                return true;
            }

        }
        return false;
       /* for (Date sdate:start_dates){

        }
        return dates.contains(day.getDate());
*/
        //return true;
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new AnnulusSpan());
    }

}
