package com.example.android.popularmovies_1;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    //Put the API key here
    private String key = "";

    private String LOG_TAG;
    private Toast mToast;
    private MovieAdapter movieAdapter;
    private RecyclerView mRecyclerView;
    private String request_url_popular = "http://api.themoviedb.org/3/movie/popular?api_key=" + key;
    private String request_url_top_rated = "http://api.themoviedb.org/3/movie/top_rated?api_key=" + key;
    private TextView Empty;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(false);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        movieAdapter = new MovieAdapter(this, this);
        String requestURL_default = "http://api.themoviedb.org/3/movie/popular?api_key=" + key;
        Empty = (TextView) findViewById(R.id.empty);
        progressBar = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        loadMovies(requestURL_default);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int popularID = R.id.action_most_popular;
        int topID = R.id.action_top_rated;
        int clickedID = item.getItemId();
        if (clickedID == popularID) {
            loadMovies(request_url_popular);
            return true;
        }

        if (clickedID == topID) {
            loadMovies(request_url_top_rated);
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    private void loadMovies(String requestURL) {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = connMgr.getActiveNetworkInfo();

        if (nInfo == null || !nInfo.isConnected()) {
            mRecyclerView.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            Empty.setText("No connection.");
            Empty.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);
            Empty.setVisibility(View.GONE);
            new MovieTask().execute(requestURL);
        }
    }

    @Override
    public void onClick(MovieDataClass thisMovie) {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = connMgr.getActiveNetworkInfo();

        if (nInfo == null || !nInfo.isConnected()) {
            mToast.makeText(this, "Please check your connection!", Toast.LENGTH_SHORT).show();
            return;
        }

        mToast.makeText(this, thisMovie.getName(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        Bundle extras = new Bundle();

        extras.putString("EXTRA_CURRENT_NAME", thisMovie.getName());
        extras.putString("EXTRA_CURRENT_IMAGE", thisMovie.getImageURL());
        extras.putString("EXTRA_CURRENT_RELEASE", thisMovie.getDate());
        extras.putString("EXTRA_CURRENT_VOTE", thisMovie.getVoteAvg());
        extras.putString("EXTRA_CURRENT_SYNOPSIS", thisMovie.getSynopsis());

        intent.putExtras(extras);
        startActivity(intent);
    }

    private class MovieTask extends AsyncTask<String, Void, ArrayList<MovieDataClass>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected ArrayList<MovieDataClass> doInBackground(String... strings) {
            if (strings.length == 0) {
                return null;
            }

            String URL = strings[0];

            try {
                return NetworkUtils.networkReq(URL);
            } catch (Exception e) {
                Log.e(LOG_TAG, "Null returned as MovieDataClasses' ArrayList");
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<MovieDataClass> movieDataClasses) {

            if (movieDataClasses != null) {
                movieAdapter.setImageURLs(movieDataClasses);
                mRecyclerView.setAdapter(movieAdapter);
                progressBar.setVisibility(View.GONE);
            } else {
                return;
            }
        }
    }
}
