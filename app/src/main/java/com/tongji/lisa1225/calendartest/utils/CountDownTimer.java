package com.tongji.lisa1225.calendartest.utils;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;

/**
 * 倒计时定时器
 * Created by lenovo on 2017/1/5.
 */

public abstract class CountDownTimer {
    //多长时间计时器应该停止
    private final long mMillisInFutrue;
    //倒计时的时间间隔
    private final long mCountdownInterval;
    private long mStopTimeInFuture;
    private boolean mCancelled=false;

    public CountDownTimer(long millisInFutrue,long countdownInterval){
        mMillisInFutrue=millisInFutrue;
        mCountdownInterval=countdownInterval;
    }

    /**
     * 取消倒计时
     * 不要从倒计时计时器线程中调用它
     */
    public final void cancel(){
        mHandler.removeMessages(MSG);
        mCancelled=true;
    }

    /**
     * 开始倒计时
     * @return
     */
    public synchronized final CountDownTimer start(){
        if(mMillisInFutrue<=0){
            onFinish();
            return this;
        }
        mStopTimeInFuture= SystemClock.elapsedRealtime()+mMillisInFutrue;
        mHandler.sendMessage(mHandler.obtainMessage(MSG));
        mCancelled=false;
        return this;
    }

    /**
     * 根据固定时间间隔进行回调
     * 在给定总的时间millisInFutrue内，每隔一个countdownInterval时间就会回调一次
     * @param millisUntilFinished  结束之前的总时间
     */
    public abstract void onTick(long millisUntilFinished);

    /**
     * 当计时器结束时调用的函数
     */
    public abstract void onFinish();

    private static final int MSG=1;

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg){
            synchronized (CountDownTimer.this){
                final long millisLeft=mStopTimeInFuture-SystemClock.elapsedRealtime();

                if(millisLeft<=0){
                    onFinish();
                }else if(millisLeft<mCountdownInterval){
                    //当要完成的时间减去当前时间小于规定时间间隔时，不调用onTick(),只是等待结束
                    sendMessageDelayed(obtainMessage(MSG),millisLeft);
                }else{
                    long lastTickStart=SystemClock.elapsedRealtime();
                    onTick(millisLeft);
                    //考虑到用户执行onTick 占用的时间
                    long delay=lastTickStart+mCountdownInterval-SystemClock.elapsedRealtime();

                    //特殊情况：用户执行onTick的时间多于一个时间间隔，则跳到下一个时间间隔
                    while(delay<0) delay+=mCountdownInterval;

                    if(!mCancelled){
                        sendMessageDelayed(obtainMessage(MSG),delay);
                    }
                }
            }
        }

    };

}
