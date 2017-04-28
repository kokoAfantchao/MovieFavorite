package io.push.movieapp.service;

import android.os.AsyncTask;
import android.support.v4.content.AsyncTaskLoader;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

/**
 * Created by nestorkokoafantchao on 4/28/17.
 */

public class MovieJobService extends JobService {

    AsyncTask asyncTask ;
    @Override
    public boolean onStartJob(final JobParameters job) {

        asyncTask = new AsyncTask<Void,Void,Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                MovieJobTask.Movie(getApplicationContext());

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                jobFinished(job,false);
            }
        };

        asyncTask.execute();
        return true;
    }


    @Override
    public boolean onStopJob(JobParameters job) {
        if(asyncTask!=null){
            asyncTask.cancel(true);
        }
        return true;
    }
}
