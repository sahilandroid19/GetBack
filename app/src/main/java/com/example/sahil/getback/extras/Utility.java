package com.example.sahil.getback.extras;


import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.example.sahil.getback.BuildConfig;
import com.example.sahil.getback.backend.Contract;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Utility {

    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w185/";
    private static final String IMAGE_BACKDROP_URL = "http://image.tmdb.org/t/p/w500/";

    private static final String TRAILER_BASE_URL = "http://api.themoviedb.org/3/movie";
    private static final String TRAILER_IMAGE_URL = "https://img.youtube.com/vi";

    public static String buildUrl(String poster_path){
        return IMAGE_BASE_URL + poster_path;
    }

    public static String buildBackdropUrl(String backdrop_path){
        return IMAGE_BACKDROP_URL + backdrop_path;
    }

    public static float getRating(String rating){
        float rat = Float.parseFloat(rating);
        float per = rat * 10;
        return ((per * 5)/100);
    }

    public static String getTrailerUrl(String id){

        Uri uri = Uri.parse(TRAILER_BASE_URL)
                .buildUpon()
                .appendPath(id)
                .appendPath("videos")
                .appendQueryParameter("api_key", BuildConfig.api_key).build();

        return uri.toString();
    }

    public static List<String> getTrailerFromKey(List<String> keys){
        List<String> trailers = new ArrayList<>();

        for(int i=0; i<keys.size(); i++){
            Uri uri = Uri.parse(TRAILER_IMAGE_URL)
                    .buildUpon()
                    .appendPath(keys.get(i))
                    .appendPath("hqdefault.jpg").build();
            trailers.add(uri.toString());
        }
        return trailers;
    }

    public static String getReviewUrl(String id){

        Uri uri = Uri.parse(TRAILER_BASE_URL)
                .buildUpon()
                .appendPath(id)
                .appendPath("reviews")
                .appendQueryParameter("api_key", BuildConfig.api_key).build();

        return uri.toString();
    }

    public static ContentValues getValuesForMovie(Movie movie){

        ContentValues values = new ContentValues();
        values.put(Contract.Movies._ID, movie.getId());
        values.put(Contract.Movies.MOVIE_TITLE, movie.getTitle());
        values.put(Contract.Movies.MOVIE_POSTER, movie.getPoster_url());
        values.put(Contract.Movies.MOVIE_BACKDROP, movie.getBackdrop_url());
        values.put(Contract.Movies.MOVIE_OVERVIEW, movie.getSynopsis());
        values.put(Contract.Movies.MOVIE_RATING, movie.getMovie_rating());
        values.put(Contract.Movies.MOVIE_DATE, movie.getRelease_date());

        return values;
    }

    public static List<Movie> getMoviesFromCursor(Cursor cursor){
        List<Movie> movies = new ArrayList<>();

        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            Movie movie = new Movie();
            movie.setId(cursor.getString(0));
            movie.setPoster_url(cursor.getString(2));
            movie.setTitle(cursor.getString(1));
            movie.setBackdrop_url(cursor.getString(3));
            movie.setSynopsis(cursor.getString(4));
            movie.setMovie_rating(cursor.getString(5));
            movie.setRelease_date(cursor.getString(6));
            movies.add(movie);
        }

        return movies;
    }

    public static boolean isConnected() throws InterruptedException, IOException
    {
        String command = "ping -c 1 google.com";
        return (Runtime.getRuntime().exec (command).waitFor() == 0);
    }
}
