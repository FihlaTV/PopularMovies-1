package com.example.android.popularmovies_1;

/**
 * Created by Arjun Vidyarthi on 05-Jan-18.
 */

public class MovieDataClass {
    private String imageURL;
    private String name;
    private String date;
    private String voteAvg;
    private String synopsis;

    public MovieDataClass(String name, String date, String voteAvg, String synopsis, String imageURL) {
        this.name = name;
        this.date = date;
        this.voteAvg = voteAvg;
        this.synopsis = synopsis;
        this.imageURL = imageURL;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getVoteAvg() {
        return voteAvg;
    }

    public String getSynopsis() {
        return synopsis;
    }
}
