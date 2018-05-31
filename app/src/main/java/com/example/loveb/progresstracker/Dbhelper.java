package com.example.loveb.progresstracker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by loveb on 30-05-2018.
 */

public class Dbhelper extends SQLiteOpenHelper {


    public Dbhelper(Context context) {
        super(context, "myDATABASE", null, 2);
        SQLiteDatabase db=this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }
}
