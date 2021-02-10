package com.example.android.popmovies.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MovieEntryDao {
  @Query("SELECT * FROM movie")
  LiveData<List<MovieEntry>> loadAllMovies();

  @Insert
  void insertMovie(MovieEntry movie);

  @Update(onConflict = OnConflictStrategy.REPLACE)
  void updateMovie(MovieEntry movie);

  @Delete
  void deleteTask(MovieEntry movie);

  @Query("SELECT * FROM movie where _id = :id")
  LiveData<MovieEntry> loadMovieById(int id);
}
