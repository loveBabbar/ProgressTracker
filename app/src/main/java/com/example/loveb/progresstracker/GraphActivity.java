package com.example.loveb.progresstracker;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;


import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.lang.Math.toIntExact;

public class GraphActivity extends AppCompatActivity {
Toolbar toolbar;
ListView listView;
    ValueAdapter adapter;
List<Value> matter;
boolean firsttry=true;
int tominus;
DataPoint[] dataPoints=new DataPoint[100];
int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        toolbar=(Toolbar)findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent=getIntent();
        String msg=intent.getStringExtra("name");
        toolbar.setTitle(msg.toString());
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));

        GraphView graph=(GraphView)findViewById(R.id.graph_view);


        //adapter me add karna h jab cursor fetch karunga tab
        matter=new ArrayList<Value>();

        listView=(ListView)findViewById(R.id.list_View);
        adapter=new ValueAdapter(this, new ArrayList<Value>());
        listView.setAdapter(adapter);



        //adapter me daalde data
        Dbhelper dbhelper=new Dbhelper(this);
        SQLiteDatabase db=dbhelper.getReadableDatabase();
        Cursor cursor=db.query(msg.toString(),null,null,null,null,null,null);
        try{
            while(cursor.moveToNext())
            {
                String  val= cursor.getString(cursor.getColumnIndex("mvalue"));
                String time=cursor.getString(cursor.getColumnIndex("mtime"));
                String date=cursor.getString(cursor.getColumnIndex("mdate"));

                adapter.add(new Value(Integer.parseInt(val),time,date));
                String dateTime=date+" "+time;
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                try{
                    Date mDate=sdf.parse(dateTime);
                    long timeinmillis=mDate.getTime();

                    if(firsttry==true)
                    {
                        firsttry=false;
                        long foo=timeinmillis;
                        foo=foo/6000;
                        int n=toIntExact(foo);
                        tominus=n-1;
                    }
                    timeinmillis=timeinmillis/6000;
                    int n=toIntExact(timeinmillis);
                    n=n-tominus;
                    DataPoint p=new DataPoint((double)n,Double.parseDouble(val));
                    dataPoints[i++]=p;

                }catch(ParseException e){
                    e.printStackTrace();
            }
            }
        }finally {
            cursor.close();
        }


        System.out.println("passsed value of i: "+i);

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(data(dataPoints,i));

        // styling grid/labels

        graph.getGridLabelRenderer().setGridColor(Color.parseColor("#bdbdbd"));

        graph.getGridLabelRenderer().setHighlightZeroLines(false);

        graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.WHITE);
        graph.getGridLabelRenderer().setVerticalLabelsColor(Color.WHITE);
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Time");
        graph.getGridLabelRenderer().setVerticalAxisTitle(msg);
        graph.getGridLabelRenderer().setHorizontalAxisTitleColor(Color.WHITE);
        graph.getGridLabelRenderer().setVerticalAxisTitleColor(Color.WHITE);
        graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        graph.getGridLabelRenderer().reloadStyles();

        // styling viewport
        graph.getViewport().setBackgroundColor(Color.parseColor("#424242"));
        graph.getViewport().isScalable();
        graph.getViewport().isScrollable();
        // styling series
        //series.setTitle("Random Curve 1");
        series.setColor(Color.parseColor("#76ff03"));
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(7);
        series.setThickness(6);
        graph.addSeries(series);


    }

    private DataPoint[] data(DataPoint[] Points,int i)
    {
        System.out.println("function k andar " + i);
        Points=new DataPoint[i];
        for(int j=0;j<i;j++)
        {
            System.out.println("loop k andar j "+ j);
            Points[j]=dataPoints[j];
        }
        return Points;
    }

}
