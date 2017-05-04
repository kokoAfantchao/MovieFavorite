package io.push.movieapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.amitshekhar.DebugDB;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.push.movieapp.adapter.MovieAdapter;
import io.push.movieapp.adapter.MyListAdapter;
import io.push.movieapp.entity.MovieContract;
import io.push.movieapp.queryResult.MovieResult;
import io.push.movieapp.service.MovieJobTask;
import io.push.movieapp.service.MovieJobUtils;
import io.push.movieapp.service.MovieService;
import io.push.movieapp.service.ServiceGeneratore;
import io.push.movieapp.entity.Movie;
import retrofit2.Call;

import static io.push.movieapp.service.MovieJobUtils.MAIN_MOVIES_PROJECTION;


public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener,
        LoaderCallbacks<Cursor>{



    private final String LOG_CAT = MainActivity.class.getSimpleName();
    public static final String ACTION_DATA_BASE_LOAD="io.push.movieapp.ACTION_DATA_BASE_LOAD";
    private static  String KEY_PARAM ="api_key";
    public  static  String API_KEY=BuildConfig.THE_MOVIE_DB_API_TOKEN;
    public  static  String POPULAR_MOVIE="popular";
    public  static  String TOP_RATE_MOVIE="top_rated";
    @BindView(R.id.myrecycler) RecyclerView mRecyclerView;
    private MovieAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Movie> movies = new ArrayList<>();
    @BindView(R.id.img_no_network) ImageView mImageError ;
    private static final  int MOVIE_LOADER_ID=500;
    private static final int FAVORITE_LOALDER_ID=501;
    private static boolean CURRENT_MOVIE_LOAD=false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
         // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new GridLayoutManager(this,2);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            mLayoutManager = new GridLayoutManager(this,2);
        } else{
            mLayoutManager = new GridLayoutManager(this,3);
        }
        mRecyclerView.setLayoutManager(mLayoutManager);


        // specify an adapter (see also next example)
        mAdapter = new MovieAdapter();
        mRecyclerView.setAdapter(mAdapter);

        if(savedInstanceState!=null ){
            boolean current_movie_load = savedInstanceState.getBoolean("CURRENT_MOVIE_LOAD");
            if(current_movie_load){
                getSupportLoaderManager().initLoader(FAVORITE_LOALDER_ID,null,this);
            }else{
                getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);
            }

        }else {
            MovieJobUtils.initialize(this);

            getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);


        }
        //getSupportLoaderManager().initLoader(FAVORITE_LOALDER_ID,null,new FavoriteLoader());



         boolean online = isOnline();
        if (online){
            mRecyclerView.setVisibility(View.VISIBLE);
            mImageError.setVisibility(View.INVISIBLE);
          //  getSupportLoaderManager().getLoader(LOADER_ID).forceLoad();
        }else{
            mRecyclerView.setVisibility(View.INVISIBLE);
            mImageError.setVisibility(View.VISIBLE);
        }

      PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);



    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("CURRENT_MOVIE_LOAD",CURRENT_MOVIE_LOAD);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d(LOG_CAT," config change");
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return mAdapter.getCursor();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent  intent = new Intent(getApplicationContext(),SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.action_favorite){
            getSupportLoaderManager().initLoader(FAVORITE_LOALDER_ID,null,this);


            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if (key.equals(getString(R.string.pref_sort_type_key))) {

            getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);

            //getSupportLoaderManager().getLoader(LOADER_ID).forceLoad();
            Log.d(LOG_CAT," Sharepreference is changed");
        }
    }

    @Override
    public Loader <Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cursorLoader;
        switch (id) {
            case MOVIE_LOADER_ID:
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                String preferenceMovie = sharedPreferences.getString(getString(R.string.pref_sort_type_key),POPULAR_MOVIE);
                String popular = isPopular(preferenceMovie);
                return new CursorLoader(this,
                        MovieContract.MovieEntry.CONTENT_POPULAR_URI.buildUpon().appendPath(popular).build(),
                        MAIN_MOVIES_PROJECTION,
                        null,
                        null,
                        null
                );
            case FAVORITE_LOALDER_ID:
                 return new CursorLoader(getApplicationContext(),
                        MovieContract.MovieEntry.CONTENT_FAVORITE_URI.buildUpon().appendPath("TRUE").build(),
                        MAIN_MOVIES_PROJECTION,null,null,null);
            default:
                return null;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader,Cursor data) {
        int loaderId = loader.getId();

        if (data != null) {
            DebugDB.getAddressLog();
            mAdapter.swapCursor(data);
            if (loaderId == MOVIE_LOADER_ID) {
                CURRENT_MOVIE_LOAD = false;
            } else {
                CURRENT_MOVIE_LOAD = true;
            }

        }
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
       mAdapter.swapCursor(null);
    }




  public static String isPopular(String sType){
      if(sType.equalsIgnoreCase(POPULAR_MOVIE)) {
          return "TRUE";
      }else {
          return "FALSE";
      }
  }

}
