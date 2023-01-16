package com.example.ip2;

import java.util.HashMap;
import java.util.Objects;

public class Database {
    private final HashMap<String, Product> database;
    public Database(){
        database = new HashMap<>();
    }

    public void initialize(String[] arr) {
        for(int i = 0; i < arr.length; ++i) {
            database.put(arr[i], new Product(R.drawable.bread, arr[i], i + 1));
        }
    }
    public Product get(String name) {
        return database.get(name);
    }

    public void put(Product p) {
        if(p == null) {
            throw new NullPointerException("Элемент не может быть null");
        }
        if(database.containsKey(p.name)) {
            throw new ArrayStoreException("Данный элемент присутствует в базе");
        }
        database.put(p.name, p);
    }

    public void change(String name, Product p) {
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
