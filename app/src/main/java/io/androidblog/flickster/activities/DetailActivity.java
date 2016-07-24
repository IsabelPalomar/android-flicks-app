package io.androidblog.flickster.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import io.androidblog.flickster.R;
import io.androidblog.flickster.models.Movie;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class DetailActivity extends AppCompatActivity {

    ImageView ivBackdrop;
    ImageView ivPoster;
    TextView tvTitle;
    RatingBar rbVoteAverage;
    TextView tvOverview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ivBackdrop = (ImageView) findViewById(R.id.ivBackdrop);
        ivPoster = (ImageView) findViewById(R.id.ivPoster);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        rbVoteAverage = (RatingBar) findViewById(R.id.rbVoteAverage);
        tvOverview = (TextView) findViewById(R.id.tvOverview);

        String movieId = getIntent().getStringExtra("movieId");
        setMovieInformation(movieId);

    }

    private void setMovieInformation(final String movieId) {

        String url = "https://api.themoviedb.org/3/movie/" + movieId + "?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
        AsyncHttpClient client = new AsyncHttpClient();


        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    Movie movie = new Movie(response);
                    Log.d("debyf", movie.getBackdropPath());

                    Picasso.with(DetailActivity.this)
                            .load(movie.getBackdropPath())
                            .transform(new RoundedCornersTransformation(10, 10))
                            .placeholder(R.drawable.backdrop_placeholder)
                            .into(ivBackdrop);

                    Picasso.with(DetailActivity.this)
                            .load(movie.getPosterPath())
                            .transform(new RoundedCornersTransformation(10, 10))
                            .placeholder(R.drawable.poster_placeholder)
                            .into(ivPoster);

                    tvTitle.setText(movie.getOriginalTitle());
                    rbVoteAverage.setRating(movie.getVoteAverage()/2);
                    tvOverview.setText(movie.getOverview());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });

    }
}
