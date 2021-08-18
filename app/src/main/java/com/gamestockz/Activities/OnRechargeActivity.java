package com.gamestockz.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.gamestockz.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OnRechargeActivity extends AppCompatActivity implements PaymentResultListener {

    private TextInputEditText enter_amount;
   private TextInputLayout enter_amountLt;
   private MaterialButton pay, hundred, twoHun, threeHun, fiveHun, sevenHun, thousand, fiveTh, tenTh, fiftyTh;
    public static String wallet;
    public static String mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_recharge);
        initElements();

        Intent intent = getIntent();
        mobile = intent.getStringExtra("mobile");
        String str=String.valueOf(mobile);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(str).child("wallet");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                wallet = snapshot.getValue(String.class);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Checkout.preload(getApplicationContext());



    }

    private void initElements() {

        pay = findViewById(R.id.payBtn);
        enter_amount = findViewById(R.id.enterAmountEd);
        enter_amountLt = findViewById(R.id.enterAmount);
        hundred = findViewById(R.id.hundred);
        twoHun = findViewById(R.id.twoHundred);
        threeHun = findViewById(R.id.threeHundred);
        fiveHun = findViewById(R.id.fiveHundred);
        sevenHun = findViewById(R.id.sevenHundred);
        thousand = findViewById(R.id.thousand);
        fiveTh = findViewById(R.id.fiveThousand);
        tenTh = findViewById(R.id.tenThousand);
        fiftyTh = findViewById(R.id.fiftyThousand);


        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int amount = Integer.parseInt(enter_amount.getText().toString());

                if (amount < 100) {
                    enter_amountLt.setError("Minimum Recharge is ₹100");
                    enter_amountLt.requestFocus();

                }
                if (enter_amount.getText().toString().isEmpty()) {
                    enter_amountLt.setError("Required");
                    enter_amountLt.requestFocus();
                } else {

                    payment();
                }
            }
        });
        hundred.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enter_amount.setText("100");
            }
        });
        twoHun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enter_amount.setText("200");
            }
        });
        threeHun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enter_amount.setText("300");
            }
        });
        fiveHun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enter_amount.setText("500");
            }
        });
        sevenHun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enter_amount.setText("700");
            }
        });
        thousand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enter_amount.setText("1000");
            }
        });
        fiveTh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enter_amount.setText("5000");
            }
        });
        tenTh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enter_amount.setText("10000");
            }
        });
        fiftyTh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enter_amount.setText("50000");
            }
        });

    }

    private void payment() {
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_BhAZsSWZ8T9Ccp");
        /**
         * Instantiate Checkout
         */


        /**
         * Set your logo here
         */
        checkout.setImage(R.drawable.ic_game_stockz);

        /**
         * Reference to current activity
         */
        final Activity activity = this;

        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            int intamount = Integer.parseInt(enter_amount.getText().toString());
            intamount = intamount * 100;
            String stramount = Integer.toString(intamount);
            JSONObject options = new JSONObject();

            options.put("name", "GameStockz");
            options.put("description", "Reference No. #123456");
            // options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            // options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
            options.put("theme.color", "#0D47A1");
            options.put("currency", "INR");
            options.put("amount", stramount);//pass amount in currency subunits
            // options.put("prefill.email", "gaurav.mandal@example.com");
            options.put("prefill.contact", mobile);
            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);

            checkout.open(activity, options);

        } catch (Exception e) {
            Toast.makeText(OnRechargeActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            // Log.e(TAG, "Error in starting Razorpay Checkout", e);
        }
    }


    @Override
    public void onPaymentSuccess(String s) {
       /* DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(mobile).child("wallet");
        int newwallet = Integer.parseInt(wallet);
        int a = Integer.parseInt(enter_amount.getText().toString());
        newwallet += a;
        String wallet = Integer.toString(newwallet);
        reference.setValue(wallet);**/
        Task<String> taskData = callcloudfunction();


        Toast.makeText(this, "Payment Successfull ", Toast.LENGTH_SHORT).show();


    }
    private Task<String> callcloudfunction() {
        FirebaseFunctions mFunctions = FirebaseFunctions.getInstance();
        Map<String, Object> data = new HashMap<>();
        data.put("mobile",mobile);
        data.put("rechargeamt",enter_amount.getText().toString());

        data.put("push",true);
        return mFunctions
                .getHttpsCallable("function1")
                .call(data)
                .continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) {
                        // This continuation runs on either success or failure, but if the task
                        // has failed then getResult() will throw an Exception which will be
                        // propagated down.
                        return (String) task.getResult().getData().toString();
                    }
                });

    }







    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "Payment Cancelled", Toast.LENGTH_SHORT).show();
    }

}