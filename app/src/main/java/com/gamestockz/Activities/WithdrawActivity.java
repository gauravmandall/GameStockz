package com.gamestockz.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.gamestockz.R;
import com.gamestockz.databinding.ActivityWithdrawBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class WithdrawActivity extends AppCompatActivity {

    ActivityWithdrawBinding binding;
    ProgressDialog progressDialog;
    private FirebaseFirestore firebaseFirestore;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWithdrawBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String availBal = getIntent().getStringExtra("walletWithdraw");
        binding.remainBalance.setText(availBal);
        firebaseFirestore = FirebaseFirestore.getInstance();


        progressDialog = new ProgressDialog(WithdrawActivity.this);
        progressDialog.setTitle("Withdrawal process");
        progressDialog.setMessage("We are sending Request");

        binding.withdrawFundsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                withdrawOnClick();
            }
        });

    }

    private void withdrawOnClick() {

        try {
            if (binding.realNameEd.getText().toString().isEmpty()) {
                binding.realName.setError("Required*");
                return;

            }
            if (!binding.realNameEd.getText().toString().matches("[a-zA-Z]+")) {
                binding.realName.setError("Allow only Characters");
                return;
            }
            if (binding.accountNumEd.getText().toString().isEmpty()) {
                binding.accountNum.setError("Required*");
                return;

            }
            if (binding.cfmAccountNumEd.getText().toString().isEmpty()) {
                binding.cfmAccountNum.setError("Required*");
                return;
            }
            if (binding.ifscEd.getText().toString().isEmpty()) {
                binding.ifsc.setError("Required*");
                return;
            }
            if (!binding.accountNumEd.getText().toString().equals(binding.cfmAccountNumEd.getText().toString())) {
                binding.accountNum.setError("Account number mismatched");
                binding.cfmAccountNum.setError("Account number mismatched");
                return;

            }
            if (binding.amountWithdrawEd.getText().toString().isEmpty()) {
                binding.amountWithdraw.setError("Required*");
                return;
            }
//            if (binding.amountWithdrawEd.getText().toString().length() > ) {

//            }
            else {

                progressDialog.show();
                progressDialog.setCanceledOnTouchOutside(false);


                withdrawVerify();
            }
        } catch (Exception e) {
            Toast.makeText(WithdrawActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

        }
    }

    private void withdrawVerify() {
        String mobile = getIntent().getStringExtra("mobilewithdraw");

        String RealName = binding.realNameEd.getText().toString();
        String AccountNumber = binding.accountNumEd.getText().toString();
        String ConfirmAccountNumber = binding.cfmAccountNumEd.getText().toString();
        String Ifsc = binding.ifscEd.getText().toString();
        String Amount = binding.amountWithdrawEd.getText().toString();
        String Status = "Pending";


        DocumentReference documentReference = firebaseFirestore.collection("Withdraw Requests").document(mobile);

        Map<String, Object> withdrawRequests = new HashMap<>();
        withdrawRequests.put("Real Name", RealName);
        withdrawRequests.put("Account Number", AccountNumber);
        withdrawRequests.put("Confirm Account Number", ConfirmAccountNumber);
        withdrawRequests.put("Ifsc Code", Ifsc);
        withdrawRequests.put("Amount", Amount);
        withdrawRequests.put("Status", Status);

        documentReference.set(withdrawRequests).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                Toast.makeText(WithdrawActivity.this, "Withdraw Request Proceed", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(WithdrawActivity.this, "Withdraw Request Failed", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

    }

}