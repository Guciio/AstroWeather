package com.example.gucio.astroweather;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class OnlineDataBase extends SQLiteOpenHelper {

    private static final String name = "onlineDataBase";
    private static final String factory = "Locations";
    private static final int FAIL_CODE = -1;

    public OnlineDataBase(Context context) {
        super(context, name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + name+
        " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "CITY TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + name);
    }

    public boolean addData(String Data){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("CITY", Data);

        if ((db.insert(name, null, contentValues)) == FAIL_CODE) {
            return false;
        } else {
            return true;
        }
    }

    public boolean deleteData(String city) {

        SQLiteDatabase db = this.getWritableDatabase();

        return (db.delete(name, "CITY=?", new String[]{city})) != FAIL_CODE;
    }

    public Cursor getListContents() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + name, null);

        return data;
    }
}
