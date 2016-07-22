package io.androidblog.flickster.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

import io.androidblog.flickster.models.Movie;

public class MovieArrayAdapter extends ArrayAdapter<Movie> {

    public MovieArrayAdapter(Context context, List<Movie> movies) {
        super(context, android.R.layout.simple_list_item_1, movies);
    }
}
