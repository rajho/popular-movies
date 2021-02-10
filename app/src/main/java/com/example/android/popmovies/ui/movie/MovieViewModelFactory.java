package com.example.android.popmovies.ui.movie;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.android.popmovies.data.AppDatabase;

public class MovieViewModelFactory extends ViewModelProvider.NewInstanceFactory {
   private final AppDatabase mDb;
   private final int mMovieId;

  public MovieViewModelFactory(AppDatabase db, int movieId) {
    mDb      = db;
    mMovieId = movieId;
  }

  @NonNull
  @Override
  public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
    return (T) new MovieViewModel(mDb, mMovieId);
  }
}
