

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
import com.hamdan.elaajmedicalapp.adapter.DoctorPreviousAppointmentsAdapter;
import com.hamdan.elaajmedicalapp.models.Appointments;
import com.hamdan.elaajmedicalapp.models.Doctor_Details;
import com.hamdan.elaajmedicalapp.models.IpAddress;
import com.hamdan.elaajmedicalapp.models.Payments;
import com.hamdan.elaajmedicalapp.models.User_Details;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DoctorPreviousAppointments extends AppCompatActivity {

    IpAddress ipAddress;
    String url="http://"+ipAddress.ip+":8000/api/showPreviousDoctorAppointments";
    User_Details user_details;
    Doctor_Details doctor_details;
    Appointments appointments;
    private List<User_Details> user_detailsList;
    private List<Doctor_Details> doctor_detailsList;
    private List<Appointments> appointmentsList;
    private List<Payments> paymentsList;
    SharedPreferences sharedPreferences;
    private RecyclerView recyclerView;
    String doctor_id, appointmentDate, appointmentTime, appointmentType, docProfilePic, doctorName, doctorExpertise;
    String userDoctorId;
    SwipeRefreshLayout swipeRefreshLayout;
    DoctorPreviousAppointmentsAdapter doctorPreviousAppointmentsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_previous_appointments);


        Toolbar toolbar = findViewById(R.id.doctorPreviousAppointmentsToolbar);
        toolbar.setTitle("Doctors Previous Appointments");
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.black));

        setSupportActionBar(toolbar);

        //4-Making the home button in toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_arrow);


        user_detailsList = new ArrayList<>();
        doctor_detailsList = new ArrayList<>();
        appointmentsList = new ArrayList<>();
        paymentsList = new ArrayList<>();
        recyclerView = findViewById(R.id.doctorsPreviousAppointmentRecyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        sharedPreferences = getSharedPreferences("credentials", MODE_PRIVATE);
        String doctorName = sharedPreferences.getString("nameOfUser", "");
        userDoctorId = sharedPreferences.getString("userId", "");
        swipeRefreshLayout = findViewById(R.id.swipeRefreshDoctorsPreviousAppointments);

        getAppointmentList();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAppointmentList();
                doctorPreviousAppointmentsAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });


    }
    private void getAppointmentList()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                user_detailsList.clear();
                appointmentsList.clear();
                try {
                    JSONArray appointmentsDetails = new JSONArray(response);
                    for (int i = 0; i < appointmentsDetails.length(); i++) {
                        JSONObject object = appointmentsDetails.getJSONObject(i);

                        String patientName = object.getString("name");
                        String patientProfilePic = object.getString("profile_pic");
                        String appointmentDate = object.getString("date");
                        String appointmentTime = object.getString("time");
                        String appointmentStatus = object.getString("status");
                        String appointmentType = object.getString("type");
                        String appointment_id = object.getString("appointment_id");
                        String patient_id = object.getString("appointment_patient_id");
                        String amount  = object.getString("amount");


                        User_Details user_details = new User_Details(patientName, patientProfilePic);
                        Appointments appointments = new Appointments(appointment_id, appointmentDate, appointmentTime, appointmentType,appointmentStatus, patient_id,null);
                        Payments payments = new Payments(amount);
                        user_detailsList.add(user_details);
                        appointmentsList.add(appointments);
                        paymentsList.add(payments);
                        doctorPreviousAppointmentsAdapter = new DoctorPreviousAppointmentsAdapter(DoctorPreviousAppointments.this,appointmentsList,user_detailsList,paymentsList);
                        recyclerView.setAdapter(doctorPreviousAppointmentsAdapter);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DoctorPreviousAppointments.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}