package com.gamestockz.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gamestockz.Activities.WithdrawActivity;
import com.gamestockz.R;
import com.gamestockz.databinding.FragmentBottomGreenBinding;
import com.gamestockz.databinding.FragmentBottomRedBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.SplittableRandom;

public class BottomGreenFragment extends BottomSheetDialogFragment {

    FragmentBottomGreenBinding binding;
    ProgressDialog progressDialog;

    FirebaseDatabase database;
    DatabaseReference myref;
    DatabaseReference price;

    int quantity = 0;
    int quantityGreen;
    String coin;
    int icoin;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBottomGreenBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        String qty = String.valueOf(quantity);
        binding.quantityGreen.setText(qty);
        quantityGreen = Integer.parseInt(binding.quantityGreen.getText().toString());

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
                }
            }
        });
        binding.subtractGreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decrement();
            }
        });
        binding.addGreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increment();
            }
        });

        binding.joinGreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantityGreen == 0 || icoin == 0){
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

//        waiting for Mobile Number


    }

    private void increment() {
        quantity++;
        String s = String.valueOf(quantity);
        binding.quantityGreen.setText(s);

        int calc = icoin * quantity;

        coin = String.valueOf(calc);
        binding.totalPriceMoney.setText(coin);

    }

    private void decrement() {
        quantity = quantity > 0 ? --quantity : 0;
        String s = String.valueOf(quantity);
        binding.quantityGreen.setText(s);

        int calc = icoin * quantity;
        coin = String.valueOf(calc);
        binding.totalPriceMoney.setText(coin);

    }

}
