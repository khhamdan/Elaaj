package com.hamdan.elaajmedicalapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hamdan.elaajmedicalapp.models.IpAddress;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorProfile extends AppCompatActivity {

    String url;
    SharedPreferences sharedPreferences;
    TextView tv_name, tName, dob, email, phoneNo, expertise, experience;
    CircleImageView profilePic;
    AppCompatButton changePassword;
    IpAddress ipAddress;
    ImageView editBtn;
    String userDoctorId,jProfile_pic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_profile);

        Toolbar toolbar = findViewById(R.id.doctorProfile_toolbar);
        toolbar.setTitle("Doctor's Profile");
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.black));

        setSupportActionBar(toolbar);

        //4-Making the home button in toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_arrow);

        sharedPreferences = getSharedPreferences("credentials", MODE_PRIVATE);
//        String patientName = sharedPreferences.getString("nameOfUser","");
         userDoctorId = sharedPreferences.getString("userId", "");

        tv_name = findViewById(R.id.tv_name);
        tName = findViewById(R.id.tName);
        profilePic = findViewById(R.id.profilePic);
        email = findViewById(R.id.email);
        phoneNo = findViewById(R.id.phoneNo);
        dob = findViewById(R.id.dob);
        expertise = findViewById(R.id.expertise);
        experience = findViewById(R.id.experience);
        editBtn = findViewById(R.id.editBtn);
        changePassword = findViewById(R.id.changePassword);

        url = "http://" + ipAddress.ip + ":8000/api/retrieveDoctorData";

        getMyProfile(userDoctorId);

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String uName = tName.getText().toString().trim();
                String uEmail = email.getText().toString().trim();
                String uPhoneNo = phoneNo.getText().toString().trim();
                String uDob = dob.getText().toString().trim();
                String uExpertise = expertise.getText().toString().trim();
                String uExperience = experience.getText().toString().trim();

                Intent intent = new Intent(DoctorProfile.this,DoctorEditProfile.class);
                intent.putExtra("name",uName);
                intent.putExtra("email",uEmail);
                intent.putExtra("phone",uPhoneNo);
                intent.putExtra("profile-pic",jProfile_pic);
                intent.putExtra("dob",uDob);
                intent.putExtra("expertise",uExpertise);
                intent.putExtra("experience",uExperience);
                startActivity(intent);
            }
        });



        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DoctorProfile.this,DoctorChangePassword.class);
                startActivity(intent);

            }
        });
    }

    private void getMyProfile(String userDoctorId) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray patientDetail = new JSONArray(response);
                    for (int i = 0; i < patientDetail.length(); i++) {
                        JSONObject object = patientDetail.getJSONObject(i);

                        String name = object.getString("name");
                        tv_name.setText(name);
                        tName.setText(name);
                        jProfile_pic = object.getString("profile_pic");
                        Picasso.get().load("http://" + ipAddress.ip + ":8000/profiles/users/" + jProfile_pic).into(profilePic);

                        String jEmail = object.getString("email");
                        email.setText(jEmail);
                        String contact_no = object.getString("contact_no");
                        phoneNo.setText(contact_no);
                        String DOB = object.getString("DOB");
                        dob.setText(DOB);
                        String jExpertise = object.getString("expertise");
                        expertise.setText(jExpertise);
                        String jExperience = object.getString("experience");
                        experience.setText(jExperience);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DoctorProfile.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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