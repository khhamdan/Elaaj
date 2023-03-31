package com.hamdan.elaajmedicalapp;

import  androidx.annotation.NonNull;
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
import com.hamdan.elaajmedicalapp.adapter.PatientHomeRecyclerViewAdapter;
import com.hamdan.elaajmedicalapp.adapter.Row;

import java.util.ArrayList;
import java.util.List;

public class PatientHomePage extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    RecyclerView recyclerView;
    private PatientHomeRecyclerViewAdapter adapter;
    private StaggeredGridLayoutManager manager;
    private List<Row> applist;
    SharedPreferences sharedPreferences;
    //Array of Images
    int[] covers = new int[]
            {
                    R.drawable.fash1,
                    R.drawable.patient_appointments,
                    R.drawable.physical

            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        //create test HashMap


        //Getting the name of the Patient and displaying it on HeaderView
//        Gson gson = new Gson();
//        String storedHashMapString = prefs.getString("hashString","oopsDintWork");
//        Type type = new TypeToken<HashMap<String,String>>(){}.getType();
//        HashMap<String,String> testHashMap2 = gson.fromJson(storedHashMapString,type);

        sharedPreferences = getSharedPreferences("credentials", MODE_PRIVATE);
        String patientName = sharedPreferences.getString("nameOfUser","");
        String userPatientId = sharedPreferences.getString("userId","");

        // Header View
        View headerView = navigationView.getHeaderView(0);
        TextView navPatientName = (TextView) headerView.findViewById(R.id.navPatientName);
        navPatientName.setText(patientName);

        //1- Toolbar:
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Elaaj");
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.black));
        setSupportActionBar(toolbar);

        //4-Making the home button in toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menuu);
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
                    case R.id.nav_myAppointments:
                        Intent intent = new Intent(PatientHomePage.this, PatientAppointment.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_myProfile:
                        Intent editProfileIntent = new Intent(PatientHomePage.this, PatientProfile.class);
                        startActivity(editProfileIntent);
                        break;
                    case R.id.nav_wallet:
                        Intent walletIntent = new Intent(PatientHomePage.this, PatientWallet.class);
                        startActivity(walletIntent);
                        break;
                        case R.id.nav_myprescription:
                        Intent myPrescriptionIntent = new Intent(PatientHomePage.this, PatientViewAllPrescription.class);
                        startActivity(myPrescriptionIntent);
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

        recyclerView = findViewById(R.id.recyclerview);
        applist = new ArrayList<>();

        //Adapter
        adapter = new PatientHomeRecyclerViewAdapter(this,applist);
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

    private void InitializeDataIntoHomeRecyclerView() {
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

        getMenuInflater().inflate(R.menu.menu_main, menu);
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