package com.hamdan.elaajmedicalapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.hamdan.elaajmedicalapp.adapter.DoctorHomeRecyclerViewAdapter;
import com.hamdan.elaajmedicalapp.adapter.Row;

import java.util.ArrayList;
import java.util.List;

public class DoctorHomePage extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    RecyclerView recyclerView;
    private DoctorHomeRecyclerViewAdapter adapter;
    private StaggeredGridLayoutManager manager;
    private List<Row> applist;
    SharedPreferences sharedPreferences;
    int[] covers = new int[]
            {
                    R.drawable.doctorappointments,
                    R.drawable.mypatients,
                    R.drawable.doctor_physical
            };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_home_page);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        //1- Toolbar:
        Toolbar toolbar = findViewById(R.id.doctorToolbar);
        toolbar.setTitle("Doctor Panel");
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.black));

        setSupportActionBar(toolbar);

        //4-Making the home button in toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menuu);

        sharedPreferences = getSharedPreferences("credentials", MODE_PRIVATE);
        String nameOfDoctor = sharedPreferences.getString("nameOfUser","");
        String userDoctorId = sharedPreferences.getString("userId","");

        // Header View
        View headerView = navigationView.getHeaderView(0);
        TextView navUserName = (TextView) headerView.findViewById(R.id.navPatientName);
        navUserName.setText(nameOfDoctor);
// -----Navigation View  ------
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.grey));

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setItemIconTintList(null);

        navigationView.setNavigationItemSelectedListener((new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        break;
                    case R.id.nav_previousAppointments:
                        Intent previousIntent = new Intent(DoctorHomePage.this, DoctorPreviousAppointments.class);
                        startActivity(previousIntent);
                        break;
                    case R.id.nav_myProfile:
                          Intent intent = new Intent(DoctorHomePage.this, DoctorProfile.class);
                          startActivity(intent);
                        break;
                    case R.id.nav_my_prescriptions:
                        Intent intentPrescription = new Intent(DoctorHomePage.this, DoctorGivenPrescription.class);
                        startActivity(intentPrescription);
                        break;
                    case R.id.nav_payment_history:
                        Intent intentPayment = new Intent(DoctorHomePage.this, DoctorPaymentsHistory.class);
                        startActivity(intentPayment);
                        break;
                    case R.id.nav_my_rating:
                        Intent intentRatings = new Intent(DoctorHomePage.this, DoctorViewRating.class);
                        startActivity(intentRatings);
                        break;
                    case R.id.nav_logout:
                        SharedPreferences sp = getSharedPreferences("credentials", MODE_PRIVATE);
                        if (sp.contains("username")) {
                            SharedPreferences.Editor editor = sp.edit();
                            editor.remove("username");
                            editor.putString("msg", "Logged out successfully");
                            editor.commit();
                            startActivity(new Intent(getApplicationContext(), LoginRegister.class));
                        }
                        break;
                }
                return true;
            }
        }));
        //5 - Initializing RecyclerView

        recyclerView = findViewById(R.id.doctorRecyclerview);
        applist = new ArrayList<>();

        //Adapter
        adapter = new DoctorHomeRecyclerViewAdapter(this,applist);
        manager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);

        // Layout Manager
        recyclerView.setLayoutManager(manager);

        //Adapter
        recyclerView.setAdapter(adapter);

        //6-Putting Data into RecyclerView
        InitializeDataIntoHomeRecyclerView();

    }
    public void onBackPressed()
    {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();

        }
    }

    private void InitializeDataIntoHomeRecyclerView()
    {
        Row row = new Row(covers[0]);
        applist.add(row);

        row = new Row(covers[1]);
        applist.add(row);

        row = new Row(covers[2]);
        applist.add(row);

        adapter.notifyDataSetChanged();
    }
    //2 - Adding Buttons to toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.doctor_main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    //3 - Handling clicks on toolbar buttons
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //handle action bar which will be automatically be handled if you specify the parent activity in AndroidManifest.xml
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_search) {
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }
}