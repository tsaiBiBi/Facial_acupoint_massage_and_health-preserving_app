package com.example.dataclass;

import java.util.List;

public class Acup {
    public int num;
    public String name, part, position, times, func, detail, img;
    public List<Acup_pos> pos;

    public Acup(int num, String name, String part, String position, String times, String func, String detail, String img, List<Acup_pos> pos) {
        this.num = num;
        this.name = name;
        this.part = part;
        this.position = position;
        this.times = times;
        this.func = func;
        this.detail = detail;
        this.img = img;
        this.pos = pos;
    }
}