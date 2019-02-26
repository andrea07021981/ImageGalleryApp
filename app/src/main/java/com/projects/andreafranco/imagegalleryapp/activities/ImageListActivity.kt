package com.projects.andreafranco.imagegalleryapp.activities

import android.net.Uri
import android.support.v4.app.Fragment

import com.projects.andreafranco.imagegalleryapp.R
import com.projects.andreafranco.imagegalleryapp.activities.SingleFragmentActivity
import com.projects.andreafranco.imagegalleryapp.fragments.ImageListFragment

class ImageListActivity : SingleFragmentActivity(), ImageListFragment.OnFragmentInteractionListener {

    override val layoutResId: Int
        get() = R.layout.activity_masterdetail

    override fun createFragment(): Fragment {
        return ImageListFragment.newInstance()
    }

    override fun onImageSelected(uri: Uri) {

    }
}
