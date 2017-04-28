package io.push.movieapp.service;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.push.movieapp.MainActivity;
import io.push.movieapp.entity.Movie;
import io.push.movieapp.entity.MovieContract;

/**
 * Created by nestorkokoafantchao on 4/28/17.
 */

public class MovieJobUtils {

    private static final int SYNC_INTERVAL_HOURS = 24;
    private static final int SYNC_INTERVAL_SECONDS = (int) TimeUnit.HOURS.toSeconds(SYNC_INTERVAL_HOURS);
    private static final int SYNC_FLEXTIME_SECONDS = SYNC_INTERVAL_SECONDS /24;

    public static final String[] MAIN_MOVIES_PROJECTION = {
            MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_OVERVIEW,
            MovieContract.MovieEntry.COLUMN_IMAGE_URL,
            MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE
    };
    public static final int INDEX_ID= 0;
    public static final int INDEX_MOVIE_ID= 1;
    public static final int INDEX_MOVIE_TITLE=2;
    public static final int INDEX_MOVIE_OVERVIEW=3;
    public static final int INDEX_MOVIE_IMAGE_URL=4;
    public static final int INDEX_MOVIE_VOTE_AVERAGE=5;


    private static final String MOVIE_JOB_TAG="movie_job_tag";

    private static boolean isInitialized=false;

    public static void periodicSchedule(@NonNull final Context context) {
        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher jobDispatcher = new FirebaseJobDispatcher(driver);
        Job job = jobDispatcher.newJobBuilder()
                .setTag(MOVIE_JOB_TAG)
                .setService(MovieJobService.class)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setTrigger(Trigger.executionWindow(SYNC_INTERVAL_SECONDS,
                        SYNC_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS))
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setReplaceCurrent(true)
                .build();
        jobDispatcher.schedule(job);
    }


    synchronized public static  void initialize(@NonNull final Context context ){

        if(isInitialized)
            return;

        isInitialized=true;
         periodicSchedule(context);
           Thread thread = new Thread(new Runnable() {
               @Override
               public void run() {
                   Cursor cursor = context.getContentResolver().
                           query(MovieContract.MovieEntry.CONTENT_URI, MAIN_MOVIES_PROJECTION, null, null, null);
                   if(cursor==null|| cursor.getCount()==0){
                    startImmediateSync(context);
                   }
               }
           });
         thread.start();
    }

    public static ContentValues[] MoviesToContentValues(@NonNull Context context,@NonNull List<Movie> moviesPopural,List<Movie>moviesTopRate){
        int size = moviesPopural.size();
        ContentValues[] contentValues = new ContentValues[size];
        for(int i=0; i<size;i++){
         ContentValues values = new ContentValues ();
           values.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID,moviesPopural.get(i).getId());
           values.put(MovieContract.MovieEntry.COLUMN_TITLE,moviesPopural.get(i).getTitle());
           values.put(MovieContract.MovieEntry.COLUMN_OVERVIEW,moviesPopural.get(i).getOverview());
           values.put(MovieContract.MovieEntry.COLUMN_IMAGE_URL,moviesPopural.get(i).getBackdrop_path());
           values.put(MovieContract.MovieEntry.COLUMN_FAVORITE,false);
           values.put(MovieContract.MovieEntry.COLUMN_POPULAR_OR_TOP_RATE,"TRUE");
           values.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE,moviesPopural.get(i).getVote_average());
           values.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE,moviesPopural.get(i).getRelease_date().toString());
           contentValues[i]=values;
        }
        size = moviesTopRate.size();
        for(int i=0; i<size;i++){
            ContentValues values = new ContentValues ();
            values.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID,moviesTopRate.get(i).getId());
            values.put(MovieContract.MovieEntry.COLUMN_TITLE,moviesTopRate.get(i).getTitle());
            values.put(MovieContract.MovieEntry.COLUMN_OVERVIEW,moviesTopRate.get(i).getOverview());
            values.put(MovieContract.MovieEntry.COLUMN_IMAGE_URL,moviesTopRate.get(i).getBackdrop_path());
            values.put(MovieContract.MovieEntry.COLUMN_FAVORITE,false);
            values.put(MovieContract.MovieEntry.COLUMN_POPULAR_OR_TOP_RATE,"FALSE");
            values.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE,moviesTopRate.get(i).getVote_average());
            values.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE,moviesTopRate.get(i).getRelease_date().toString());
            contentValues[i]=values;
        }
        return  contentValues ;
    }


    public static void startImmediateSync(@NonNull final Context context) {
        Intent intentToSyncImmediately = new Intent(context,MovieBackGroundService.class);
        context.startService(intentToSyncImmediately);
    }

}
