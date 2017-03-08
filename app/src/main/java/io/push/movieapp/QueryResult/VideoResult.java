package io.push.movieapp.QueryResult;

import java.util.List;

import io.push.movieapp.Entity.Review;
import io.push.movieapp.Entity.Video;

/**
 * Created by nestorkokoafantchao on 3/7/17.
 */

public class VideoResult {

    private Integer id;
    private List<Video> results;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Video> getResults() {
        return results;
    }

    public void setResults(List<Video> results) {
        this.results = results;
    }
}
