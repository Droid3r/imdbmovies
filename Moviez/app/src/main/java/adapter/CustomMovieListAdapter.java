package adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import model.Movies;
import network.PicassoSingleton;
import moviez.example.com.movieApp.DetailedMovie;
import moviez.example.com.movieApp.R;

/**
 * Created by Akshay on 8/27/15.
 */
public class CustomMovieListAdapter extends RecyclerView.Adapter<CustomMovieListAdapter.CustomViewHolder> {

    private Activity activity;
    private List<Movies.Movie> movieList;

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        public TextView movieNameView;
        public TextView movieYearView;
        public TextView movieRatingView;
        public ImageView movieImageView;

        public CustomViewHolder(View itemView) {
            super(itemView);

            movieNameView = (TextView) itemView.findViewById(R.id.movie_name);
            movieYearView = (TextView) itemView.findViewById(R.id.release_year);
            movieRatingView = (TextView) itemView.findViewById(R.id.rating);
            movieImageView = (ImageView) itemView.findViewById(R.id.movie_image);
        }
    }

    @Override
    public CustomMovieListAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent,
                                                                      int viewType) {


        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, null);
        final RecyclerView recyclerView = (RecyclerView) activity.findViewById(R.id.movie_recycler_view);

        //handle user click event to launch the detailed activity
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = recyclerView.getChildAdapterPosition(v);

                Movies.Movie mov = movieList.get(position);
                Intent intent = new Intent(activity, DetailedMovie.class);
                Bundle movieInfo = new Bundle();
                movieInfo.putString("ImageUrl", mov.getUrl());
                movieInfo.putString("Title", mov.getTitle());
                movieInfo.putString("Rating", String.valueOf(mov.getRating()));
                movieInfo.putString("Year", String.valueOf(mov.getYear()));

                intent.putExtras(movieInfo);
                activity.startActivity(intent);
            }
        });

        CustomViewHolder vh = new CustomViewHolder(v);
        return vh;

    }


    @Override
    public void onBindViewHolder(CustomViewHolder viewHolder, int position) {


        Movies.Movie movie = this.movieList.get(position);

        //Picasso debugging logs to verify that L1 and L2 caches are working as expected
        //PicassoSingleton.getPicassoInstance(activity).setIndicatorsEnabled(true);
        //PicassoSingleton.getPicassoInstance(activity).setLoggingEnabled(true);


        PicassoSingleton
                .getPicassoInstance(activity)
                .load(movie.getUrl())
                .into(viewHolder.movieImageView);

        viewHolder.movieNameView.setText(movie.getTitle());
        viewHolder.movieYearView.setText(String.valueOf(movie.getYear()));
        viewHolder.movieRatingView.setText(String.valueOf(movie.getRating()));

    }

    public CustomMovieListAdapter(Activity activity, List<Movies.Movie> movieList) {


        this.activity = activity;
        this.movieList = movieList;
    }

    @Override
    public int getItemCount() {
        return (null != movieList ? movieList.size() : 0);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


}
