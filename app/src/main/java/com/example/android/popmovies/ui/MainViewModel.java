package com.example.android.popmovies.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.android.popmovies.data.AppDatabase;
import com.example.android.popmovies.data.MovieEntry;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

  private LiveData<List<MovieEntry>> mMovieEntries;

  public MainViewModel(@NonNull Application application) {
    super(application);
    AppDatabase database = AppDatabase.getInstance(this.getApplication());
    mMovieEntries = database.mMovieDao().loadAllMovies();
  }

  public LiveData<List<MovieEntry>> getMovieEntries() {
    return mMovieEntries;
  }
}
