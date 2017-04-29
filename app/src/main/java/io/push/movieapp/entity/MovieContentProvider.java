package io.push.movieapp.entity;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by nestorkokoafantchao on 3/31/17.
 */

public class MovieContentProvider extends ContentProvider {

    private static  final int MOVIES=200;
    private static  final int MOVIES_WITH_ID=201;
    private UriMatcher uriMatcher =  buildUriMatcher();
    private MovieDbHelper movieDbHelper;


    public  UriMatcher buildUriMatcher(){

        UriMatcher  matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(MovieContract.AUTHORITY,MovieContract.PATH_MOVIES,MOVIES);
        matcher.addURI(MovieContract.AUTHORITY,MovieContract.PATH_MOVIES+"/#",MOVIES_WITH_ID);
        return  matcher;

    }

    @Override
    public boolean onCreate() {
       movieDbHelper= new MovieDbHelper(getContext());
        return  true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        SQLiteDatabase database = movieDbHelper.getReadableDatabase();
        int match = uriMatcher.match(uri);
        Cursor cursorReturn ;

        switch(match){
            case MOVIES:
                 cursorReturn =
                         database.query(MovieContract.MovieEntry.TABLE_NAME,
                         projection,
                         selection,
                         selectionArgs,
                         null,
                         null,
                         sortOrder
                       );


                break;

            default:

                throw new UnsupportedOperationException("Unsupport Uri"+ uri);
        }
     getContext().getContentResolver().notifyChange(uri,null);

        return  cursorReturn;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        SQLiteDatabase sqLiteDatabase = movieDbHelper.getWritableDatabase();
        int rowInset=0;
        uriMatcher.match(uri);
                switch(uriMatcher.match(uri)){
                    case MOVIES:
                       sqLiteDatabase.beginTransaction();
                        try {
                        for (ContentValues contentValue : values){
                            long insert = sqLiteDatabase.insert(MovieContract.MovieEntry.TABLE_NAME, null, contentValue);
                            if (insert != -1) {
                                rowInset++;
                            }
                        }
                        sqLiteDatabase.setTransactionSuccessful();

                        }finally {
                            sqLiteDatabase.close();
                        }

                        if (rowInset> 0) {
                            getContext().getContentResolver().notifyChange(uri, null);
                        }
                        return  rowInset;

                    default:
                        return super.bulkInsert(uri, values);

                }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }


    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        SQLiteDatabase database = movieDbHelper.getWritableDatabase();

        int match = uriMatcher.match(uri);
        Uri uriRetrun = null;
        switch (match){

            case MOVIES:
              long  insert = database.insert(MovieContract.MovieEntry.TABLE_NAME,null,values);
                if(insert>0){
                 uriRetrun=ContentUris.withAppendedId(MovieContract.BASE_CONTENT_URI,insert);
                }else {
                    throw new android.database.SQLException("Failed to insert row into" + uri);
                }
             break ;
            case MOVIES_WITH_ID:
                break;
            default:
                throw  new UnsupportedOperationException("Unsupport Uri"+ uri);

        }
        getContext().getContentResolver().notifyChange(uri,null);

        return uriRetrun ;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        SQLiteDatabase database = movieDbHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        int deletedRow=0;
        String  movie_id = uri.getPathSegments().get(1);

        switch(match){

            case MOVIES_WITH_ID:
          deletedRow=database.delete(MovieContract.MovieEntry.TABLE_NAME,
                  "movie_id=?",new String[]{movie_id});
                break;
            default:
                throw new UnsupportedOperationException("Unsupport Uri "+ uri);
        }
        if(deletedRow!=0){
            getContext().getContentResolver().notifyChange(uri,null);
        }


        return deletedRow;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
