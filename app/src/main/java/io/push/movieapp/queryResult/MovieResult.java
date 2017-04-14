package io.push.movieapp.queryResult;

import java.util.List;

import io.push.movieapp.entity.Movie;

/**
 * Created by nestorkokoafantchao on 3/7/17.
 */

public class MovieResult {
    private  Integer page ;
    private List<Movie> results;
    private  Integer totale_results;
    private  Integer totale_pages;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public List<Movie> getResults() {
        return results;
    }

    public void setResults(List<Movie> results) {
        this.results = results;
    }

    public Integer getTotale_results() {
        return totale_results;
    }

    public void setTotale_results(Integer totale_results) {
        this.totale_results = totale_results;
    }

    public Integer getTotale_pages() {
        return totale_pages;
    }

    public void setTotale_pages(Integer totale_pages) {
        this.totale_pages = totale_pages;
    }

    @Override
    public String toString() {
        return "MovieResult{" +
                "page=" + page +
                ", results=" + results +
                ", totale_results=" + totale_results +
                ", totale_pages=" + totale_pages +
                '}';
    }
}
