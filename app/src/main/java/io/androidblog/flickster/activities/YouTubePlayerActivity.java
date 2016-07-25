package io.androidblog.flickster.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import io.androidblog.flickster.R;
import io.androidblog.flickster.models.Movie;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class YouTubePlayerActivity extends YouTubeBaseActivity {

    YouTubePlayerView youTubePlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_player);

        String movieId = getIntent().getStringExtra("movieId");
        youTubePlayerView = (YouTubePlayerView) findViewById(R.id.ypMovieTrailer);

        setMovieVideo(movieId);

    }

    private void setMovieVideo(String movieId) {


        String url = "https://api.themoviedb.org/3/movie/" + movieId + "/trailers?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
        AsyncHttpClient client = new AsyncHttpClient();


        client.get(url, new JsonHttpResponseHandler() {
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
                                    youTubePlayer.loadVideo(source);
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

    }
}
