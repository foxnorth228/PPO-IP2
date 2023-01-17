package com.example.ip2;

import java.io.Serializable;
import java.util.Calendar;

public class Order implements Serializable {
    public int id;
    public String name;
    public Calendar date;
    public Integer cost;

    public Order(int id, String name, Calendar date, Integer cost) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.cost = cost;
    }
}
