package com.gamestockz.Activities;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.gamestockz.Connection;
import com.gamestockz.Fragments.BottomRedFragment;
import com.gamestockz.R;
import com.gamestockz.databinding.FragmentBottomGreenBinding;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout loginMobileLt, loginPassLt;
    private TextInputEditText loginMobile, loginPass;
    private TextView forgetPass;
    private Button login;


    SharedPreferences preferences;
    private static final String LOGIN_CHECK = "MyLoginCheck";
    private static final String KEY_MOBILE = "mobile";


    BroadcastReceiver broadcastReceiver;
    ProgressDialog progressDialog;

    FirebaseAuth mauth;
    FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.activity_login);
        initElements();
        registernetwork();

    }

    private void initElements() {

        broadcastReceiver = new Connection();
        mauth = FirebaseAuth.getInstance();
        preferences = getSharedPreferences(LOGIN_CHECK, MODE_PRIVATE);

        String sharedPrefmobile = preferences.getString(KEY_MOBILE, null);
        if (sharedPrefmobile != null) {
            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
            startActivity(intent);
        }

//        For all TextInput Layouts
        loginMobileLt = findViewById(R.id.forgetMobile);
        loginPassLt = findViewById(R.id.forgetNewPass);

//        For all Textinput EditTexts
        loginMobile = findViewById(R.id.loginMobileEd);
        loginPass = findViewById(R.id.loginPassEd);

//        For all Clickable Buttons and TextViews
        forgetPass = findViewById(R.id.forgetpass);
        login = findViewById(R.id.login);
        //firebase instance
        database = FirebaseDatabase.getInstance();

        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setTitle("Login...");
        progressDialog.setMessage("we are Login in Your Account");

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginOnClick();
            }
        });

        forgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });

    }

    public void newUserRegisterNow(View view) {
        startActivity(new Intent(this, SignupActivity.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.stay);
    }

    public void loginOnClick() {


        try {
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
            if (loginMobile.getText().toString().isEmpty()) {
                loginMobileLt.setError("Required");
                progressDialog.dismiss();
            }
            if (loginPass.getText().toString().isEmpty()) {
                loginPassLt.setError("Required");
                progressDialog.dismiss();
            }
            if (loginMobile.getText().toString().length() < 10) {
                loginMobileLt.setError("Not Valid");
                progressDialog.dismiss();
            }
            Query checkUsers = reference.orderByChild("mobilenumber").equalTo(loginMobile.getText().toString());

            checkUsers.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String passwordfromdb = snapshot.child(loginMobile.getText().toString()).child("password").getValue(String.class);

//                         Toast.makeText(LoginActivity.this, ""+passwordfromdb, Toast.LENGTH_SHORT).show();
                        if (passwordfromdb.equals(loginPass.getText().toString())) {
                            String namefromdb = snapshot.child(loginMobile.getText().toString()).child("Name").getValue(String.class);
                            String walletfromdb = snapshot.child(loginMobile.getText().toString()).child("wallet").getValue(String.class);
                            String mobilefromdb = snapshot.child(loginMobile.getText().toString()).child("mobilenumber").getValue(String.class);
                            Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();



                            SharedPreferences.Editor editor = preferences.edit();

//                            preferences = getSharedPreferences(LOGIN_CHECK, MODE_PRIVATE);


                            editor.putString(KEY_MOBILE, loginMobile.getText().toString());
                            editor.apply();

                            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                            startActivity(intent);
                            finish();




//                            intent.putExtra("name", namefromdb);
//                            intent.putExtra("wallet", walletfromdb);
//                            intent.putExtra("mobile", mobilefromdb);
//                            Intent intent1=new Intent(getApplicationContext(),OnRechargeActivity.class);
//                            startActivity(intent);
//                            intent1.putExtra("mobile1",mobilefromdb);
//                            finish();

                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Invalid Login Credentials", Toast.LENGTH_SHORT).show();
                        }


                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "User Not Found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

                    /*String email = phone1.getText().toString() + "@gmail.com";
                    String p = password.getText().toString();
                    mauth.signInWithEmailAndPassword(email, p).
                            addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressDialog.dismiss();
                                    if (task.isSuccessful()) {



                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }


                                }

                            });*/


        } catch (Exception e) {
            progressDialog.dismiss();
            Toast.makeText(LoginActivity.this, "Fields are Empty", Toast.LENGTH_SHORT).show();
        }
    }


    protected void registernetwork() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        }


    }

    protected void unregisternetwork() {
        try {
            unregisterReceiver(broadcastReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();


        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisternetwork();
    }


}
