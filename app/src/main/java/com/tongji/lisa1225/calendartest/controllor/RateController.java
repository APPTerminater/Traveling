package com.tongji.lisa1225.calendartest.controllor;

import com.tongji.lisa1225.calendartest.model.TripInfo;

public class RateController {

    private TripInfo tripInfo;
    private int recordDays;
    private float averageBudget;//预计日均开销
    private float daliyWalk;
    private float rate;
    public static final String one = "你都没有记录下来，不能给你打分！";
    public static final String two = "你的旅行规划不是很好，下次需要改进哦！";
    public static final String three = "你的旅行规划还不错，下次继续努力！";
    public static final String four = "很棒的旅行，请继续保持哦！";
    public static final String five = "天啊！这次旅行太完美了，你一定是一个擅长规划的人！";

    public RateController(TripInfo tripInfo,int dailyWalk,int recordDays) {
        this.tripInfo = tripInfo;
        this.daliyWalk = dailyWalk;
        rate = 3;
        this.recordDays = recordDays;
        averageBudget = tripInfo.budget / tripInfo.total_day;
    }

    public TripInfo getRate() {
        if (recordDays == 0) {
            tripInfo.rates = 1;
            tripInfo.comment = one;
            return tripInfo;
        } else {
            float averageCost = tripInfo.total_cost / recordDays;
            float averageStep = tripInfo.total_walk / recordDays;
            rate = rate + (averageBudget - averageCost) / averageCost + (averageStep - daliyWalk) / (averageStep + 10);

        }
        if (rate <= 2) {
            tripInfo.rates = 2;
            tripInfo.comment = two;
        } else if (rate >= 5) {
            tripInfo.rates = 5;
            tripInfo.comment = five;
        } else {
            int temp = (int)(rate * 10);
            tripInfo.rates = (float) temp / 10;
            if (tripInfo.rates >= 2 && tripInfo.rates < 3) {
                tripInfo.comment = two;
            } else if (tripInfo.rates >= 3 && tripInfo.rates < 4) {
                tripInfo.comment = three;
            } else {
                tripInfo.comment = four;
            }
        }

        return tripInfo;
    }
}
