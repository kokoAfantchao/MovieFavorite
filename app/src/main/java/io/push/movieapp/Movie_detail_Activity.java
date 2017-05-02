package io.push.movieapp;

import android.content.ContentValues;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.push.movieapp.adapter.MyListAdapter;
import io.push.movieapp.adapter.ReviewAdapter;
import io.push.movieapp.adapter.VideoAdapter;
import io.push.movieapp.entity.MovieContract;
import io.push.movieapp.entity.Review;
import io.push.movieapp.entity.Video;
import io.push.movieapp.queryResult.ReviewResult;
import io.push.movieapp.queryResult.VideoResult;
import io.push.movieapp.service.MovieService;
import io.push.movieapp.service.ServiceGeneratore;
import io.push.movieapp.fragment.OverViewFragment;
import io.push.movieapp.fragment.ReviewFragment;
import io.push.movieapp.fragment.VideoFragment;
import io.realm.FavoriteMovieRealmProxy;
import retrofit2.Call;

import static io.push.movieapp.adapter.MovieAdapter.EXTRA_DESCRIPTION;
import static io.push.movieapp.adapter.MovieAdapter.EXTRA_IMAGE_URL;
import static io.push.movieapp.adapter.MovieAdapter.EXTRA_MOVIE_ID;
import static io.push.movieapp.adapter.MovieAdapter.EXTRA_RELEASED_DATE;
import static io.push.movieapp.adapter.MovieAdapter.EXTRA_TITLE;
import static io.push.movieapp.adapter.MovieAdapter.EXTRA_VOTE_AVERAGE;

public class Movie_detail_Activity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Movie_detail_Activity.QueryResult>{


    @BindView(R.id.image_movie_detail) ImageView imageView;
    @BindView(R.id.viewpager) ViewPager viewPager;
    @BindView(R.id.tablayout)TabLayout tabLayout;
    @BindView(R.id.collapsing_toolbar)CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.toolbar)Toolbar toolbar;
    @BindView(R.id.fab_favorite) FloatingActionButton fab_favorite;
    private Integer movieId;
    private static  Integer ASYNCLAODER_TASK=  90;
    private ReviewAdapter reviewAdapter;
    private VideoAdapter videoAdapter;
    private String date ;
    private String description;
    private String title;
    private Double averageVote;
    private String imageURL;
    private ReviewFragment reviewFragment;
    private VideoFragment  videoFragment;
    private Boolean isFavorite = false;
    List<Review> reviwResults = new ArrayList<Review>();
    List<Video> videoResults = new ArrayList<Video>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail_);
        if(savedInstanceState!= null){
          date= savedInstanceState.getString("date");
          description= savedInstanceState.getString("description");
          title=savedInstanceState.getString("title");
          averageVote= savedInstanceState.getDouble("averageVote");
          movieId=savedInstanceState.getInt("movie_id");
          imageURL=savedInstanceState.getString("image_url");
          //reviwResults= (List<Review>) savedInstanceState.getSparseParcelableArray("review");
          //videoResults=(List<Video>) savedInstanceState.getSparseParcelableArray("video");
        }
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar supportActionBar = getSupportActionBar();
        fab_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                   favoriteMovie();
            }
        });
        //collapsingToolbar.setTitle();
        collapsingToolbar.setTitleEnabled(false);
        tabLayout.setupWithViewPager(viewPager);
        movieId= getIntent().getIntExtra(EXTRA_MOVIE_ID,0);
        imageURL=getIntent().getStringExtra(EXTRA_IMAGE_URL);
        Picasso.with(this).load(imageURL).into(imageView);
        date =getIntent().getStringExtra(EXTRA_RELEASED_DATE);
        description =getIntent().getStringExtra(EXTRA_DESCRIPTION);
        averageVote =Double.parseDouble(getIntent().getStringExtra(EXTRA_VOTE_AVERAGE));
        title =getIntent().getStringExtra(EXTRA_TITLE);
        movieId=getIntent().getIntExtra(EXTRA_MOVIE_ID,0);
        supportActionBar.setTitle(title);
        Loader<QueryResult> queryResultLoader = getSupportLoaderManager().initLoader(ASYNCLAODER_TASK, null, this);
        setupViewPager(viewPager);
        queryResultLoader.forceLoad();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("date", date);
        outState.putString("description",description);
        outState.putString("title",title);
        outState.putDouble("averageVote",averageVote);
        outState.putInt("movie_id",movieId);
        outState.putString("image_url",imageURL);

       // outState.putParcelableArrayList("video",(ArrayList<? extends Parcelable>)videoResults);
       // outState.putParcelableArrayList("review",(ArrayList<? extends Parcelable>)reviwResults);
        super.onSaveInstanceState(outState);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new OverViewFragment().newInstance(title,description,date,averageVote)
                ,getString(R.string.text_tab_over_view));
        reviewFragment = new ReviewFragment();
        videoFragment = new VideoFragment();
        adapter.addFragment(reviewFragment,getString(R.string.text_tab_review));
        adapter.addFragment(videoFragment,getString(R.string.text_tab_video));
        viewPager.setAdapter(adapter);
    }

    public void favoriteMovie(){
        ContentValues contentValues = new ContentValues();
        if(!isFavorite){
            //TODO Save the favorite in th database
            contentValues.put(MovieContract.MovieEntry.COLUMN_FAVORITE,"TRUE");
            new FavoriteAsync().execute(contentValues);
        }
        else {
            contentValues.put(MovieContract.MovieEntry.COLUMN_FAVORITE,"FALSE");
            new FavoriteAsync().execute(contentValues);

        }

    }




    @Override
    public Loader<QueryResult> onCreateLoader(int id, Bundle args) {
        return  new AsyncTaskLoader<QueryResult>(this) {

            @Override
            protected void onStartLoading() {
                super.onStartLoading();

            }

            @Override
            public QueryResult loadInBackground(){


                MovieService movieService = ServiceGeneratore.createService(MovieService.class);
                Call<ReviewResult> reviewResultCall = movieService.movieReviews(movieId, MainActivity.API_KEY);
                Call<VideoResult> videoResultCall = movieService.movieVideos(movieId, MainActivity.API_KEY);
                try {
                    ReviewResult reviewResult  = reviewResultCall.execute().body();
                    VideoResult videoResult    = videoResultCall.execute().body();

                     return new QueryResult(videoResult,reviewResult);

                } catch (IOException e) {
                    e.printStackTrace();
                }
             return null;
            }

            @Override
            public void deliverResult(QueryResult data) {
                super.deliverResult(data);

            }
        };
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

       // if (item.getItemId()== Men)
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLoadFinished(Loader<QueryResult> loader, QueryResult data) {
        Log.d("ON_LOADFINISHED", data.toString());
        reviwResults = data.getReviewResult().getResults();
        videoResults = data.getVideoResult().getResults();
        reviewFragment.setReviews(reviwResults);
        videoFragment.setVidoes(videoResults);
    }


    @Override
    public void onLoaderReset(Loader<QueryResult> loader) {
    }

     public  class QueryResult{
      private VideoResult  videoResult;
      private ReviewResult reviewResult;

       public  QueryResult(VideoResult videoResult ,ReviewResult  reviewResult){
           this.videoResult= videoResult;
           this.reviewResult=reviewResult;
       }

         public VideoResult getVideoResult() {
             return videoResult;
         }

         public void setVideoResult(VideoResult videoResult) {
             this.videoResult = videoResult;
         }

         public ReviewResult getReviewResult() {
            return reviewResult;
        }

        public void setReviewResult(ReviewResult reviewResult) {
            this.reviewResult = reviewResult;
        }

         @Override
         public String toString() {
             return "QueryResult{" +
                     "videoResult=" + videoResult +
                     ",reviewResult=" + reviewResult +
                     '}';
         }
     }


     public  class FavoriteAsync extends AsyncTask<ContentValues ,Void,Integer>{
        /// Uri insert = null ;
         Integer result =  new Integer(-2);
         @Override
         protected Integer doInBackground(ContentValues ... params) {

                 result = getContentResolver().update(
                         MovieContract.MovieEntry.CONTENT_URI.buildUpon().appendPath(movieId.toString()).build(),
                         params[0], null,null);

             return result;
         }

         @Override
         protected void onPostExecute(Integer integer) {
             isFavorite = !isFavorite;
             if(isFavorite ){
                 fab_favorite.setImageResource(R.drawable.ic_favorite_white_24dp);
                 Toast.makeText(getApplicationContext(),"Added to favorites",Toast.LENGTH_LONG).show();
             }else{
                 fab_favorite.setImageResource(R.drawable.ic_favorite_border_white_24dp);
                 Toast.makeText(getApplicationContext(),"Removed from favorites",Toast.LENGTH_LONG).show();
             }

         }
     }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
