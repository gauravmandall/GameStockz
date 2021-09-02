package com.gamestockz.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.gamestockz.R;
import com.gamestockz.databinding.FragmentBottomRedBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BottomRedFragment extends BottomSheetDialogFragment {

    FragmentBottomRedBinding binding;

    //    Button tenred,fiftyred,hundredred;
    FirebaseDatabase database;
    DatabaseReference myref;
    DatabaseReference price;

    int quantity = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentBottomRedBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.subtractRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                decrement();

            }
        });


        return view;
    }

    private void decrement() {

        quantity = quantity - 1;
        binding.subtractRed.setText(" " + quantity);
    }

}
