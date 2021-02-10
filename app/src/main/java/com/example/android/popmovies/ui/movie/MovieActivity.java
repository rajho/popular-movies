package com.example.android.popmovies.ui.movie;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;

import com.example.android.popmovies.R;
import com.example.android.popmovies.data.AppDatabase;
import com.example.android.popmovies.data.MovieEntry;
import com.example.android.popmovies.databinding.ActivityMovieBinding;
import com.example.android.popmovies.databinding.MovieReviewItemBinding;
import com.example.android.popmovies.databinding.MovieTrailerItemBinding;
import com.example.android.popmovies.ui.models.Movie;
import com.example.android.popmovies.ui.models.Review;
import com.example.android.popmovies.ui.models.Trailer;
import com.example.android.popmovies.utilities.AppExecutors;
import com.example.android.popmovies.utilities.MoviesJsonUtils;
import com.example.android.popmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

public class MovieActivity extends AppCompatActivity {
  public static final  String MOVIE_ID_EXTRA        = "movie-id";
  public static final  String MOVIE_IS_FAVORITE        = "movie-is-favorite";
  private static final int    FETCH_TRAILERS_LOADER = 20;
  private static final int    FETCH_REVIEWS_LOADER  = 30;

  private ActivityMovieBinding mBinding;
  private boolean mIsFavorite;
  private Movie mMovie;
  private AppDatabase mDb;

  private final LoaderManager.LoaderCallbacks<List<Review>> reviewLoaderCallbacks =
      new LoaderManager.LoaderCallbacks<List<Review>>() {
    @NonNull
    @Override
    public Loader<List<Review>> onCreateLoader(int id, @Nullable Bundle args) {
      return new AsyncTaskLoader<List<Review>>(MovieActivity.this) {
        List<Review> mReviews = null;

        @Override
        protected void onStartLoading() {
          super.onStartLoading();
          if (args == null) {
            return;
          }

          if (mReviews != null) {
            deliverResult(mReviews);
          } else {
            forceLoad();
          }
        }

        @Nullable
        @Override
        public List<Review> loadInBackground() {
          int movieId = args.getInt(MOVIE_ID_EXTRA);
          if (movieId == 0) {
            return null;
          }

          try {
            URL trailersUrl = NetworkUtils.buildAPIURL(String.valueOf(movieId),
                NetworkUtils.MOVIE_REVIEWS_PATH
            );
            String jsonReviewsResponse = NetworkUtils.getResponseFromHttpUrl(trailersUrl);

            return MoviesJsonUtils.getReviewsListFromJson(jsonReviewsResponse);
          } catch (Exception e) {
            e.printStackTrace();
            return null;
          }
        }

        @Override
        public void deliverResult(@Nullable List<Review> data) {
          mReviews = data;
          super.deliverResult(data);
        }
      };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Review>> loader, List<Review> data) {
      LinearLayout reviewsContainer = mBinding.reviewsContainer;

      if (data.size() > 0) {
        for (Review review : data) {
          LayoutInflater         inflater = LayoutInflater.from(MovieActivity.this);
          MovieReviewItemBinding binding  = MovieReviewItemBinding.inflate(inflater);
          binding.tvAuthor.setText(review.getAuthor());
          binding.tvContent.setText(review.getContent());
          reviewsContainer.addView(binding.getRoot());
        }
      } else {
        mBinding.tvNoReviews.setVisibility(View.VISIBLE);
      }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Review>> loader) {

    }
  };

  private final LoaderManager.LoaderCallbacks<List<Trailer>> trailerLoaderCallbacks =
      new LoaderManager.LoaderCallbacks<List<Trailer>>() {
    @NonNull
    @Override
    public Loader<List<Trailer>> onCreateLoader(int id, @Nullable Bundle args) {
      return new AsyncTaskLoader<List<Trailer>>(MovieActivity.this) {
        List<Trailer> mTrailers;

        @Override
        protected void onStartLoading() {
          super.onStartLoading();
          if (args == null) {
            return;
          }

          if (mTrailers != null) {
            deliverResult(mTrailers);
          } else {
            forceLoad();
          }
        }

        @Nullable
        @Override
        public List<Trailer> loadInBackground() {
          int movieId = args.getInt(MOVIE_ID_EXTRA);
          if (movieId == 0) {
            return null;
          }

          try {
            URL trailersUrl = NetworkUtils.buildAPIURL(String.valueOf(movieId),
                NetworkUtils.MOVIE_TRAILERS_PATH
            );
            String jsonTrailersResponse = NetworkUtils.getResponseFromHttpUrl(trailersUrl);

            return MoviesJsonUtils.getTrailersListFromJson(jsonTrailersResponse);
          } catch (Exception e) {
            e.printStackTrace();
            return null;
          }
        }

        @Override
        public void deliverResult(@Nullable List<Trailer> data) {
          mTrailers = data;
          super.deliverResult(data);
        }
      };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Trailer>> loader, List<Trailer> data) {
      LinearLayout trailersContainer = mBinding.trailersContainer;

      if (data.size() > 0) {
        for (Trailer trailer : data) {
          LayoutInflater          inflater = LayoutInflater.from(MovieActivity.this);
          MovieTrailerItemBinding binding  = MovieTrailerItemBinding.inflate(inflater);
          binding.tvTrailerName.setText(trailer.getName());
          binding.getRoot().setOnClickListener(v -> {
            URL    youtubeUrl = NetworkUtils.buildYoutubeURL(trailer.getYoutubeId());
            Intent intent     = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(String.valueOf(youtubeUrl)));
            if (intent.resolveActivity(getPackageManager()) != null) {
              startActivity(intent);
            }
          });
          trailersContainer.addView(binding.getRoot());
        }
      } else {
        mBinding.tvNoTrailers.setVisibility(View.VISIBLE);
      }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Trailer>> loader) {

    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = ActivityMovieBinding.inflate(getLayoutInflater());
    setContentView(mBinding.getRoot());

    if (savedInstanceState != null){
        mIsFavorite = savedInstanceState.getBoolean(MOVIE_IS_FAVORITE, false);
    }

    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setTitle("Movie Detail");
    }

    mDb = AppDatabase.getInstance(getApplicationContext());

    Intent intent = getIntent();
    mMovie  = intent.getParcelableExtra(Intent.EXTRA_STREAM);

    URL imageUrl = NetworkUtils.buildImageURL(mMovie.getImagePath());
    if (imageUrl != null) {
      Picasso.get().load(imageUrl.toString()).into(mBinding.ivMovie);
    }
    mBinding.tvTitle.setText(mMovie.getTitle());
    mBinding.tvReleaseDate.setText(mMovie.getReleaseDate());
    String voteAverage = mMovie.getVoteAverage() + " / 10";
    mBinding.tvVoteAverage.setText(voteAverage);
    mBinding.tvPlot.setText(mMovie.getPlotSynopsis());

    retrieveFavoriteStatus(mMovie.getId());
    loadMovieData(mMovie.getId(), FETCH_TRAILERS_LOADER);
    loadMovieData(mMovie.getId(), FETCH_REVIEWS_LOADER);
  }

  private void loadMovieData(int movieId, int loaderId) {
    if (!NetworkUtils.isOnline()) {
      Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
      return;
    }

    Bundle bundle = new Bundle();
    bundle.putInt(MOVIE_ID_EXTRA, movieId);
    LoaderManager loaderManager = LoaderManager.getInstance(this);

    if (FETCH_TRAILERS_LOADER == loaderId) {
      loaderManager.restartLoader(loaderId, bundle, trailerLoaderCallbacks);
    }

    if (FETCH_REVIEWS_LOADER == loaderId) {
      loaderManager.restartLoader(loaderId, bundle, reviewLoaderCallbacks);
    }
  }

  private void retrieveFavoriteStatus(int movieId) {
    MovieViewModelFactory factory = new MovieViewModelFactory(mDb, movieId);
    MovieViewModel viewModel = new ViewModelProvider(this, factory).get(MovieViewModel.class);
    viewModel.getMovie().observe(this, movie -> {
      mIsFavorite = movie != null;
      setFavoriteDrawable(mIsFavorite);
    });
  }

  @SuppressLint("UseCompatLoadingForDrawables")
  public void setFavoriteDrawable(boolean isFavorite) {
    if (isFavorite){
      mBinding.ivFavoriteStar.setImageDrawable(getResources().getDrawable(R.drawable.ic_star));
    } else {
      mBinding.ivFavoriteStar.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_outline));
    }
  }

  @Override
  protected void onSaveInstanceState(@NonNull Bundle outState) {
    outState.putBoolean(MOVIE_IS_FAVORITE, mIsFavorite);
    super.onSaveInstanceState(outState);
  }

  @SuppressLint("UseCompatLoadingForDrawables")
  public void setFavoriteStatus(View view) {
    // Favorite or Unfavorite the movie
    mIsFavorite = !mIsFavorite;

    MovieEntry movie = new MovieEntry(mMovie.getId(),
        mMovie.getTitle(),
        mMovie.getReleaseDate(),
        mMovie.getImagePath(),
        mMovie.getVoteAverage(),
        mMovie.getPlotSynopsis()
    );

    if (mIsFavorite) {
      AppExecutors.getInstance().diskIO().execute(() -> {
        mDb.mMovieDao().insertMovie(movie);
      });
    } else {
      AppExecutors.getInstance().diskIO().execute(() -> mDb.mMovieDao().deleteTask(movie));
    }
    setFavoriteDrawable(mIsFavorite);
  }
}