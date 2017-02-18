package io.push.movieapp;

import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import io.push.movieapp.entity.MyListAdapter;

public class Movie_detail_Activity extends AppCompatActivity {

    private TextView textView_decription ;
    private TextView textViewReleasedDate;
    private RatingBar mratingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail_);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView imageView = (ImageView)findViewById(R.id.image_movie_detail);
        textView_decription = (TextView)findViewById(R.id.tv_description);
        textViewReleasedDate =(TextView) findViewById(R.id.tv_released_date);
        mratingBar =(RatingBar) findViewById(R.id.movie_ratingBar);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(getIntent().getStringExtra(MyListAdapter.EXTRA_TITLE));
        Picasso.with(this).load(getIntent().getStringExtra(MyListAdapter.EXTRA_IMAGE_URL)).into(imageView);

        textView_decription.setText(getIntent().getStringExtra(MyListAdapter.EXTRA_DESCRIPTION));
        textViewReleasedDate.setText(getIntent().getStringExtra(MyListAdapter.EXTRA_RELEASED_DATE));

        Double aDouble = getIntent().getDoubleExtra(MyListAdapter.EXTRA_VOTE_AVERAGE,0);
        mratingBar.setRating(aDouble.floatValue());


    }

}
