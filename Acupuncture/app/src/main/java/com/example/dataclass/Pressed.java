package com.example.dataclass;

import java.io.Serializable;

public class Pressed implements Serializable {
    public int num;
    public int usr;
    public String date;
    public String func;
    public int times;

    public Pressed(int num, int usr, String date, String func, int times) {
        this.num = num;
        this.usr = usr;
        this.date = date;
        this.func = func;
        this.times = times;
    }
}
