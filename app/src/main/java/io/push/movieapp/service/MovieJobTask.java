package io.push.movieapp.service;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContentResolverCompat;
import android.util.Log;

import java.io.IOException;
import java.util.Arrays;

import io.push.movieapp.MainActivity;
import io.push.movieapp.entity.MovieContract;
import io.push.movieapp.queryResult.MovieResult;
import retrofit2.Call;
import retrofit2.Response;

import static io.push.movieapp.MainActivity.API_KEY;

/**
 * Created by nestorkokoafantchao on 4/28/17.
 */

public class MovieJobTask {
  private static String TAG_LOG=MovieJobTask.class.getName();


    synchronized public static void Movie(Context context)  {

        MovieService movieService = ServiceGeneratore.createService(MovieService.class);
        Call<MovieResult> reposPopular = movieService.listmovie(MainActivity.POPULAR_MOVIE, API_KEY);
        Call<MovieResult> reposTopRate = movieService.listmovie(MainActivity.TOP_RATE_MOVIE, API_KEY);
        try {
            MovieResult movieResult = reposPopular.execute().body();
            MovieResult movieResult1=reposTopRate.execute().body();
            if (movieResult1!=null || movieResult1.getResults().size()!= 0||movieResult!=null || movieResult.getResults().size()!= 0){

                    ContentValues[] contentValues = MovieJobUtils.MoviesToContentValues(context, movieResult.getResults(),movieResult1.getResults());
                    ContentResolver contentResolver = context.getContentResolver();

                contentResolver.delete(MovieContract.MovieEntry.CONTENT_URI,null,null);
                int insert = contentResolver.bulkInsert(MovieContract.MovieEntry.CONTENT_URI, contentValues);
                Log.d(TAG_LOG,"NUmber of insertion "+insert);
                Log.d(TAG_LOG,"Number of toprate "+movieResult1.getResults().size());
                Log.d(TAG_LOG,"Number of Popular "+movieResult.getResults().size());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
