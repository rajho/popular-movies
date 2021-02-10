package com.example.android.popmovies.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.example.android.popmovies.R;
import com.example.android.popmovies.ui.models.Movie;
import com.example.android.popmovies.databinding.MoviesListItemBinding;
import com.example.android.popmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesAdapterViewholder> {

  private final MoviesAdapterOnClickHandler mClickHandler;
  private       List<Movie>                 mMovies;

  public MoviesAdapter(MoviesAdapterOnClickHandler clickHandler) {
    mClickHandler = clickHandler;
  }

  public void setData(List<Movie> movies) {
    mMovies = movies;
    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public MoviesAdapterViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
                              .inflate(R.layout.movies_list_item, parent, false);

    return new MoviesAdapterViewholder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull MoviesAdapterViewholder holder, int position) {
    String movieImage = mMovies.get(position).getImagePath();
    URL    imageURL   = NetworkUtils.buildImageURL(movieImage);
    Picasso.get().load(imageURL.toString()).into(holder.mBinding.ivMoviePoster);
  }

  @Override
  public int getItemCount() {
    if (mMovies == null) {
      return 0;
    }
    return mMovies.size();
  }

  public interface MoviesAdapterOnClickHandler {
    void onClick(Movie movie);
  }

  public class MoviesAdapterViewholder extends ViewHolder implements View.OnClickListener {
    public final MoviesListItemBinding mBinding;

    public MoviesAdapterViewholder(View view) {
      super(view);
      mBinding = MoviesListItemBinding.bind(view);
      view.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
      int   adapterPosition = getAdapterPosition();
      Movie movie           = mMovies.get(adapterPosition);
      mClickHandler.onClick(movie);
    }
  }
}
