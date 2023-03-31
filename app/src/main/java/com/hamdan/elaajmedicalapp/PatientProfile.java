package com.hamdan.elaajmedicalapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hamdan.elaajmedicalapp.models.IpAddress;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class PatientProfile extends AppCompatActivity {



    String url;
    SharedPreferences sharedPreferences;
    TextView tName,dob,emergencyContact,maritalStatus,tOccupation,tReferedBy;
    String profile_pic;
    TextView tv_name,tEmail,tPhoneNo;
    CircleImageView profilePic;
    ImageView editBtn;
    private RequestQueue rQueue;
    AppCompatButton changePassword;
    IpAddress ipAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_profile);

        tv_name = findViewById(R.id.tv_name);
        tName = findViewById(R.id.tName);
        tEmail = findViewById(R.id.email);
        tPhoneNo = findViewById(R.id.phoneNo);
        dob = findViewById(R.id.dob);
        emergencyContact = findViewById(R.id.emergencyContact);
        maritalStatus = findViewById(R.id.maritalStatus);
        tOccupation = findViewById(R.id.occupation);
        tReferedBy = findViewById(R.id.referredBy);

        profilePic = findViewById(R.id.profilePic);
        changePassword = findViewById(R.id.changePassword);
        editBtn = findViewById(R.id.editBtn);

        Toolbar toolbar = findViewById(R.id.myProfile);
        toolbar.setTitle("My Profile");
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.black));

        setSupportActionBar(toolbar);

        //4-Making the home button in toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_arrow);


        sharedPreferences = getSharedPreferences("credentials", MODE_PRIVATE);
//        String patientName = sharedPreferences.getString("nameOfUser","");
        String userPatientId = sharedPreferences.getString("userId","");

        url = "http://"+ipAddress.ip+":8000/api/retrieveData/" + userPatientId;




        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PatientProfile.this,PatientChangePassword.class);
                startActivity(intent);
            }
        });

        getMyProfile();

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uName = tName.getText().toString().trim();
                String uEmail = tEmail.getText().toString().trim();
                String uPhoneNo = tPhoneNo.getText().toString().trim();
                String uDob = dob.getText().toString().trim();
                String uEmergencyContact = emergencyContact.getText().toString().trim();
                String uOccupation = tOccupation.getText().toString().trim();
                String uReferredBy = tReferedBy.getText().toString().trim();


                Intent intent = new Intent(PatientProfile.this,PatientEditProfile.class);
                intent.putExtra("name",uName);
                intent.putExtra("email",uEmail);
                intent.putExtra("profile-pic",profile_pic);
                intent.putExtra("dob",uDob);
                intent.putExtra("phone",uPhoneNo);
                intent.putExtra("emergency-Contact",uEmergencyContact);
                intent.putExtra("occupation",uOccupation);
                intent.putExtra("referredBy",uReferredBy);
                startActivity(intent);
            }
        });

    }




    private void getMyProfile() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray patientDetail = new JSONArray(response);
                    for (int i = 0; i < patientDetail.length(); i++) {
                        JSONObject object = patientDetail.getJSONObject(i);

                        String name = object.getString("name");
                        tv_name.setText(name);
                        tName.setText(name);
                        profile_pic = object.getString("profile_pic");

                        Picasso.get().load("http://"+ipAddress.ip+":8000/profiles/users/" + profile_pic).into(profilePic);

                        String email = object.getString("email");
                        tEmail.setText(email);
                        String contact_no = object.getString("contact_no");
                        tPhoneNo.setText(contact_no);
                        String DOB = object.getString("DOB");
                        dob.setText(DOB);
                        String emergency_contact = object.getString("emergency_contact");
                        emergencyContact.setText(emergency_contact);
                        String occupation = object.getString("occupation");
                        tOccupation.setText(occupation);
                        String referred_by = object.getString("referred_by");
                        tReferedBy.setText(referred_by);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PatientProfile.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


}