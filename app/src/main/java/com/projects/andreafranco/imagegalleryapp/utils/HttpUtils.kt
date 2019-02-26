package com.projects.andreafranco.imagegalleryapp.utils

import android.annotation.TargetApi
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.text.TextUtils
import android.util.Log

import com.projects.andreafranco.imagegalleryapp.models.Image
import com.projects.andreafranco.imagegalleryapp.utils.HttpUtils.extractDataFromJson

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.ArrayList


object HttpUtils {

    private val LOG_TAG = HttpUtils::class.java.simpleName
    private val sUriBuilder = buildUriFlickr()

    private fun buildUriFlickr(): Uri.Builder {
        val builder = Uri.Builder()
        builder.scheme("https")
                .authority("api.flickr.com")
                .appendPath("services")
                .appendPath("rest")
                .appendQueryParameter("method", "flickr.photos.search")
                .appendQueryParameter("api_key", "949e98778755d1982f537d56236bbb42")
                .appendQueryParameter("format", "json")
                .appendQueryParameter("per_page", "20")
                .appendQueryParameter("media", "photos")
        return builder
    }

    /**
     * User the query parameter for creating url and getting the List of images
     * @param query
     * @return
     */
    fun fetchImageListData(query: String): ArrayList<Image>? {
        //TODO CHANGE THE CONSTRUCTION OF THE QUERY, IT KEEPS THE OLD TAGS
        sUriBuilder.appendQueryParameter("tags", query)
        val url = createUrl(sUriBuilder.build().toString())
        var jsonResponse: String? = null
        try {
            jsonResponse = makeHttpRequest(url)
        } catch (e: IOException) {
            Log.e(LOG_TAG, "Error during connection", e)
        }

        return extractDataFromJson(jsonResponse)
    }

    @Throws(IOException::class)
    private fun makeHttpRequest(url: URL?): String? {
        var jsonResponse: String? = null
        if (url == null) {
            return jsonResponse
        }

        var httpURLConnection: HttpURLConnection? = null
        var inputStream: InputStream? = null

        try {
            httpURLConnection = url.openConnection() as HttpURLConnection
            httpURLConnection.requestMethod = "GET"
            httpURLConnection.connectTimeout = 15000
            httpURLConnection.readTimeout = 10000
            httpURLConnection.connect()
            if (httpURLConnection.responseCode == HttpURLConnection.HTTP_OK) {
                inputStream = httpURLConnection.inputStream
                jsonResponse = readFromStream(inputStream)
            } else {
                Log.e(LOG_TAG, "Error with connection code " + httpURLConnection.responseCode)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            httpURLConnection?.disconnect()
            inputStream?.close()
        }
        return jsonResponse
    }

    private fun readFromStream(inputStream: InputStream?): String {
        val stringBuilder = StringBuilder()
        if (inputStream != null) {
            val inputStreamReader = InputStreamReader(inputStream)
            val bufferedReader = BufferedReader(inputStreamReader)
            try {
                var line: String? = bufferedReader.readLine()
                while (line != null) {
                    stringBuilder.append(line)
                    line = bufferedReader.readLine()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        return stringBuilder.toString()
    }

    private fun createUrl(query: String): URL? {
        var url: URL? = null
        try {
            url = URL(query)
        } catch (e: MalformedURLException) {
            Log.e(LOG_TAG, "Error creating URL", e)
        }

        return url
    }

    private fun extractDataFromJson(jsonResponse: String?): ArrayList<Image>? {
        if (jsonResponse == null || TextUtils.isEmpty(jsonResponse)) {
            return null
        }

        val imagesList = ArrayList<Image>()

        try {
            val root = JSONObject(jsonResponse.replace("jsonFlickrApi(", "").replace(")", ""))
            val photos = root.getJSONObject("photos")
            val imageJSONArray = photos.getJSONArray("photo")
            for (i in 0 until imageJSONArray.length()) {
                val item = imageJSONArray.getJSONObject(i)
                val url = "http://farm" +
                        item.getString("farm") +
                        ".staticflickr.com/" +
                        item.getString("server") +
                        "/" + item.getString("id") + "_" +
                        item.getString("secret") +
                        "_t" +
                        ".jpg"
                val image = createBitmap(url)
                var size = 0.0
                var width = 0
                var height = 0
                if (image != null) {
                    size = getSizeOf(image).toDouble()
                    width = image.width
                    height = image.height
                }
                val title = item.getString("title")
                imagesList.add(Image(title, size, width, height, image!!))
            }
        } catch (e: JSONException) {
            Log.e(LOG_TAG, "Error parsing json ")
        }

        return imagesList
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    internal fun getSizeOf(data: Bitmap): Int {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR1) {
            data.rowBytes * data.height
        } else {
            data.byteCount
        }
    }

    private fun createBitmap(url: String): Bitmap? {
        var bitmap: Bitmap? = null
        try {
            bitmap = BitmapFactory.decodeStream(URL(url).content as InputStream)
        } catch (e: IOException) {
            Log.e(LOG_TAG, e.message)
        }

        return bitmap
    }
}
