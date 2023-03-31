package com.hamdan.elaajmedicalapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import com.hamdan.elaajmedicalapp.adapter.DoctorGivenPrescriptionAdapter;
import com.hamdan.elaajmedicalapp.adapter.PatientReceivePrescriptionAdapter;
import com.hamdan.elaajmedicalapp.models.IpAddress;
import com.hamdan.elaajmedicalapp.models.Prescription;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DoctorGivenPrescription extends AppCompatActivity {

    IpAddress ipAddress;
    private RecyclerView recyclerView;
    SharedPreferences sharedPreferences;
    String userDoctorId;
    String url = "http://" + ipAddress.ip + ":8000/api/doctorGivenPrescription";
    private List<Prescription> prescriptionList;
    DoctorGivenPrescriptionAdapter doctorGivenPrescriptionAdapter;
    SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_given_prescription);

        swipeRefreshLayout = findViewById(R.id.swipeRefreshDoctorGivenPrescription);

        Toolbar toolbar = findViewById(R.id.toolbar_doctorGivenPrescription);
        toolbar.setTitle("My Prescription");
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.black));

        setSupportActionBar(toolbar);

//        //4-Making the home button in toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_arrow);

        recyclerView = findViewById(R.id.myPrescribedPrescriptionsRecyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        sharedPreferences = getSharedPreferences("credentials", MODE_PRIVATE);
        userDoctorId = sharedPreferences.getString("userId", "");

        prescriptionList = new ArrayList<>();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPrescriptionList();
                doctorGivenPrescriptionAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        getPrescriptionList();
    }

    private void getPrescriptionList() {
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

                        Prescription prescription = new Prescription(appointmentDate, appointmentTime, appointmentPrescriptions, appointmentVoiceNote, appointmentId, patientId, doctorId, name, profile_pic);
                        prescriptionList.add(prescription);
                        doctorGivenPrescriptionAdapter = new DoctorGivenPrescriptionAdapter(DoctorGivenPrescription.this, prescriptionList);
                        recyclerView.setAdapter(doctorGivenPrescriptionAdapter);
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
                data.put("doctor_id", userDoctorId);
                return data;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}