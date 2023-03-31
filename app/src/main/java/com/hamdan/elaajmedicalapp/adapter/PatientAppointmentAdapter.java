package com.hamdan.elaajmedicalapp.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.hamdan.elaajmedicalapp.PatientPreviousAppointmentsFragments;
import com.hamdan.elaajmedicalapp.PatientCurrentAppointmentsFragments;
import com.hamdan.elaajmedicalapp.PatientTodayAppointmentFragment;

public class PatientAppointmentAdapter extends FragmentPagerAdapter {

    private Context context;
    int totalTabs;
    private String[] tabTitles = new String[]{"Current","Today/Upcoming", "Previous"};


    public PatientAppointmentAdapter(@NonNull FragmentManager fm,Context context, int totalTabs) {
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
                PatientCurrentAppointmentsFragments patientCurrentAppointmentsFragments = new PatientCurrentAppointmentsFragments();
                    return patientCurrentAppointmentsFragments;
                case 1:
                PatientTodayAppointmentFragment patientTodayAppointmentFragment = new PatientTodayAppointmentFragment();
                return patientTodayAppointmentFragment;
            case 2:
                PatientPreviousAppointmentsFragments patientPreviousAppointmentsFragments = new PatientPreviousAppointmentsFragments();
                return patientPreviousAppointmentsFragments;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }
}
