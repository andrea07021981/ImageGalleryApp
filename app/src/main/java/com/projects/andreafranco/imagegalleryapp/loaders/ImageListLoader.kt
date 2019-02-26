package com.projects.andreafranco.imagegalleryapp.loaders

import android.content.Context
import android.support.v4.content.AsyncTaskLoader
import android.text.TextUtils

import com.projects.andreafranco.imagegalleryapp.models.Image
import com.projects.andreafranco.imagegalleryapp.utils.HttpUtils

import java.util.ArrayList

class ImageListLoader(context: Context, val mQuery: String?) : AsyncTaskLoader<ArrayList<Image>>(context) {

    override fun loadInBackground(): ArrayList<Image>? {
        return if (mQuery == null || TextUtils.isEmpty(mQuery)) {
            null
        } else HttpUtils.fetchImageListData(mQuery)
    }

    override fun onStartLoading() {
        forceLoad()
    }
}
