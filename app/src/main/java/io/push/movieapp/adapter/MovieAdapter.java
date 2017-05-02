package io.push.movieapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.push.movieapp.Movie_detail_Activity;
import io.push.movieapp.R;

import static io.push.movieapp.service.MovieJobUtils.INDEX_MOVIE_ID;
import static io.push.movieapp.service.MovieJobUtils.INDEX_MOVIE_IMAGE_URL;
import static io.push.movieapp.service.MovieJobUtils.INDEX_MOVIE_OVERVIEW;
import static io.push.movieapp.service.MovieJobUtils.INDEX_MOVIE_RELEASE_DATE;
import static io.push.movieapp.service.MovieJobUtils.INDEX_MOVIE_TITLE;
import static io.push.movieapp.service.MovieJobUtils.INDEX_MOVIE_VOTE_AVERAGE;

/**
 * Created by nestorkokoafantchao on 3/31/17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder>{

    private static final String LOG_CAT = MovieAdapter.class.toString();
    private static final String IMAGE_BASED_PATH="http://image.tmdb.org/t/p/w342";
    private String IMAGE_UR;
    public static String EXTRA_TITLE ="io.push.movieapp.EXTRA_MOVIE_TITLE";
    public static String EXTRA_IMAGE_URL ="io.push.movieapp.EXTRA_MOVIE_URL";
    public static String EXTRA_DESCRIPTION="io.push.movieapp.EXTRA_DESCRIPTION";
    public static String EXTRA_RELEASED_DATE="io.push.movieapp.EXTRA_RELEASED_DATE";
    public static String EXTRA_VOTE_AVERAGE="io.push.movieapp.EXTRA_VOTE_AVERAGE";
    public static String EXTRA_MOVIE_ID="io.push.movieapp.EXTRA_MOVIE_ID";

    private Cursor cursor;

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_holder, parent, false);
        //set the view's size, margins, paddings and layout parameters
        MovieViewHolder vh = new MovieViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        cursor.moveToPosition(position);
        holder.loadImage(cursor.getString(INDEX_MOVIE_IMAGE_URL));
        holder.textViewTitle.setText(cursor.getString(INDEX_MOVIE_TITLE));
        holder.textViewRating.setText(cursor.getString(INDEX_MOVIE_VOTE_AVERAGE));
        holder.itemView.setTag(cursor.getInt(INDEX_MOVIE_ID));
        holder.date=cursor.getString(INDEX_MOVIE_RELEASE_DATE);
        holder.overView=cursor.getString(INDEX_MOVIE_OVERVIEW);
        holder.voteAverage=cursor.getString(INDEX_MOVIE_VOTE_AVERAGE);
    }

    @Override
    public int getItemCount() {
        if(cursor!= null) {
            return cursor.getCount();
        }
        return 0;
    }
    public void swapCursor(Cursor cursor) {
        this.cursor=cursor;
        notifyDataSetChanged();
    }

    public Cursor getCursor() {
        return cursor;
    }

    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
    }

    public class  MovieViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
        @BindView(R.id.image_movie) ImageView imageView;
        @BindView(R.id.tv_title) TextView textViewTitle;
        @BindView(R.id.tv_rating) TextView textViewRating;
        public Context context;
        public String overView;
        public String date;
        public String voteAverage;

        public MovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            context=itemView.getContext();
            itemView.setOnClickListener(this);
        }

        public void loadImage(String  stringUrl ){
            Picasso.with(context).load(IMAGE_BASED_PATH+stringUrl).into(imageView);
            IMAGE_UR= stringUrl;

        }


        @Override
        public void onClick(View view) {
            Intent intentVideoDetail = new Intent(context,Movie_detail_Activity.class);
            intentVideoDetail.putExtra(EXTRA_TITLE,textViewTitle.getText());
            intentVideoDetail.putExtra(EXTRA_IMAGE_URL,IMAGE_BASED_PATH+IMAGE_UR);
            intentVideoDetail.putExtra(EXTRA_DESCRIPTION,overView);
           // String stringDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
            intentVideoDetail.putExtra(EXTRA_RELEASED_DATE,date);
            intentVideoDetail.putExtra(EXTRA_VOTE_AVERAGE,voteAverage);
            intentVideoDetail.putExtra(EXTRA_MOVIE_ID,(int)itemView.getTag());
            context.startActivity(intentVideoDetail);



        }
    }

}
