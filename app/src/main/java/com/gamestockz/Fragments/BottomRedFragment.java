package com.gamestockz.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.gamestockz.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BottomRedFragment extends BottomSheetDialogFragment {

    Button tenred,fiftyred,hundredred;
    FirebaseDatabase database;
    DatabaseReference myref;
    DatabaseReference price;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_bottom_red, container, false);
        String mobile=getActivity().getIntent().getStringExtra("mobile");

        return v;
    }
}