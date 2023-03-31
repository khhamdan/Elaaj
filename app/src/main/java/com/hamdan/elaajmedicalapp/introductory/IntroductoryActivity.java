package com.hamdan.elaajmedicalapp.introductory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.hamdan.elaajmedicalapp.LoginRegister;
import com.hamdan.elaajmedicalapp.R;


public class IntroductoryActivity extends AppCompatActivity {
    ImageView logo,splashImg;

    private static final int NUM_PAGES = 2;
    private ViewPager viewPager;
    private ScreenSlidePageAdapter pageAdapter;
    private static int SPLASH_TIME_OUT = 5000;
    SharedPreferences mSharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introductory);

        logo = findViewById(R.id.logo);
        splashImg = findViewById(R.id.img);

        viewPager = findViewById(R.id.pager);
        pageAdapter = new ScreenSlidePageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pageAdapter);

        splashImg.animate().translationY(-2200).setDuration(1000).setStartDelay(4000);
        logo.animate().translationY(1400).setDuration(1000).setStartDelay(4000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSharedPref = getSharedPreferences("SharedPref",MODE_PRIVATE);
                boolean isFirstTime = mSharedPref.getBoolean("firstTime",true);
                if(isFirstTime)
                {
                    //go to on boarding activity
                    SharedPreferences.Editor editor = mSharedPref.edit();
                    editor.putBoolean("firstTime",false);
                    editor.commit();
                }
                else
                {
                    Intent intent = new Intent(IntroductoryActivity.this, LoginRegister.class);
                    startActivity(intent);
                    finish();
                }
            }
        },SPLASH_TIME_OUT);

    }
    private class ScreenSlidePageAdapter extends FragmentStatePagerAdapter
    {

        public ScreenSlidePageAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position)
            {
                case 0:
                    OnBoardingFragment1 tab1 = new OnBoardingFragment1();
                    return tab1;
                case 1:
                    OnBoardingFragment2 tab2= new OnBoardingFragment2();
                    return tab2;

            }
            return null;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}