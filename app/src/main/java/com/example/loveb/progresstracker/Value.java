package com.example.loveb.progresstracker;

/**
 * Created by loveb on 30-05-2018.
 */

public class Value {

    private int val;
    private String time;
    private String date;


    public Value(int v,String t,String d)
    {
        val=v;
        time=t;
        date=d;
    }
    public int getval()
    {
        return val;
    }
    public String getTime()
    {
        return time;
    }
    public String getDate()
    {
        return date;
    }


}
