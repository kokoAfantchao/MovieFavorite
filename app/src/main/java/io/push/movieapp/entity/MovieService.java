package io.push.movieapp.entity;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by nestorkokoafantchao on 12/7/16.
 */

public interface MovieService {



    @GET("movie/{movie_type}")
    Call<MovieResult> listmovie(@Path("movie_type") String movie_type,@Query("api_key") String api_key);



}
