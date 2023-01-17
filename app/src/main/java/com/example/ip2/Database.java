package com.example.ip2;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class Database {
    public Database(){

    }

    public ArrayList<Integer> initialize(String[] arr) {
        ArrayList<Integer> arrId = new ArrayList<>();
        for(int i = 0; i < arr.length; ++i) {
            arrId.add(put(new Order(0, arr[i], Calendar.getInstance(), i + 1)));
        }
        return arrId;
    }

    public Order get(Integer id) {
        boolean isFound = false;
        SQLDatabase sql = MainActivity.sqlbase;
        SQLiteDatabase db = sql.getWritableDatabase();
        Cursor c = db.query(SQLDatabase.table, null, null, null, null, null, null, null);
        if(c.moveToFirst()) {
            int idColIndex = c.getColumnIndex(SQLDatabase.id);
            int nameColIndex = c.getColumnIndex(SQLDatabase.name);
            int dateColIndex = c.getColumnIndex(SQLDatabase.date);
            int costColIndex = c.getColumnIndex(SQLDatabase.cost);
            do {
                if(c.getInt(idColIndex) == id) {
                    isFound = true;
                    break;
                }
            } while(c.moveToNext());
            if(isFound) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(Objects.requireNonNull(ProductEditorActivity.dateFormat.parse(c.getString(dateColIndex), new ParsePosition(0))));
                return new Order(
                        c.getInt(idColIndex),
                        c.getString(nameColIndex),
                        cal,
                        c.getInt(costColIndex)
                );
            }
        }
        return null;
    }

    public Integer put(Order p) {
        SQLDatabase sql = MainActivity.sqlbase;
        SQLiteDatabase db = sql.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(SQLDatabase.name, p.name);
        String date = ProductEditorActivity.dateFormat.format(p.date.getTime());
        cv.put(SQLDatabase.date, date);
        cv.put(SQLDatabase.cost, p.cost);
        int id = (int) db.insert(SQLDatabase.table, null, cv);
        return id;
    }

    public void change(Integer id, Order p) {
        if(p == null) {
            throw new NullPointerException("Элемент не может быть null");
        }
        SQLiteDatabase db = MainActivity.sqlbase.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(SQLDatabase.name, p.name);
        String date = ProductEditorActivity.dateFormat.format(p.date.getTime());
        cv.put(SQLDatabase.date, date);
        cv.put(SQLDatabase.cost, p.cost);
        db.update(SQLDatabase.table, cv, SQLDatabase.id + " = " + id, null);
    }

    public void remove(Integer id) {
        SQLiteDatabase db = MainActivity.sqlbase.getWritableDatabase();
        db.delete(SQLDatabase.table, SQLDatabase.id + " = " + id, null);
    }

    public static void deleteSqlTable() {
        SQLiteDatabase db = MainActivity.sqlbase.getWritableDatabase();
        db.delete(SQLDatabase.table, null, null);
    }
}
