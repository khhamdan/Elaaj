package com.hamdan.elaajmedicalapp;

import androidx.appcompat.app.AppCompatActivity;
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
import com.hamdan.elaajmedicalapp.adapter.PatientAppointmentsListAdapter;
import com.hamdan.elaajmedicalapp.models.Appointments;
import com.hamdan.elaajmedicalapp.models.Doctor_Details;
import com.hamdan.elaajmedicalapp.models.IpAddress;
import com.hamdan.elaajmedicalapp.models.User_Details;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PatientAppointments extends AppCompatActivity {

    IpAddress ipAddress;
    String url;
    User_Details user_details;
    Doctor_Details doctor_details;
    Appointments appointments;
    private List<User_Details> user_detailsList;
    private List<Doctor_Details> doctor_detailsList;
    private List<Appointments> appointmentsList;
    SharedPreferences sharedPreferences;
    private RecyclerView recyclerView;
    String doctor_id,appointmentDate,appointmentTime,appointmentType,docProfilePic,doctorName,doctorExpertise;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_appointments);

        user_detailsList = new ArrayList<>();
        doctor_detailsList = new ArrayList<>();
        appointmentsList = new ArrayList<>();
        recyclerView = findViewById(R.id.myAppointmentRecyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Toolbar toolbar = findViewById(R.id.myAppointmentsToolbar);
        toolbar.setTitle("My Appointments");
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.black));

        setSupportActionBar(toolbar);

        //4-Making the home button in toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_arrow);

        sharedPreferences = getSharedPreferences("credentials", MODE_PRIVATE);
        String patientName = sharedPreferences.getString("nameOfUser","");
        String userPatientId = sharedPreferences.getString("userId","");

        url = "http://"+ipAddress.ip+":8000/api/myAppointments/"+userPatientId; //TODO get the intent of user_detail ID

        getAppointmentList();

    }

    private void getAppointmentList()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                user_detailsList.clear();
                doctor_detailsList.clear();
                appointmentsList.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArrayAppointment = jsonObject.getJSONArray("appointment");

                    JSONArray jsonArray = jsonObject.getJSONArray("doctor");

                    for (int i=0; i <jsonArray.length();i++)
                    {
                        JSONArray ja = jsonArray.getJSONArray(i);
                        JSONObject jo = jsonArrayAppointment.getJSONObject(i);
                        doctor_id =jo.getString("doctor_id");
                        System.out.println(doctor_id);
                        appointmentDate = jo.getString("date");
                        System.out.println(appointmentDate);
                        appointmentTime = jo.getString("time");
                        System.out.println(appointmentTime);
                        appointmentType = jo.getString("type");
                        System.out.println(appointmentType);

                        for (int j=0; j<ja.length();j++)
                        {

                            JSONObject jb = ja.getJSONObject(j);
                        docProfilePic = jb.getString("profile_pic");
                            System.out.println(docProfilePic);
                        doctorName = jb.getString("name");
                            System.out.println(doctorName);
                            doctorExpertise = jb.getString("expertise");
                            System.out.println(doctorExpertise);
                            String doctorFees = jb.getString("fee");;

                            User_Details user_details = new User_Details(doctorName,docProfilePic);
                            Doctor_Details doctor_details = new Doctor_Details(doctor_id, doctorExpertise,doctorFees);
                            Appointments appointments = new Appointments(appointmentDate,appointmentTime,appointmentType,doctor_id);
                            user_detailsList.add(user_details);
                            doctor_detailsList.add(doctor_details);
                            appointmentsList.add(appointments);
                            recyclerView.setAdapter(new PatientAppointmentsListAdapter(PatientAppointments.this, appointmentsList, user_detailsList,doctor_detailsList));

                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PatientAppointments.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
    }
