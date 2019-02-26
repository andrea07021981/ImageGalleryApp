package com.projects.andreafranco.imagegalleryapp.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.projects.andreafranco.imagegalleryapp.R
import com.projects.andreafranco.imagegalleryapp.models.Image

import java.util.ArrayList


class ImageListAdapter(private var mImageList: MutableList<Image>?) : RecyclerView.Adapter<ImageListAdapter.ImageListHolder>() {

    /**
     * View holder class
     */
    inner class ImageListHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        internal var mImageImageVIew: ImageView
        internal var mTitleTextView: TextView
        internal var mSizeTextView: TextView
        internal var mDimensionsTextView: TextView
        internal lateinit var mImage: Image

        init {
            mImageImageVIew = view.findViewById<View>(R.id.image_image_view) as ImageView
            mTitleTextView = view.findViewById<View>(R.id.title_text_view) as TextView
            mSizeTextView = view.findViewById<View>(R.id.size_text_view) as TextView
            mDimensionsTextView = view.findViewById<View>(R.id.dimensions_text_view) as TextView
        }

        fun bindImage(image: Image) {
            mImage = image
            mImageImageVIew.setImageBitmap(mImage.image)
            mTitleTextView.text = mImage.title
            val sizeFormat = itemView.context.getString(R.string.format_size)
            mSizeTextView.text = String.format(sizeFormat, mImage.size / 1024L)
            val dimensionFormat = itemView.context.getString(R.string.format_dimensions)
            mDimensionsTextView.text = String.format(dimensionFormat, mImage.width, mImage.height)
        }

        override fun onClick(v: View) {
            //TODO action on click
        }
    }

    override fun onBindViewHolder(holder: ImageListHolder, position: Int) {
        val image = mImageList!![position]
        holder.bindImage(image)
    }

    override fun getItemCount(): Int {
        return mImageList!!.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageListHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_image, parent, false)
        return ImageListHolder(v)
    }

    /**
     * Clears all items from the data set.
     */
    fun clear() {
        if (mImageList != null) {
            mImageList!!.clear()
            notifyDataSetChanged()
        }
    }

    /**
     * Adds all of the items to the data set.
     * @param items The item array to be added.
     */
    fun addAll(items: List<Image>) {
        if (mImageList == null) {
            mImageList = ArrayList()
        }
        mImageList!!.addAll(items)
        notifyDataSetChanged()
    }
}