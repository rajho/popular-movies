package com.example.android.popmovies.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.android.popmovies.R;
import com.example.android.popmovies.data.MovieEntry;
import com.example.android.popmovies.databinding.ActivityMainBinding;
import com.example.android.popmovies.ui.models.Movie;
import com.example.android.popmovies.ui.movie.MovieActivity;
import com.example.android.popmovies.utilities.MoviesJsonUtils;
import com.example.android.popmovies.utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
    implements MoviesAdapter.MoviesAdapterOnClickHandler,
               LoaderManager.LoaderCallbacks<List<Movie>> {
  private static final int    MOVIES_LOADER     = 1;
  private static final String MOVIES_LIST_EXTRA = "movies-list";
  private static final String FAVORITE_MOVIES   = "favorite-movies";

  private ActivityMainBinding mBinding;
  private MoviesAdapter       mMoviesAdapter;
  private String              mActiveMovieList = NetworkUtils.POPULAR_MOVIE_PATH;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = ActivityMainBinding.inflate(getLayoutInflater());
    setContentView(mBinding.getRoot());
    Log.d(MainActivity.class.getSimpleName(), "onCreate");

    if (savedInstanceState != null) {
      mActiveMovieList = savedInstanceState.getString(MOVIES_LIST_EXTRA,
          NetworkUtils.POPULAR_MOVIE_PATH
      );
    }

    int               columnWithDp  = 180;
    GridLayoutManager layoutManager = new GridLayoutManager(
        this,
        calculateBestSpanCount(columnWithDp)
    );
    mMoviesAdapter = new MoviesAdapter(this);

    mBinding.rvMovies.setLayoutManager(layoutManager);
    mBinding.rvMovies.setHasFixedSize(true);
    mBinding.rvMovies.setAdapter(mMoviesAdapter);

    loadMoviesData();
  }

  @Override
  protected void onRestart() {
    super.onRestart();
    Log.d(MainActivity.class.getSimpleName(), "onRestart");
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.sort_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    int id = item.getItemId();

    if (id == R.id.popular_item) {
      mActiveMovieList = NetworkUtils.POPULAR_MOVIE_PATH;
    }

    if (id == R.id.top_rated_item) {
      mActiveMovieList = NetworkUtils.TOP_RATED_MOVIE_PATH;
    }

    if (id == R.id.favorites_item) {
      mActiveMovieList = FAVORITE_MOVIES;
    }
    loadMoviesData();

    return super.onOptionsItemSelected(item);
  }

  private void loadFavoriteMovies() {
    MainViewModel viewModel =
        new ViewModelProvider.AndroidViewModelFactory(this.getApplication()).create(
        MainViewModel.class);

    viewModel.getMovieEntries().observe(this, movieEntries -> {
      Log.d(MainActivity.class.getSimpleName(), "Updating list of movies from ViewModel");
      List<Movie> movies = new ArrayList<>();

      // Converting movies fetched from the database to the movie class recognized by the adapter
      for (MovieEntry entry : movieEntries) {
        Movie movie = new Movie(entry.getId(),
            entry.getTitle(),
            entry.getReleaseDate(),
            entry.getImagePath(),
            entry.getVoteAverage(),
            entry.getPlotSynopsis()
        );
        movies.add(movie);
      }

      // Sending movies retrieved from the database to the adapter
      if (!movies.isEmpty()) {
        mMoviesAdapter.setData(movies);
      } else {
        showErrorMessage();
      }
    });
  }

  private void showMoviesView() {
    mBinding.tvErrorMessage.setVisibility(View.INVISIBLE);
    mBinding.rvMovies.setVisibility(View.VISIBLE);
  }

  private void showErrorMessage() {
    mBinding.rvMovies.setVisibility(View.INVISIBLE);
    mBinding.tvErrorMessage.setVisibility(View.VISIBLE);
  }

  @Override
  public void onClick(Movie movie) {
    Intent intent = new Intent(this, MovieActivity.class);
    intent.putExtra(Intent.EXTRA_STREAM, movie);
    startActivity(intent);
  }

  /**
   * Load movies data sorted by path specified. (popular or top_rated)
   */
  private void loadMoviesData() {
    showMoviesView();

    if (mActiveMovieList.equals(FAVORITE_MOVIES)) {
      loadFavoriteMovies();
      return;
    }

    if (!NetworkUtils.isOnline()) {
      Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
      return;
    }

    Bundle bundle = new Bundle();
    bundle.putString(MOVIES_LIST_EXTRA, mActiveMovieList);
    LoaderManager       loaderManager    = LoaderManager.getInstance(this);
    Loader<List<Movie>> moviesListLoader = loaderManager.getLoader(MOVIES_LOADER);
    if (moviesListLoader == null) {
      loaderManager.initLoader(MOVIES_LOADER, bundle, this);
    } else {
      loaderManager.restartLoader(MOVIES_LOADER, bundle, this);
    }
  }

  private int calculateBestSpanCount(int posterWidthDP) {
    DisplayMetrics metrics = new DisplayMetrics();
    getWindowManager().getDefaultDisplay().getMetrics(metrics);
    float logicalDensity = metrics.density;
    float widthInPixels  = posterWidthDP * logicalDensity;
    int   spanCount      = (int) Math.floor(metrics.widthPixels / widthInPixels);
    return Math.max(spanCount, 2);
  }

  @NonNull
  @Override
  public Loader<List<Movie>> onCreateLoader(int id, @Nullable Bundle args) {
    return new AsyncTaskLoader<List<Movie>>(this) {
      List<Movie> mMovies;

      @Override
      protected void onStartLoading() {
        super.onStartLoading();
        if (args == null) {
          return;
        }

        Log.d(MainActivity.class.getSimpleName(), "onStartLoading caching");
        if (mMovies != null) {
          deliverResult(mMovies);
        } else {
          mBinding.rvMovies.setVisibility(View.INVISIBLE);
          mBinding.loadingIndicator.setVisibility(View.VISIBLE);
          forceLoad();
        }
      }

      @Nullable
      @Override
      public List<Movie> loadInBackground() {
        String path = args.getString(MOVIES_LIST_EXTRA);
        if (TextUtils.isEmpty(path)) {
          return null;
        }
        try {
          URL    moviesRequestUrl   = NetworkUtils.buildAPIURL(path);
          String jsonMoviesResponse = NetworkUtils.getResponseFromHttpUrl(moviesRequestUrl);

          return MoviesJsonUtils.getMovieListFromJson(jsonMoviesResponse);
        } catch (Exception e) {
          e.printStackTrace();
          return null;
        }
      }

      @Override
      public void deliverResult(@Nullable List<Movie> data) {
        mMovies = data;
        super.deliverResult(data);
      }
    };
  }

  @Override
  public void onLoadFinished(@NonNull Loader<List<Movie>> loader, List<Movie> data) {
    mBinding.loadingIndicator.setVisibility(View.INVISIBLE);
    if (data != null && !data.isEmpty()) {
      showMoviesView();
      mMoviesAdapter.setData(data);
    } else {
      showErrorMessage();
    }
  }

  @Override
  public void onLoaderReset(@NonNull Loader<List<Movie>> loader) {

  }

  @Override
  protected void onSaveInstanceState(@NonNull Bundle outState) {
    outState.putString(MOVIES_LIST_EXTRA, mActiveMovieList);

    super.onSaveInstanceState(outState);
  }
}