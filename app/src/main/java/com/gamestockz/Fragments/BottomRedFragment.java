package com.gamestockz.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.gamestockz.Activities.WithdrawActivity;
import com.gamestockz.R;
import com.gamestockz.databinding.FragmentBottomRedBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.SplittableRandom;

public class BottomRedFragment extends BottomSheetDialogFragment {

    public static Boolean isjoined = true;
    FragmentBottomRedBinding binding;
    ProgressDialog progressDialog;
    private FirebaseFirestore firebaseFirestore;
    private SimpleDateFormat simpleDateFormat;
    private String currentDateandTime;
    int quantity = 1;
    int quantityRed;
    int icoin;
    String coin;
    String mobile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBottomRedBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        initElements();
        toggleButtonConditions();

        binding.subtractRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decrement();
            }
        });
        binding.addRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increment();
            }
        });
        binding.joinRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantityRed == 0 || icoin == 0) {
                    Toast.makeText(getActivity(), "Please choose Correct Values", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.show();
                    progressDialog.setCanceledOnTouchOutside(false);
                    isjoined = true;
                    storeDataToRealtimeDatabase();
                }
            }
        });
        return view;
    }

    private void initElements() {
        String qty = String.valueOf(quantity);
        binding.quantityRed.setText(qty);
        icoin = Integer.parseInt(binding.totalPriceMoney.getText().toString());
        quantityRed = Integer.parseInt(binding.quantityRed.getText().toString());
        mobile = getActivity().getIntent().getStringExtra("mobile");
        firebaseFirestore = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Joining Red");
        progressDialog.setMessage("Please Wait");
    }

    private void toggleButtonConditions() {
        binding.toggleButton.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                if (group.getCheckedButtonId() == R.id.btn1) {
                    icoin = 10;
                    int ten = icoin * quantity;
                    coin = String.valueOf(ten);
                    binding.totalPriceMoney.setText(coin);
                } else if (group.getCheckedButtonId() == R.id.btn2) {
                    icoin = 50;
                    int fifty = icoin * quantity;
                    coin = String.valueOf(fifty);
                    binding.totalPriceMoney.setText(coin);
                } else if (group.getCheckedButtonId() == R.id.btn3) {
                    icoin = 100;
                    int hundred = icoin * quantity;
                    coin = String.valueOf(hundred);
                    binding.totalPriceMoney.setText(coin);
                } else {
                    Toast.makeText(getActivity(), "Please Choose a valid amount", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void storeDataToRealtimeDatabase() {
        Toast.makeText(getContext(), mobile, Toast.LENGTH_SHORT).show();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(mobile).child("wallet");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value1 = snapshot.getValue(String.class);
                int ivalue = Integer.parseInt(value1);
                int tcmoney = Integer.parseInt(binding.totalPriceMoney.getText().toString());
                if (tcmoney > ivalue) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Insufficient Funds", Toast.LENGTH_LONG).show();
                } else {
                    DatabaseReference predictReference = FirebaseDatabase.getInstance().getReference("Users").child(mobile).child("predict");
                    predictReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String predictDb = snapshot.getValue(String.class);
                            int ipredictDb = Integer.parseInt(predictDb);
                            if (ipredictDb == 0) {
                                if (isjoined) {
                                    int ijoin = ivalue - tcmoney;
                                    String join = String.valueOf(ijoin);
                                    databaseReference.setValue(join);
                                    Task<Void> reference = FirebaseDatabase.getInstance().getReference("Users").child(mobile).child("predict").setValue("1");
                                    Task<Void> databaseReference2 = FirebaseDatabase.getInstance().getReference("Users").child(mobile).child("price").setValue(String.valueOf(tcmoney));
                                    storeDataToCloudFirestore(tcmoney, join, ivalue);
                                    Toast.makeText(getActivity(), "Joined Successfully", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                    isjoined = false;
                                }
                            } else {
                                binding.joinRed.setEnabled(false);
                                binding.joinRed.setText("Joined");
                                binding.joinRed.setBackgroundColor(Color.GRAY);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void storeDataToCloudFirestore(int tcmoney, String join, int ivalue) {
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.getDefault());
        currentDateandTime = simpleDateFormat.format(new Date());
        DocumentReference documentReference = firebaseFirestore.collection("Users").document(mobile)
                .collection("Play History").document(currentDateandTime);
        Map<String, Object> playHistory = new HashMap<>();
        playHistory.put("predict", "1");
        playHistory.put("color", "Red");
        playHistory.put("price", String.valueOf(tcmoney));
        playHistory.put("Old Wallet", ivalue);
        playHistory.put("New wallet", join);
        playHistory.put("Date", currentDateandTime);
        documentReference.set(playHistory);
    }

    private void increment() {
        quantity++;
        String s = String.valueOf(quantity);
        binding.quantityRed.setText(s);
        int calc = icoin * quantity;
        coin = String.valueOf(calc);
        binding.totalPriceMoney.setText(coin);
    }

    private void decrement() {
        quantity = quantity > 1 ? --quantity : 1;
        String s = String.valueOf(quantity);
        binding.quantityRed.setText(s);
        int calc = icoin * quantity;
        coin = String.valueOf(calc);
        binding.totalPriceMoney.setText(coin);
    }
}