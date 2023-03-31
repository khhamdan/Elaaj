package com.hamdan.elaajmedicalapp;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hamdan.elaajmedicalapp.adapter.PatientPreviousAppointmentsAdapter;
import com.hamdan.elaajmedicalapp.models.Appointments;
import com.hamdan.elaajmedicalapp.models.Doctor_Details;
import com.hamdan.elaajmedicalapp.models.IpAddress;
import com.hamdan.elaajmedicalapp.models.User_Details;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PatientPreviousAppointmentsFragments extends Fragment {


    IpAddress ipAddress;
    String url="http://"+ipAddress.ip+":8000/api/showPatientPreviousAppointments";
    User_Details user_details;
    Doctor_Details doctor_details;
    Appointments appointments;
    private List<User_Details> user_detailsList;
    private List<Doctor_Details> doctor_detailsList;
    private List<Appointments> appointmentsList;
    SharedPreferences sharedPreferences;
    private RecyclerView recyclerView;
    String doctor_id, appointmentDate, appointmentTime, appointmentType, docProfilePic, doctorName, doctorExpertise;
    String userPatientId;
    SwipeRefreshLayout swipeRefreshLayout;
    PatientPreviousAppointmentsAdapter patientPreviousAppointmentsAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragments
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.patient_previous_appointments_fragments, container, false);

        user_detailsList = new ArrayList<>();
        doctor_detailsList = new ArrayList<>();
        appointmentsList = new ArrayList<>();
        recyclerView = root.findViewById(R.id.previousAppointmentRecyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));



        sharedPreferences = getActivity().getSharedPreferences("credentials", getActivity().MODE_PRIVATE);
        String patientName = sharedPreferences.getString("nameOfUser", "");
        userPatientId = sharedPreferences.getString("userId", "");

        swipeRefreshLayout = root.findViewById(R.id.swipeRefreshPreviousAppointments);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAppointmentList();
                patientPreviousAppointmentsAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        getAppointmentList();

        return root;
    }

    private void getAppointmentList() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                user_detailsList.clear();
                doctor_detailsList.clear();
                appointmentsList.clear();
                try {
                    JSONArray appointmentsDetails = new JSONArray(response);
                    for (int i = 0; i < appointmentsDetails.length(); i++) {
                        JSONObject object = appointmentsDetails.getJSONObject(i);

                        String doctorName = object.getString("name");
                        String doctorProfilePic = object.getString("profile_pic");
                        String appointmentDate = object.getString("date");
                        String appointmentTime = object.getString("time");
                        String appointmentType = object.getString("type");
                        String doctorFees = object.getString("fee");
                        String doctorExpertise = object.getString("expertise");
                        String user_details_id_of_doctor = object.getString("appointment_doctor_id");
                        String doctor_details_id = object.getString("doctor_details_id");


                        User_Details user_details = new User_Details(doctorName, doctorProfilePic);
                        Doctor_Details doctor_details = new Doctor_Details(doctor_details_id, doctorExpertise, doctorFees);
                        Appointments appointments = new Appointments(appointmentDate, appointmentTime, appointmentType, user_details_id_of_doctor);
                        user_detailsList.add(user_details);
                        doctor_detailsList.add(doctor_details);
                        appointmentsList.add(appointments);
                        patientPreviousAppointmentsAdapter = new PatientPreviousAppointmentsAdapter(getActivity(), appointmentsList, user_detailsList, doctor_detailsList);
                        recyclerView.setAdapter(patientPreviousAppointmentsAdapter);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("patient_id", userPatientId);
                return data;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

    }
}