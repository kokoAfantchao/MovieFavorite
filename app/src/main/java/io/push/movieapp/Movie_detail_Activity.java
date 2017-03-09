package io.push.movieapp;

import android.icu.text.LocaleDisplayNames;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.push.movieapp.Adapter.MyListAdapter;
import io.push.movieapp.QueryResult.ReviewResult;
import io.push.movieapp.QueryResult.VideoResult;
import io.push.movieapp.Service.MovieService;
import io.push.movieapp.Service.ServiceGeneratore;
import retrofit2.Call;

public class Movie_detail_Activity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Movie_detail_Activity.QueryResult>{

    @BindView(R.id.tv_description) TextView textView_decription ;
    @BindView(R.id.tv_released_date) TextView textViewReleasedDate;
    @BindView(R.id.movie_ratingBar)RatingBar mratingBar;
    @BindView(R.id.image_movie_detail) ImageView imageView;
    private Integer movieId;
    private static  Integer ASYNCLAODER_TASK=  90;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail_);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(getIntent().getStringExtra(MyListAdapter.EXTRA_TITLE));
        movieId= getIntent().getIntExtra(MyListAdapter.EXTRA_MOVIE_ID,0);
        Picasso.with(this).load(getIntent().getStringExtra(MyListAdapter.EXTRA_IMAGE_URL)).into(imageView);
        textViewReleasedDate.setText(getIntent().getStringExtra(MyListAdapter.EXTRA_RELEASED_DATE));
        textView_decription.setText(getIntent().getStringExtra(MyListAdapter.EXTRA_DESCRIPTION));
        Double aDouble = getIntent().getDoubleExtra(MyListAdapter.EXTRA_VOTE_AVERAGE,0);
        mratingBar.setRating(aDouble.floatValue());

        Loader<QueryResult> queryResultLoader = getSupportLoaderManager().initLoader(ASYNCLAODER_TASK, null, this);
        queryResultLoader.forceLoad();

    }


    @Override
    public Loader<QueryResult> onCreateLoader(int id, Bundle args) {
        return  new AsyncTaskLoader<QueryResult>(this) {

            @Override
            protected void onStartLoading() {
                super.onStartLoading();

            }

            @Override
            public QueryResult loadInBackground(){


                MovieService movieService = ServiceGeneratore.createService(MovieService.class);
                Call<ReviewResult> reviewResultCall = movieService.movieReviews(movieId, MainActivity.API_KEY);
                Call<VideoResult> videoResultCall = movieService.movieVideos(movieId, MainActivity.API_KEY);
                try {
                    ReviewResult reviewResult  = reviewResultCall.execute().body();
                    VideoResult videoResult    = videoResultCall.execute().body();

                     return new QueryResult(videoResult,reviewResult);

                } catch (IOException e) {
                    e.printStackTrace();
                }
             return null;
            }

            @Override
            public void deliverResult(QueryResult data) {

                super.deliverResult(data);

            }
        };
    }

    @Override
    public void onLoadFinished(Loader<QueryResult> loader, QueryResult data) {
        Log.d("ON_LOADFINISHED", data.toString());

    }


    @Override
    public void onLoaderReset(Loader<QueryResult> loader) {
    }

     public  class QueryResult{
      private VideoResult  videoResult;
      private ReviewResult reviewResult;

       public  QueryResult(VideoResult videoResult ,ReviewResult  reviewResult){
           this.videoResult= videoResult;
           this.reviewResult=reviewResult;
       }

         public VideoResult getVideoResult() {
             return videoResult;
         }

         public void setVideoResult(VideoResult videoResult) {
             this.videoResult = videoResult;
         }

         public ReviewResult getReviewResult() {
            return reviewResult;
        }

        public void setReviewResult(ReviewResult reviewResult) {
            this.reviewResult = reviewResult;
        }

         @Override
         public String toString() {
             return "QueryResult{" +
                     "videoResult=" + videoResult +
                     ", reviewResult=" + reviewResult +
                     '}';
         }
     }
}
