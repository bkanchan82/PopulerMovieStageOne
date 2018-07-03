package com.example.android.populermoviestageone;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.populermoviestageone.utilities.PopularMovie;
import com.example.android.populermoviestageone.utilities.TheMovieDbJsonUtils;
import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity {

    public static final String EXTRA_MOVIE_DATA = "extra_movie_object";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        final Toolbar toolbar = findViewById(R.id.MyToolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapse_toolbar);
        collapsingToolbar.setExpandedTitleTextAppearance(R.style.AppTheme_ExpendAppBar);
        collapsingToolbar.setCollapsedTitleTextAppearance(R.style.AppTheme_CollapsedAppBar);



        ImageView mPosterImageView = findViewById(R.id.iv_movie_poster);
        TextView mTitleTextView = findViewById(R.id.tv_title);
        TextView mRatingTextView = findViewById(R.id.tv_user_rating);
        TextView mReleaseDateTextView = findViewById(R.id.tv_release_date);
        TextView mOverviewTextView = findViewById(R.id.tv_overview);

        Intent intentThatInvokeTheActivity = getIntent();
        if (intentThatInvokeTheActivity.hasExtra(EXTRA_MOVIE_DATA)){
            PopularMovie popularMovie = intentThatInvokeTheActivity.getParcelableExtra(EXTRA_MOVIE_DATA);

            if(popularMovie!=null){
                Picasso.with(this).load(popularMovie.getPosterPath(TheMovieDbJsonUtils.POSTER_SIZE)).into(mPosterImageView);
                collapsingToolbar.setTitle(popularMovie.getTitle());
                mTitleTextView.setText(popularMovie.getOriginalTitle());
                mRatingTextView.setText(String.valueOf(popularMovie.getVoteCount()));
                mReleaseDateTextView.setText(getString(R.string.releasing_on,popularMovie.getReleaseDate()));
                mOverviewTextView.setText(popularMovie.getOverview());
            }

        }

    }

}
