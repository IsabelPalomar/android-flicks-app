package io.androidblog.flickster.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import io.androidblog.flickster.R;
import io.androidblog.flickster.models.Movie;

public class MovieRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Movie> mMovies;
    private final int NORMAL = 0, POPULAR = 1;

    public MovieRecyclerViewAdapter(Context context, List<Movie> movies){
        mContext = context;
        mMovies = movies;
    }

    private Context getContext() {
        return mContext;
    }

    @Override
    public int getItemViewType(int position) {

        if (mMovies.get(position).getVoteAverage() < 5) {
            return NORMAL;
        }else{
            return POPULAR;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        RecyclerView.ViewHolder viewHolder = null;

        switch (viewType) {
            case NORMAL:
                View movieView = inflater.inflate(R.layout.item_movie, parent, false);
                viewHolder = new ViewHolder(movieView);
                break;
            case POPULAR:
                View popularMovieView = inflater.inflate(R.layout.item_popular_movie, parent, false);
                viewHolder = new ViewHolderPopular(popularMovieView);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType()) {
            case NORMAL:
                ViewHolder vhNormal = (ViewHolder) holder;
                configureViewNormalHolder(vhNormal, position);
                break;
            case POPULAR:
                ViewHolderPopular vhPopular = (ViewHolderPopular) holder;
                configureViewPopularHolder(vhPopular, position);
                break;
        }

    }

    private void configureViewNormalHolder(ViewHolder vhNormal, int position) {

        Movie movie = mMovies.get(position);

        // Set item views based on your views and data model
        ImageView ivImage = vhNormal.ivImage;
        TextView tvTitle = vhNormal.tvTitle;
        TextView tvOverview = vhNormal.tvOverview;

        //populate data
        tvTitle.setText(movie.getOriginalTitle());
        tvOverview.setText(movie.getOverview());

        Picasso.with(getContext())
                .load(getImagePath(movie))
                .placeholder(getPlaceHolderImg())
                .into(ivImage);
    }

    private void configureViewPopularHolder(ViewHolderPopular vhPopular, int position) {

        Movie movie = mMovies.get(position);

        // Set item views based on your views and data model
        ImageView ivImage = vhPopular.ivImage;

        Picasso.with(getContext())
                .load(movie.getBackdropPath())
                .placeholder(getPlaceHolderImg())
                .into(ivImage);

    }


    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    private String getImagePath(Movie movie) {

        if(getContext().getResources().getConfiguration().orientation ==  Configuration.ORIENTATION_PORTRAIT){
            return movie.getPosterPath();
        }else{
            return movie.getBackdropPath();
        }

    }

    private int getPlaceHolderImg() {

        if(getContext().getResources().getConfiguration().orientation ==  Configuration.ORIENTATION_PORTRAIT){
            return R.drawable.poster_placeholder;
        }else{
            return R.drawable.backdrop_placeholder;

        }
    }


    public void clear() {
        mMovies.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Movie> list) {
        mMovies.addAll(list);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView ivImage;
        public TextView tvTitle;
        public TextView tvOverview;

        public ViewHolder(View itemView) {
            super(itemView);
            ivImage = (ImageView) itemView.findViewById(R.id.ivMovieImage);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvOverview = (TextView) itemView.findViewById(R.id.tvOverview);

        }
    }

    public static class ViewHolderPopular extends RecyclerView.ViewHolder {

        public ImageView ivImage;

        public ViewHolderPopular(View itemView) {
            super(itemView);
            ivImage = (ImageView) itemView.findViewById(R.id.ivMovieImage);
        }
    }

}
