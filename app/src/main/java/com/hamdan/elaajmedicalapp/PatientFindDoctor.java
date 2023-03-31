package com.hamdan.elaajmedicalapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;
import android.widget.Toast;

import com.hamdan.elaajmedicalapp.adapter.DoctorSpecialityAdapter;
import com.hamdan.elaajmedicalapp.models.DoctorSpeciality;

import java.util.ArrayList;
import java.util.List;

public class PatientFindDoctor extends AppCompatActivity {
    private SearchView searchView;
    private List<DoctorSpeciality> doctorSpecialityList;
    private DoctorSpecialityAdapter doctorSpecialityAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_doctor);
        RecyclerView recyclerView = findViewById(R.id.findDoctorRecyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));

        //1- Toolbar:
        Toolbar toolbar = findViewById(R.id.FindDoctorToolbar);
        toolbar.setTitle("Find Doctor");
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.black));

        setSupportActionBar(toolbar);

        //4-Making the home button in toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_arrow);

        //-Search View

        searchView = findViewById(R.id.searchView);
        searchView.clearFocus();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });

        doctorSpecialityList = new ArrayList<>();

        doctorSpecialityList.add(new DoctorSpeciality("Cardiologist",R.drawable.cardialogy_icon));
        doctorSpecialityList.add(new DoctorSpeciality("Gastroenterology",R.drawable.gastrologist_icon));
        doctorSpecialityList.add(new DoctorSpeciality("Dentist",R.drawable.dentist_icon));
        doctorSpecialityList.add(new DoctorSpeciality("General Physician",R.drawable.general_physician_icon));
        doctorSpecialityList.add(new DoctorSpeciality("Homeopathy",R.drawable.homeopathy_icon));
        doctorSpecialityList.add(new DoctorSpeciality("Neurologist",R.drawable.neorologist_icon));
        doctorSpecialityList.add(new DoctorSpeciality("Pediatrician",R.drawable.pediatrician_icon));
        doctorSpecialityList.add(new DoctorSpeciality("Urologist",R.drawable.urologist_icon));

/*
        DoctorSpeciality[] myDoctorSpeciality = new DoctorSpeciality[]{
                new DoctorSpeciality("Cardiologist",R.drawable.cardialogy_icon),
                new DoctorSpeciality("Gastrologist",R.drawable.gastrologist_icon),
                new DoctorSpeciality("Dentist",R.drawable.dentist_icon),
                new DoctorSpeciality("General Physician",R.drawable.general_physician_icon),
                new DoctorSpeciality("Homeopathy",R.drawable.homeopathy_icon),
                new DoctorSpeciality("Neurologist",R.drawable.neorologist_icon),
                new DoctorSpeciality("Pediatrician",R.drawable.pediatrician_icon),
                new DoctorSpeciality("Urologist",R.drawable.urologist_icon),
        };
*/


        doctorSpecialityAdapter = new DoctorSpecialityAdapter(doctorSpecialityList, PatientFindDoctor.this);
        recyclerView.setAdapter(doctorSpecialityAdapter);
    }

    private void filterList(String text)
    {
        List<DoctorSpeciality> filteredList = new ArrayList<>();
        for(DoctorSpeciality doctorSpeciality : doctorSpecialityList)
        {
            if (doctorSpeciality.getDoctorSpecialityText().toLowerCase().contains(text.toLowerCase()))
            {
                filteredList.add(doctorSpeciality);
            }
        }
        if (filteredList.isEmpty())
        {
            Toast.makeText(this, "No data Found", Toast.LENGTH_SHORT).show();
        }else
        {
         doctorSpecialityAdapter.setFilteredList(filteredList);
        }
    }
    //3 - Handling clicks on toolbar buttons

}