package com.hamdan.elaajmedicalapp.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.hamdan.elaajmedicalapp.PatientCurrentAppointmentsFragments;
import com.hamdan.elaajmedicalapp.PatientCurrentPhysicalAppFragment;
import com.hamdan.elaajmedicalapp.PatientPreviousAppointmentsFragments;
import com.hamdan.elaajmedicalapp.PatientPreviousPhysicalAppFragment;
import com.hamdan.elaajmedicalapp.PatientTodayAppointmentFragment;
import com.hamdan.elaajmedicalapp.PatientTodayPhysicalAppFragment;

public class PatientPhysicalAppointmentAdapter extends FragmentPagerAdapter {


    private Context context;
    int totalTabs;
    private String[] tabTitles = new String[]{"Current","Today/Upcoming", "Previous"};


    public PatientPhysicalAppointmentAdapter(@NonNull FragmentManager fm, Context context, int totalTabs) {
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
                PatientCurrentPhysicalAppFragment patientCurrentPhysicalAppFragment = new PatientCurrentPhysicalAppFragment();
                return patientCurrentPhysicalAppFragment;
            case 1:
                PatientTodayPhysicalAppFragment patientTodayPhysicalAppFragment = new PatientTodayPhysicalAppFragment();
                return patientTodayPhysicalAppFragment;
            case 2:
                PatientPreviousPhysicalAppFragment patientPreviousPhysicalAppFragment = new PatientPreviousPhysicalAppFragment();
                return patientPreviousPhysicalAppFragment;
            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return tabTitles.length;
    }
}
