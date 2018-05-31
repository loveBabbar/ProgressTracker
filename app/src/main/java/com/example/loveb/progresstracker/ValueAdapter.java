package com.example.loveb.progresstracker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by loveb on 30-05-2018.
 */

public class ValueAdapter extends ArrayAdapter<Value>{


    public ValueAdapter(Context context, List<Value> values)
    {
        super(context,0,values);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.value_item, parent, false);
        }
        Value channel = getItem(position);

            TextView val = (TextView) listItemView.findViewById(R.id.val_view);
            val.setText(channel.getval()+" ");

            TextView mtime = (TextView) listItemView.findViewById(R.id.time_view);
            mtime.setText(channel.getTime());

            TextView mdate = (TextView) listItemView.findViewById(R.id.date_view);
        mdate.setText(channel.getDate());

        return listItemView;
    }
}
