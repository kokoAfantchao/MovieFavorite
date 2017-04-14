package io.push.movieapp.entity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by nestorkokoafantchao on 3/31/17.
 */

public class MovieDbHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME="movie.db";

    private static final int VERSION =3;


    MovieDbHelper(Context  context){
        super(context,DATABASE_NAME,null,VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {

        final String CREATE_TABLE = "CREATE TABLE "  +MovieContract.MovieEntry.TABLE_NAME+ " (" +
                MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY, " +
                MovieContract.MovieEntry.COLUMN_IMAGE_URL + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_MOVIE_ID   + " INTEGER NOT NULL);";

        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("ALTER TABLE IF EXISTS"+MovieContract.MovieEntry.TABLE_NAME);
        onCreate(db);

    }
}
