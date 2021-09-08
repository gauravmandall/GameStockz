package com.gamestockz.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.gamestockz.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GameSectionFragment extends Fragment {

    MaterialButton red,green;
    private  TextView walletshow;
    ListView listview;
    public static String yesno;

    TextView period,time;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference time1=database.getReference("Time").child("Time");
    DatabaseReference period1=database.getReference("period");
    DatabaseReference result;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_game_section, container, false);

        time=v.findViewById(R.id.time);
        listview=v.findViewById(R.id.listview);
        walletshow=v.findViewById(R.id.walletshow);
        String mobile=getActivity().getIntent().getStringExtra("mobile");
        ArrayList<String> arrayList=new ArrayList<>();
        BottomRedFragment bottomRedFragment = new BottomRedFragment();
        BottomGreenFragment bottomGreenFragment=new BottomGreenFragment();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users").child(mobile).child("wallet");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                walletshow.setText(value);


            }




            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        time1.addValueEventListener(new ValueEventListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String a = snapshot.getValue(String.class);
                time.setText(a);
                int itime = Integer.parseInt(a);

                verifyClicked(mobile, itime, bottomRedFragment, bottomGreenFragment);


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

                bottomRedFragment.show(getChildFragmentManager(), bottomRedFragment.getTag());
            }
        });
        green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bottomGreenFragment.show(getChildFragmentManager(),bottomGreenFragment.getTag());
            }
        });
       
        ArrayAdapter<String> myArrayadap=new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1,arrayList);
        result=FirebaseDatabase.getInstance().getReference("Result");
        listview.setAdapter(myArrayadap);
        result.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String value=snapshot.getValue(String.class);
                arrayList.add(value);
                myArrayadap.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            myArrayadap.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        verifyJoined();

        return v;
    }

    private void verifyClicked(String mobile, int itime, BottomRedFragment bottomRedFragment, BottomGreenFragment bottomGreenFragment) {


        if (itime > 10) {

            red.setBackgroundColor(ContextCompat.getColor(getContext().getApplicationContext(), R.color.red_a700));
            green.setBackgroundColor(ContextCompat.getColor(getContext().getApplicationContext(), R.color.green_A700));

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(mobile).child("predict");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String predictDb = snapshot.getValue(String.class);
                    int ipredictDb = Integer.parseInt(String.valueOf(predictDb));

                    if (ipredictDb == 0) {
                        red.setEnabled(true);
                        green.setEnabled(true);


                    } else {
                        red.setEnabled(false);
                        green.setEnabled(false);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        } else {

            red.setEnabled(false);
            green.setEnabled(false);

            red.setBackgroundColor(Color.GRAY);
            green.setBackgroundColor(Color.GRAY);

        }



    }

    private void verifyJoined() {


    }
}