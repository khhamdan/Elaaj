package com.hamdan.elaajmedicalapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.barteksc.pdfviewer.PDFView;
import com.hamdan.elaajmedicalapp.adapter.DoctorReceivePatientFilesAdapter;
import com.hamdan.elaajmedicalapp.models.DoctorReceivePrescriptionOfPatient;
import com.hamdan.elaajmedicalapp.models.IpAddress;
import com.hamdan.elaajmedicalapp.models.Symptoms;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DoctorReceivePatientFiles extends AppCompatActivity /*implements DocumentClickHandler*/ {

    IpAddress ipAddress;
    String ip = "192.168.56.1";
    String url = "http://" + ipAddress.ip + ":8000/pdfs/Prescription/1662490587.pdf";
    String url2 = "http://" + ipAddress.ip + ":8000/api/patientPrescriptions";
    WebView webView;
    PDFView pdfView;
    DoctorReceivePatientFilesAdapter doctorReceivePatientFilesAdapter;
    private List<DoctorReceivePrescriptionOfPatient> doctorPatientAppointment;
    private List<Symptoms> symptomsList;
    RecyclerView recyclerView;
    String userPatientId;


//    Activity activity;
//    private ProgressDialog progDailog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_appointment_detail);


        Toolbar toolbar = findViewById(R.id.doctorPatientAppointmentDetailsToolbar);
        toolbar.setTitle("Prescriptions From Patient");
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.black));

        setSupportActionBar(toolbar);

        //4-Making the home button in toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_arrow);
        doctorPatientAppointment = new ArrayList<>();
        symptomsList = new ArrayList<>();

//        activity = this;
//        Intent intent = new Intent(Intent.ACTION_VIEW)
//        intent.setDataAndType(Uri.parse("http://192.168.56.1:8000/pdfs/Prescription/1662623946.pdf"),"application/pdf");
//        startActivity(intent);

        Intent intent = getIntent();
        userPatientId = intent.getStringExtra("patient_id");
        System.out.println(userPatientId);

        getPatientPrescriptions(userPatientId);

//    }
        recyclerView = findViewById(R.id.recyclerViewOfDoctorDetails);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        //        doctorPatientAppointmentDetailList = new ArrayList<>();
//        pdfView = findViewById(R.id.pdf_view);


//        webView = findViewById(R.id.web_view);
//        webView.setWebViewClient(new WebViewClient());
//        webView.getSettings().setSupportZoom(true);
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.getSettings().setSaveFormData(true);
//        webView.loadUrl();
        //        webView.loadUrl(url);
    }

    private void getPatientPrescriptions(String userPatientId) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                doctorPatientAppointment.clear();
                symptomsList.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray pastHistory = jsonObject.getJSONArray("pastHistory");
                    JSONArray symptoms = jsonObject.getJSONArray("symptoms");

                    for (int i = 0; i < symptoms.length(); i++) {
                        JSONObject symptomsObject = symptoms.getJSONObject(i);
                        JSONObject pastHistoryObjects = pastHistory.getJSONObject(i);

                        String symptomsOfPatient = symptomsObject.getString("symptoms");
                        String noOfDays = symptomsObject.getString("noOfDays");
                        String current_medications = pastHistoryObjects.getString("current_medications");
                        String recent_prescription = pastHistoryObjects.getString("recent_prescription");
                        String recent_lab_test = pastHistoryObjects.getString("recent_lab_test");
                        String created_at = pastHistoryObjects.getString("created_At");

                        Symptoms symptoms1 = new Symptoms(symptomsOfPatient,noOfDays);
                        DoctorReceivePrescriptionOfPatient doctorPrescription = new DoctorReceivePrescriptionOfPatient(current_medications,recent_prescription, recent_lab_test,created_at);
                        doctorPatientAppointment.add(doctorPrescription);
                        symptomsList.add(symptoms1);
                        doctorReceivePatientFilesAdapter = new DoctorReceivePatientFilesAdapter(getApplicationContext(), doctorPatientAppointment,symptomsList);
                        recyclerView.setAdapter(doctorReceivePatientFilesAdapter);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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


//    @Override
//    public void onView(String Url) {
////        String url3 = "http://192.168.56.1:8000/pdfs/Prescription/1662490587.pdf";
//
//    }
//
//    @Override
//    public void onDownload(String Url) {
//
//    }



//   progDailog = ProgressDialog.show(activity, "Loading","Please wait...", true);
//        progDailog.setCancelable(false);
//        webView = (WebView) findViewById(R.id.web_view);
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.getSettings().setLoadWithOverviewMode(true);
//        webView.getSettings().setUseWideViewPort(true);
//        webView.setWebViewClient(new WebViewClient(){
//
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                progDailog.show();
//                view.loadUrl(url);
//
//                return true;
//            }
//            @Override
//            public void onPageFinished(WebView view, final String url) {
//                progDailog.dismiss();
//            }
//        });
//
//        webView.loadUrl( "http://192.168.56.1:8000/pdfs/Prescription/1662623946.pdf");
