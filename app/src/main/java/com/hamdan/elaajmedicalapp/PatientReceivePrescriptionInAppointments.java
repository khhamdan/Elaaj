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
import com.hamdan.elaajmedicalapp.models.IpAddress;
import com.hamdan.elaajmedicalapp.models.Prescription;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PatientReceivePrescriptionInAppointments extends AppCompatActivity
{

    IpAddress ipAddress;
    private RecyclerView recyclerView;
    SharedPreferences sharedPreferences;
    String userPatientId;
    String url = "http://" + ipAddress.ip + ":8000/api/prescriptionByDoctor";
    private List<Prescription> prescriptionList;
    PatientReceivePrescriptionAdapter patientReceivePrescriptionAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    String doctor_id,appointmentDate,appointmentTime;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_recieve_prescription_in_appointments);


        Toolbar toolbar = findViewById(R.id.toolbar_patientReceivingPrescriptionInAppointments);
        toolbar.setTitle("My Prescription");
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.black));

        setSupportActionBar(toolbar);

        //4-Making the home button in toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_arrow);


        Intent intent = getIntent();
        doctor_id = intent.getStringExtra("doctor_id");
        appointmentDate = intent.getStringExtra("appointmentDate");
        appointmentTime = intent.getStringExtra("appointmentTime");


        recyclerView = findViewById(R.id.myPrescriptionInAppointmentsRecyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        sharedPreferences = getSharedPreferences("credentials", MODE_PRIVATE);
        userPatientId = sharedPreferences.getString("userId", "");
        prescriptionList = new ArrayList<>();

        swipeRefreshLayout = findViewById(R.id.swipeRefreshPatientReceivePres);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPrescriptionList(doctor_id,appointmentDate,appointmentTime);
                patientReceivePrescriptionAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        getPrescriptionList(doctor_id,appointmentDate,appointmentTime);
    }
    private void getPrescriptionList(String doctor_id,String appointmentDate,String appointmentTime) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                prescriptionList.clear();
                try {
                    JSONArray patientDetail = new JSONArray(response);
                    for (int i = 0; i < patientDetail.length(); i++) {
                        JSONObject object = patientDetail.getJSONObject(i);
                        String appointmentDate = object.getString("date");
                        String appointmentTime = object.getString("time");
                        String appointmentPrescriptions = object.getString("prescriptions");
                        String appointmentVoiceNote = object.getString("voice_note");
                        String appointmentId = object.getString("appointment_id");
                        String doctorId = object.getString("doctor_id");
                        String patientId = object.getString("patient_id");
                        String name = object.getString("name");
                        String profile_pic = object.getString("profile_pic");

                        Prescription prescription = new Prescription(appointmentDate, appointmentTime, appointmentPrescriptions, appointmentVoiceNote, appointmentId,patientId,doctorId,name,profile_pic);
                        prescriptionList.add(prescription);
                        patientReceivePrescriptionAdapter = new PatientReceivePrescriptionAdapter(PatientReceivePrescriptionInAppointments.this, prescriptionList);
                        recyclerView.setAdapter(patientReceivePrescriptionAdapter);
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
                data.put("date", appointmentDate);
                data.put("time", appointmentTime);
                return data;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

}