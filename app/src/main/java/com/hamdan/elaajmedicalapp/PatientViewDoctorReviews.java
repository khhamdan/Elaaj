package com.hamdan.elaajmedicalapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hamdan.elaajmedicalapp.adapter.PatientReceivePrescriptionAdapter;
import com.hamdan.elaajmedicalapp.adapter.PatientViewDoctorReviewsAdapter;
import com.hamdan.elaajmedicalapp.models.IpAddress;
import com.hamdan.elaajmedicalapp.models.Prescription;
import com.hamdan.elaajmedicalapp.models.Reviews;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PatientViewDoctorReviews extends AppCompatActivity {

    IpAddress ipAddress;
    String url = "http://" + ipAddress.ip + ":8000/api/checkDoctorRatings";
    private List<Reviews> reviewsList;
    RecyclerView recyclerView;
    PatientViewDoctorReviewsAdapter patientViewDoctorReviewsAdapter;
    String doctor_id;
    SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_view_doctor_reviews);

        Toolbar toolbar = findViewById(R.id.toolbar_patientViewDoctorReviews);
        toolbar.setTitle("Doctor Reviews");
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.black));

        setSupportActionBar(toolbar);

        //4-Making the home button in toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_arrow);

        Intent intent = getIntent();
        doctor_id = intent.getStringExtra("doctor_id");

        recyclerView = findViewById(R.id.reviewOfDoctorRecyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        reviewsList = new ArrayList<>();

        swipeRefreshLayout = findViewById(R.id.swipeRefreshReviewOfDoctor);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                checkDoctorRatings(doctor_id);
                patientViewDoctorReviewsAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        checkDoctorRatings(doctor_id);

    }

    private void checkDoctorRatings(String doctor_id) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                reviewsList.clear();
                try {
                    JSONArray patientDetail = new JSONArray(response);
                    for (int i = 0; i < patientDetail.length(); i++) {
                        JSONObject object = patientDetail.getJSONObject(i);
                        String jRatings = object.getString("rating");
                        String jComments = object.getString("comments");
                        String jName = object.getString("name");

                        Reviews reviews = new Reviews(jName, jComments, jRatings);
                        reviewsList.add(reviews);
                        patientViewDoctorReviewsAdapter = new PatientViewDoctorReviewsAdapter(PatientViewDoctorReviews.this, reviewsList);
                        recyclerView.setAdapter(patientViewDoctorReviewsAdapter);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("doctor_id", doctor_id);
                return data;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}
