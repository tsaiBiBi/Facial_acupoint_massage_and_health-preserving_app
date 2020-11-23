package com.example.dataclass;

import java.io.Serializable;

public class Acup_pos implements Serializable {
    public String pos_type;
    public double bias_x, bias_y, ratio;
    public int a_idx, b_idx, c_idx, d_idx, a_con, b_con, c_con, d_con;

    public Acup_pos(String pos_type, int a_con, int a_idx, int b_con, int b_idx, int c_con, int c_idx, int d_con, int d_idx, double ratio, double bias_x, double bias_y) {
        this.pos_type = pos_type;
        this.a_con = a_con;
        this.b_con = b_con;
        this.c_con = c_con;
        this.d_con = d_con;

        this.a_idx = a_idx;
        this.b_idx = b_idx;
        this.c_idx = c_idx;
        this.d_idx = d_idx;

        this.ratio = ratio;
        this.bias_x = bias_x;
        this.bias_y = bias_y;
    }
}
