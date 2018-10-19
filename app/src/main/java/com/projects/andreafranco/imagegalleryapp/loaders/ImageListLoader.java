package com.projects.andreafranco.imagegalleryapp.loaders;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.text.TextUtils;

import com.projects.andreafranco.imagegalleryapp.models.Image;
import com.projects.andreafranco.imagegalleryapp.utils.HttpUtils;

import java.util.ArrayList;

public class ImageListLoader extends AsyncTaskLoader<ArrayList<Image>> {

    String mQuery;

    public ImageListLoader(@NonNull Context context, String query) {
        super(context);
        mQuery = query;
    }

    @Nullable
    @Override
    public ArrayList<Image> loadInBackground() {
        if (mQuery == null || TextUtils.isEmpty(mQuery)) {
            return null;
        }
        return HttpUtils.fetchImageListData(mQuery);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
