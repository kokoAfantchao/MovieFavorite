package io.push.movieapp.Service;

import io.push.movieapp.Entity.Review;
import io.push.movieapp.QueryResult.MovieResult;
import io.push.movieapp.QueryResult.ReviewResult;
import io.push.movieapp.QueryResult.VideoResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by nestorkokoafantchao on 12/7/16.
 */

public interface MovieService {



    @GET("movie/{movie_type}")
    Call<MovieResult> listmovie(@Path("movie_type") String movie_type, @Query("api_key") String api_key);

    @GET("movie/{id}/videos")
    Call<VideoResult> movieVideos(@Path("id") int  movie_id, @Query("api_key") String api_key);

    @GET("movie/{id}/reviews")
    Call<ReviewResult> movieReviews(@Path("id") int  movie_id, @Query("api_key") String api_key);



}
