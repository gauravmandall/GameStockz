package com.gamestockz.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.gamestockz.R;
import com.gamestockz.data.modals.Users;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class SignupActivity extends AppCompatActivity {

    private TextInputLayout nameLt, mobileLt, enterOtpLt, emailLt, passLt, confirmPassLt;
    private TextInputEditText name, mobile, enterOtp, email, pass, confirmPass, refferal;
    private MaterialButton getOtp;
    private TextView countDownTimer;
    private TextView resendOtp;
    private Button signup;

    //    Integer wallet = 0;
    private FirebaseFunctions mfunctions;
    private FirebaseAuth mAuth;
    String CodeSent;
    FirebaseDatabase database;
    ProgressDialog progressDialog;
    ProgressDialog pd;

    //ProgressDialog Registerdialog;
    //ProgressBar progressBar;
    DatabaseReference db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        changeStatusBarColor();
        initElements();

    }

    private void initElements() {

//        For Database and Loading bar
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        progressDialog = new ProgressDialog(SignupActivity.this);
        pd = new ProgressDialog(SignupActivity.this);

        progressDialog.setTitle("Sending OTP...");
        progressDialog.setMessage("we are sending OTP");

        pd.setTitle("Registration");
        pd.setMessage("please wait");


//        For all Layouts That's Provides Errors
        nameLt = findViewById(R.id.fullname);
        mobileLt = findViewById(R.id.forgetMobile);
        enterOtpLt = findViewById(R.id.enter_otp);
        emailLt = findViewById(R.id.email);
        passLt = findViewById(R.id.forgetNewPass);
        confirmPassLt = findViewById(R.id.confpass);

//        For all Edittexts
        name = findViewById(R.id.fullnameEd);
        mobile = findViewById(R.id.loginMobileEd);
        enterOtp = findViewById(R.id.enter_otpEd);
        email = findViewById(R.id.emailEd);
        pass = findViewById(R.id.loginPassEd);
        confirmPass = findViewById(R.id.confpassEd);
        refferal = findViewById(R.id.refferalEd);

        getOtp = findViewById(R.id.sendOTP);
        countDownTimer = findViewById(R.id.countdownTimer);
        resendOtp = findViewById(R.id.resend_otp);

        signup = findViewById(R.id.signup);

        name.setError(null);
        mobile.setError(null);
        enterOtp.setError(null);
        email.setError(null);
        pass.setError(null);
        confirmPass.setError(null);
        refferal.setError(null);


        /*
//        Textwatcher for name
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                Check Condition
                if (!s.toString().isEmpty() && !s.toString().matches("[a-zA-Z]+")){
                    // When value is not empty and contains a numeric value
                    // Shows Error
                    nameLt.setError("Allow only Character");
                }else {
                    nameLt.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
*/

//        Listeners
        getOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                progressDialog.setCanceledOnTouchOutside(false);
                sendVerificationCode();

            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signupOnClick();
            }
        });
        signup.setTextColor(Color.parseColor("#C0BEBE"));

    }

// ------------------------------- All methods Starts from here ----------------------

    //    This is an optional feature to change the status bar color when user in the Signup Page
    public void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.blue_900));


            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    //    When clicks on Already Have an account they will redirect to the Login Page
    public void alreadyHaveAccount(View view) {
        startActivity(new Intent(this, LoginActivity.class));
        overridePendingTransition(R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    //    When clicks on OTP button to recieve otp on the preffered mobile number
    private void sendVerificationCode() {
        String usersnumber = mobile.getText().toString();

        String phoneNumber = "+91" + mobile.getText().toString();

        if (usersnumber.isEmpty()) {
            mobileLt.setError("Required");
            mobileLt.requestFocus();
            progressDialog.dismiss();
            return;
        }
        if (usersnumber.length() != 10) {
            mobileLt.setError("Not Valid");
            mobileLt.requestFocus();
            progressDialog.dismiss();
            return;

        }
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
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

    //    When clicks on Signup Button to become a member of the application
    private void signupOnClick() {

        try {
            if (name.getText().toString().isEmpty()) {
                nameLt.setError("Required*");

            }
            if (!name.getText().toString().matches("[a-zA-Z]+")) {
                nameLt.setError("Allow only Characters");
            }
            if (mobile.getText().toString().isEmpty()) {
                mobileLt.setError("Required*");

            }
            if (email.getText().toString().isEmpty()) {
                emailLt.setError("Required*");
            }
            if (enterOtp.getText().toString().isEmpty()) {
                enterOtpLt.setError("Required*");

            }
            if (pass.getText().toString().isEmpty() || pass.getText().toString().length() < 6) {
                passLt.setError("Atleast 8 Characters");
            }
            if (confirmPass.getText().toString().isEmpty() || confirmPass.getText().toString().equals(pass)) {
                confirmPassLt.setError("Not valid");

            } else {


                // Registerdialog.show();
                pd.show();
                pd.setCanceledOnTouchOutside(false);


                verifySigninCode();
            }
        } catch (Exception e) {
            Toast.makeText(SignupActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

        }


    }

    private void verifySigninCode() {

        String code = enterOtp.getText().toString();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(CodeSent, code);
        signInWithPhoneAuthCredential(credential);


    }

    //  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //                      String id=task.getResult().getUser().getUid();
                            db = FirebaseDatabase.getInstance().getReference("Users");
                            Query query = db.orderByChild("mobilenumber").equalTo(mobile.getText().toString());


                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    if (snapshot.exists()) {
                                        // Registerdialog.dismiss();

                                        Toast.makeText(SignupActivity.this, "Mobile Number already Registered ", Toast.LENGTH_SHORT).show();
                                    } else {
                                        if (pass.getText().toString().length() < 6) {
                                            pass.setError("Password is too short");
                                        } else {

                                            // !Users users = new Users(name.getText().toString(), pass.getText().toString(), mobile.getText().toString()
                                            //!     , refferal.getText().toString(), wallet.toString(), email.getText().toString());
                                            // String id = task.getResult().getUser().getUid();
                                            Task<String> taskData = callcloudfunction();
                                            //!database.getReference().child("Users").child(mobile.getText().toString()).setValue(users);
                                            Toast.makeText(SignupActivity.this, "Account created Successfully", Toast.LENGTH_SHORT).show();
                                            // Registerdialog.dismiss();
                                            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                            finish();


                                        }
                                    }


                                }

                                private Task<String> callcloudfunction() {
                                    FirebaseFunctions mFunctions = FirebaseFunctions.getInstance();
                                    Map<String, Object> data = new HashMap<>();
                                    data.put("name", name.getText().toString());
                                    data.put("pass", pass.getText().toString());
                                    data.put("mobile", mobile.getText().toString());
                                    data.put("email", email.getText().toString());
                                    data.put("referral", refferal.getText().toString());
                                    data.put("push", true);
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
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });









                           /* mauth.createUserWithEmailAndPassword("example@gmail.com",pass.getText().toString()).
                                    addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            //  Toast.makeText(Register.this, "email", Toast.LENGTH_SHORT).show();
                                            if (task.isSuccessful()) {


                                                Users users = new Users(name.getText().toString(), pass.getText().toString(), phone.getText().toString()
                                                        , refer.getText().toString(), wallet.toString());
                                                String id = task.getResult().getUser().getUid();
                                                database.getReference().child("Users").child(id).setValue(users);
                                                Toast.makeText(Register.this, "Account created Successfully", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(Register.this, LoginActivity.class);
                                                startActivity(intent);
                                            }
                                            else {
                                                Toast.makeText(Register.this, "Mobile Number already in use", Toast.LENGTH_SHORT).show();
                                            }


                                        }
                                    });*/


                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // Toast.makeText(Register.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();


                                //problem
                                //progressDialog.show();
                                enterOtp.setError("Invalid");

                            }

                        }
                    }

                });
    }

    //    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

//            This method in No Longer Required

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            CodeSent = s;
            progressDialog.dismiss();
            Toast.makeText(SignupActivity.this, "Sent", Toast.LENGTH_SHORT).show();
            countDownTimer.setVisibility(View.VISIBLE);

            new CountDownTimer(60000, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {
                    countDownTimer.setText("" + millisUntilFinished / 1000);
                }

                @Override
                public void onFinish() {
                    resendOtp.setVisibility(View.VISIBLE);
                    countDownTimer.setVisibility(View.INVISIBLE);
                }
            }.start();


        }


    };

    //do you want to exit
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
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user select "No", just cancel this dialog and continue with app
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }


}
