package com.example.android.popmovies.ui.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {

  public static final Creator<Movie> CREATOR = new Creator<Movie>() {
    @Override
    public Movie createFromParcel(Parcel in) {
      return new Movie(in);
    }

    @Override
    public Movie[] newArray(int size) {
      return new Movie[size];
    }
  };

  private int mId;
  private String mTitle;
  private String mReleaseDate;
  private String mImagePath;
  private Double mVoteAverage;
  private String mPlotSynopsis;

  public Movie(
      int id,
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

  protected Movie(Parcel in) {
    mId           = in.readInt();
    mTitle        = in.readString();
    mReleaseDate  = in.readString();
    mImagePath    = in.readString();
    mVoteAverage  = in.readDouble();
    mPlotSynopsis = in.readString();
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

  public String getReleaseDate() {
    return mReleaseDate;
  }

  public void setReleaseDate(String releaseDate) {
    mReleaseDate = releaseDate;
  }

  public String getImagePath() {
    return mImagePath;
  }

  public void setImagePath(String imagePath) {
    this.mImagePath = imagePath;
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

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel parcel, int i) {
    parcel.writeInt(mId);
    parcel.writeString(mTitle);
    parcel.writeString(mReleaseDate);
    parcel.writeString(mImagePath);
    parcel.writeDouble(mVoteAverage);
    parcel.writeString(mPlotSynopsis);
  }
}
