package com.example.sahil.getback.extras;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Movie implements Parcelable {

    @SerializedName("id")
    private String id;

    @SerializedName("title")
    private String title;

    @SerializedName("poster_path")
    private String poster_url;

    @SerializedName("backdrop_path")
    private String backdrop_url;

    @SerializedName("overview")
    private String synopsis;

    @SerializedName("release_date")
    private String release_date;

    @SerializedName("vote_average")
    private String movie_rating;

    public Movie(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster_url() {
        return poster_url;
    }

    public void setPoster_url(String poster_url) {
        this.poster_url = poster_url;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getBackdrop_url() {
        return backdrop_url;
    }

    public void setBackdrop_url(String backdrop_url) {
        this.backdrop_url = backdrop_url;
    }

    protected Movie(Parcel in) {
        id = in.readString();
        title = in.readString();
        poster_url = in.readString();
        backdrop_url = in.readString();
        synopsis = in.readString();
        release_date = in.readString();
        movie_rating = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(poster_url);
        dest.writeString(backdrop_url);
        dest.writeString(synopsis);
        dest.writeString(release_date);
        dest.writeString(movie_rating);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getMovie_rating() {
        return movie_rating;
    }

    public void setMovie_rating(String movie_rating) {
        this.movie_rating = movie_rating;
    }
}