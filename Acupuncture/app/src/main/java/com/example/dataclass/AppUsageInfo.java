package com.example.dataclass;

public class AppUsageInfo {
    public String appName, packageName;
    public long timeInForeground = 0;

    public AppUsageInfo(String pName, String aName) {
        this.packageName = pName;
        this.appName = aName;
    }
}