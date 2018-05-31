package com.example.loveb.progresstracker;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    android.support.v7.widget.Toolbar toolbar;
        DrawerLayout drawerLayout;
        ActionBarDrawerToggle drawerToggle;
        FloatingActionButton fbutton;
        EditText input;
    private LinearLayout parentRelativeLayout;
    private LinearLayout listItem;
    Dbhelper dbhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        System.out.println("Oncreate called");

        toolbar=(android.support.v7.widget.Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Progress Tracker");
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));

        drawerLayout=(DrawerLayout)findViewById(R.id.dl);
        drawerToggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.Open,R.string.Closed);

        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        //bhot mehnat lagi h yaar iss line ko dhundne me
        drawerToggle.getDrawerArrowDrawable().setColor(Color.parseColor("#ffffff"));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //upar ka saaara kaam material design ka tha , ab niche saari functioning h


        parentRelativeLayout=(LinearLayout)findViewById(R.id.parent_relative_layout);

         dbhelper=new Dbhelper(this);
         //display existing tables
        displayExistingTables();


        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Insert Attribute");
        builder.setIcon(R.drawable.ic_file_download_black_24dp);
       // builder.setMessage("This is the message settted");


        input=new EditText(MainActivity.this );
        builder.setView(input);

        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String txt =input.getText().toString();
                input.setText("");
                input.setHint("Enter Message");

                //create table
                SQLiteDatabase db=dbhelper.getWritableDatabase();
                String create_table="create table "+ txt.toString()+"(mvalue INTEGER, mtime varchar(20),mdate varchar(20) ); ";
                db.execSQL(create_table);
                onAddView(txt);
                Toast.makeText(MainActivity.this,txt+" table has been created",Toast.LENGTH_SHORT).show();

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

     final AlertDialog ad=   builder.create();

        fbutton=(FloatingActionButton)findViewById(R.id.fab);
        fbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //show  a dialog and get input
             ad.show();
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return drawerToggle.onOptionsItemSelected(item) ||  super.onOptionsItemSelected(item);
    }

    private void onAddView(String msg)
    {
        LayoutInflater inflater=(LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView=inflater.inflate(R.layout.list_item,null);

        TextView textView = (TextView) rowView.findViewById(R.id.attribute_name);
        textView.setText(msg);
          final  String a=msg;

        ImageButton imageButton=(ImageButton) rowView.findViewById(R.id.add_button);

        final TextView textView1 = (TextView) rowView.findViewById(R.id.textview1);
        final TextView textView2 = (TextView) rowView.findViewById(R.id.textview2);
        final TextView textView3 = (TextView) rowView.findViewById(R.id.textview3);

        fillTheThreeTextViews(textView1,textView2,textView3,a );

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeinput(textView1,textView2,textView3,a);
            }
        });

        LinearLayout linearLayout=(LinearLayout) rowView.findViewById(R.id.inner_linear_layout);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,"nayi activity khol de",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(MainActivity.this,GraphActivity.class);
                intent.putExtra("name",a);
                startActivity(intent);
            }
        });

        parentRelativeLayout.addView(rowView,parentRelativeLayout.getChildCount());

        rowView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
            Toast.makeText(MainActivity.this,"Long Click Listener added!!!",Toast.LENGTH_SHORT).show();
                showOptionToDelete(view,a);
                return true;
            }
        });

    }

    public void takeinput(final TextView t1,final TextView t2,final TextView t3,final String msg)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Insert Value");
        final EditText editText=new EditText(this);
        builder.setView(editText);
        builder.setPositiveButton("Insert", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                     String msg1=t1.getText().toString();
                     String msg2=t2.getText().toString();
                     String msg3=t3.getText().toString();
                     msg3=msg2;
                     msg2=msg1;
                     msg1=editText.getText().toString();

                     t1.setText(msg1);
                     t2.setText(msg2);
                     t3.setText(msg3);
                     //backend me database me dalega msg
                String time,date;
                //fetch time and date
                time=FetchTime();
                date=FetchDate();
                System.out.println( time+"  <-->  "+date);
                String insert_stmt="INSERT into "+ msg.toString()+" values( "+ Integer.parseInt(msg1)+", "+time+" , " + date+");";
                SQLiteDatabase db=dbhelper.getWritableDatabase();
                ContentValues values=new ContentValues();
                values.put("mvalue",Integer.parseInt(msg1));
                values.put("mtime",time+"");
                values.put("mdate",date+"");
//                db.execSQL(insert_stmt);
                db.insert(msg.toString(),null,values);
                db.close();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
           dialogInterface.dismiss();
            }
        });
        //ye dono lines bhool jata hu baar baar
        AlertDialog ad=builder.create();
        ad.show();
    }
    public void showOptionToDelete(final View v, final String tableName)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("Want to delete this ?");
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                SQLiteDatabase db=dbhelper.getWritableDatabase();
                String drop_table=" drop table "+tableName+";";
                db.execSQL(drop_table);

                parentRelativeLayout.removeView(v);

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog ad=builder.create();
        ad.show();
    }
    String FetchTime()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String time = sdf.format(new Date());
            return time;
    }
    String FetchDate()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date());
        return date;
    }
    void displayExistingTables()
    {
        SQLiteDatabase db=dbhelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'AND name!='android_metadata'", null);

        if (c.moveToFirst()) {
            while ( !c.isAfterLast() ) {
               // Toast.makeText(MainActivity.this, "Table Name=> "+c.getString(0), Toast.LENGTH_LONG).show();
               String tableName=c.getString(0);
               onAddView(tableName);
                c.moveToNext();
            }
        }
    }
    void fillTheThreeTextViews(TextView t1,TextView t2,TextView t3,String tableName)
    {
        int val1=0,val2=0,val3=0;
        SQLiteDatabase db=dbhelper.getReadableDatabase();
        String query="Select mvalue from "+tableName+";";
        String[] col={"mvalue"};
        Cursor cursor=db.query(tableName,col,null,null,null,null,null);
        try{
            while(cursor.moveToNext())
            {
                val3=val2;
                val2=val1;
                val1=Integer.parseInt(cursor.getString(cursor.getColumnIndex("mvalue")));
            }
            t1.setText(""+val1);
            t2.setText(""+val2);
            t3.setText(""+val3);
        }
        finally {
            cursor.close();
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("on start called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("on resume called");//teeno textview fill karde onresuma and on start me
    }
}
