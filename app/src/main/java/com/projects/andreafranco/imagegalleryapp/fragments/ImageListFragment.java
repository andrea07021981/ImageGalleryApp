package com.projects.andreafranco.imagegalleryapp.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.projects.andreafranco.imagegalleryapp.R;
import com.projects.andreafranco.imagegalleryapp.adapters.ImageListAdapter;
import com.projects.andreafranco.imagegalleryapp.loaders.ImageListLoader;
import com.projects.andreafranco.imagegalleryapp.models.Image;
import com.projects.andreafranco.imagegalleryapp.utils.NetworkUtils;

import java.util.ArrayList;


public class ImageListFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<Image>> {

    private static final String LOG_TAG = ImageListFragment.class.getSimpleName();
    private static final int IMAGES_LOADER_ID = 1;

    EditText mSearchEditText = null;
    ImageButton mSearchImageButton = null;
    ProgressBar mWaitProgressBar = null;
    TextView mNoNetworkTextView = null;
    TextView mEmptyListTextView = null;
    RecyclerView mImageRecyclerView = null;
    LoaderManager mLoaderManager = null;
    ArrayList<Image> mImageArrayList;
    ImageListAdapter mImageAdapter;

    private OnFragmentInteractionListener mListener;

    public ImageListFragment() {
        // Required empty public constructor
    }

    /**
     * This factory is used to create a new instance of fragment. We should uuse it for parameters
     *
     * @return A new instance of fragment ImageListFragment.
     */
    public static ImageListFragment newInstance() {
        ImageListFragment fragment = new ImageListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.image_list_fragment, container, false);

        // initialize views and variables
        initView(rootView);
        initValues(rootView);

        if (mImageArrayList.size() == 0 && savedInstanceState == null) {
            showToast(rootView, getString(R.string.search_text));
        } else {
            mImageRecyclerView.setAdapter(mImageAdapter);
        }

        return rootView;
    }

    private void initView(final View rootView) {
        mSearchEditText = rootView.findViewById(R.id.search_edit_text);
        mSearchImageButton = rootView.findViewById(R.id.search_image_button);
        mWaitProgressBar = rootView.findViewById(R.id.wait_progress_bar);
        mNoNetworkTextView = rootView.findViewById(R.id.no_network_text_view);
        mEmptyListTextView = rootView.findViewById(R.id.empty_list_text_view);
        mImageRecyclerView = rootView.findViewById(R.id.images_recycler_view);
        mImageRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mLoaderManager = getLoaderManager();

        // on Button click
        mSearchImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NetworkUtils.isNetworkAvailable(getContext())) {
                    if (mNoNetworkTextView.isShown()) mNoNetworkTextView.setVisibility(View.GONE);

                    mSearchEditText.clearFocus();
                    if (!TextUtils.isEmpty(mSearchEditText.getText().toString().trim())) {
                        mWaitProgressBar.setVisibility(View.VISIBLE);
                        if (mLoaderManager.getLoader(IMAGES_LOADER_ID) == null) {
                            mLoaderManager.initLoader(IMAGES_LOADER_ID, null, ImageListFragment.this);
                        } else {
                            mLoaderManager.restartLoader(IMAGES_LOADER_ID, null, ImageListFragment.this);
                        }
                    } else {
                        showToast(rootView, getString(R.string.nothing_entered));
                    }
                } else {
                    mNoNetworkTextView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void initValues(View view) {
        mImageArrayList = new ArrayList<>();
        mImageAdapter = new ImageListAdapter(mImageArrayList);
        mImageRecyclerView.setAdapter(mImageAdapter);
    }

    private void showToast(View view, String msg) {
        Toast.makeText(view.getContext(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onImageSelected(Uri uri);
    }

    @NonNull
    @Override
    public Loader<ArrayList<Image>> onCreateLoader(int i, @Nullable Bundle bundle) {
        return new ImageListLoader(getContext(), mSearchEditText.getText().toString().trim());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<Image>> loader, ArrayList<Image> data) {
        if (data != null && data.size() != 0) {
            mImageAdapter.clear();
            mImageAdapter.addAll(data);
        }
        mWaitProgressBar.setVisibility(View.GONE);
        mImageArrayList = data;
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<Image>> loader) {
        mImageAdapter.clear();
        mImageArrayList = null;
    }
}
