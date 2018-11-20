package com.projects.andreafranco.imagegalleryapp.utils;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.projects.andreafranco.imagegalleryapp.models.Image;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    private static final Uri.Builder sUriBuilder = buildUriFlickr();

    private static Uri.Builder buildUriFlickr() {
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

    /**
     * User the query parameter for creating url and getting the List of images
     * @param query
     * @return
     */
    public static ArrayList<Image> fetchImageListData(String query) {
        //TODO CHANGE THE CONSTRUCTION OF THE QUERY, IT KEEPS THE OLD TAGS
        sUriBuilder.appendQueryParameter("tags", query);
        URL url = createUrl(sUriBuilder.build().toString());
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error during connection", e);
        }

        ArrayList<Image> imageArrayList = extractDataFromJson(jsonResponse);;
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

    private static ArrayList<Image> extractDataFromJson(String jsonResponse) {
        if (jsonResponse == null || TextUtils.isEmpty(jsonResponse)) {
            return null;
        }

        ArrayList<Image> imagesList = new ArrayList<>();

        try {
            JSONObject root = new JSONObject(jsonResponse.replace("jsonFlickrApi(", "").replace(")", ""));
            JSONObject photos = root.getJSONObject("photos");
            JSONArray imageJSONArray = photos.getJSONArray("photo");
            for (int i = 0; i < imageJSONArray.length(); i++) {
                JSONObject item = imageJSONArray.getJSONObject(i);
                String url = "http://farm" +
                        item.getString("farm") +
                        ".staticflickr.com/" +
                        item.getString("server") +
                        "/" + item.getString("id") + "_" +
                        item.getString("secret") +
                        "_t" +
                        ".jpg";
                Bitmap image = createBitmap(url);
                double size = 0L;
                int width = 0;
                int height = 0;
                if (image != null) {
                    size = new Integer(getSizeOf(image)).doubleValue();
                    width = image.getWidth();
                    height = image.getHeight();
                }
                String title = item.getString("title");
                imagesList.add(new Image(title, size, width, height, image));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error parsing json ");
        }
        return imagesList;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    protected static int getSizeOf(Bitmap data) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR1) {
            return data.getRowBytes() * data.getHeight();
        } else {
            return data.getByteCount();
        }
    }

    private static Bitmap createBitmap(String url) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream((InputStream) new URL(url).getContent());
        } catch (IOException e) {
            Log.e(LOG_TAG, e.getMessage());
        }
        return bitmap;
    }
}
