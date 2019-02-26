package com.projects.andreafranco.imagegalleryapp.activities

import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import com.projects.andreafranco.imagegalleryapp.R

abstract class SingleFragmentActivity : AppCompatActivity() {

    protected open val layoutResId: Int
        @LayoutRes
        get() = R.layout.activity_fragment

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutResId)
        val fm = supportFragmentManager
        var fragment = fm.findFragmentById(R.id.fragment_container)
        if (fragment == null) {
            fragment = createFragment()
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit()
        }
    }

    protected abstract fun createFragment(): Fragment
}
