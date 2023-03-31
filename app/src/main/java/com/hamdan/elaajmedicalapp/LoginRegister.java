package com.hamdan.elaajmedicalapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.hamdan.elaajmedicalapp.adapter.LoginAdapter;

public class LoginRegister extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_paper);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(viewPager);

        final LoginAdapter adapter = new LoginAdapter(getSupportFragmentManager(),this,tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setTranslationY(300);

        tabLayout.setAlpha(0);
        tabLayout.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(100).start();
    }
}