package com.projects.andreafranco.imagegalleryapp.utils;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.projects.andreafranco.imagegalleryapp.models.Image;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class HttpUtils {

    private static final String LOG_TAG = HttpUtils.class.getSimpleName();
    private static final Uri.Builder sUriBuilder = buildUriMatcher();

    public static Uri.Builder buildUriMatcher() {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("api.flickr.com")
                .appendPath("services")
                .appendPath("rest")
                .appendQueryParameter("method", "flickr.photos.search")
                .appendQueryParameter("api_key", "949e98778755d1982f537d56236bbb42")
                .appendQueryParameter("format", "json")
                .appendQueryParameter("per_page", "20")
                .appendQueryParameter("media", "photos");
        return builder;
    }

    //EXAMPLE CALL, REMBER HTTPS INSTEAD OF HTTP
    //https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=949e98778755d1982f537d56236bbb42&tags=android&format=json&per_page=20&media=photos


    public static ArrayList<Image> fetchImageListData(String query) {
        sUriBuilder.appendQueryParameter("tags", query);
        URL url = createUrl(sUriBuilder.build().toString());
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error during connection", e);
        }

        ArrayList<Image> imageArrayList = extractFeatureFromJson(jsonResponse);;
        return imageArrayList;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = null;
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;

        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.connect();
            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = httpURLConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error with connection code " + httpURLConnection.getResponseCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) {
        StringBuilder stringBuilder = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            try {
                String line = bufferedReader.readLine();
                while (line != null) {
                    stringBuilder.append(line);
                    line = bufferedReader.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return stringBuilder.toString();
    }

    private static URL createUrl(String query) {
        URL url = null;
        try {
            url = new URL(query);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error creating URL", e);
        }
        return url;
    }

    private static ArrayList<Image> extractFeatureFromJson(String jsonResponse) {
        if (jsonResponse == null || TextUtils.isEmpty(jsonResponse)) {
            return null;
        }

        //TODO extract data
        ArrayList<Image> imagesList = new ArrayList<>();

        return imagesList;
    }
}
