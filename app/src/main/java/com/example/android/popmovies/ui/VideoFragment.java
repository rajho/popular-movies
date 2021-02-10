package com.example.android.popmovies.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.android.popmovies.databinding.FragmentInstructionVideoBinding;

public class VideoFragment extends Fragment {
  private FragmentInstructionVideoBinding mBinding;

  public VideoFragment() {
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    mBinding = FragmentInstructionVideoBinding.inflate(inflater, container, false);

    return mBinding.getRoot();
  }
}
