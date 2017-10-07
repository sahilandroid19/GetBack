package com.example.sahil.getback.ui;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
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

import com.example.sahil.getback.R;
import com.example.sahil.getback.adapters.TrailerAdapter;
import com.example.sahil.getback.extras.Utility;
import com.example.sahil.getback.loaders.TrailerLoader;

import java.io.IOException;
import java.util.List;


public class TrailerFragment extends Fragment implements
        TrailerAdapter.TrailerClicked, LoaderManager.LoaderCallbacks<List<String>> {

    private TrailerAdapter trailerAdapter;
    private List<String> trailerKeys;

    private TextView mEmptyView;
    private RecyclerView recyclerView;

    private String movieId;

    public TrailerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trailer, container, false);

        movieId = getArguments().getString("id");

        mEmptyView = view.findViewById(R.id.empty_trailer_view);

        recyclerView = view.findViewById(R.id.trailer_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        trailerAdapter = new TrailerAdapter(this, true);
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
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + trailerKeys.get(position)));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + trailerKeys.get(position)));
        try {
            getActivity().startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            getActivity().startActivity(webIntent);
        }
    }

    @Override
    public Loader<List<String>> onCreateLoader(int id, Bundle args) {
        return new TrailerLoader(getContext(), Utility.getTrailerUrl(movieId));
    }

    @Override
    public void onLoadFinished(Loader<List<String>> loader, List<String> data) {
        if(data != null && data.size() > 0){
            mEmptyView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            trailerAdapter.addTrailers(Utility.getTrailerFromKey(data));
            trailerKeys = data;
        }else {
            recyclerView.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onLoaderReset(Loader<List<String>> loader) {}

}
