package com.managment.doctor.doctorappoinment.imageauth;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.managment.doctor.doctorappoinment.R;

import java.util.ArrayList;

interface OnClickListner {
    void onItemClick(int pos, Bitmap b);
}

public class CustomAdapter extends BaseAdapter {
    Context context;
    ArrayList<Bitmap> logos;
    LayoutInflater inflter;
    OnClickListner listner;

    public CustomAdapter(Context applicationContext, ArrayList<Bitmap> logos, OnClickListner listner) {
        this.context = applicationContext;
        this.logos = logos;
        inflter = (LayoutInflater.from(applicationContext));
        this.listner = listner;
    }

    @Override
    public int getCount() {
        return logos.size();
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.item_gride_view, null); // inflate the layout
        ImageView icon = view.findViewById(R.id.icon); // get the reference of ImageView
        icon.setImageBitmap(logos.get(i)); // set logo images
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listner != null)
                    listner.onItemClick(i, logos.get(i));
            }
        });
        return view;
    }

}