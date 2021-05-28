package com.gamestockz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

public class SplashScreenActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN = 3000;

    //    Variables uses in the splash screen
    Animation topAnim, bottomAnim;
    ImageView splashLogo;
    TextView logo, splashSlogan;
//
////  This Variables for the onboarding screen
//    private static final int NUM_PAGES = 5;
//    private ViewPager viewPager;
//    private ScreenSlidePagerAdapter pagerAdapter;

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

////
//        viewPager = findViewById(R.id.pager);
//        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
//        viewPager.setAdapter(pagerAdapter);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreenActivity.this, OnBoardingActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_SCREEN);

    }

//    class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
//
//
//        public ScreenSlidePagerAdapter(@NonNull @NotNull FragmentManager fm) {
//            super(fm);
//        }
//
//        @NonNull
//        @NotNull
//        @Override
//        public Fragment getItem(int position) {
//            switch (position){
//                case 0:
//                    OnBoardingFragment1 tab1 = new OnBoardingFragment1();
//                    return tab1;
//                case 1:
//                    OnBoardingFragment2 tab2 = new OnBoardingFragment2();
//                    return tab2;
//                case 2:
//                    OnBoardingFragment3 tab3 = new OnBoardingFragment3();
//                    return tab3;
//                case 3:
//                    OnBoardingFragment4 tab4 = new OnBoardingFragment4();
//                    return tab4;
//                case 4:
//                    OnBoardingFragment5 tab5 = new OnBoardingFragment5();
//                    return tab5;
//            }
//            return null;
//        }
//
//        @Override
//        public int getCount() {
//            return NUM_PAGES;
//        }
//    }

}