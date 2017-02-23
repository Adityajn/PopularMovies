package com.example.aditya.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent=getIntent();
        movie mv=(movie)intent.getParcelableExtra("object");

        TextView mvname=(TextView)findViewById(R.id.movie_name);
        mvname.setText(mv.name);
        ImageView img=(ImageView)findViewById(R.id.imageView);
        Picasso.with(this).load("http://image.tmdb.org/t/p/w185/"+mv.pic).into(img);

        TextView mvpop=(TextView)findViewById(R.id.popularity);
        mvpop.setText("Popularity :"+Long.toString(mv.popularity));

        TextView mvrat=(TextView)findViewById(R.id.rating);
        mvrat.setText("Rating :"+Long.toString(mv.rating));

        TextView mvdesc=(TextView)findViewById(R.id.desc);
        mvdesc.setText(mv.desc);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
