package com.example.android.populermoviestageone.utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;

/**
 * Created on 20-12-2017.
 */

public class TheMovieDbJsonUtils {

    //Movies information is contained in the array named 'results'
    private static final String TMD_RESULTS = "results";

    //this variable holds the value of vote count of the movie
    private static final String TMD_VOTE_COUNT = "vote_count";

    //this variable holds the value of title of the movie
    private static final String TMD_TITLE = "title";

    //this variable holds the value of popularity of the movie
    private static final String TMD_POPULARITY = "popularity";


    //this variable holds the value of poster image path of the movie
    private static final String TMD_POSTER_PATH = "poster_path";

    //this variable holds the value of overview of the movie
    private static final String TMD_OVERVIEW = "overview";

    //this variable holds the value of release date of the movie
    private static final String TMD_RELEASE_DATE = "release_date";

    private static final String TMD_ORIGINAL_TITLE = "original_title";


    //this variable holds the error code if there is any problem in fulfilling the request
    private static final String TMD_MESSAGE_CODE = "status_message";

    public static final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/";
    public static final String POSTER_LOGO_SIZE = "w185/";
    public static final String POSTER_SIZE = "w300/";

    public static ArrayList<PopularMovie> getSimpleMovieArrayListFromJson(JSONObject moviesJson)
            throws JSONException {

        /* String array list to hold each movie String */
        ArrayList<PopularMovie> parsedMovieData;

        /* Is there an error? */
        if (moviesJson.has(TMD_MESSAGE_CODE)) {
            int errorCode = moviesJson.getInt(TMD_MESSAGE_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    /* Location invalid */
                    return null;
                default:
                    /* Server probably down */
                    return null;
            }
        }

        JSONArray movieArray = moviesJson.getJSONArray(TMD_RESULTS);

        parsedMovieData = new ArrayList<>();

        for (int i = 0; i < movieArray.length(); i++) {

            /* These are the values that will be collected */
            String title;
            String overview;
            int voteCount;
            String releaseDate;
            int popularity;
            String posterPath;
            String originalTitle;

            /* Get the JSON object representing the movie */
            JSONObject movieDetail = movieArray.getJSONObject(i);

            title = movieDetail.getString(TMD_TITLE);
            overview = movieDetail.getString(TMD_OVERVIEW);
            voteCount = movieDetail.getInt(TMD_VOTE_COUNT);
            releaseDate = movieDetail.getString(TMD_RELEASE_DATE);
            popularity = movieDetail.getInt(TMD_POPULARITY);
            posterPath = movieDetail.getString(TMD_POSTER_PATH);
            originalTitle = movieDetail.getString(TMD_ORIGINAL_TITLE);

            parsedMovieData.add( new PopularMovie(title,voteCount,popularity,posterPath,overview,releaseDate,originalTitle));
        }

        return parsedMovieData;
    }


}
