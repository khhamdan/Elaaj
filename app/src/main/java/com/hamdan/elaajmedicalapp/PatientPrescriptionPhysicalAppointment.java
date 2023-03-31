package com.hamdan.elaajmedicalapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.hamdan.elaajmedicalapp.models.IpAddress;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PatientPrescriptionPhysicalAppointment extends AppCompatActivity {

    IpAddress ipAddress;
    ImageView pdfImage;
    private Button btn, btn2, btn3;
    private TextView tv;
    private String upload_URL = "http://" + ipAddress.ip + ":8000/api/uploadPdf";
    private RequestQueue rQueue;
    private ArrayList<HashMap<String, String>> arraylist;
    AppCompatButton send;
    TextView pdf1,pdf2,skipBtn;
    EditText current_medications;
    String url = "https://www.google.com";
    //    PDFView pdfView, pdfView2, pdfView3;
    Uri uri, uri2;
//    Uri uri= Uri.parse("content://com.android.providers.downloads.documents/document/raw%3A%2Fstorage%2Femulated%2F0%2FDownload%2Fblank1.png")

    String displayName, displayName2;
    SharedPreferences sharedPreferences;
    String userPatientId, doctor_id, doctorName, doctorExpertise, doctorProfilePic, doctorFees, doctorExperience;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_prescription_physical_appointment);


        //1- Toolbar:
        Toolbar toolbar = findViewById(R.id.toolbar_patient_prescriptions);
        toolbar.setTitle("Patient Prescriptions");
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.black));
        setSupportActionBar(toolbar);


        //4-Making the home button in toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_arrow);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        current_medications = findViewById(R.id.current_medications);
        pdf1 = findViewById(R.id.pdf1);
        pdf2 = findViewById(R.id.pdf2);
        skipBtn = findViewById(R.id.skipBtn);

//        pdfView = findViewById(R.id.pdf);
//        pdfView2 = findViewById(R.id.pdf2);
//        pdfView3 = findViewById(R.id.pdf3);
        send = findViewById(R.id.send);

        sharedPreferences = getSharedPreferences("credentials", MODE_PRIVATE);
        userPatientId = sharedPreferences.getString("userId", "");

        //Intent Doctor Details
        Intent intent = getIntent();
        doctor_id = intent.getStringExtra("doctor_id");
        doctorName = intent.getStringExtra("doctorName");
        doctorExpertise = intent.getStringExtra("doctorExpertise");
        doctorProfilePic = intent.getStringExtra("doctorProfilePicture");
        doctorFees = intent.getStringExtra("docFees");
        doctorExperience = intent.getStringExtra("doctorExperience");


        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] mimeTypes = {"image/*", "application/pdf"};

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
                    if (mimeTypes.length > 0) {
                        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                    }
                } else {
                    String mimeTypesStr = "";

                    for (String mimeType : mimeTypes) {
                        mimeTypesStr += mimeType + "|";
                    }

                    intent.setType(mimeTypesStr.substring(0, mimeTypesStr.length() - 1));
                }
                startActivityForResult(intent, 1);

            }

        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                intent.setType("application/pdf");

                String[] mimeTypes = {"image/*", "application/pdf"};

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
                    if (mimeTypes.length > 0) {
                        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                    }
                } else {
                    String mimeTypesStr = "";

                    for (String mimeType : mimeTypes) {
                        mimeTypesStr += mimeType + "|";
                    }

                    intent.setType(mimeTypesStr.substring(0, mimeTypesStr.length() - 1));
                }
                startActivityForResult(intent, 2);
            }
        });

        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent skipIntent = new Intent(PatientPrescriptionPhysicalAppointment.this,BookAppointment.class);
                skipIntent.putExtra("doctor_id", doctor_id);
                skipIntent.putExtra("doctorName", doctorName);
                skipIntent.putExtra("doctorExpertise", doctorExpertise);
                skipIntent.putExtra("doctorProfilePicture", doctorProfilePic);
                skipIntent.putExtra("doctorExperience", doctorExperience);
                skipIntent.putExtra("docFees", doctorFees);
                skipIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(skipIntent);


            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadPDF();

            }
        });
    }

    @SuppressLint("Range")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            // Get the Uri of the selected file

            uri = data.getData();
            String uriString = uri.toString();
            System.out.println("what in the uri:" + uri);
            System.out.println(uriString);
            System.out.println("what in the uriString:" + uri);
//            pdfView.fromUri(uri).load();

//            DocumentMimtype documentMimtype = new DocumentMimtype();
//            documentMimtype.uri = uriString;
//
//            documentMimtype.databasePrescriptionName = "recent_prescription";

            File myFile = new File(uriString);
            System.out.println("what in the my File:" + myFile);
            String path = myFile.getAbsolutePath();
            System.out.println("what in the path:" + path);
            displayName = null;
            Uri path2 = Uri.fromFile(myFile);

            if (uriString.startsWith("content://")) {
                Cursor cursor = null;
                try {
                    cursor = this.getContentResolver().query(uri, null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        System.out.println("whats in cursor" + cursor);
                        displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                        Log.d("nameeeee>>>>  ", displayName);
                        pdf1.setText(displayName + " has been selected");

                    }
                } finally {
                    cursor.close();
                }
            } else if (uriString.startsWith("file://")) {
                displayName = myFile.getName();
                Log.d("nameeeee>>>>  ", displayName);
            }
//            documentMimetypeList.add(documentMimtype);


        } else if (requestCode == 2) {
            // Get the Uri of the selected file


            uri2 = data.getData();
            String uriString = uri2.toString();
//            pdfView2.fromUri(uri2).load();

//
//            DocumentMimtype documentMimtype = new DocumentMimtype();
//            documentMimtype.uri = uriString;
//            documentMimtype.databasePrescriptionName = "recent_prescription";


            File myFile = new File(uriString);
            String path = myFile.getAbsolutePath();
            displayName2 = null;
            Uri path2 = Uri.fromFile(myFile);


            if (uriString.startsWith("content://")) {
                Cursor cursor = null;
                try {
                    cursor = this.getContentResolver().query(uri2, null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        displayName2 = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                        Log.d("nameeeee>>>>  ", displayName2);
                        pdf2.setText(displayName2 + " has been selected");

                    }
                } finally {
                    cursor.close();
                }
            } else if (uriString.startsWith("file://")) {
                displayName2 = myFile.getName();
                Log.d("nameeeee>>>>  ", displayName2);
            }
//            documentMimetypeList.add(documentMimtype);


            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    private void uploadPDF() {
//
//        for (DocumentMimtype documentMimtype : documentMimetypeList) {

        String uCurrent_medications = current_medications.getText().toString().trim();

        InputStream iStream = null;
        InputStream iStream1 = null;

        try {

            iStream = getContentResolver().openInputStream(uri);
            final byte[] inputData = getBytes(iStream);


            System.out.println("Inside istream :"+iStream);
            System.out.println("Inside uri :"+uri);
            System.out.println("Inside InputData:"+inputData);
            System.out.println("userPatientId:"+userPatientId);


            iStream = getContentResolver().openInputStream(uri2);
            final byte[] inputData1 = getBytes(iStream);


            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, upload_URL,
                    new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {


                            Intent intent = new Intent(getApplicationContext(), BookPhysicalAppointment.class);
                            intent.putExtra("doctor_id", doctor_id);
                            intent.putExtra("doctorName", doctorName);
                            intent.putExtra("doctorExpertise", doctorExpertise);
                            intent.putExtra("doctorProfilePicture", doctorProfilePic);
                            intent.putExtra("doctorExperience", doctorExperience);
                            intent.putExtra("docFees", doctorFees);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
//                                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }) {

                /*
                 * If you want to add more parameters with the image
                 * you can do it here
                 * here we have only one parameter with the image
                 * which is tags
                 * */
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("patient_id", userPatientId);
                    params.put("current_medications", uCurrent_medications);
                    return checkParams(params);
                }

                /*
                 *pass files using below method
                 * */
                @Override
                protected Map<String, DataPart> getByteData()
                {
                    Map<String, DataPart> params = new HashMap<>();
                    params.put("recent_prescription", new DataPart(displayName, inputData));
                    params.put("recent_lab_test", new DataPart(displayName2, inputData1));
                    return params;
                }

                private Map<String, String> checkParams(Map<String, String> map) {
                    Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry<String, String> pairs = (Map.Entry<String, String>) it.next();
                        if (pairs.getValue() == null) {
                            map.put(pairs.getKey(), "");
                        }
                    }
                    return map;
                }
            };

            iStream.close();

            volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            rQueue = Volley.newRequestQueue(PatientPrescriptionPhysicalAppointment.this);
            rQueue.add(volleyMultipartRequest);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }
}