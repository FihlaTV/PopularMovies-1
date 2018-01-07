package com.example.android.popularmovies_1;

import android.text.TextUtils;
import android.util.Log;

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
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by Arjun Vidyarthi on 06-Jan-18.
 */

public class NetworkUtils {

    private static final String LOG_TAG = "";

    private NetworkUtils() {
        //to prevent instantiating this class.
    }

    private static URL convertToURL(String url) {
        URL URL = null;
        try {
            URL = new URL(url);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return URL;
    }

    private static String getFromStream(InputStream inputStream) throws IOException {

        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();

    }

    private static String makeHTTPRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = getFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static ArrayList<MovieDataClass> parseResponse(String JSONResponse) {

        if (TextUtils.isEmpty(JSONResponse)) {
            return null;
        }
        ArrayList<MovieDataClass> movieList = new ArrayList<>();

        try {
            JSONObject baseJsonResponse = new JSONObject(JSONResponse);
            JSONArray resultsArray = baseJsonResponse.getJSONArray("results");

            for (int i = 0; i < resultsArray.length(); i++) {

                JSONObject currentMovie = resultsArray.getJSONObject(i);

                String title = currentMovie.getString("title");
                String releaseDate = currentMovie.getString("release_date");
                String voteAvg = currentMovie.getString("vote_average");
                String imageURLRaw = currentMovie.getString("poster_path");
                String imageURL = "http://image.tmdb.org/t/p/w342/" + imageURLRaw;
                String synopsis = currentMovie.getString("overview");

                MovieDataClass movie = new MovieDataClass(title, releaseDate, voteAvg, synopsis, imageURL);

                movieList.add(movie);
            }
        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the JSON results", e);
        }

        return movieList;
    }

    public static ArrayList<MovieDataClass> networkReq(String url) throws JSONException {

        Log.e(LOG_TAG, "onNetworkReq");

        URL URL = convertToURL(url);

        String jsonResponse = null;

        try {
            jsonResponse = makeHTTPRequest(URL);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        ArrayList<MovieDataClass> movieList = parseResponse(jsonResponse);

        return movieList;
    }
}
