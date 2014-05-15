package com.example.Resources;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.Grid.util.DynamicHeightImageView;
import com.example.quotes_pin.R;

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
        final DealHolder holder;

        if (row == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            row = inflater.inflate(resource, parent, false);

            holder = new DealHolder();
            holder.image = (DynamicHeightImageView)row.findViewById(R.id.image);
            holder.name = (TextView)row.findViewById(R.id.name);
            holder.quote = (TextView)row.findViewById(R.id.quote_view);
            holder.project_name = (TextView)row.findViewById(R.id.project_name);

            row.setTag(holder);
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

