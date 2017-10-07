package com.example.sahil.getback.adapters;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.sahil.getback.R;
import com.example.sahil.getback.extras.Movie;
import com.example.sahil.getback.extras.Utility;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder>  {

    private List<Movie> mMovieList;

    private MovieClicked movieClicked;

    private boolean isConnected;

    public MovieAdapter(MovieClicked movieClicked, boolean connected) {
        this.movieClicked = movieClicked;
        isConnected = connected;
    }

    public interface MovieClicked{
         void onMovieClick(int position);
    }

    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

        if(isConnected) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.movie_list_row, parent, false);
        }else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.no_net_list_row, parent, false);
        }
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(isConnected) {
            Picasso.with(holder.posterView.getContext()).
                    load(Utility.buildUrl(mMovieList.get(position).getPoster_url())).into(holder.posterView);
        }
        holder.titleView.setText(mMovieList.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return mMovieList != null? mMovieList.size() : 0;
    }

    public void addMovies(List<Movie> movies){
        mMovieList = movies;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView posterView;
        TextView titleView;

        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            if(isConnected) {
                posterView = itemView.findViewById(R.id.movie_poster);
                titleView = itemView.findViewById(R.id.movie_title);
            }else {
                titleView = itemView.findViewById(R.id.movie_title_no_net);
            }
        }

        @Override
        public void onClick(View view) {
            movieClicked.onMovieClick(getAdapterPosition());
        }
    }
}
