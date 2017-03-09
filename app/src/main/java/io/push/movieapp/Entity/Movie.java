package io.push.movieapp.Entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;
import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by nestorkokoafantchao on 12/7/16.
 */

public class Movie implements Parcelable{

    private String post_path;
    private Boolean adult;
    private String overview;
    private Date release_date;
    private Integer [] genre_is;
    private Integer id;
    private String originale_title;
    private String originale_language;
    private String title;
    private String backdrop_path;
    private Double  popularity ;
    private Integer vote_count;
    private Boolean video ;
    private Double  vote_average;

    public  Movie(){

    }

    protected Movie(Parcel in) {
        post_path = in.readString();
        overview = in.readString();
        originale_title = in.readString();
        originale_language = in.readString();
        title = in.readString();
        backdrop_path = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(post_path);
        dest.writeString(overview);
        dest.writeString(originale_title);
        dest.writeString(originale_language);
        dest.writeString(title);
        dest.writeString(backdrop_path);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getPost_path() {
        return post_path;
    }

    public void setPost_path(String post_path) {
        this.post_path = post_path;
    }

    public Boolean getAdult() {
        return adult;
    }

    public void setAdult(Boolean adult) {
        this.adult = adult;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Date getRelease_date() {
        return release_date;
    }

    public void setRelease_date(Date release_date) {
        this.release_date = release_date;
    }

    public Integer[] getGenre_is() {
        return genre_is;
    }

    public void setGenre_is(Integer[] genre_is) {
        this.genre_is = genre_is;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOriginale_title() {
        return originale_title;
    }

    public void setOriginale_title(String originale_title) {
        this.originale_title = originale_title;
    }

    public String getOriginale_language() {
        return originale_language;
    }

    public void setOriginale_language(String originale_language) {
        this.originale_language = originale_language;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public Integer getVote_count() {
        return vote_count;
    }

    public void setVote_count(Integer vote_count) {
        this.vote_count = vote_count;
    }

    public Boolean getVideo() {
        return video;
    }

    public void setVideo(Boolean video) {
        this.video = video;
    }

    public Double getVote_average() {
        return vote_average;
    }

    public void setVote_average(Double vote_average) {
        this.vote_average = vote_average;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "post_path='" + post_path + '\'' +
                ", adult=" + adult +
                ", overview='" + overview + '\'' +
                ", release_date=" + release_date +
                ", genre_is=" + Arrays.toString(genre_is) +
                ", id=" + id +
                ", originale_title='" + originale_title + '\'' +
                ", originale_language='" + originale_language + '\'' +
                ", title='" + title + '\'' +
                ", backdrop_path='" + backdrop_path + '\'' +
                ", popularity=" + popularity +
                ", vote_count=" + vote_count +
                ", video=" + video +
                ", vote_average=" + vote_average +
                '}';
    }

}
