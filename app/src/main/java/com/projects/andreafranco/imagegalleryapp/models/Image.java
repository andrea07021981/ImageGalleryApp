package com.projects.andreafranco.imagegalleryapp.models;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

public class Image implements Parcelable{

    private String mTitle;
    private String mSize;
    private int mWidth;
    private int mHeight;
    private Bitmap mImage;

    public Image(String title, String size, int width, int height, Bitmap image) {
        mTitle = title;
        mSize = size;
        mWidth = width;
        mHeight = height;
        mImage = image;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public int getWidth() {
        return mWidth;
    }

    public void setHeight(int height) {
        this.mHeight = height;
    }

    public int getHeight() {
        return mHeight;
    }

    public void setWidth(int width) {
        this.mWidth = width;
    }

    public Bitmap getImage() {
        return mImage;
    }

    public void setmImage(Bitmap image) {
        this.mImage = image;
    }

    public String getSize() {
        return mSize;
    }

    public void setSize(String size) {
        this.mSize = size;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.mImage, flags);
        dest.writeString(this.mTitle);
        dest.writeString(this.mSize);
        dest.writeInt(this.mWidth);
        dest.writeInt(this.mHeight);
    }

    protected Image(Parcel in) {
        this.mImage = in.readParcelable(null);
        this.mTitle = in.readString();
        this.mSize = in.readString();
        this.mWidth= in.readInt();
        this.mHeight= in.readInt();
    }

    public static final Parcelable.Creator<Image> CREATOR = new Parcelable.Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel source) {
            return new Image(source);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };
}

