package com.example.smartlights.smartapps.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.smartlights.R;
import com.example.smartlights.smartapps.basepojo.Ward;

import java.util.ArrayList;


public class WardAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<Ward> objects;
    int globalInc = 0;

    public WardAdapter(Context context, ArrayList<Ward> products) {
        ctx = context;
        objects = products;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = lInflater.inflate(R.layout.activity_livedata_text, parent, false);
        }

        Ward p = getProduct(position);
        ((TextView) view.findViewById(R.id.texterV)).setText(p.name);
        return view;
    }

    Ward getProduct(int position) {
        return ((Ward) getItem(position));
    }
}

