package io.push.movieapp.entity;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by nestorkokoafantchao on 3/30/17.
 */

public class MovieContract {


    public static final String AUTHORITY="io.push.movieapp";

    public static final Uri BASE_CONTENT_URI= Uri.parse("content://"+AUTHORITY);

    public static final String PATH_MOVIES="movies";
    public static final String PATH_FAVORITE_MOVIES="favorite";
    public static final String PATH_POPULAR_MOVIES="popular";

    public  static  final class  MovieEntry implements BaseColumns{

        public static  final Uri CONTENT_URI=
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();
        public static  final Uri CONTENT_FAVORITE_URI=
                CONTENT_URI.buildUpon().appendPath(PATH_FAVORITE_MOVIES).build();
        public static final Uri CONTENT_POPULAR_URI=
                CONTENT_URI.buildUpon().appendPath(PATH_POPULAR_MOVIES).build();
        public static final String TABLE_NAME="movies";

        public static final String COLUMN_MOVIE_ID="movie_id";
        public static final String COLUMN_TITLE="title";
        public static final String COLUMN_OVERVIEW="overview";
        public static final String COLUMN_VOTE_AVERAGE="vote_average";
        public static final String COLUMN_IMAGE_URL="image_url";
        public static final String COLUMN_RELEASE_DATE="release_date";
        public static final String COLUMN_FAVORITE="favotire";
        public static final String COLUMN_POPULAR_OR_TOP_RATE="popular_or_top_rate";

    }




}
