package com.gamestockz.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.gamestockz.databinding.ActivityWithdrawBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class WithdrawActivity extends AppCompatActivity {

    ActivityWithdrawBinding binding;
    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWithdrawBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.withdrawFundsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = FirebaseFirestore.getInstance();
                String RealName = binding.realNameEd.getText().toString();
                String AccountNumber = binding.accountNumEd.getText().toString();
                String ConfirmAccountNumber = binding.cfmAccountNumEd.getText().toString();
                String Ifsc = binding.ifscEd.getText().toString();
                String Amount = binding.AmountWithdrawEd.getText().toString();
                String Status = "Pending";

                Map<String, Object> WithdrawRequests = new HashMap<>();
                WithdrawRequests.put("Real Name", RealName);
                WithdrawRequests.put("Account Number", AccountNumber);
                WithdrawRequests.put("Confirm Account Number", ConfirmAccountNumber);
                WithdrawRequests.put("Ifsc Code", Ifsc);
                WithdrawRequests.put("Amount", Amount);
                WithdrawRequests.put("Status", Status);

                db.collection("Withdraw Requests")
                        .add(WithdrawRequests)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(WithdrawActivity.this, "Withdraw Request Proceed", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(WithdrawActivity.this, "Withdraw Request Failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }

}

//.document("mobile")