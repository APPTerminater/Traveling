package com.tongji.lisa1225.calendartest.model;



public class PathPlanDataBean {
    private String Time;
    private String distance;

    public PathPlanDataBean(String time, String distance) {
        Time = time;
        this.distance = distance;
    }

    public String getTime() {
        return Time;
    }

    public String getDistance() {
        return distance;
    }

    @Override
    public String toString() {
        return "PathPlanDataBean{" +
                "Time='" + Time + '\'' +
                ", distance='" + distance + '\'' +
                '}';
    }
}
