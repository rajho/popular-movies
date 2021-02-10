package com.example.android.popmovies.utilities;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.Scanner;

public class NetworkUtils {
  public static final String MOVIE_PATH           = "movie";
  public static final String POPULAR_MOVIE_PATH   = "popular";
  public static final String TOP_RATED_MOVIE_PATH = "top_rated";
  public static final String MOVIE_TRAILERS_PATH  = "videos";
  public static final String MOVIE_REVIEWS_PATH   = "reviews";
  static final        String BASE_URL             = "https://api.themoviedb.org/3/";
  static final        String API_KEY              = "";
  static final        String IMAGE_BASE_URL       = "http://image.tmdb.org/t/p/";
  static final        String IMAGE_SIZE           = "w185";
  static final        String YOUTUBE_BASE_URL     = "https://www.youtube.com/watch";
  static final        String YOUTUBE_VIDEO_PARAM  = "v";

  static final String API_KEY_PARAM = "api_key";
  static final String LANGUAGE_PARAM = "language";


  public static URL buildAPIURL(String... paths) {
    Uri.Builder uriBuilder = Uri.parse(BASE_URL)
                      .buildUpon()
                      .appendPath(MOVIE_PATH);

    for (String s: paths){
      uriBuilder.appendPath(s);
    }

    Uri buildUri = uriBuilder.appendQueryParameter(API_KEY_PARAM, API_KEY)
                      .appendQueryParameter(LANGUAGE_PARAM, Locale.getDefault().getLanguage())
                      .build();

    URL url = null;
    try {
      url = new URL(buildUri.toString());
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }

    return url;
  }

  public static URL buildImageURL(String path) {
    Uri buildUri = Uri.parse(IMAGE_BASE_URL)
                      .buildUpon()
                      .appendPath(IMAGE_SIZE)
                      .appendPath(path)
                      .build();

    URL url = null;
    try {
      url = new URL(buildUri.toString());
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }

    return url;
  }

  public static URL buildYoutubeURL(String youtubeId){
    Uri buildUri = Uri.parse(YOUTUBE_BASE_URL)
                      .buildUpon()
                      .appendQueryParameter(YOUTUBE_VIDEO_PARAM, youtubeId)
                      .build();
    URL url = null;
    try {
      url = new URL(buildUri.toString());
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
    return url;
  }

  public static String getResponseFromHttpUrl(URL url) throws IOException {
    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
    try {
      InputStream in      = urlConnection.getInputStream();
      Scanner     scanner = new Scanner(in);
      scanner.useDelimiter("\\A");

      boolean hasInput = scanner.hasNext();
      if (hasInput) {
        return scanner.next();
      } else {
        return null;
      }
    } finally {
      urlConnection.disconnect();
    }
  }

  public static boolean isOnline() {
    Runtime runtime = Runtime.getRuntime();
    try {
      Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
      int     exitValue = ipProcess.waitFor();
      return (exitValue == 0);
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }

    return false;
  }
}
