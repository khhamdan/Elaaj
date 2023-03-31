package com.hamdan.elaajmedicalapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hamdan.elaajmedicalapp.models.IpAddress;
import com.hamdan.elaajmedicalapp.videoCall.PatientVideoCall;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class PatientAppointmentDetails extends AppCompatActivity {


    IpAddress ipAddress;
    String sPatientName,sAppointmentDate,sAppointmentTime,doctor_id,appointmentFees,profile_pic,doctorSpeciality,sDoctorName;
    SharedPreferences sharedPreferences;
    CircleImageView doctorProfilePic;
    TextView doctorExpertise,doctorName,patientName,doctorFees,appointmentDate,appointmentTime;
    AppCompatButton videoCall_btn,my_prescription_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_appointment_details);


        Toolbar toolbar = findViewById(R.id.appointmentDetailsToolbar);
        toolbar.setTitle("Appointment Detail");
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.black));

        setSupportActionBar(toolbar);

        //4-Making the home button in toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_arrow);

        doctorProfilePic = findViewById(R.id.doctorProfilePic);
        patientName = findViewById(R.id.patientName);
        doctorName = findViewById(R.id.doctorName);
        doctorFees = findViewById(R.id.doctorFees);
        appointmentDate = findViewById(R.id.appointmentDate);
        appointmentTime = findViewById(R.id.appointmentTime);
        doctorExpertise = findViewById(R.id.doctorExpertise);
        videoCall_btn = findViewById(R.id.videoCall_btn);
        my_prescription_btn = findViewById(R.id.my_prescription_btn);


        sharedPreferences = getSharedPreferences("credentials", MODE_PRIVATE);
        sPatientName = sharedPreferences.getString("nameOfUser","");


        Intent intent = getIntent();
        doctor_id =  intent.getStringExtra("doctor_id");
        sAppointmentDate = intent.getStringExtra("appointmentDate");
        sAppointmentTime = intent.getStringExtra("appointmentTime");
        appointmentFees = intent.getStringExtra("appointmentFees");
        profile_pic = intent.getStringExtra("profile_pic");
        doctorSpeciality = intent.getStringExtra("doctorSpeciality");
        sDoctorName = intent.getStringExtra("doctorName");



        Picasso.get().load("http://"+ipAddress.ip+":8000/profiles/users/" + profile_pic).into(doctorProfilePic);
        patientName.setText(sPatientName);
        appointmentDate.setText(sAppointmentDate);
        appointmentTime.setText(sAppointmentTime);
        appointmentTime.setText(sAppointmentTime);
        doctorFees.setText(appointmentFees);
        doctorExpertise.setText(doctorSpeciality);
        doctorName.setText(sDoctorName);
        videoCall_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PatientVideoCall.class);
                startActivity(intent);
            }
        });
        my_prescription_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), PatientReceivePrescriptionInAppointments.class);
                intent.putExtra("appointmentTime",sAppointmentTime );
                intent.putExtra("appointmentDate", sAppointmentDate);
                intent.putExtra("doctor_id",doctor_id );
                startActivity(intent);

            }
        });

    }

//    private void getAppointmentDetails()
//    {
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                user_detailsList.clear();
//                doctor_detailsList.clear();
//                appointmentsList.clear();
//                try {
//                    JSONArray appointmentDetails = new JSONArray(response);
//                    for (int i = 0; i < appointmentDetails.length(); i++) {
//                        JSONObject object = appointmentDetails.getJSONObject(i);
//
//                        String docProfilePic = object.getString("profile_pic");
//                        String doctorName = object.getString("name");
//                        String docExpertise = object.getString("expertise");
//                        String docFee = object.getString("fee");
//
//                        User_Details user_details = new User_Details(docProfilePic, patientName,doctorName);
//                        Doctor_Details doctor_details = new Doctor_Details(doctor_id, docExpertise,docFee);
//                        Appointments appointments = new Appointments(appointmentDate,appointmentTime, doctor_id);
//                        user_detailsList.add(user_details);
//                        doctor_detailsList.add(doctor_details);
//                        appointmentsList.add(appointments);
//
//                        recyclerView.setAdapter(new PatientAppointmentDetailsAdapter(PatientAppointmentDetails.this, appointmentsList, user_detailsList,doctor_detailsList));
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(PatientAppointmentDetails.this, error.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        requestQueue.add(stringRequest);
//    }
}