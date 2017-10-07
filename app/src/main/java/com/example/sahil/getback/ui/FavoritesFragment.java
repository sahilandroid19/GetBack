package com.example.sahil.getback.ui;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sahil.getback.R;
import com.example.sahil.getback.adapters.MovieAdapter;
import com.example.sahil.getback.backend.Contract;
import com.example.sahil.getback.extras.Movie;
import com.example.sahil.getback.extras.Utility;

import java.io.IOException;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoritesFragment extends Fragment implements
        MovieAdapter.MovieClicked, LoaderManager.LoaderCallbacks<Cursor> {

    private List<Movie> movies;

    private TextView mEmptyView;
    private RecyclerView recyclerView;

    private MovieAdapter movieAdapter;

    private boolean isConnected;

    public FavoritesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        try {
            isConnected = Utility.isConnected();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }

        getLoaderManager().initLoader(2, null, this);

        mEmptyView = view.findViewById(R.id.empty_view);

        recyclerView = view.findViewById(R.id.fav_view);

        if(isConnected) {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2, LinearLayoutManager.VERTICAL, false));
        }else {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        }
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        movieAdapter = new MovieAdapter(this, isConnected);
        recyclerView.setAdapter(movieAdapter);

        return view;
    }

    @Override
    public void onMovieClick(int position) {
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra("movie", movies.get(position));
        startActivity(intent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                getContext(),
                Contract.Movies.CONTENT_URI,
                null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data != null && data.getCount() > 0){
            mEmptyView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            movies = Utility.getMoviesFromCursor(data);
            movieAdapter.addMovies(movies);
        }else {
            recyclerView.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {}
}
