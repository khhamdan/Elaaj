package com.hamdan.elaajmedicalapp.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.hamdan.elaajmedicalapp.DoctorCurrentPhysicalAppFragment;
import com.hamdan.elaajmedicalapp.DoctorTodayPhysicalAppointmentFragment;
import com.hamdan.elaajmedicalapp.DoctorsTodayOnlineAppointmentsFragments;

public class DoctorPhysicalAppointmentAdapter extends FragmentPagerAdapter {
    private Context context;
    int totalTabs;
    private String[] tabTitles = new String[]{"Current","Today/Upcoming"};


    public DoctorPhysicalAppointmentAdapter(@NonNull FragmentManager fm, Context context, int totalTabs) {
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
                DoctorCurrentPhysicalAppFragment doctorCurrentPhysicalAppFragment = new DoctorCurrentPhysicalAppFragment();
                return doctorCurrentPhysicalAppFragment;
            case 1:
                DoctorTodayPhysicalAppointmentFragment doctorTodayPhysicalAppointmentFragment = new DoctorTodayPhysicalAppointmentFragment();
                return doctorTodayPhysicalAppointmentFragment;
            default:
                return null;
        }}


    @Override
    public int getCount() {
        return tabTitles.length;
    }

}
