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

import com.gamestockz.Fragments.BottomRedFragment;
import com.gamestockz.Fragments.WithdrawRequestsFragment;
import com.gamestockz.R;
import com.gamestockz.databinding.ActivityWithdrawBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
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

public class WithdrawActivity extends AppCompatActivity {

    WithdrawRequestsFragment fragment;

    ActivityWithdrawBinding binding;
    ProgressDialog progressDialog;
    private FirebaseFirestore firebaseFirestore;

    SimpleDateFormat simpleDateFormat;
    String currentDateandTime;

    SimpleDateFormat formattedSimpleDate;
    String formattedCurrentDateandTime;

    public static String wallet;
    String s;


    int fenterAmount;
    String availBal;



    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWithdrawBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String mobile = getIntent().getStringExtra("mobilewithdraw");

        DatabaseReference walRef = FirebaseDatabase.getInstance().getReference("Users").child(mobile).child("wallet");
        walRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                availBal = snapshot.getValue(String.class);
                binding.remainBalance.setText(availBal);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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


        binding.requests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                WithdrawRequestsFragment withdrawRequestsFragment = new WithdrawRequestsFragment();
                withdrawRequestsFragment.show(getSupportFragmentManager(), withdrawRequestsFragment.getTag());

            }
        });


    }

    private void withdrawOnClick() {

        binding.realName.setError(null);
        binding.accountNum.setError(null);
        binding.cfmAccountNum.setError(null);
        binding.ifsc.setError(null);
        binding.amountWithdraw.setError(null);


        fenterAmount = Integer.parseInt(binding.amountWithdrawEd.getText().toString());
        int favailBal = Integer.parseInt(this.availBal);

        try {
            if (binding.realNameEd.getText().toString().isEmpty()) {
                binding.realName.setError("Required*");
                binding.realName.requestFocus();
                return;

            }
            if (!binding.realNameEd.getText().toString().matches("^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$")) {
                binding.realName.setError("Allow only Characters");
                binding.realName.requestFocus();
                return;
            }
            if (binding.accountNumEd.getText().toString().isEmpty()) {
                binding.accountNum.setError("Required*");
                binding.accountNum.requestFocus();
                return;

            }
            if (binding.cfmAccountNumEd.getText().toString().isEmpty()) {
                binding.cfmAccountNum.setError("Required*");
                binding.cfmAccountNum.requestFocus();
                return;
            }
            if (binding.ifscEd.getText().toString().isEmpty()) {
                binding.ifsc.setError("Required*");
                binding.ifsc.requestFocus();
                return;
            }
            if (!binding.accountNumEd.getText().toString().equals(binding.cfmAccountNumEd.getText().toString())) {
                binding.accountNum.setError("Account number mismatched");
                binding.cfmAccountNum.setError("Account number mismatched");
                binding.cfmAccountNum.requestFocus();
                return;

            }
            if (binding.amountWithdrawEd.getText().toString().isEmpty()) {
                binding.amountWithdraw.setError("Required*");
                binding.amountWithdraw.requestFocus();
                return;
            }
            if (fenterAmount > favailBal) {
                binding.amountWithdraw.setError("Insufficient Funds");
                binding.amountWithdraw.requestFocus();
                return;
            }
            if (fenterAmount < 31) {
                binding.amountWithdraw.setError("1. Minimum Withdraw is Rs.31 \n2. Withdraw Charges:\ni)less than 1500\nCharges-Rs.30\nii)Greater than 1500\nCharges-Rs.2%\n3. Withdraw Timing:\ni)Monday to Saturday\nii)10 AM to 5 PM\n");
            } else {
                progressDialog.show();
                progressDialog.setCanceledOnTouchOutside(false);

                int fremainBal = favailBal - fenterAmount;
                s = String.valueOf(fremainBal);
                binding.remainBalance.setText(s);

                withdrawVerify();
            }
        } catch (Exception e) {
            Toast.makeText(WithdrawActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

        }
    }

    private void withdrawVerify() {


        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.getDefault());
        currentDateandTime = simpleDateFormat.format(new Date());


        formattedSimpleDate = new SimpleDateFormat("EEE, dd MMM yyyy 'at' hh:mm:ss aaa", Locale.getDefault());
        formattedCurrentDateandTime = formattedSimpleDate.format(new Date());

        String mobile = getIntent().getStringExtra("mobilewithdraw");

        String RealName = binding.realNameEd.getText().toString();
        String AccountNumber = binding.accountNumEd.getText().toString();
        String ConfirmAccountNumber = binding.cfmAccountNumEd.getText().toString();
        String Ifsc = binding.ifscEd.getText().toString();
        String Amount = binding.amountWithdrawEd.getText().toString();
        String Status = "PENDING";


        DocumentReference documentReference = firebaseFirestore.collection("Users").document(mobile)
                .collection("Withdraw Requests").document(currentDateandTime);

        Map<String, Object> withdrawRequests = new HashMap<>();
        withdrawRequests.put("Real Name", RealName);
        withdrawRequests.put("Account Number", AccountNumber);
        withdrawRequests.put("Confirm Account Number", ConfirmAccountNumber);
        withdrawRequests.put("Ifsc Code", Ifsc);
        withdrawRequests.put("Amount", Amount);
        withdrawRequests.put("Status", Status);
        withdrawRequests.put("Date", formattedCurrentDateandTime);
        withdrawRequests.put("Numeric Date", currentDateandTime);

        documentReference.set(withdrawRequests).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                DocumentReference documentReference1 = firebaseFirestore.collection("Withdraw Requests").document(mobile);
                Map<String, Object> remainRequests = new HashMap<>();
                remainRequests.put("Mobile", mobile);
                documentReference1.set(remainRequests);

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(mobile).child("wallet");
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        reference.setValue(s);

                        Toast.makeText(WithdrawActivity.this, "Withdraw Request Proceed", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


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