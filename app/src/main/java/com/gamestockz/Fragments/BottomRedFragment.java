package com.gamestockz.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.SplittableRandom;

public class BottomRedFragment extends BottomSheetDialogFragment {

    FragmentBottomRedBinding binding;
    ProgressDialog progressDialog;

//    FirebaseDatabase firebaseDatabase;
//    DatabaseReference databaseReference;
//    DatabaseReference price;

    int quantity = 1;
    int quantityRed;
    String coin;
    int icoin;
    String mobile;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBottomRedBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        String qty = String.valueOf(quantity);
        binding.quantityRed.setText(qty);

        icoin = Integer.parseInt(binding.totalPriceMoney.getText().toString());

//        firebaseDatabase = FirebaseDatabase.getInstance();



        quantityRed = Integer.parseInt(binding.quantityRed.getText().toString());
        mobile = getActivity().getIntent().getStringExtra("mobile");

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Joining Red");
        progressDialog.setMessage("Please Wait");

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
                } else{
                    Toast.makeText(getActivity(), "Please Choose a valid amount", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
                if (quantityRed == 0 || icoin == 0){
                    Toast.makeText(getActivity(), "Please choose Correct Values", Toast.LENGTH_SHORT).show();

                }else{
                    progressDialog.show();
                    progressDialog.setCanceledOnTouchOutside(false);
                    joinVerify();
                }
            }
        });

        return view;
    }

    private void joinVerify() {
        Toast.makeText(getContext(), mobile, Toast.LENGTH_SHORT).show();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(mobile).child("wallet");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                int ivalue = Integer.parseInt(value);
                int tcmoney = Integer.parseInt(binding.totalPriceMoney.getText().toString());

                if (tcmoney > ivalue){
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Insufficient Funds", Toast.LENGTH_LONG).show();
                } else {
                    int ijoin = ivalue - tcmoney;
                    String join = String.valueOf(ijoin);

                    databaseReference.setValue(join);
                    Toast.makeText(getActivity(), "Joined Successfully", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


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
