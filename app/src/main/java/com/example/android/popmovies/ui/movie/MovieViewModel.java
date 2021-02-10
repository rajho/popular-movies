package com.example.android.popmovies.ui.movie;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.android.popmovies.data.AppDatabase;
import com.example.android.popmovies.data.MovieEntry;

public class MovieViewModel extends ViewModel {
  private LiveData<MovieEntry> mMovie;

  public MovieViewModel(AppDatabase database, int movieId) {
    mMovie = database.mMovieDao().loadMovieById(movieId);
  }

  public LiveData<MovieEntry> getMovie() {
    return mMovie;
  }
}
