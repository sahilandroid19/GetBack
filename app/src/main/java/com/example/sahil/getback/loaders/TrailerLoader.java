package com.example.sahil.getback.loaders;


import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.github.kevinsawicki.http.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TrailerLoader extends AsyncTaskLoader<List<String>>{

    private String trailerUrl;
    private List<String> mTrailerKeys = new ArrayList<>();

    public TrailerLoader(Context context, String url) {
        super(context);
        trailerUrl = url;
    }

    @Override
    public List<String> loadInBackground() {
        String json = HttpRequest.get(trailerUrl).body();

        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray results = jsonObject.getJSONArray("results");

            for(int i=0; i<results.length(); i++){
                JSONObject trailer = results.getJSONObject(i);
                mTrailerKeys.add(trailer.getString("key"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return mTrailerKeys;
    }
}
