package com.hamdan.elaajmedicalapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;
import com.hamdan.elaajmedicalapp.adapter.DoctorOnlineAppointmentAdapter;
import com.hamdan.elaajmedicalapp.adapter.DoctorPhysicalAppointmentAdapter;

public class DoctorPhysicalAppointments extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_physical_appointments);

        Toolbar toolbar = findViewById(R.id.doctorPhysicalAppointmentsToolbar);
        toolbar.setTitle("Physical Appointments");
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.black));

        setSupportActionBar(toolbar);

//        //4-Making the home button in toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_arrow);


        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_paper);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(viewPager);

        final DoctorPhysicalAppointmentAdapter adapter = new DoctorPhysicalAppointmentAdapter(getSupportFragmentManager(),this,tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setTranslationY(300);

        tabLayout.setAlpha(0);
        tabLayout.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(100).start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent intent = new Intent(this, DoctorHomePage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}