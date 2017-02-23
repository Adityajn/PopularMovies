package com.example.aditya.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by aditya on 30/6/16.
 */
public class movie_adapter extends BaseAdapter {

    private ArrayList<movie> res;
    private LayoutInflater minflator;
    public movie_adapter(Context context,ArrayList<movie> results) {
        res=results;
        minflator=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return res.size();
    }

    @Override
    public Object getItem(int position) {
        return res.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView =minflator.inflate(R.layout.custom_grid,null);

        ImageView img=(ImageView)convertView.findViewById(R.id.image);
        Picasso.with(convertView.getContext()).load("http://image.tmdb.org/t/p/w185/"+res.get(position).pic).into(img);
        return convertView;
    }
}
