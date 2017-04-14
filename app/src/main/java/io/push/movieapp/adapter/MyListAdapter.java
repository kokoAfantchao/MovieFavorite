package io.push.movieapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.push.movieapp.Movie_detail_Activity;
import io.push.movieapp.R;
import io.push.movieapp.entity.Movie;

/**
 * Created by nestorkokoafantchao on 12/9/16.
 */

public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder> {
    private static String DEBUD_LOG= "MY_ADAPTER_CLASS";
    private List<Movie> movies = new ArrayList<>();
    public static String EXTRA_TITLE ="io.push.movieapp.EXTRA_MOVIE_TITLE";
    public static String EXTRA_IMAGE_URL ="io.push.movieapp.EXTRA_MOVIE_URL";
    public static String EXTRA_DESCRIPTION="io.push.movieapp.EXTRA_DESCRIPTION";
    public static String EXTRA_RELEASED_DATE="io.push.movieapp.EXTRA_RELEASED_DATE";
    public static String EXTRA_VOTE_AVERAGE="io.push.movieapp.EXTRA_VOTE_AVERAGE";
    public static String EXTRA_MOVIE_ID="io.push.movieapp.EXTRA_MOVIE_ID";


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_holder, parent, false);
        //set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //String Url = movies.get(position).getBackdrop_path();
        holder.movie= movies.get(position);
        holder.loadImage();
        holder.setUpview();


    }
    public void  updateMovieDat(List<Movie> movies){
        this.movies= movies;
        notifyDataSetChanged();

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

       @BindView(R.id.image_movie)  ImageView imageView;
       public  Context context ;
       public Movie movie;
       @BindView(R.id.tv_title) TextView textViewTitle;
       @BindView(R.id.tv_rating) TextView textViewRating;
       private String image_url;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            context= itemView.getContext();
            itemView.setOnClickListener(this);
        }


        public void loadImage(){
            Picasso.with(context).load("http://image.tmdb.org/t/p/w342"+movie.getBackdrop_path()).into(imageView);
            image_url="http://image.tmdb.org/t/p/w342"+movie.getBackdrop_path();
        }

        public void setUpview(){
            textViewTitle.setText(movie.getTitle());
            textViewRating.setText(context.getString(R.string.tv_rating)+""+movie.getVote_average().toString());
        }

        @Override
        public void onClick(View v) {
            Intent intentVideoDetail = new Intent(context,Movie_detail_Activity.class);
            Log.d(DEBUD_LOG,movie.getTitle());
            intentVideoDetail.putExtra(EXTRA_TITLE,movie.getTitle());
            intentVideoDetail.putExtra(EXTRA_IMAGE_URL,"http://image.tmdb.org/t/p/w342"+movie.getBackdrop_path());
            intentVideoDetail.putExtra(EXTRA_DESCRIPTION,movie.getOverview());
            String stringDate = new SimpleDateFormat("yyyy-MM-dd").format(movie.getRelease_date());
            intentVideoDetail.putExtra(EXTRA_RELEASED_DATE,stringDate);
            intentVideoDetail.putExtra(EXTRA_VOTE_AVERAGE,movie.getVote_average());
            intentVideoDetail.putExtra(EXTRA_MOVIE_ID,movie.getId());
            context.startActivity(intentVideoDetail);
          }
    }



}
