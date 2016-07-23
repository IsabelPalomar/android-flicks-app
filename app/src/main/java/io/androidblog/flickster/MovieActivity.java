package io.androidblog.flickster;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import io.androidblog.flickster.adapters.MovieRecyclerViewAdapter;
import io.androidblog.flickster.models.Movie;

public class MovieActivity extends AppCompatActivity {

    ArrayList<Movie> movies;
    MovieRecyclerViewAdapter movieAdapter;
    RecyclerView rvMovies;
    SwipeRefreshLayout srlMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        srlMovies = (SwipeRefreshLayout) findViewById(R.id.srlMovies);
        rvMovies = (RecyclerView) findViewById(R.id.rvMovies);
        movies = new ArrayList<>();

        movieAdapter = new MovieRecyclerViewAdapter(this, movies);
        rvMovies.setAdapter(movieAdapter);
        rvMovies.setLayoutManager(new LinearLayoutManager(this));

        fetchMoviesAsync();

        srlMovies.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                movieAdapter.clear();
                fetchMoviesAsync();
                movieAdapter.addAll(movies);
            }
        });


    }

    private void fetchMoviesAsync() {

        String url = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

        AsyncHttpClient client = new AsyncHttpClient();

        client.get(url, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray movieJsonResults = null;

                try {
                    movieJsonResults = response.getJSONArray("results");
                    movies.addAll(Movie.fromJSONArray(movieJsonResults));
                    movieAdapter.notifyDataSetChanged();
                    srlMovies.setRefreshing(false);

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
