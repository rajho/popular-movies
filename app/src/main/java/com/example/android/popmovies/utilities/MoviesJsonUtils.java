package com.example.android.popmovies.utilities;

import com.example.android.popmovies.ui.models.Movie;
import com.example.android.popmovies.ui.models.Review;
import com.example.android.popmovies.ui.models.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MoviesJsonUtils {

  public static List<Movie> getMovieListFromJson(String jsonMoviesResponse) throws
      JSONException {

    final String MOV_RESULTS = "results";
    final String MOV_TITLE = "title";
    final String MOV_RELEASE_DATE = "release_date";
    final String MOV_IMAGE_PATH = "poster_path";
    final String MOV_VOTE_AVERAGE = "vote_average";
    final String MOV_PLOT_SYNOPSIS = "overview";
    final String MOV_ID = "id";

    JSONObject moviesJson = new JSONObject(jsonMoviesResponse);
    List<Movie> moviesData = new ArrayList<>();

    JSONArray moviesArray = moviesJson.getJSONArray(MOV_RESULTS);
    for (int i = 0; i < moviesArray.length(); i++){
      JSONObject movieObject = moviesArray.getJSONObject(i);
      Movie movie = new Movie(
          movieObject.getInt(MOV_ID),
          movieObject.getString(MOV_TITLE),
          movieObject.getString(MOV_RELEASE_DATE),
          movieObject.getString(MOV_IMAGE_PATH).replaceAll("/", ""),
          movieObject.getDouble(MOV_VOTE_AVERAGE),
          movieObject.getString(MOV_PLOT_SYNOPSIS)
      );

      moviesData.add(movie);
    }

    return moviesData;
  }

  public static List<Trailer> getTrailersListFromJson(String jsonTrailersResponse) throws
      JSONException {

    final String MOV_TRAILERS_RESULTS = "results";
    final String TRAILER_YOUTUBE_ID = "key";
    final String TRAILER_NAME = "name";

    JSONObject trailersJson = new JSONObject(jsonTrailersResponse);
    List<Trailer> trailersData = new ArrayList<>();

    JSONArray trailersArray = trailersJson.getJSONArray(MOV_TRAILERS_RESULTS);
    for (int i = 0; i < trailersArray.length(); i++){
      JSONObject trailerObject = trailersArray.getJSONObject(i);
      Trailer trailer = new Trailer(
          trailerObject.getString(TRAILER_YOUTUBE_ID),
          trailerObject.getString(TRAILER_NAME)
      );

      trailersData.add(trailer);
    }

    return trailersData;
  }

  public static List<Review> getReviewsListFromJson(String jsonReviewsResponse) throws
      JSONException {

    final String MOV_REVIEWS_RESULTS = "results";
    final String REVIEW_AUTHOR = "author";
    final String REVIEW_CONTENT = "content";

    JSONObject   reviewsJson = new JSONObject(jsonReviewsResponse);
    List<Review> reviewsData = new ArrayList<>();

    JSONArray reviewsArray = reviewsJson.getJSONArray(MOV_REVIEWS_RESULTS);
    for (int i = 0; i < reviewsArray.length(); i++){
      JSONObject trailerObject = reviewsArray.getJSONObject(i);
      Review trailer = new Review(
          trailerObject.getString(REVIEW_AUTHOR),
          trailerObject.getString(REVIEW_CONTENT)
      );

      reviewsData.add(trailer);
    }

    return reviewsData;
  }
}
