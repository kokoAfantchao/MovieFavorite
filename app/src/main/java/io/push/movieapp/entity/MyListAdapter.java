package io.push.movieapp.entity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import io.push.movieapp.Movie_detail_Activity;
import io.push.movieapp.R;

/**
 * Created by nestorkokoafantchao on 12/9/16.
 */

public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder> {

    private List<Movie> movies = new ArrayList<>();
    public static String EXTRA_TITLE ="io.push.movieapp.EXTRA_MOVIE_TITLE";
    public static String EXTRA_IMAGE_URL ="io.push.movieapp.EXTRA_MOVIE_URL";


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_holder, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);

        return vh;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //String Url = movies.get(position).getBackdrop_path();
        holder.movie= movies.get(position);
        holder.loadImage();

    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }







    public class ViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{

       public  ImageView imageView;
       public  Context context ;
       public Movie movie;
       private String image_url;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView =(ImageView)itemView.findViewById(R.id.image_movie);
            context= itemView.getContext();
            itemView.setOnClickListener(this);
        }

        public void loadImage(){

            Picasso.with(context).load("http://image.tmdb.org/t/p/w342"+movie.getBackdrop_path()).into(imageView);
            image_url="http://image.tmdb.org/t/p/w342"+movie.getBackdrop_path();

        }

        @Override
        public void onClick(View v) {
            Intent intentVideoDetail = new Intent(context,Movie_detail_Activity.class);
            Log.d("this",movie.getTitle());
            intentVideoDetail.putExtra(EXTRA_TITLE,movie.getTitle());
            intentVideoDetail.putExtra(EXTRA_IMAGE_URL,"http://image.tmdb.org/t/p/w342"+movie.getBackdrop_path());

            context.startActivity(intentVideoDetail);



          }
    }

}
