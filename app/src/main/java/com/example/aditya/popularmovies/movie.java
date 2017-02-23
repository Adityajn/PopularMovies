package com.example.aditya.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by aditya on 30/6/16.
 */
public class movie implements Parcelable{
    public String name;
    public String pic;
    public long rating;
    public long popularity;
    public String desc;

    movie(String n,String pic,long r,long pop,String desc){
        name=n;
        this.pic=pic;
        rating=r;
        popularity=pop;
        this.desc=desc;
    }
    movie(Parcel in){
        name=in.readString();
        pic=in.readString();
        rating=in.readLong();
        popularity=in.readLong();
        desc=in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(pic);
        dest.writeLong(rating);
        dest.writeLong(popularity);
        dest.writeString(desc);
    }


    public static final Parcelable.Creator<movie> CREATOR=new Parcelable.Creator<movie>(){
        @Override
        public movie createFromParcel(Parcel source) {
            return new movie(source);
        }

        @Override
        public movie[] newArray(int size) {
            return new movie[size];
        }
    };
}
