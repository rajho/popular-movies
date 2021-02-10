package com.example.android.popmovies.data;

import android.provider.BaseColumns;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "movie")
public class MovieEntry {

  @PrimaryKey
  @ColumnInfo(name = BaseColumns._ID)
  private int mId;

  @ColumnInfo(name = "title")
  private String mTitle;

  @ColumnInfo(name = "release_date")
  private String mReleaseDate;

  @ColumnInfo(name = "image_path")
  private String mImagePath;

  @ColumnInfo(name = "vote_average")
  private Double mVoteAverage;

  @ColumnInfo(name = "plot_synopsis")
  private String mPlotSynopsis;

  public MovieEntry(int id,
      String title,
      String releaseDate,
      String imagePath,
      Double voteAverage,
      String plotSynopsis) {
    mId           = id;
    mTitle        = title;
    mReleaseDate  = releaseDate;
    mImagePath    = imagePath;
    mVoteAverage  = voteAverage;
    mPlotSynopsis = plotSynopsis;
  }

  public int getId() {
    return mId;
  }

  public void setId(int id) {
    mId = id;
  }

  public String getTitle() {
    return mTitle;
  }

  public void setTitle(String title) {
    mTitle = title;
  }

  public String getImagePath() {
    return mImagePath;
  }

  public void setImagePath(String imagePath) {
    mImagePath = imagePath;
  }

  public String getReleaseDate() {
    return mReleaseDate;
  }

  public void setReleaseDate(String releaseDate) {
    mReleaseDate = releaseDate;
  }

  public Double getVoteAverage() {
    return mVoteAverage;
  }

  public void setVoteAverage(Double voteAverage) {
    mVoteAverage = voteAverage;
  }

  public String getPlotSynopsis() {
    return mPlotSynopsis;
  }

  public void setPlotSynopsis(String plotSynopsis) {
    mPlotSynopsis = plotSynopsis;
  }
}
