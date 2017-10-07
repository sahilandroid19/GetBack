package com.example.sahil.getback.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sahil.getback.R;
import com.example.sahil.getback.adapters.TrailerAdapter;
import com.example.sahil.getback.extras.Utility;
import com.example.sahil.getback.loaders.ReviewLoader;

import java.io.IOException;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewsFragment extends Fragment implements
        TrailerAdapter.TrailerClicked, LoaderManager.LoaderCallbacks<List<String>>{

    private String movieId;

    private TextView mEmptyView;
    private RecyclerView recyclerView;

    private TrailerAdapter trailerAdapter;

    public ReviewsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reviews, container, false);

        movieId = getArguments().getString("id");

        mEmptyView = view.findViewById(R.id.empty_reviews_view);

        recyclerView = view.findViewById(R.id.review_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        trailerAdapter = new TrailerAdapter(this, false);
        recyclerView.setAdapter(trailerAdapter);

        try {
            if(Utility.isConnected()) {
                getLoaderManager().initLoader(1, null, this).forceLoad();
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }

        return view;
    }

    @Override
    public void onTrailerClick(int position) {
        Toast.makeText(getContext(), "Review Clicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    public Loader<List<String>> onCreateLoader(int id, Bundle args) {
        return new ReviewLoader(getContext(), Utility.getReviewUrl(movieId));
    }

    @Override
    public void onLoadFinished(Loader<List<String>> loader, List<String> data) {
        if(data != null && data.size() > 0){
            mEmptyView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            trailerAdapter.addTrailers(data);
        }else {
            recyclerView.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onLoaderReset(Loader<List<String>> loader) {}
}
