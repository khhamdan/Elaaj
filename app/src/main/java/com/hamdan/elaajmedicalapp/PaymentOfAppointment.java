package com.hamdan.elaajmedicalapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
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
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PaymentOfAppointment extends AppCompatActivity {
    IpAddress ipAddress;
    SharedPreferences sharedPreferences;
    String sessionDoctorName, sessionDoctorExpertise, sessionDoctorExperience, sessionDoctorFees;
    TextView doctorName, doctorSpeciality, fees, doctorsExperience, appointmentTimeText, appointmentDateText, walletMoney;
    AppCompatButton paymentOfBookedAppointment, walletPayment, onlinePayment;
    String url = "http://" + ipAddress.ip + ":8000/api/getAppointmentDetails";
    String url2 = "http://" + ipAddress.ip + ":8000/api/appointmentPayment";
    String url3 = "http://" + ipAddress.ip + ":8000/api/patientWallet";
    String url4 = "http://" + ipAddress.ip + ":8000/api/patientWalletPayment";
    String url5 = "http://" + ipAddress.ip + ":8000/api/patientWalletAndStripePayment";

    String userPatientId;
    String doctor_id, appointmentDate, appointmentTime, appointmentId, amountFeesOfDoctor;
    int sumOfWallet, feesOfDoctor;
    String SECRET_KEY = "sk_test_51LJlXwJEXCfnBjA9FdqMHJyXbCgm3r2um32eOOefCTGuPAnRmMvG095yraSOjSuLInjdXvX0DfLWjzdDMX0ihlbi00EO1sgMbt";
    String PUBLISH_KEY = "pk_test_51LJlXwJEXCfnBjA9ngDz1oqyLZoXYRiJIcvzq0YMKffSLpBzF2OG1pITVXIzziIGFVe24RtKmPBqydY1qtxAFaQa00jsYZ449E";
    PaymentSheet paymentSheet;
    String customerID;
    String EphericalKey;
    String ClientSecret;
    LinearLayout linearLayout;
    String stringremainingamount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment);

//        paymentOfBookedAppointment = findViewById(R.id.paymentOfBookedAppointment);
        Toolbar toolbar = findViewById(R.id.paymentToolbar);
        toolbar.setTitle("Payment");
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.black));

        setSupportActionBar(toolbar);

        //4-Making the home button in toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_arrow);


        Intent intent = getIntent();
        appointmentId = intent.getStringExtra("appointment_id");
        appointmentTime = intent.getStringExtra("appointmentTime");
        appointmentDate = intent.getStringExtra("appointmentDate");


        sharedPreferences = getSharedPreferences("appointmentDoctorData", MODE_PRIVATE);
        sessionDoctorName = sharedPreferences.getString("doctorName", "");
        sessionDoctorExpertise = sharedPreferences.getString("doctorExpertise", "");
        sessionDoctorExperience = sharedPreferences.getString("doctorExperience", "");
        sessionDoctorFees = sharedPreferences.getString("doctorFee", "");

        feesOfDoctor = Integer.parseInt(sessionDoctorFees);
        sharedPreferences = getSharedPreferences("credentials", MODE_PRIVATE);
//        String patientName = sharedPreferences.getString("nameOfUser","");
        userPatientId = sharedPreferences.getString("userId", "");
        walletMoney = findViewById(R.id.walletMoney);
        doctorName = (TextView) findViewById(R.id.doctorName);
        doctorSpeciality = (TextView) findViewById(R.id.doctorSpeciality);
        appointmentTimeText = findViewById(R.id.appointmentTime);
        doctorsExperience = findViewById(R.id.doctorsExperience);
        fees = findViewById(R.id.fees);
        appointmentDateText = findViewById(R.id.appointmentDate);
        paymentOfBookedAppointment = findViewById(R.id.paymentOfBookedAppointment);
//        walletPayment = findViewById(R.id.walletPayment);
//        onlinePayment = findViewById(R.id.onlinePayment);

        getPatientWallet(userPatientId);
        doctorName.setText(sessionDoctorName);
        doctorSpeciality.setText(sessionDoctorExpertise);
        doctorsExperience.setText(sessionDoctorExperience);
        fees.setText(sessionDoctorFees);
        appointmentTimeText.setText(appointmentTime);
        appointmentDateText.setText(appointmentDate);
        System.out.println("Checked outside the function " + sumOfWallet);

//        appointmentTimeText.setText(appointmentDate);
//      Doctor Fees
        amountFeesOfDoctor = fees.getText().toString().trim();
//        linearLayout = findViewById(R.id.linearList);


        paymentOfBookedAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("sum is:" + sumOfWallet + "\n" + "fees is" + feesOfDoctor);
                if (sumOfWallet >= feesOfDoctor) {
                    patientWalletPayment();
                } else {
                    PaymentDone();
                    PaymentFlow();
                }
            }
        });


//        walletPayment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                linearLayout.removeAllViews();
//                View view1 = getLayoutInflater().inflate(R.layout.pay_with_wallet, null, false);
//                linearLayout.addView(view1);
//                walletPayment.setEnabled(false);
//                onlinePayment.setEnabled(true);
//                AppCompatButton button = (AppCompatButton) view1.findViewById(R.id.payWithWallet);
//                button.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        if (sumOfWallet >= feesOfDoctor) {
//                            patientWalletPayment();
//                            //todo
//                        } else {
//                            Toast.makeText(PaymentOfAppointment.this, "Your Wallet have less credit ", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//
//            }
//        });
//        onlinePayment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                linearLayout.removeAllViews();
//                View view1 = getLayoutInflater().inflate(R.layout.pay_with_stripe_wallet, null, false);
//                linearLayout.addView(view1);
//                AppCompatButton button = (AppCompatButton) view1.findViewById(R.id.payWithStripe);
//                onlinePayment.setEnabled(false);
//                button.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        if (sumOfWallet < feesOfDoctor){
//                            int remainingAmountToBePaid = Math.abs(feesOfDoctor-sumOfWallet);
//                            PaymentDone();
//                            PaymentFlow();
//                        }
//                    }
//                });
//                walletPayment.setEnabled(true);
//
//            }
//        });


        PaymentConfiguration.init(this, PUBLISH_KEY);
        paymentSheet = new PaymentSheet(this, paymentSheetResult -> {

            onPaymentResult(paymentSheetResult);
        });

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                " https://api.stripe.com/v1/customers",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject object = new JSONObject(response);
                            customerID = object.getString("id");
                            getEphericalKey(customerID);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + SECRET_KEY);
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(PaymentOfAppointment.this);
        requestQueue.add(stringRequest);


    }

    private void getPatientWallet(String userPatientId) { //todo not catching properly money

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url3, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray patientWallet = new JSONArray(response);
                    for (int i = 0; i < patientWallet.length(); i++) {
                        JSONObject object = patientWallet.getJSONObject(i);

                        String appointmentAmount = object.getString("amount");
                        int amountOfWallet = Integer.parseInt(appointmentAmount);
                        System.out.println("Wallet before filling" + sumOfWallet);
                        sumOfWallet = sumOfWallet + amountOfWallet;
                        System.out.println("What I get from wallet is :" + sumOfWallet);
                    }
                    System.out.println("And Total is : " + sumOfWallet);
                    walletMoney.setText(String.valueOf(sumOfWallet));

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

    private void patientWalletPayment() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url4, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(PaymentOfAppointment.this, response, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), PatientAppointment.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                startActivity(i);
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
                data.put("appointment_id", appointmentId);
                data.put("fee", sessionDoctorFees);
                data.put("amount", sessionDoctorFees);
                data.put("type", "Online");
                data.put("platform", "Wallet");
                return data;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void patientWalletAndStripePayment(int sumOfWallet) {

        String stringSumOfWallet = String.valueOf(sumOfWallet);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url5, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

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
                data.put("appointment_id", appointmentId);
                data.put("amount", stringSumOfWallet);
                return data;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

//    private void getAppointmentId() {
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                try {
//                    JSONArray appointmentids = new JSONArray(response);
//                    for (int i = 0; i < appointmentids.length(); i++) {
//                        JSONObject object = appointmentids.getJSONObject(i);
//
//                        appointmentId = object.getString("id");
//                        Toast.makeText(PaymentOfAppointment.this, appointmentId, Toast.LENGTH_SHORT).show();
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
//            }
//        }) {
//            @Nullable
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> data = new HashMap<>();
//                data.put("time", appointmentTime);
//                data.put("date", appointmentDate);
//                data.put("patient_id", userPatientId);
//                data.put("doctor_id", doctor_id);
//                return data;
//            }
//        };
//        RequestQueue requestQueue = Volley.newRequestQueue(PaymentOfAppointment.this);
//        requestQueue.add(stringRequest);
//    } a appoi


    private void onPaymentResult(PaymentSheetResult paymentSheetResult) {
        if (paymentSheetResult instanceof PaymentSheetResult.Completed) {

//            paymentDetails();
            Toast.makeText(getApplicationContext(), "payment success", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getApplicationContext(), PatientAppointment.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            startActivity(i);
        }
    }


    private void getEphericalKey(String customerID) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "https://api.stripe.com/v1/ephemeral_keys",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject object = new JSONObject(response);
                            EphericalKey = object.getString("id");
                            Toast.makeText(getApplicationContext(), EphericalKey, Toast.LENGTH_SHORT).show();

                            getClientSecret(customerID, EphericalKey);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + SECRET_KEY);
                headers.put("Stripe-Version", "2020-08-27");
                return headers;
            }

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("customer", customerID);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(PaymentOfAppointment.this);
        requestQueue.add(stringRequest);

    }

    private void getClientSecret(String customerID, String ephericalKey) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "https://api.stripe.com/v1/payment_intents",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject object = new JSONObject(response);
                            ClientSecret = object.getString("client_secret");
                            Toast.makeText(getApplicationContext(), ClientSecret, Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + SECRET_KEY);
                return headers;
            }

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("customer", customerID);
                int remainingAmountToBePaid = Math.abs(feesOfDoctor - sumOfWallet);
                if ((sumOfWallet > 0) && (sumOfWallet < feesOfDoctor)) {
                    patientWalletAndStripePayment(sumOfWallet);
                }
                stringremainingamount = String.valueOf(remainingAmountToBePaid * 100);
                System.out.println("stringremainingamount is:" + stringremainingamount + "\n" + "feesOfDoctor is" + feesOfDoctor + "\n" + "sumOfWalletis" + sumOfWallet);
                params.put("amount", stringremainingamount);
                params.put("currency", "pkr");
                params.put("automatic_payment_methods[enabled]", "true");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(PaymentOfAppointment.this);
        requestQueue.add(stringRequest);

    }

    private void PaymentDone() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(PaymentOfAppointment.this, response, Toast.LENGTH_SHORT).show();
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
                data.put("type", "Visa");
                data.put("platform", "Stripe");
                data.put("amount", amountFeesOfDoctor);
                data.put("status", "Paid");
                data.put("appointment_id", appointmentId);
                return data;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(PaymentOfAppointment.this);
        requestQueue.add(stringRequest);
    }


    private void PaymentFlow() {
        paymentSheet.presentWithPaymentIntent(
                ClientSecret, new PaymentSheet.Configuration("Elaaj",
                        new PaymentSheet.CustomerConfiguration(customerID, EphericalKey))
        );
    }
}