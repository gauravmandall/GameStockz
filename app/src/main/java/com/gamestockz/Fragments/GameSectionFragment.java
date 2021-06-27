package com.gamestockz.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.gamestockz.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GameSectionFragment extends Fragment {

    MaterialButton red,green;

    TextView period,time;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference time1=database.getReference("Time").child("Time");
    DatabaseReference period1=database.getReference("period");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_game_section, container, false);

        time=v.findViewById(R.id.time);
        String mobile=getActivity().getIntent().getStringExtra("mobile");
        time1.addValueEventListener(new ValueEventListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String a = snapshot.getValue(String.class);
                time.setText(a);
//                int b = Integer.parseInt(a);
                // if(b>30){red.setEnabled(true);
                //  red.setBackgroundColor(R.color.red);
                // red.setTextColor(R.color.white);}


                // if (b<30){
                // red.setEnabled(false);
                // red.setBackgroundColor(R.color.teal_700);


            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        period=v.findViewById(R.id.period);
        period1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String b=snapshot.getValue(String.class);
                period.setText(b);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        red = v.findViewById(R.id.redBtn);
        green=v.findViewById(R.id.greenBtn);
        //blue=v.findViewById(R.id.blue_button);

        //time=v.findViewById(R.id.time);
        red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(getContext(), mobile, Toast.LENGTH_SHORT).show();
                BottomRedFragment bottomRedFragment=new BottomRedFragment();
                bottomRedFragment.show(getChildFragmentManager(),bottomRedFragment.getTag());

            }
        });
        green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomGreenFragment bottomGreenFragment=new BottomGreenFragment();
                bottomGreenFragment.show(getChildFragmentManager(),bottomGreenFragment.getTag());
            }
        });

        return v;
    }
}