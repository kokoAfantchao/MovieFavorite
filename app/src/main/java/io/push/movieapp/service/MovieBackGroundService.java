package io.push.movieapp.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by nestorkokoafantchao on 4/28/17.
 */

public class MovieBackGroundService extends IntentService{

    public MovieBackGroundService() {
        super("MovieBackGroundService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        MovieJobTask.Movie(this);

    }


}
