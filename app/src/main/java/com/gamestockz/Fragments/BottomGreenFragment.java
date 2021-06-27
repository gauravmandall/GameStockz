package com.gamestockz.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gamestockz.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomGreenFragment extends BottomSheetDialogFragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bottom_green, container, false);
    }
}