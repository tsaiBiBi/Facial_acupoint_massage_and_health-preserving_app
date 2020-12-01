package com.example.dataclass;

public class Clinic {
    public int num;
    public String name, phone, address, type;
    public Double lat, lng;

    public Clinic(int num, String name, String phone, String address, String type, Double lat, Double lng) {
        this.num = num;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.type = type;
        this.lat = lat;
        this.lng = lng;
    }
}
