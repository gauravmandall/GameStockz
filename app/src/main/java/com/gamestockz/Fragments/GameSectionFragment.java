package com.gamestockz.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
//


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
                //bottomRedFragment.dismiss();

            }
        });
        green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomGreenFragment bottomGreenFragment=new BottomGreenFragment();
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
        return v;
    }
}