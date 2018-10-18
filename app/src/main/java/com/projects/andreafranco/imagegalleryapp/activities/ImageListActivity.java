package com.projects.andreafranco.imagegalleryapp.activities;

import android.net.Uri;
import android.support.v4.app.Fragment;

import com.projects.andreafranco.imagegalleryapp.R;
import com.projects.andreafranco.imagegalleryapp.activities.SingleFragmentActivity;
import com.projects.andreafranco.imagegalleryapp.fragments.ImageListFragment;

public class ImageListActivity extends SingleFragmentActivity implements ImageListFragment.OnFragmentInteractionListener{

    @Override
    protected Fragment createFragment() {
        return ImageListFragment.newInstance(null, null);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }

    @Override
    public void onImageSelected(Uri uri) {

    }
}
