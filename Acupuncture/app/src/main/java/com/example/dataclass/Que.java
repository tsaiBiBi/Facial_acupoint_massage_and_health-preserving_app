package com.example.dataclass;

public class Que {
    public int num;
    public String topic, answer, select1, select2, select3, parsing;

    public Que(int num,String topic,String answer,String select1,String select2,String select3,String parsing) {
        this.num = num;
        this.topic = topic;
        this.answer = answer;
        this.select1 = select1;
        this.select2 = select2;
        this.select3 = select3;
        this.parsing = parsing;
    }
}
