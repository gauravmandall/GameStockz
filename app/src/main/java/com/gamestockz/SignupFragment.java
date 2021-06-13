package com.gamestockz;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.gamestockz.Modals.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

public class SignupFragment extends Fragment {


    View view;
    FirebaseAuth objectFirebaseAuth;

    TextInputLayout name, mobile, enter_otp, email, pass, conf_pass, refferal;
    ImageButton otp;
    Button signup;
    TextView resend_otp;
    String CodeSent;
    Integer wallet = 0;
    String cc = "91";
    FirebaseDatabase database;
    ProgressDialog progressDialog;
    ProgressDialog pd;
    DatabaseReference db;


    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_signup, container, false);

        objectFirebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        progressDialog = new ProgressDialog(getActivity());
        pd=new ProgressDialog(getActivity());


//        For all the Edit Texts
        name = view.findViewById(R.id.fullname);
        mobile = view.findViewById(R.id.mobile);
        enter_otp = view.findViewById(R.id.otp);
        email = view.findViewById(R.id.email);
        pass = view.findViewById(R.id.pass);
        conf_pass = view.findViewById(R.id.confpass);
        refferal = view.findViewById(R.id.refferal);
//        For the buttons
        otp = view.findViewById(R.id.sendOTP);
        signup = view.findViewById(R.id.signup);
        resend_otp = view.findViewById(R.id.resend_otp);

        otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                progressDialog.setCanceledOnTouchOutside(false);
                sendVerificationCode();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick (View view){
                    try {
                        if (Objects.requireNonNull(name.getEditText()).toString().isEmpty()) {
                            name.setError("Required");
                            pd.dismiss();
                        }
                        if (mobile.getEditText().toString().isEmpty()) {
                            mobile.setError("Required");
                            pd.dismiss();
                        }
                        if (pass.getEditText().toString().isEmpty()) {
                            pass.setError("Required");
                            pd.dismiss();
                        }
                        if (enter_otp.getEditText().toString().isEmpty()) {
                            enter_otp.setError("Required");
                            pd.dismiss();
                        }
                        if (pass.getEditText().toString().length() < 6) {
                            pass.setError("Not Valid");
                        }
                        if (conf_pass.getEditText().toString().isEmpty()) {
                            conf_pass.setError("Required");
                            pd.dismiss();
                        }
                        if (conf_pass.getEditText().toString().equals(pass)) {
                            conf_pass.setError("Not Matched");
                            pd.dismiss();
                        } else {
                            pd.show();
                            pd.setCanceledOnTouchOutside(false);

                            verifySigninCode();
                        }
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();

                    }
                }
            });
        return null;
    }

    private void verifySigninCode() {
        String code = enter_otp.getEditText().toString();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(CodeSent, code);
        signInWithPhoneAuthCredential(credential);

    }

    private void sendVerificationCode() {

        String usersnumber = mobile.getEditText().toString();

        String phoneNumber = "+" + cc + mobile.getEditText().toString();

        if (usersnumber.isEmpty()) {
            mobile.setError("Required");
            mobile.requestFocus();
            progressDialog.dismiss();
            return;
        }
        if (usersnumber.length() < 10) {
            mobile.setError("Not Valid");
            mobile.requestFocus();
            progressDialog.dismiss();

            return ;

    }
            PhoneAuthOptions options =
                    PhoneAuthOptions.newBuilder(objectFirebaseAuth)
                            .setPhoneNumber(phoneNumber)       // Phone number to verify
                            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                            .setActivity(getActivity())                 // Activity (for callback binding)
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

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {


        objectFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener((Executor) this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //                      String id=task.getResult().getUser().getUid();
                            db = FirebaseDatabase.getInstance().getReference("Users");
                            Query query = db.orderByChild("mobilenumber").equalTo(mobile.getEditText().toString());


                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    if (snapshot.exists()) {
                                        // RegisterDialog.dismiss();
                                        Toast.makeText(getActivity(), "Mobile Number already Registered ", Toast.LENGTH_SHORT).show();

                                    } else {
                                        if (pass.getEditText().toString().length() < 6) {
                                            pass.setError("Password is too short");
                                        } else {
                                            Users users = new Users(name.getEditText().toString(), pass.getEditText().toString(), mobile.getEditText().toString()
                                                    , refferal.getEditText().toString(), wallet.toString(), email.getEditText().toString());
                                            // String id = task.getResult().getUser().getUid();
                                            database.getReference().child("Users").child(mobile.getEditText().toString()).setValue(users);
                                            Toast.makeText(getActivity(), "Account created Successfully", Toast.LENGTH_SHORT).show();

                                        }
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                           /* mauth.createUserWithEmailAndPassword("sudhirkumar79823@gmail.com",pass.getText().toString()).
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
                                enter_otp.setError("Invalid");

                            }

                        }
                    }

                });
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {


            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                CodeSent = s;
                Toast.makeText(getContext(), "Sent", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }


        };

        //do you want to exit
        public void onBackPressed (View view) {
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setCancelable(false);
            builder.setMessage("Do you want to Exit?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //if user pressed "yes", then he is allowed to exit from application
                    getActivity().finish();
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

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View getView() {
        return view;
    }


    }