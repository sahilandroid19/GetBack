package com.example.sahil.getback.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.sahil.getback.BuildConfig;
import com.example.sahil.getback.R;
import com.example.sahil.getback.adapters.MovieAdapter;
import com.example.sahil.getback.extras.Movie;
import com.example.sahil.getback.extras.Utility;
import com.example.sahil.getback.retrofit.ApiClient;
import com.example.sahil.getback.retrofit.ApiInterface;
import com.example.sahil.getback.retrofit.MovieResponse;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MoviesFragment extends Fragment implements MovieAdapter.MovieClicked {

    private List<Movie> movies;

    private MovieAdapter movieAdapter;

    private boolean isConnected;

    public MoviesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movies, container, false);

        String message = getArguments().getString("message");

        try {
            isConnected = Utility.isConnected();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }

        if(isConnected){
            RecyclerView recyclerView = view.findViewById(R.id.movie_view);
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2, LinearLayoutManager.VERTICAL, false));
            recyclerView.setItemAnimator(new DefaultItemAnimator());

            movieAdapter = new MovieAdapter(this, isConnected);
            recyclerView.setAdapter(movieAdapter);

            if(message.equals("Top Rated Movies")){
                getTopRatedMovies();
            }else {
                getPopularMovies();
            }
        }else {
            if(!(message.equals("Top Rated Movies"))) {
                Toast.makeText(getContext(), "No Internet Available!!Only Favorite Movies can be seen", Toast.LENGTH_LONG).show();
            }
        }
        return view;
    }

    private void getTopRatedMovies(){
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<MovieResponse> call = apiInterface.getTopRatedMovies(BuildConfig.api_key);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                movies = response.body().getMovies();
                movieAdapter.addMovies(movies);
            }

            @Override
            public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {
                Log.e("Error", "Movies not received");
            }
        });
    }

    private void getPopularMovies(){
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<MovieResponse> call = apiInterface.getPopularMovies(BuildConfig.api_key);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                movies = response.body().getMovies();
                movieAdapter.addMovies(movies);
            }

            @Override
            public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {
                Log.e("Error", "Movies not received");
            }
        });
    }

    @Override
    public void onMovieClick(int position) {
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra("movie", movies.get(position));
        startActivity(intent);
    }
}
