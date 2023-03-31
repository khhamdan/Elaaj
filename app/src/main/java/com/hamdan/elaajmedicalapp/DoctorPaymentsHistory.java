package com.hamdan.elaajmedicalapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hamdan.elaajmedicalapp.adapter.DoctorPaymentHistoryAdapter;
import com.hamdan.elaajmedicalapp.adapter.DoctorPreviousAppointmentsAdapter;
import com.hamdan.elaajmedicalapp.models.Appointments;
import com.hamdan.elaajmedicalapp.models.DoctorPayments;
import com.hamdan.elaajmedicalapp.models.IpAddress;
import com.hamdan.elaajmedicalapp.models.Payments;
import com.hamdan.elaajmedicalapp.models.User_Details;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DoctorPaymentsHistory extends AppCompatActivity {

    DoctorPaymentHistoryAdapter doctorPaymentHistoryAdapter;
    RecyclerView recyclerView;
    SharedPreferences sharedPreferences;
    String userDoctorId;
    IpAddress ipAddress;
    String url;
    TextView money;
    int totalCredit= 0;
    private List<DoctorPayments> doctorPaymentsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_payments_history);

        Toolbar toolbar = findViewById(R.id.payment_history_toolbar);
        toolbar.setTitle("Payment History");
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.black));
        setSupportActionBar(toolbar);
//
//        //4-Making the home button in toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_arrow);

        money = findViewById(R.id.money);
        recyclerView = findViewById(R.id.doctorPaymentRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        doctorPaymentsList = new ArrayList<>();
        sharedPreferences = getSharedPreferences("credentials", MODE_PRIVATE);
        String doctorsName = sharedPreferences.getString("nameOfUser","");
        userDoctorId = sharedPreferences.getString("userId","");

        url = "http://"+ipAddress.ip+":8000/api/showDoctorPayment";

        showDoctorPayment(userDoctorId);


    }

    private void showDoctorPayment(String userDoctorId) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                doctorPaymentsList.clear();
                try {
                    JSONArray appointmentsDetails = new JSONArray(response);
                    for (int i = 0; i < appointmentsDetails.length(); i++) {
                        JSONObject object = appointmentsDetails.getJSONObject(i);

                        String date = object.getString("created_at");
                        String amountReceived = object.getString("amount");
                        int amount = Integer.parseInt(amountReceived);

                        totalCredit = totalCredit + amount;

                        DoctorPayments doctorPayments = new DoctorPayments(date,amountReceived);
                        doctorPaymentsList.add(doctorPayments);
                        doctorPaymentHistoryAdapter = new DoctorPaymentHistoryAdapter(DoctorPaymentsHistory.this,doctorPaymentsList);
                        recyclerView.setAdapter(doctorPaymentHistoryAdapter);
                    }
                    money.setText(String.valueOf(totalCredit));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
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