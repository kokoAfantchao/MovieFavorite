package io.push.movieapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.push.movieapp.Adapter.MyListAdapter;
import io.push.movieapp.Service.MovieService;
import io.push.movieapp.Service.ServiceGeneratore;
import io.push.movieapp.Entity.Movie;
import io.push.movieapp.QueryResult.MovieResult;
import retrofit2.Call;

public class MainActivity extends AppCompatActivity {

    private static  String KEY_PARAM ="api_key";
    public  static final String API_KEY="60ef851a2b0c8a62248a58458a84808e";
    public  static  String POPULAR_MOVIE="popular";
    public  static  String TOP_RATE_MOVIE="top_rated";
    @BindView(R.id.myrecycler) RecyclerView mRecyclerView;
    private MyListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Movie> movies = new ArrayList<>();
    @BindView(R.id.img_no_network) ImageView mImageError ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new GridLayoutManager(this,2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new MyListAdapter();

        mRecyclerView.setAdapter(mAdapter);




         FecthMovieTask fecthMovieTask = new FecthMovieTask();

        boolean online = isOnline();

        if (online){
            mRecyclerView.setVisibility(View.VISIBLE);
            mImageError.setVisibility(View.INVISIBLE);
            fecthMovieTask.execute();
        }else{

            mRecyclerView.setVisibility(View.INVISIBLE);
            mImageError.setVisibility(View.VISIBLE);

        }

        PreferenceManager.setDefaultValues(this, R.xml.pref_generals, false);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("movies", (ArrayList<? extends Parcelable>) movies);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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

        return super.onOptionsItemSelected(item);
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }





    public class FecthMovieTask extends AsyncTask<String ,Void,MovieResult> {

        public String REQUEST_TYPE = POPULAR_MOVIE;


        protected void onPostExecute(MovieResult movieResult) {
            super.onPostExecute(movieResult);
             movies=movieResult.getResults();
             mAdapter.setMovies(movieResult.getResults());
             mAdapter.notifyDataSetChanged();
        }




        private final String LOG_CAT = FecthMovieTask.class.getSimpleName();


        @Override
        protected MovieResult doInBackground(String... params) {
            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;



            String format = "json";
            String unit = "metric";

            int numday = 7;

            String  MoviesJson = null;

            try {

//                final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/movie/";
//                Uri uri = Uri.parse(MOVIE_BASE_URL+REQUEST_TYPE).buildUpon()
//                        .appendQueryParameter(KEY_PARAM, getResources().getString(R.string.api_key))
//                      //  .appendQueryParameter(API_ID, APPID)
//                        .build();
//                URL url = new URL(uri.toString());
//                Log.v(LOG_CAT, "Uri.builder :" + uri.toString());
//
//
//                httpURLConnection = (HttpURLConnection) url.openConnection();
//                httpURLConnection.setRequestMethod("GET");
//                httpURLConnection.connect();
//                InputStream inputStream = httpURLConnection.getInputStream();
//
//                StringBuffer stringBuffer = new StringBuffer();
//                StringBuilder  stringBuilder = new StringBuilder();
//
//                if (inputStream == null) {
//                    MoviesJson = null;
//                }
//                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//
//                 // bufferedReader.readLine();
//
//
//
///*
//                String line ;
//                while ((line = bufferedReader.readLine()) != null) {
//                    Log.d(LOG_CAT, stringBuffer+ "output line by line ");
//                    stringBuffer.append(line);
//                }
//*/

                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String sharedPrefString = sharedPref.getString(SettingsActivity.PREF_SORT_TYPE_KEY,POPULAR_MOVIE);
                Log.d(LOG_CAT,"++++++Share preference out put  "+sharedPrefString);
                MovieService movieService = ServiceGeneratore.createService(MovieService.class);
                Call<MovieResult> repos = movieService.listmovie(sharedPrefString,API_KEY);
                MovieResult ReponseMovies = repos.execute().body();
                Log.d(LOG_CAT,"buffer reader "+ReponseMovies.toString()+"output");
                return  ReponseMovies ;
            } catch (IOException e) {
                Log.e(LOG_CAT, "Error" + e.toString(), e);

            } finally {



            }

            return null ;

        }
    }
}
