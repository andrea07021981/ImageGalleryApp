package com.projects.andreafranco.imagegalleryapp.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast

import com.projects.andreafranco.imagegalleryapp.R
import com.projects.andreafranco.imagegalleryapp.adapters.ImageListAdapter
import com.projects.andreafranco.imagegalleryapp.loaders.ImageListLoader
import com.projects.andreafranco.imagegalleryapp.models.Image
import com.projects.andreafranco.imagegalleryapp.utils.NetworkUtils

import java.util.ArrayList


class ImageListFragment : Fragment(), LoaderManager.LoaderCallbacks<ArrayList<Image>> {

    internal var mSearchEditText: EditText? = null
    internal var mSearchImageButton: ImageButton? = null
    internal var mWaitProgressBar: ProgressBar? = null
    internal var mNoNetworkTextView: TextView? = null
    internal var mEmptyListTextView: TextView? = null
    internal var mImageRecyclerView: RecyclerView? = null
    internal var mLoaderManager: LoaderManager? = null
    internal var mImageArrayList: ArrayList<Image>? = null
    internal lateinit var mImageAdapter: ImageListAdapter

    private var mListener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.image_list_fragment, container, false)

        // initialize views and variables
        initView(rootView)
        initValues(rootView)

        if (mImageArrayList!!.size == 0 && savedInstanceState == null) {
            showToast(rootView, getString(R.string.search_text))
        } else {
            mImageRecyclerView!!.adapter = mImageAdapter
        }

        return rootView
    }

    private fun initView(rootView: View) {
        mSearchEditText = rootView.findViewById(R.id.search_edit_text)
        mSearchImageButton = rootView.findViewById(R.id.search_image_button)
        mWaitProgressBar = rootView.findViewById(R.id.wait_progress_bar)
        mNoNetworkTextView = rootView.findViewById(R.id.no_network_text_view)
        mEmptyListTextView = rootView.findViewById(R.id.empty_list_text_view)
        mImageRecyclerView = rootView.findViewById(R.id.images_recycler_view)
        mImageRecyclerView!!.layoutManager = LinearLayoutManager(activity)

        mLoaderManager = loaderManager

        // on Button click
        mSearchImageButton!!.setOnClickListener {
            if (NetworkUtils.isNetworkAvailable(context!!)) {
                if (mNoNetworkTextView!!.isShown) mNoNetworkTextView!!.visibility = View.GONE

                mSearchEditText!!.clearFocus()
                if (!TextUtils.isEmpty(mSearchEditText!!.text.toString().trim { it <= ' ' })) {
                    mWaitProgressBar!!.visibility = View.VISIBLE
                    if (mLoaderManager!!.getLoader<Any>(IMAGES_LOADER_ID) == null) {
                        mLoaderManager!!.initLoader(IMAGES_LOADER_ID, null, this@ImageListFragment)
                    } else {
                        mLoaderManager!!.restartLoader(IMAGES_LOADER_ID, null, this@ImageListFragment)
                    }
                } else {
                    showToast(rootView, getString(R.string.nothing_entered))
                }
            } else {
                mNoNetworkTextView!!.visibility = View.VISIBLE
            }
        }
    }

    private fun initValues(view: View) {
        mImageArrayList = ArrayList()
        mImageAdapter = ImageListAdapter(mImageArrayList)
        mImageRecyclerView!!.adapter = mImageAdapter
    }

    private fun showToast(view: View, msg: String) {
        Toast.makeText(view.context, msg, Toast.LENGTH_LONG).show()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onImageSelected(uri: Uri)
    }

    override fun onCreateLoader(i: Int, bundle: Bundle?): Loader<ArrayList<Image>> {
        return ImageListLoader(context!!, mSearchEditText!!.text.toString().trim { it <= ' ' })
    }

    override fun onLoadFinished(loader: Loader<ArrayList<Image>>, data: ArrayList<Image>?) {
        if (data != null && data.size != 0) {
            mImageAdapter.clear()
            mImageAdapter.addAll(data)
        }
        mWaitProgressBar!!.visibility = View.GONE
        mImageArrayList = data
    }

    override fun onLoaderReset(loader: Loader<ArrayList<Image>>) {
        mImageAdapter.clear()
        mImageArrayList = null
    }

    companion object {

        private val LOG_TAG = ImageListFragment::class.java.simpleName
        private val IMAGES_LOADER_ID = 1

        /**
         * This factory is used to create a new instance of fragment. We should uuse it for parameters
         *
         * @return A new instance of fragment ImageListFragment.
         */
        fun newInstance(): ImageListFragment {
            return ImageListFragment()
        }
    }
}// Required empty public constructor
