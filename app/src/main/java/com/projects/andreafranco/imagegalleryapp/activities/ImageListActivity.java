package com.projects.andreafranco.imagegalleryapp.activities;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;

import com.projects.andreafranco.imagegalleryapp.activities.SingleFragmentActivity;
import com.projects.andreafranco.imagegalleryapp.fragments.ImageListFragment;

public class ImageListActivity extends SingleFragmentActivity implements ImageListFragment.OnFragmentInteractionListener{

    @Override
    protected Fragment createFragment() {
        return ImageListFragment.newInstance(null, null);
    }

    @Override
    protected int getLayoutResId() {
        return super.getLayoutResId();
    }

    @Override
    public void onImageSelected(Uri uri) {

    }
}
