package com.example.android.populermoviestageone.utilities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created on 21-12-2017.
 */

public class PopularMovie implements Parcelable{

    private String title;
    private int voteCount;
    private int popularity;
    private String posterPath;
    private String overview;
    private String releaseDate;
    private String originalTitle;


    public PopularMovie(String title,
                        int voteCount,
                        int popularity,
                        String posterPath,
                        String overview,
                        String releaseDate,
                        String originalTitle) {
        this.title = title;
        this.voteCount = voteCount;
        this.popularity = popularity;
        this.posterPath = posterPath;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.originalTitle = originalTitle;
    }

    private PopularMovie(Parcel in) {
        title = in.readString();
        voteCount = in.readInt();
        popularity = in.readInt();
        posterPath = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
        originalTitle = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeInt(voteCount);
        dest.writeInt(popularity);
        dest.writeString(posterPath);
        dest.writeString(overview);
        dest.writeString(releaseDate);
        dest.writeString(originalTitle);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PopularMovie> CREATOR = new Creator<PopularMovie>() {
        @Override
        public PopularMovie createFromParcel(Parcel in) {
            return new PopularMovie(in);
        }

        @Override
        public PopularMovie[] newArray(int size) {
            return new PopularMovie[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public String getPosterPath(String posterSize) {
        return TheMovieDbJsonUtils.POSTER_BASE_URL+posterSize+this.posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }
}
