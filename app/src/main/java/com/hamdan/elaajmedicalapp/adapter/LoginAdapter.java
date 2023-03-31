package com.hamdan.elaajmedicalapp.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.hamdan.elaajmedicalapp.LoginTabFragment;
import com.hamdan.elaajmedicalapp.SignupTabFragment;

public class LoginAdapter extends FragmentPagerAdapter {

    private Context context;
    int totalTabs;
    private String[] tabTitles = new String[]{"Login", "Signup"};

    public LoginAdapter(@NonNull FragmentManager fm, Context context, int totalTabs) {
        super(fm);
        this.context = context;
        this.totalTabs = totalTabs;
    }
    // overriding getPageTitle()
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {

            case 0:
                LoginTabFragment loginTabFragment = new LoginTabFragment();
                return loginTabFragment;
            case 1:
                SignupTabFragment signupTabFragment = new SignupTabFragment();
                return signupTabFragment;
            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return tabTitles.length;
    }
}
