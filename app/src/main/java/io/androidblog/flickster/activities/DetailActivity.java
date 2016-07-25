package io.androidblog.flickster.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import io.androidblog.flickster.R;
import io.androidblog.flickster.models.Movie;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class DetailActivity extends YouTubePlayerActivity {

    YouTubePlayerView youTubePlayerView;
    ImageView ivPoster;
    TextView tvTitle;
    RatingBar rbVoteAverage;
    TextView tvOverview;
    TextView tvVoteCount;
    TextView tvPopularity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        youTubePlayerView = (YouTubePlayerView) findViewById(R.id.ypMovieTrailer);
        ivPoster = (ImageView) findViewById(R.id.ivPoster);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        rbVoteAverage = (RatingBar) findViewById(R.id.rbVoteAverage);
        tvOverview = (TextView) findViewById(R.id.tvOverview);
        tvVoteCount = (TextView) findViewById(R.id.tvVoteCount);
        tvPopularity = (TextView) findViewById(R.id.tvPopularity);

        String movieId = getIntent().getStringExtra("movieId");
        setMovieInformation(movieId);

    }

    private void setMovieInformation(final String movieId) {

        //Get the video Information
        String source = "";
        String trailersUrl = "https://api.themoviedb.org/3/movie/" + movieId + "/trailers?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
        AsyncHttpClient trailersClient = new AsyncHttpClient();

        trailersClient.get(trailersUrl, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                JSONArray trailersJsonResults = null;

                try {

                    trailersJsonResults = response.getJSONArray("youtube");
                    JSONObject firstTrailer = trailersJsonResults.getJSONObject(0);

                    final String source = firstTrailer.getString("source");

                    youTubePlayerView.initialize("AIzaSyBRBuUU5BZGanRMp3r535-H8zIBpAtRKig",
                            new YouTubePlayer.OnInitializedListener() {
                                @Override
                                public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                                    YouTubePlayer youTubePlayer, boolean b) {

                                    // do any work here to cue video, play video, etc.
                                    youTubePlayer.cueVideo(source);
                                }

                                @Override
                                public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                                    YouTubeInitializationResult youTubeInitializationResult) {

                                }
                            });


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });


        //Get the detail information
        String url = "https://api.themoviedb.org/3/movie/" + movieId + "?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
        AsyncHttpClient client = new AsyncHttpClient();


        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    Movie movie = new Movie(response);

                    Picasso.with(DetailActivity.this)
                            .load(movie.getPosterPath())
                            .transform(new RoundedCornersTransformation(10, 10))
                            .placeholder(R.drawable.poster_placeholder)
                            .into(ivPoster);

                    tvTitle.setText(movie.getOriginalTitle());
                    rbVoteAverage.setRating(movie.getVoteAverage() / 2);
                    tvOverview.setText(movie.getOverview());
                    tvVoteCount.setText(movie.getVoteCount());
                    tvPopularity.setText(String.format("%.0f", movie.getPopularity()) + "%");

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
