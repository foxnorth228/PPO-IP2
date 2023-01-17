package com.example.ip2;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class Database {
    private final HashMap<Integer, Order> database;
    private int nextId;
    public Database(){
        nextId = 0;
        database = new HashMap<>();
    }

    public ArrayList<Integer> initialize(String[] arr) {
        ArrayList<Integer> arrayId = new ArrayList<>();
        for(int i = 0; i < arr.length; ++i) {
            arrayId.add(nextId);
            database.put(nextId, new Order(nextId, arr[i], Calendar.getInstance(),i + 1));
            ++nextId;
        }
        return arrayId;
    }
    public Order get(Integer id) {
        return database.get(id);
    }

    public Integer put(Order p) {
        if(p == null) {
            throw new NullPointerException("Элемент не может быть null");
        }
        database.put(nextId, new Order(nextId, p.name, p.date, p.cost));
        nextId++;
        return nextId - 1;
    }

    public void change(Integer id, Order p) {
        if(!database.containsKey(id)) {
            throw new ArrayStoreException("Данный элемент не присутствует в базе");
        }
        if(p == null) {
            throw new NullPointerException("Элемент не может быть null");
        }
        database.remove(id);
        database.put(id, new Order(id, p.name, p.date, p.cost));
    }

    public void remove(Integer id) {
        if(database.containsKey(id)) {
            database.remove(id);
        } else {
            throw new ArrayStoreException("Данный элемент не присутствует в базе");
        }
    }
}
