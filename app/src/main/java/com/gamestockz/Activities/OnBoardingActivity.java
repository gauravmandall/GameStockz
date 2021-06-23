package com.gamestockz.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.WindowManager;

import com.gamestockz.Fragments.OnBoardingFragment1;
import com.gamestockz.Fragments.OnBoardingFragment2;
import com.gamestockz.Fragments.OnBoardingFragment3;
import com.gamestockz.Fragments.OnBoardingFragment4;
import com.gamestockz.Fragments.OnBoardingFragment5;
import com.gamestockz.R;

import org.jetbrains.annotations.NotNull;

public class OnBoardingActivity extends AppCompatActivity {


    //  This Variables for the onboarding screen
    private static final int NUM_PAGES = 5;
    private ViewPager viewPager;
    private ScreenSlidePagerAdapter pagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);


//
        viewPager = findViewById(R.id.pager);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);


    }

    @Deprecated
    class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {


        public ScreenSlidePagerAdapter(@NonNull @NotNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @NotNull
        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    OnBoardingFragment1 tab1 = new OnBoardingFragment1();
                    return tab1;
                case 1:
                    OnBoardingFragment2 tab2 = new OnBoardingFragment2();
                    return tab2;
                case 2:
                    OnBoardingFragment3 tab3 = new OnBoardingFragment3();
                    return tab3;
                case 3:
                    OnBoardingFragment4 tab4 = new OnBoardingFragment4();
                    return tab4;
                case 4:
                    OnBoardingFragment5 tab5 = new OnBoardingFragment5();
                    return tab5;
            }
            return null;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }


}