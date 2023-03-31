package com.hamdan.elaajmedicalapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hamdan.elaajmedicalapp.adapter.SpecializedDoctorListAdapter;
import com.hamdan.elaajmedicalapp.models.Doctor_Details;
import com.hamdan.elaajmedicalapp.models.IpAddress;
import com.hamdan.elaajmedicalapp.models.User_Details;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SpecializedDoctorList extends AppCompatActivity {


    String url;
    User_Details user_details;
    Doctor_Details doctor_details;
    private List<User_Details> user_detailsList;
    private List<Doctor_Details> doctor_detailsList;
    SearchView searchView;

    String doctorSpecialityText;
    private RecyclerView recyclerView;
    SpecializedDoctorListAdapter specializedDoctorListAdapter;
    IpAddress ipAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specialized_doctor_list);

        user_detailsList = new ArrayList<>();
        doctor_detailsList = new ArrayList<>();
        recyclerView = findViewById(R.id.findDoctorRecyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SharedPreferences sharedPreferences = getSharedPreferences("doctorData", MODE_PRIVATE);
        String doctorSpecialityText = sharedPreferences.getString("doctorSpeciality","");

//        url = "http://10.0.2.2/Elaaj/login/fetchDoctorProfile.php";
        url = "http://"+ipAddress.ip+":8000/api/search/" + doctorSpecialityText;

//        //1- Toolbar:
        Toolbar toolbar = findViewById(R.id.specializedDoctorListToolbar);
        toolbar.setTitle(doctorSpecialityText);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.black));

        setSupportActionBar(toolbar);
//
//        //4-Making the home button in toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_arrow);

        searchView = findViewById(R.id.searchViewOfDoctor);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                specializedDoctorListAdapter.getFilter().filter(newText);

                return false;
            }
        });
        getSpecializedDoctorList();

    }

    private void getSpecializedDoctorList() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                user_detailsList.clear();
                doctor_detailsList.clear();
                try {
                    JSONArray doctorDetail = new JSONArray(response);

                    for (int i = 0; i < doctorDetail.length(); i++) {
                        JSONObject object = doctorDetail.getJSONObject(i);

                        String doctorId = object.getString("id");
                        System.out.println(doctorId);
                        String docProfilePic = object.getString("profile_pic");
                        System.out.println(docProfilePic);

                        String docName = object.getString("name");
                        String docExpertise = object.getString("expertise");
                        String docFees = object.getString("fee");
                        String docExperience = object.getString("experience");


                        User_Details user_details = new User_Details(docName, docProfilePic);
                        Doctor_Details doctor_details = new Doctor_Details(doctorId, docExpertise,docFees,docExperience);
                        user_detailsList.add(user_details);
                        doctor_detailsList.add(doctor_details);
                        specializedDoctorListAdapter =new SpecializedDoctorListAdapter(SpecializedDoctorList.this, user_detailsList, doctor_detailsList);
                        recyclerView.setAdapter(specializedDoctorListAdapter);

                  }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SpecializedDoctorList.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}

