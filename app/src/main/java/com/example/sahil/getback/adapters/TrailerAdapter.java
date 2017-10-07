
package com.example.sahil.getback.adapters;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sahil.getback.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder>  {

    private List<String> mTrailerList = new ArrayList<>();

    private TrailerClicked trailerClicked;
    private boolean indication = true;

    public TrailerAdapter(TrailerClicked movieClicked, boolean ind) {
        this.trailerClicked = movieClicked;
        indication = ind;
    }

    public interface TrailerClicked{
        void onTrailerClick(int position);
    }

    @Override
    public TrailerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if(indication){
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.trailer_list_row, parent, false);
        }else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.review_list_row, parent, false);
        }
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(indication) {
            Picasso.with(holder.posterView.getContext()).
                    load(mTrailerList.get(position)).into(holder.posterView);
        }else {
            holder.reviewView.setText(mTrailerList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        if(mTrailerList != null){
            return mTrailerList.size();
        }else {
            return 0;
        }
    }

    public void addTrailers(List<String> trailers){
        mTrailerList.clear();
        mTrailerList = trailers;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView posterView;
        TextView reviewView;

        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            if(indication) {
                posterView = itemView.findViewById(R.id.trailer_poster);
            }else {
                reviewView = itemView.findViewById(R.id.review_comment);
            }
        }

        @Override
        public void onClick(View view) {
            trailerClicked.onTrailerClick(getAdapterPosition());
        }
    }
}
