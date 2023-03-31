package com.hamdan.elaajmedicalapp.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.hamdan.elaajmedicalapp.DoctorCurrentOnlineAppFragment;
import com.hamdan.elaajmedicalapp.DoctorsTodayOnlineAppointmentsFragments;

public class DoctorOnlineAppointmentAdapter extends FragmentPagerAdapter {


    private Context context;
    int totalTabs;
    private String[] tabTitles = new String[]{"Current","Today/Upcoming"};


    public DoctorOnlineAppointmentAdapter(@NonNull FragmentManager fm, Context context, int totalTabs) {
        super(fm);
        this.context = context;
        this.totalTabs = totalTabs;
    }

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
                DoctorCurrentOnlineAppFragment doctorCurrentOnlineAppFragment = new DoctorCurrentOnlineAppFragment();
                return doctorCurrentOnlineAppFragment;
            case 1:
                DoctorsTodayOnlineAppointmentsFragments doctorsTodayOnlineAppointmentsFragments = new DoctorsTodayOnlineAppointmentsFragments();
                return doctorsTodayOnlineAppointmentsFragments;
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }
}
