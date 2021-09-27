package com.gamestockz.Fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gamestockz.Adapters.ResultAdapter;
import com.gamestockz.R;
import com.gamestockz.data.modals.GameSectionResult;
import com.gamestockz.databinding.FragmentGameSectionBinding;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class GameSectionFragment extends Fragment {

    MaterialButton red, green;
    private TextView walletshow;
    public static String yesno;

    TextView period, time;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference time1 = database.getReference("Time").child("Time");
    DatabaseReference period1 = database.getReference("period");
    DatabaseReference result;

    public static Boolean isClicked = true;

    FragmentGameSectionBinding binding;
    ArrayList<GameSectionResult> gameSectionResults;
    ResultAdapter resultAdapter;
    FirebaseFirestore firestore;
    ProgressDialog dialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGameSectionBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        dialog = new ProgressDialog(getActivity());
        dialog.setCancelable(false);
        dialog.setMessage("Fetching Data...");
        dialog.show();

        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        firestore = FirebaseFirestore.getInstance();
        gameSectionResults = new ArrayList<GameSectionResult>();
        resultAdapter = new ResultAdapter(getActivity(), gameSectionResults);
        binding.recyclerView.setAdapter(resultAdapter);
        FetchResultData();


        time = binding.time;
        walletshow = binding.walletshow;
        String mobile = getActivity().getIntent().getStringExtra("mobile");
        ArrayList<String> arrayList = new ArrayList<>();
        BottomRedFragment bottomRedFragment = new BottomRedFragment();
        BottomGreenFragment bottomGreenFragment = new BottomGreenFragment();

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


        period = binding.period;
        period1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String b = snapshot.getValue(String.class);
                period.setText(b);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        red = binding.redBtn;
        green = binding.greenBtn;


        //blue=v.findViewById(R.id.blue_button);

        //time=v.findViewById(R.id.time);
        red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bottomRedFragment.show(getChildFragmentManager(), bottomRedFragment.getTag());
                isClicked = true;


            }
        });
        green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bottomGreenFragment.show(getChildFragmentManager(), bottomGreenFragment.getTag());
            }
        });

//        ArrayAdapter<String> myArrayadap = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, arrayList);
//        result = FirebaseDatabase.getInstance().getReference("Result");
//        result.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                String value = snapshot.getValue(String.class);
//                arrayList.add(value);
//                myArrayadap.notifyDataSetChanged();
//
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                myArrayadap.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        return view;
    }

    private void FetchResultData() {

        firestore.collection("Results").orderBy("gameId", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    if (dialog.isShowing())
                        dialog.dismiss();

                    Log.e("Firestore Error", error.getMessage());
                    return;
                }

                for (DocumentChange dc : value.getDocumentChanges()) {
                    if (dc.getType() == DocumentChange.Type.ADDED) {
                        gameSectionResults.add(dc.getDocument().toObject(GameSectionResult.class));

                    }
                    resultAdapter.notifyDataSetChanged();
                    if (dialog.isShowing())
                        dialog.dismiss();
                }

            }
        });

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
}