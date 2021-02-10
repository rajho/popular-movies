package com.example.android.popmovies.utilities;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppExecutors {
  private static final Object LOCK = new Object();
  private static AppExecutors sInstance;
  private final Executor diskIO;

  public AppExecutors(Executor diskIO) {
    this.diskIO = diskIO;
  }

  public static AppExecutors getInstance() {
    if (sInstance == null){
      synchronized (LOCK) {
        sInstance = new AppExecutors(Executors.newSingleThreadExecutor());
      }
    }
    return sInstance;
  }

  public Executor diskIO() {
    return diskIO;
  }
}

