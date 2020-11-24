package com.example.dataclass;

public class usageTime {

    private int week;
    private String totalTime;

    public usageTime(int week, String totalTime) {
        this.week = week;
        this.totalTime = totalTime;
    }

    public String getWeek(){
        String day_of_week = "";
        switch (week){
            case 1:
                day_of_week = "星期日";
                break;
            case 2:
                day_of_week = "星期一";
                break;
            case 3:
                day_of_week = "星期二";
                break;
            case 4:
                day_of_week = "星期三";
                break;
            case 5:
                day_of_week = "星期四";
                break;
            case 6:
                day_of_week = "星期五";
                break;
            case 7:
                day_of_week = "星期六";
        }
        return day_of_week;
    }
    public String getTotalTime(){
        return totalTime;
    }
}
