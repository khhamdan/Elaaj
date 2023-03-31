package com.hamdan.elaajmedicalapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PatientSymptoms extends AppCompatActivity {

    String symptoms;
    TextView symptomsTexts;
    EditText noOfDays;
    ArrayList<String> symptomLists = new ArrayList<>();
    private List<String> list;
    String[] listOfSymptoms;
    boolean[] checkedItems;
    ArrayList<Integer> mUserLists = new ArrayList<>();
    IpAddress ipAddress;
    ArrayAdapter<String> symptomsAdapter;
    String doctor_id, appointmentDate, appointmentTime, appointmentId;
    SharedPreferences sharedPreferences;
    String userPatientId;
    String url = "http://" + ipAddress.ip + ":8000/api/getAppointmentDetails";
    String url2 = "http://" + ipAddress.ip + ":8000/api/symptomsOfExpertise";
    String url3 = "http://" + ipAddress.ip + ":8000/api/enterSymptoms";
    String doctorSpecialityText;
    AppCompatButton bookAppointmentButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_symptoms);

//Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_patientSymptoms);
        toolbar.setTitle("Patient Symptoms");
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.black));
        setSupportActionBar(toolbar);

//        //4-Making the home button in toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_arrow);
        noOfDays = findViewById(R.id.noOfDays);
        symptomsTexts = findViewById(R.id.symptomsText);
        bookAppointmentButton = findViewById(R.id.bookAppointmentButton);

        //Intent
        Intent intent = getIntent();
        doctor_id = intent.getStringExtra("doctor_id");
        appointmentDate = intent.getStringExtra("date");
        appointmentTime = intent.getStringExtra("time");

//      sharedPreferences
        sharedPreferences = getSharedPreferences("credentials", MODE_PRIVATE);
//        String patientName = sharedPreferences.getString("nameOfUser","");
        userPatientId = sharedPreferences.getString("userId", "");

        SharedPreferences sharedPreferences = getSharedPreferences("doctorData", MODE_PRIVATE);
        doctorSpecialityText = sharedPreferences.getString("doctorSpeciality", "");
//        Toast.makeText(this, doctorSpecialityText, Toast.LENGTH_SHORT).show();

        list = new ArrayList<>();


        getAppointmentId();
        getSymptomsOfExpertise();

        bookAppointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postPatientSymptoms();
            }
        });
    }


    public void postPatientSymptoms() {
        String uSymptoms = symptomsTexts.getText().toString().trim();
        String uNoOfDays = noOfDays.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url3, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(PatientSymptoms.this, response, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), PaymentOfAppointment.class);
                intent.putExtra("appointment_id",appointmentId);
                intent.putExtra("appointmentTime",appointmentTime);
                intent.putExtra("appointmentDate",appointmentDate);
                startActivity(intent);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("symptoms", uSymptoms);
                data.put("numberOfDays", uNoOfDays);
                data.put("appointment_id", appointmentId);
                return data;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(PatientSymptoms.this);
        requestQueue.add(stringRequest);
    }


    public void getSymptomsOfExpertise() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray appointmentDetails = new JSONArray(response);
                    for (int i = 0; i < appointmentDetails.length(); i++) {
                        JSONObject object = appointmentDetails.getJSONObject(i);

                        symptoms = object.getString("symptoms");

                        listOfSymptoms = symptoms.split(",");

//                        for(int j=0;j< listOfSymptoms.length;j++)
//                        {
//                            symptomLists.add(listOfSymptoms[j]);
//                        }
                        checkedItems = new boolean[listOfSymptoms.length];


//                            symptomsAdapter =new ArrayAdapter<>(PatientSymptoms .this,android.R.layout.simple_spinner_item,symptomLists);
//                        symptomsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
//                        symptomsSpinner.setAdapter(symptomsAdapter);
                    }
                    symptomsTexts.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(PatientSymptoms.this);
                            builder.setTitle("Select Symptoms"); //
                            builder.setCancelable(false);
                            builder.setMultiChoiceItems(listOfSymptoms, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                                    if (isChecked) {
                                        mUserLists.add(position);
                                        Collections.sort(mUserLists);
                                    } else {
                                        mUserLists.remove(Integer.valueOf(position));
                                    }
                                }
                            });
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    StringBuilder stringBuilder = new StringBuilder();
                                    for (int j = 0; j < mUserLists.size(); j++) {
                                        stringBuilder.append(listOfSymptoms[mUserLists.get(j)]);

                                        if (j != mUserLists.size() - 1) {
                                            stringBuilder.append(",");
                                        }
                                    }
                                    symptomsTexts.setText(stringBuilder.toString());
                                }
                            });
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // dismiss dialog
                                    dialogInterface.dismiss();
                                }
                            });
                            builder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // use for loop
                                    for (int j = 0; j < checkedItems.length; j++) {
                                        // remove all selection
                                        checkedItems[j] = false;
                                        // clear language list
                                        mUserLists.clear();
                                        // clear text view value
                                        symptomsTexts.setText("");
                                    }
                                }
                            });
                            // show dialog
                            builder.show();

                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PatientSymptoms.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();

                data.put("expertise", doctorSpecialityText);
                return data;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    private void getAppointmentId() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray appointmentids = new JSONArray(response);
                    for (int i = 0

                         ; i < appointmentids.length(); i++) {
                        JSONObject object = appointmentids.getJSONObject(i);

                        appointmentId = object.getString("id");
//                        Toast.makeText(PatientSymptoms.this, appointmentId, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("time", appointmentTime);
                data.put("date", appointmentDate);
                data.put("patient_id", userPatientId);
                data.put("doctor_id", doctor_id);
                return data;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(PatientSymptoms.this);
        requestQueue.add(stringRequest);
    }

}


