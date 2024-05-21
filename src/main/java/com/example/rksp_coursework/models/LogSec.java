package com.example.rksp_coursework.models;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogSec {
    public LogSec(int id, int supId, String strId, String name, double price, String datetime){
        this.id = id;
        this.supId = supId;
        this.strId = strId;
        this.name = name;
        this.price = price;
        this.datetime = datetime;
    }

    private int id;

    private int supId;


    private String strId;

    private String name;

    private double price;

    private String datetime;
}
