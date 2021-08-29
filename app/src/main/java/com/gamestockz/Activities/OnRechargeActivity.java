package com.gamestockz.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
    private MaterialButton pay;
    public static String wallet;
    public static String mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_recharge);

       // initElements();

        Intent intent=getIntent();
        mobile=intent.getStringExtra("mobile2");
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users").child(mobile).child("wallet");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                wallet=  snapshot.getValue(String.class);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Checkout.preload(getApplicationContext());

        pay = findViewById(R.id.pay);


        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)

            {
               // int amount=Integer.parseInt(enter_amount.getText().toString());

              //  if (amount<100) {
                //    Toast.makeText(onrecharge.this, "Minimum Recharge is 100", Toast.LENGTH_SHORT).show();

               // }
               // if (enter_amount.getText().toString().isEmpty()){
                    //enter_amount.setError("Not Valid");
                //}


                    payment();

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
            int intamount=100;

            intamount=intamount*100;
            String stramount=Integer.toString(intamount);
            JSONObject options = new JSONObject();

            options.put("name", "GameStockz");
            options.put("description", "Reference No. #123456");
            // options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            // options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
            options.put("theme.color", "#3399cc");
            options.put("currency", "INR");
            options.put("amount", stramount);//pass amount in currency subunits
            // options.put("prefill.email", "gaurav.kumar@example.com");
            options.put("prefill.contact",mobile);
            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);

            checkout.open(activity, options);

        } catch(Exception e) {
            // Log.e(TAG, "Error in starting Razorpay Checkout", e);
        }
    }


    @Override
    public void onPaymentSuccess(String s) {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users").child(mobile).child("wallet");
        int newwallet= Integer.parseInt(wallet);
       // int a=Integer.parseInt(enter_amount.getText().toString());
        newwallet += 100;
        String wallet=Integer.toString(newwallet);
        reference.setValue(wallet);
        Toast.makeText(this, ""+s, Toast.LENGTH_SHORT).show();
        DatabaseReference reference2= FirebaseDatabase.getInstance().getReference("Users").child(mobile).child("Transactions").child(""+s);
        reference2.setValue("100");










        Toast.makeText(this, "Payment Successfull ", Toast.LENGTH_SHORT).show();






    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this,"Payment Cancelled", Toast.LENGTH_SHORT).show();
    }
}

