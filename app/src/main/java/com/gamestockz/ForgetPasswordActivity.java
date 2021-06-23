package com.gamestockz;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class ForgetPasswordActivity extends AppCompatActivity {


    private TextInputLayout mobileLt, enterOtpLt, newPassLt, confirmNewPassLt;
    private TextInputEditText mobile, enterOtp, newPass, confirmNewPass;
    private Button getOtp, submit;

    String CodeSent;
    FirebaseAuth mauth;
    FirebaseDatabase database;
    DatabaseReference myref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        initElements();
    }

    private void initElements() {

//        For Database and Authentication
        mauth= FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();

//        For all TextInput Layout
        mobileLt = findViewById(R.id.forgetMobile);
        enterOtpLt = findViewById(R.id.forgetEnterOTP);
        newPassLt = findViewById(R.id.forgetNewPass);
        confirmNewPassLt = findViewById(R.id.forgetConfirmPass);

//        For all Textinput EditText
        mobile = findViewById(R.id.forgetMobileEt);
        enterOtp = findViewById(R.id.forgetEnterOTPEt);
        newPass = findViewById(R.id.forgetNewPassEt);
        confirmNewPass = findViewById(R.id.forgetConfirmPassEt);

//        For all buttons
        getOtp = findViewById(R.id.getOtp);
        submit = findViewById(R.id.submit);

//        All ClickListeners
        getOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendVerificationCode();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifySigninCode();
            }
        });


    }

//    All Methods Starts From Here

    //return
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Do you want to Exit?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user pressed "yes", then he is allowed to exit from application
                finish();
            }
        });
        builder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user select "No", just cancel this dialog and continue with app
                dialog.cancel();
            }
        });
        AlertDialog alert=builder.create();
        alert.show();
    }

    private void sendVerificationCode() {
        String  usersnumber=mobile.getText().toString();

        String phoneNumber="91"+mobile.getText().toString();

        if(usersnumber.isEmpty()){
            mobileLt.setError("Required");
            mobileLt.requestFocus();
            // progressDialog.dismiss();
            return;
        }
        if(usersnumber.length()<10){
            mobileLt.setError("Not Valid");
            mobileLt.requestFocus();
            //progressDialog.dismiss();

            return;

        }
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mauth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);




        /*PhoneAuthProvider.getInstance().verifyPhoneNumber(

                phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks);*/


    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks= new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {



        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            CodeSent=s;
            Toast.makeText(ForgetPasswordActivity.this, "Sent", Toast.LENGTH_SHORT).show();
            //progressDialog.dismiss();
        }


    };


    private void verifySigninCode() {
        String code = enterOtp.getText().toString();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(CodeSent, code);
        signInWithPhoneAuthCredential(credential);


    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mauth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            DatabaseReference reference1=FirebaseDatabase.getInstance().getReference("Users").
                                    child(mobile.getText().toString()).child("password");
                            reference1.setValue(newPass.getText().toString());
                            Toast.makeText(ForgetPasswordActivity.this, "Password Changed", Toast.LENGTH_SHORT).show();
                            //Query checkUsers=reference1.orderByChild("mobilenumber").equalTo(phone1.getText().toString());

                           /* myref=FirebaseDatabase.getInstance().getReference("Users");
                            Query query=FirebaseDatabase.getInstance().getReference("Users")
                                    .orderByChild("mobilenumber").equalTo(phoneforget.getText().toString());

                            Toast.makeText(forgetpassword.this, ""+query, Toast.LENGTH_SHORT).show();*/


                        }
                        else {
                            Toast.makeText(ForgetPasswordActivity.this, "Incorrect otp", Toast.LENGTH_SHORT).show();
                        }


                    }


                });
    }






}