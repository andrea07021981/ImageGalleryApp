package com.projects.andreafranco.imagegalleryapp.models

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable

class Image : Parcelable {

    var title: String? = null
    var size: Double = 0.toDouble()
    var width: Int = 0
    var height: Int = 0
    var image: Bitmap? = null
        private set

    constructor(title: String, size: Double, width: Int, height: Int, image: Bitmap) {
        this.title = title
        this.size = size
        this.width = width
        this.height = height
        this.image = image
    }

    fun setmImage(image: Bitmap) {
        this.image = image
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeParcelable(this.image, flags)
        dest.writeString(this.title)
        dest.writeDouble(this.size)
        dest.writeInt(this.width)
        dest.writeInt(this.height)
    }

    protected constructor(`in`: Parcel) {
        this.image = `in`.readParcelable(null)
        this.title = `in`.readString()
        this.size = `in`.readDouble()
        this.width = `in`.readInt()
        this.height = `in`.readInt()
    }

    companion object CREATOR : Parcelable.Creator<Image> {
            override fun createFromParcel(source: Parcel): Image {
                return Image(source)
            }

            override fun newArray(size: Int): Array<Image?> {
                return arrayOfNulls(size)
            }
        }
}

