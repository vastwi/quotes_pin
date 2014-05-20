package com.example.Resources;

import android.app.Activity;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.Example_one.MyActivity;
import com.example.Example_one.R;
import com.example.Grid.util.DynamicHeightImageView;
//import com.example.quotes_pin.R;

import java.util.List;

public class DataAdapter extends ArrayAdapter<Data> {

    Activity activity;
    int resource;
    List<Data> datas;

    public DataAdapter(Activity activity, int resource, List<Data> objects) {
        super(activity, resource, objects);

        this.activity = activity;
        this.resource = resource;
        this.datas = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

         View row = convertView;
//        final View row;
        final DealHolder holder;

        if (row == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            row = inflater.inflate(resource, parent, false);
            final View row2 = row;

            holder = new DealHolder();
            holder.image = (DynamicHeightImageView)row.findViewById(R.id.image);
            holder.name = (TextView)row.findViewById(R.id.name);
            holder.project_name = (TextView)row.findViewById(R.id.project_name);
            holder.quote = (TextView)row.findViewById(R.id.quote_view);

//            holder.image.setAdjustViewBounds(true);
            row.setTag(holder);

            final int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35, activity.getResources().getDisplayMetrics());
            final int height = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35, activity.getResources().getDisplayMetrics());

            final int width_big = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, activity.getResources().getDisplayMetrics());
            final int height_big = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, activity.getResources().getDisplayMetrics());

            final RelativeLayout.LayoutParams layoutParams_big = new RelativeLayout.LayoutParams(width_big, height_big);
            final RelativeLayout.LayoutParams layoutParams_small = new RelativeLayout.LayoutParams(width , height );


            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    RelativeLayout tl = (RelativeLayout) row2.findViewById(R.id.relative_info);
                    if(tl.getVisibility() == View.GONE || tl.getVisibility() == View.INVISIBLE){
                     tl.setVisibility(View.VISIBLE);
                     holder.image.setLayoutParams(layoutParams_big);
                    }
                    else    {
                        tl.setVisibility(View.GONE);
                    holder.image.setLayoutParams(layoutParams_small);
                    }
                }
            });
        }
        else {
            holder = (DealHolder) row.getTag();
        }

        final Data data = datas.get(position);
//        holder.image.setImageResource(R.drawable.img);

        holder.image.setHeightRatio(1.0);
        holder.name.setText(data.name);
        holder.quote.setText(data.quote);
        holder.project_name.setText(data.project_name);

        return row;
    }

    static class DealHolder {
        DynamicHeightImageView image;
        TextView name;
        TextView quote;
        TextView project_name;
    }
}

