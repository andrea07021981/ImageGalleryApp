package com.projects.andreafranco.imagegalleryapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.projects.andreafranco.imagegalleryapp.R;
import com.projects.andreafranco.imagegalleryapp.models.Image;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.ImageListHolder>{
    private List<Image> mImageList;

    /**
     * View holder class
     * */
    public class ImageListHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView mImageImageVIew;
        TextView mTitleTextView;
        TextView mSizeTextView;
        TextView mDimensionsTextView;
        Image mImage;

        public ImageListHolder(View view) {
            super(view);
            mImageImageVIew = (ImageView) view.findViewById(R.id.image_image_view);
            mTitleTextView = (TextView) view.findViewById(R.id.title_text_view);
            mSizeTextView = (TextView) view.findViewById(R.id.size_text_view);
            mDimensionsTextView = (TextView) view.findViewById(R.id.dimensions_text_view);
        }

        public void bindImage(Image image) {
            mImage = image;
            mImageImageVIew.setImageBitmap(mImage.getImage());
            mTitleTextView.setText(mImage.getTitle());
            mSizeTextView.setText(mImage.getSize());
            mDimensionsTextView.setText(mImage.getDimension());
        }

        @Override
        public void onClick(View v) {
            //TODO action on click
        }
    }

    public ImageListAdapter(List<Image> imageList) {
        mImageList = imageList;
    }

    @Override
    public void onBindViewHolder(ImageListHolder holder, int position) {
        Image image = mImageList.get(position);
        holder.bindImage(image);
    }

    @Override
    public int getItemCount() {
        return mImageList.size();
    }

    @Override
    public ImageListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_image,parent, false);
        return new ImageListHolder(v);
    }

    /**
     * Clears all items from the data set.
     */
    public void clear(){
        if(mImageList != null){
            mImageList.clear();
            notifyDataSetChanged();
        }
    }

    /**
     * Adds all of the items to the data set.
     * @param items The item array to be added.
     */
    public void addAll(List<Image> items){
        if(mImageList == null){
            mImageList = new ArrayList<>();
        }
        mImageList.addAll(items);
        notifyDataSetChanged();
    }
}