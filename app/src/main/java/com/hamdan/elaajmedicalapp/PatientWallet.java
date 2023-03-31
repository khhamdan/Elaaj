package com.hamdan.elaajmedicalapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hamdan.elaajmedicalapp.adapter.PatientWalletAdapter;
import com.hamdan.elaajmedicalapp.models.IpAddress;
import com.hamdan.elaajmedicalapp.models.PatientWalletModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PatientWallet extends AppCompatActivity {

    IpAddress ipAddress;
    private RecyclerView recyclerView;
    String url = "http://"+ipAddress.ip+":8000/api/patientWallet";

    private List<PatientWalletModel> patientWalletModelList, patientWalletModelList2;
    String doctorName;
    String userPatientId;
    TextView money;
    int sumOfWallet;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_wallet);


        Toolbar toolbar = findViewById(R.id.toolbar_patient_wallet);
        toolbar.setTitle("Patient Wallet");
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.black));

        setSupportActionBar(toolbar);

        //4-Making the home button in toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_arrow);

        recyclerView = findViewById(R.id.patientWalletRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        money = findViewById(R.id.money);

        patientWalletModelList = new ArrayList<>();
        patientWalletModelList2 = new ArrayList<>();

        sharedPreferences = getSharedPreferences("credentials", MODE_PRIVATE);
        String patientName = sharedPreferences.getString("nameOfUser","");
        userPatientId = sharedPreferences.getString("userId","");

        getPatientWallet();
    }

    private void getPatientWallet() {
        patientWalletModelList.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray patientWallet = new JSONArray(response);
                    for (int i = 0; i < patientWallet.length(); i++) {
                        JSONObject object = patientWallet.getJSONObject(i);

                        String appointmentAmount =object.getString("amount");
                        int amountOfWallet = Integer.parseInt(appointmentAmount);
                        sumOfWallet = sumOfWallet + amountOfWallet;
                        System.out.println(sumOfWallet);
                        String appointmentDate = object.getString("date");
                        String appointmentTime = object.getString("time");
                        String appointmentStatus = object.getString("status");
                        String doctor_id = object.getString("doctor_id");
                        String name = object.getString("name");
                        //Another function is called inside get name

                        PatientWalletModel patientWalletModel = new PatientWalletModel(appointmentDate, appointmentTime, appointmentAmount, appointmentStatus, name);
                        patientWalletModelList.add(patientWalletModel);
                        recyclerView.setAdapter(new PatientWalletAdapter(PatientWallet.this, patientWalletModelList));
                    }
                    System.out.println(sumOfWallet);
                    money.setText(String.valueOf(sumOfWallet));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(PatientAppointmentDetails.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}