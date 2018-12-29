package com.tongji.lisa1225.calendartest.decorator;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import android.graphics.Color;
//import com.prolificinteractive.materialcalendarview.*;
import android.text.style.ForegroundColorSpan;
import java.util.Calendar;

public class HighlightWeekendsDecorator implements DayViewDecorator {
    private final Calendar calendar = Calendar.getInstance();

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        day.copyTo(calendar);
        int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
        return weekDay == Calendar.SATURDAY || weekDay == Calendar.SUNDAY;
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new ForegroundColorSpan(Color.parseColor("#fd755c")));
    }

}
