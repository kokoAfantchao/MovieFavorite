package io.push.movieapp.entity;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by nestorkokoafantchao on 3/31/17.
 */

public class MovieContentProvider extends ContentProvider {

    private static  final int MOVIES=200;
    private static  final int MOVIES_WITH_ID=201;
    private static  final int MOVIES_FAVORITE=202;
    private static  final int MOVIES_POPURAL=203;
    private static final String TAG_LOG = MovieContentProvider.class.toString();
    private UriMatcher uriMatcher =  buildUriMatcher();
    private MovieDbHelper movieDbHelper;


    public  UriMatcher buildUriMatcher(){

        UriMatcher  matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(MovieContract.AUTHORITY,MovieContract.PATH_MOVIES,MOVIES);
        matcher.addURI(MovieContract.AUTHORITY,MovieContract.PATH_MOVIES+"/#",MOVIES_WITH_ID);
        matcher.addURI(MovieContract.AUTHORITY,MovieContract.PATH_MOVIES+"/"+MovieContract.PATH_FAVORITE_MOVIES+"/*",MOVIES_FAVORITE);
        matcher.addURI(MovieContract.AUTHORITY,MovieContract.PATH_MOVIES+"/"+MovieContract.PATH_POPULAR_MOVIES+"/*",MOVIES_POPURAL);


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
        String pathSegment;
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
            case  MOVIES_FAVORITE:
                pathSegment   = uri.getPathSegments().get(2);
                cursorReturn =
                        database.query(MovieContract.MovieEntry.TABLE_NAME,
                                projection,
                                MovieContract.MovieEntry.COLUMN_FAVORITE+"=?",
                                new String[]{pathSegment},
                                null,
                                null,
                                sortOrder
                        );
                Log.d(TAG_LOG,(pathSegment));

                break;
            case  MOVIES_POPURAL:
                pathSegment = uri.getPathSegments().get(2);
                cursorReturn =
                        database.query(MovieContract.MovieEntry.TABLE_NAME,
                                projection,
                                MovieContract.MovieEntry.COLUMN_POPULAR_OR_TOP_RATE+"=?",
                                new String[]{pathSegment},
                                null,
                                null,
                                sortOrder
                        );
                Log.d(TAG_LOG,(pathSegment));
                break;


            default:

                throw new UnsupportedOperationException("Unsupport Uri"+ uri);
        }
        // I have to call cursor.setNoticationUri
      // getContext().getContentResolver().notifyChange(uri,null);
        cursorReturn.setNotificationUri(getContext().getContentResolver(), uri);

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
                            sqLiteDatabase.endTransaction();
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


        switch(match){
            case MOVIES :
                deletedRow=database.delete(MovieContract.MovieEntry.TABLE_NAME,null,null);

                break;


            case MOVIES_WITH_ID:
                String  movie_id = uri.getPathSegments().get(1);
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
         SQLiteDatabase sqLiteDatabase =movieDbHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        int update=0;
        switch (match){
            
            case MOVIES_WITH_ID:
                String  movie_id = uri.getPathSegments().get(1);
                update= sqLiteDatabase.update(MovieContract.MovieEntry.TABLE_NAME, values, "movie_id=?", new String[]{movie_id});

                break;
        }
        
        return update ;
    }
}
