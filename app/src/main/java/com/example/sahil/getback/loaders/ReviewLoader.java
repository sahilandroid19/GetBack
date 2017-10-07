package com.example.sahil.getback.loaders;


import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.github.kevinsawicki.http.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ReviewLoader extends AsyncTaskLoader<List<String>>{

    private String reviewUrl;
    private List<String> mReviews = new ArrayList<>();

    public ReviewLoader(Context context, String url) {
        super(context);
        reviewUrl = url;
    }

    @Override
    public List<String> loadInBackground() {
        String json = HttpRequest.get(reviewUrl).body();

        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray results = jsonObject.getJSONArray("results");

            for(int i=0; i<results.length(); i++){
                JSONObject review = results.getJSONObject(i);
                mReviews.add(review.getString("content"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return mReviews;
    }
}
