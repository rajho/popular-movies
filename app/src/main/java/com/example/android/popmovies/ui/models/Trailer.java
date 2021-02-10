package com.example.android.popmovies.ui.models;

public class Trailer {
  private String mYoutubeId;
  private String mName;

  public Trailer(String youtubeId, String name) {
    mYoutubeId = youtubeId;
    mName      = name;
  }

  public String getYoutubeId() {
    return mYoutubeId;
  }

  public void setYoutubeId(String youtubeId) {
    mYoutubeId = youtubeId;
  }

  public String getName() {
    return mName;
  }

  public void setName(String name) {
    mName = name;
  }
}
