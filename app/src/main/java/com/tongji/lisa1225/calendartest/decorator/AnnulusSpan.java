package com.tongji.lisa1225.calendartest.decorator;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.style.LineBackgroundSpan;

public class AnnulusSpan implements LineBackgroundSpan {
    @Override
    public void drawBackground(Canvas canvas, Paint paint,
                               int left, int right, int top, int baseline, int bottom,
                               CharSequence charSequence,
                               int start, int end, int lineNum) {
        canvas.save();
        canvas.drawColor(Color.parseColor("#a4a8d7"));
        canvas.restore();
    }



}
