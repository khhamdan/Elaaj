package com.hamdan.elaajmedicalapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
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

import java.util.HashMap;
import java.util.Map;

public class PatientGiveReviewsToDoctor extends AppCompatActivity {
    String appointment_id;
    EditText message;
    RatingBar ratingBar;
    TextView txtRating;
    String ratingsOfPatient,comments;
    IpAddress ipAddress;
    private String url;
    AppCompatButton rateNow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_review_of_doctor);

        url = "http://"+ipAddress.ip+":8000/api/giveRatingsToDoctor";

        Intent intent = getIntent();
        appointment_id = intent.getStringExtra("appointment_id");
        System.out.println("app_id is:"+appointment_id);

        ratingBar = findViewById(R.id.ratingBar);
        message = (EditText) findViewById(R.id.message);
        rateNow = findViewById(R.id.rate_Btn);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean b) {
                ratingsOfPatient = String.valueOf(rating);
                System.out.println("ratings are:"+ratingsOfPatient);
            }
        });
        rateNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postRatingOfDoctor(ratingsOfPatient,appointment_id);
            }
        });

    }

    private void postRatingOfDoctor(String ratingsOfPatient,String appointment_id)
    {
        String comments = message.getText().toString();
        System.out.println("comments are :"+comments);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(PatientGiveReviewsToDoctor.this, response, Toast.LENGTH_SHORT).show();
                Intent intent  = new Intent(PatientGiveReviewsToDoctor.this,PatientHomePage.class);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PatientGiveReviewsToDoctor.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("rating", ratingsOfPatient);
                data.put("comments", comments);
                data.put("appointment_id", appointment_id);
                return data;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


    }

