package io.androidblog.flickster.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Movie {

    String id;
    String posterPath;
    String backdropPath;
    String originalTitle;
    String overview;
    float voteAverage;
    String voteCount;
    float popularity;

    public String getId() {
        return id;
    }
    public String getPosterPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s", posterPath);
    }

    public String getBackdropPath() {
        return String.format("https://image.tmdb.org/t/p/w780/%s", backdropPath);
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public String getVoteCount() {
        return voteCount;
    }

    public float getPopularity() {
        return popularity;
    }

    public float getVoteAverage() {
        return voteAverage;
    }
    public Movie(JSONObject jsonObject) throws JSONException{
        this.id = jsonObject.getString("id");
        this.posterPath = jsonObject.getString("poster_path");
        this.backdropPath = jsonObject.getString("backdrop_path");
        this.originalTitle = jsonObject.getString("original_title");
        this.overview = jsonObject.getString("overview");
        this.voteAverage = jsonObject.getLong("vote_average");
        this.voteCount = jsonObject.getString("vote_count");
        this.popularity = jsonObject.getLong("popularity");
    }

    public static ArrayList<Movie> fromJSONArray(JSONArray array){

        ArrayList<Movie> results = new ArrayList<>();

        for (int x=0;x<array.length();x++){
            try {
                results.add(new Movie(array.getJSONObject(x)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return results;
    }

}
