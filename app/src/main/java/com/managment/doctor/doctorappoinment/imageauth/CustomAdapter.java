package com.managment.doctor.doctorappoinment.imageauth;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.managment.doctor.doctorappoinment.R;

public class CustomAdapter extends BaseAdapter {
    Context context;
    Bitmap logos[];
    LayoutInflater inflter;

    public CustomAdapter(Context applicationContext, Bitmap logos[]) {
        this.context = applicationContext;
        this.logos = logos;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return logos.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.item_gride_view, null); // inflate the layout
        ImageView icon = view.findViewById(R.id.icon); // get the reference of ImageView
        icon.setImageBitmap(logos[i]); // set logo images
        return view;
    }
}