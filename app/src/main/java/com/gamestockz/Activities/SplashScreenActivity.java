package com.gamestockz.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.gamestockz.R;

public class SplashScreenActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN = 2000;

    //    Variables uses in the splash screen
    Animation topAnim, bottomAnim;
    ImageView splashLogo;
    TextView logo, splashSlogan;

    SharedPreferences onBoardingScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

//        Animations
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

//        Hooks
        splashLogo = findViewById(R.id.splash_logo);
        logo = findViewById(R.id.splashAppName);
        splashSlogan = findViewById(R.id.splashSlogan);

//        Give animation to the views
        splashLogo.setAnimation(topAnim);
        logo.setAnimation(bottomAnim);
        splashSlogan.setAnimation(bottomAnim);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                onBoardingScreen = getSharedPreferences("onBoardingScreen", MODE_PRIVATE);
                boolean isFirstTime = onBoardingScreen.getBoolean("firstTime",true);


                SharedPreferences preferences = getSharedPreferences(LoginActivity.LOGIN_CHECK, 0);
                boolean hasLoggedIn = preferences.getBoolean("hasLoggedIn", false);


                if (isFirstTime){

                    SharedPreferences.Editor editor = onBoardingScreen.edit();
                    editor.putBoolean("firstTime",false);
                    editor.apply();

                    Intent intent = new Intent(SplashScreenActivity.this, OnBoardingActivity.class);
                    startActivity(intent);
                    finish();
                }
                else if (hasLoggedIn){
                    Intent intent = new Intent(SplashScreenActivity.this, DashboardActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        },SPLASH_SCREEN);

    }

}