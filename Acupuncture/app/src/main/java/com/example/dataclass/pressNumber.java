package com.example.dataclass;

public class pressNumber {
    private String name;
    private float pressCount;

    public pressNumber(String name, float pressCount){
        this.name = name;
        this.pressCount = pressCount;
    }
    public String getName(){
        return name;
    }
    public float getpressCount(){
        return pressCount;
    }
}
