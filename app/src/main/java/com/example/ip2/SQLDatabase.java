package com.example.ip2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLDatabase extends SQLiteOpenHelper {
    public static final String databaseName = "orderStore.db";
    public static final int schema = 1;
    public static final String table = "orders";
    public static final String id = "id";
    public static final String name = "name";
    public static final String date = "date";
    public static final String cost = "cost";

    public SQLDatabase(Context context) {
        super(context, databaseName, null, schema);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("LOG_TAG", "--- onCreate database ---");
        // создаем таблицу с полями
        db.execSQL("create table " + table + " ("
                + id + " integer primary key autoincrement, "
                + name + " text, "
                + date + " text, "
                + cost + " integer" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+databaseName);
        onCreate(db);
    }
}
