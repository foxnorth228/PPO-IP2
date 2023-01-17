package com.example.ip2;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Objects;

public class Database {
    private final HashMap<String, Order> database;
    public Database(){
        database = new HashMap<>();
    }

    public void initialize(String[] arr) {
        for(int i = 0; i < arr.length; ++i) {
            database.put(arr[i], new Order(0, arr[i], Calendar.getInstance(),i + 1));
        }
    }
    public Order get(String name) {
        return database.get(name);
    }

    public void put(Order p) {
        if(p == null) {
            throw new NullPointerException("Элемент не может быть null");
        }
        if(database.containsKey(p.name)) {
            throw new ArrayStoreException("Данный элемент присутствует в базе");
        }
        database.put(p.name, p);
    }

    public void change(String name, Order p) {
        if(!database.containsKey(name)) {
            throw new ArrayStoreException("Данный элемент не присутствует в базе");
        }
        if(p == null) {
            throw new NullPointerException("Элемент не может быть null");
        }
        if(!Objects.equals(name, p.name) && database.containsKey(p.name)) {
            throw new ArrayStoreException("ошибка данных");
        }
        database.remove(name);
        database.put(p.name, p);
    }

    public void remove(String str) {
        if(database.containsKey(str)) {
            database.remove(str);
        } else {
            throw new ArrayStoreException("Данный элемент не присутствует в базе");
        }
    }
}
