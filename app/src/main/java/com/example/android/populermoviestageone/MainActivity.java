package com.example.android.populermoviestageone;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.android.populermoviestageone.app.PopularMovieAppController;
import com.example.android.populermoviestageone.utilities.NetworkUtils;
import com.example.android.populermoviestageone.utilities.PopularMovie;
import com.example.android.populermoviestageone.utilities.TheMovieDbJsonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnItemClickListener,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String MOVIE_ARRAY_LIST = "movie_array_list";

    //mDisplayErrorTV will display error message if there is any error in loading the movie list
    private TextView mDisplayErrorTV;

    //mDisplayMoviesRV is used to display the movie list on the entire screen
    private RecyclerView mDisplayMoviesRV;

    //loadingMovieListPb will show progress bar while it will load movie list from network
    private ProgressBar loadingMovieListPb;

    private MovieAdapter adapter;

    private static ArrayList<PopularMovie> mPopularMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get the java object from xml layout resource
        mDisplayErrorTV = findViewById(R.id.tv_display_error);
        mDisplayMoviesRV = findViewById(R.id.rv_display_movies);
        loadingMovieListPb = findViewById(R.id.pb_loading_movies);

        final int columns = getResources().getInteger(R.integer.gallery_column);
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(columns, StaggeredGridLayoutManager.VERTICAL);

        mDisplayMoviesRV.setLayoutManager(gridLayoutManager);
        mDisplayMoviesRV.setHasFixedSize(true);

        adapter = new MovieAdapter(this, this);

        mDisplayMoviesRV.setAdapter(adapter);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        if (savedInstanceState != null) {
            ArrayList<PopularMovie> data = savedInstanceState.getParcelableArrayList(MOVIE_ARRAY_LIST);
            if (data != null && data.size() > 0) {
                loadingMovieListPb.setVisibility(View.INVISIBLE);
                adapter.setMovieData(data);
                showMovies();
            } else {
                loadMovies(sharedPreferences);
            }
        } else {
            loadMovies(sharedPreferences);
        }
    }


    private String getShortingOrder(SharedPreferences sharedPreferences) {
        return sharedPreferences.getString(getString(R.string.pref_movie_shorting_key),
                getString(R.string.pref_default_movie_shorting_value));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int selectedId = item.getItemId();
        if (selectedId == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadMovies(SharedPreferences sharedPreferences) {
        showMovies();
        adapter.setMovieData(null);

        String shortingOrder = getShortingOrder(sharedPreferences);
        try {
            String movieListString = NetworkUtils.buildUrl(shortingOrder,getString(R.string.THE_MOVIE_API_KEY)).toString();

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                    movieListString,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.v(TAG, "MOVIE Response : " + response.toString());
                            loadingMovieListPb.setVisibility(View.INVISIBLE);
                            try {
                                ArrayList<PopularMovie> data = TheMovieDbJsonUtils.getSimpleMovieArrayListFromJson(response);
                                if (data != null && data.size() > 0) {
                                    mPopularMovies = data;
                                    adapter.setMovieData(data);
                                    showMovies();
                                } else {
                                    showErrorMessage();
                                }
                            } catch (JSONException jsonException) {
                                jsonException.printStackTrace();
                                showErrorMessage();
                            }

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    loadingMovieListPb.setVisibility(View.INVISIBLE);
                    showErrorMessage();
                    error.printStackTrace();
                }
            });
            PopularMovieAppController.getInstance().addRequestQueue(jsonObjectRequest);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.toString());
        }

    }

    //This is called when we will have a valid data
    private void showMovies() {
        mDisplayErrorTV.setVisibility(View.INVISIBLE);
        mDisplayMoviesRV.setVisibility(View.VISIBLE);
    }


    //This is called when an error occurred in loading movie list
    private void showErrorMessage() {
        mDisplayErrorTV.setVisibility(View.VISIBLE);
        mDisplayMoviesRV.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onMovieItemClickListener(PopularMovie movieObject) {

        Context context = this;
        Class staticClass = MovieDetailActivity.class;
        Intent intent = new Intent(context, staticClass);
        intent.putExtra(MovieDetailActivity.EXTRA_MOVIE_DATA, movieObject);
        startActivity(intent);

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_movie_shorting_key))) {
            String shortBy = sharedPreferences.getString(getString(R.string.pref_movie_shorting_key), getString(R.string.pref_default_movie_shorting_value));

            Log.d(TAG, "On Shared Preference Change Listener short by : " + shortBy);

            loadMovies(sharedPreferences);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(MOVIE_ARRAY_LIST, mPopularMovies);
    }

}
