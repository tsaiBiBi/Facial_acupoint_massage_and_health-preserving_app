package com.example.dataclass;

public class Acup {
    public int num;
    public String name, part, position, times, func, detail, img;

    public Acup(int num, String name, String part, String position, String times, String func, String detail, String img) {
        this.num = num;
        this.name = name;
        this.part = part;
        this.position = position;
        this.times = times;
        this.func = func;
        this.detail = detail;
        this.img = img;
    }
}
