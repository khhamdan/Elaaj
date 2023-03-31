package com.hamdan.elaajmedicalapp;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.hamdan.elaajmedicalapp.adapter.TimeAdapter;
import com.hamdan.elaajmedicalapp.models.Appointments;
import com.hamdan.elaajmedicalapp.models.ColorOfTime;
import com.hamdan.elaajmedicalapp.models.IpAddress;
import com.hamdan.elaajmedicalapp.models.TimeModel;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

import de.hdodenhof.circleimageview.CircleImageView;

public class BookAppointment extends AppCompatActivity {
    IpAddress ipAddress;

    String doctor_id, doctorName, doctorExpertise, doctorProfilePic, doctorFees, docExperience;
    private List<Appointments> appointmentsList;
    CircleImageView doctorProfilePicText;
    TextView doctorNameText, doctorExpertiseText, doctorFeesText, doctorsExperienceText;
    List<String> time;
    String appointmentDate, appointmentTime;
    RadioGroup radioGroup;
    RadioButton radioButton;
    String selectedRadioButton;
    AppCompatButton bookAppointmentButton, dateButton;
    SharedPreferences sharedPreferences;
    String userPatientId;
    String date;
    DatePickerDialog.OnDateSetListener setListener;
    String url = "http://" + ipAddress.ip + ":8000/api/bookAppointment";
    String url2 = "http://" + ipAddress.ip + ":8000/api/bookedSlots";
    //Time slots
    RecyclerView recyclerView;
    List<TimeModel> timeModelList;
    Context context;
    Boolean[] array;
    List<Boolean> al = new ArrayList<Boolean>();
    int totalTimeSlots;
    int counter = 0;
    String colorResource = "white";
    List<ColorOfTime> thirdList;

    String[] bookedAppointmentsDateArray, bookedAppointmentsTimeArray;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_appointment);

        sharedPreferences = getSharedPreferences("credentials", MODE_PRIVATE);
//        String patientName = sharedPreferences.getString("nameOfUser","");
        userPatientId = sharedPreferences.getString("userId", "");

        //Radio button
//        radioGroup = (RadioGroup) findViewById(R.id.radio_group);

        bookAppointmentButton = findViewById(R.id.bookAppointmentButton);

//        int radioSelectedId = radioGroup.getCheckedRadioButtonId();
//        radioButton = (RadioButton) findViewById(radioSelectedId);
//        selectedRadioButton = (String) radioButton.getText();
//        bookedTimeSlots();

        bookAppointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                int radioSelectedId = (radioGroup.getCheckedRadioButtonId());
//                radioButton = (RadioButton) findViewById(radioSelectedId);
//                selectedRadioButton = radioButton.getText().toString();
//                Toast.makeText(BookAppointment.this,selectedRadioButton , Toast.LENGTH_SHORT).show();
                if (date != null) {
                    postBookAppointment();
                } else {
                    Toast.makeText(context, "some fields are not selected", Toast.LENGTH_SHORT).show();
                }


            }
        });
        //date Button
        dateButton = findViewById(R.id.dateButton);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, +2);
        Date result = calendar.getTime();

        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        BookAppointment.this, new DatePickerDialog.OnDateSetListener() {
                    String fmonth, fDate, fyear;
                    int month;

                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
//                        month = month + 1;
//                        date = year + "-"+ month + "-"+ day ;
                        try {
                            if (monthOfYear < 10 && dayOfMonth < 10) {

                                fmonth = "0" + monthOfYear;
                                month = Integer.parseInt(fmonth) + 1;
                                fDate = "0" + dayOfMonth;
                                String paddedMonth = String.format("%02d", month);
                                fyear = String.valueOf(year);
                                date = fDate + "-" + paddedMonth + "-" + fyear;
                                dateButton.setText(date);
                                position(date);
                                getSlots(date);
                            } else {

                                fmonth = "0" + monthOfYear;
                                month = Integer.parseInt(fmonth) + 1;
                                String paddedMonth = String.format("%02d", month);
                                fyear = String.valueOf(year);
                                date = dayOfMonth + "-" + paddedMonth + "-" + year;
                                dateButton.setText(date);
                                position(date);
                                getSlots(date);

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                }, year, month, day);
                Calendar cal = Calendar.getInstance();

                cal.add(Calendar.DAY_OF_MONTH, +2);
                Date result = cal.getTime();
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.getDatePicker().setMaxDate(result.getTime());
                datePickerDialog.show();
            }
        });

        //RecyclerView For timeslots
        recyclerView = findViewById(R.id.timeSlots);
        recyclerView.setLayoutManager(new FlexboxLayoutManager(this, FlexDirection.ROW));

        //TextView
        doctorNameText = (TextView) findViewById(R.id.doctorNameText);
        doctorExpertiseText = (TextView) findViewById(R.id.doctorExpertiseText);
        doctorProfilePicText = findViewById(R.id.doctorProfilePicText);
        doctorFeesText = findViewById(R.id.doctorsFees);
        doctorsExperienceText = findViewById(R.id.doctorsExperience);
        //Toolbar
        Toolbar toolbar = findViewById(R.id.bookAppointmentToolbar);
        toolbar.setTitle("Book an Appointment");
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.black));
        setSupportActionBar(toolbar);

//        //4-Making the home button in toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_arrow);


        Intent intent = getIntent();
        doctor_id = intent.getStringExtra("doctor_id");
        doctorName = intent.getStringExtra("doctorName");
        doctorExpertise = intent.getStringExtra("doctorExpertise");
        doctorProfilePic = intent.getStringExtra("doctorProfilePicture");
        doctorFees = intent.getStringExtra("docFees");
        docExperience = intent.getStringExtra("doctorExperience");

        Picasso.get().load("http://" + ipAddress.ip + ":8000/profiles/users/" + doctorProfilePic).into(doctorProfilePicText);

        doctorNameText.setText(doctorName);
        doctorExpertiseText.setText(doctorExpertise);
        doctorFeesText.setText(doctorFees);
        doctorsExperienceText.setText(docExperience);


        SharedPreferences sp = getSharedPreferences("appointmentDoctorData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("doctorName", doctorName);
        editor.putString("doctorExpertise", doctorExpertise);
        editor.putString("doctorExperience", docExperience);
        editor.putString("doctorFee", doctorFees);
        editor.apply();

        System.out.println("outputting " + doctorName);
        System.out.println("outputting " + doctorExpertise);
        System.out.println("outputting " + docExperience);
        System.out.println("outputting " + doctorFees);

    }

    private void postBookAppointment() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    if (response.equals("Slot Already Taken")) {
                        Toast.makeText(BookAppointment.this, "Slot Already Taken", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(getApplicationContext(), PatientSymptoms.class);
                        intent.putExtra("date", date);
                        intent.putExtra("time", appointmentTime);
                        intent.putExtra("doctor_id", doctor_id);
                        intent.putExtra("patient_id", userPatientId);

                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        startActivity(intent);
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

                data.put("date", date);
                data.put("time", appointmentTime);
                data.put("type", "online");
                data.put("status", "Approved");
                data.put("patient_id", userPatientId);
                data.put("doctor_id", doctor_id);
                return data;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

//    private void bookedTimeSlots() {
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                try {
//                    JSONArray bookedSlots = new JSONArray(response);
//                    bookedAppointmentsDateArray = new String[bookedSlots.length()];
//                    bookedAppointmentsTimeArray = new String[bookedSlots.length()];
//                    for (int i = 0; i < bookedSlots.length(); i++) {
//                        JSONObject object = bookedSlots.getJSONObject(i);
//
//                        String bookedAppointmentDate = object.getString("date");
//                        bookedAppointmentsDateArray[i] = bookedAppointmentDate;
//                        String bookedAppointmentTime = object.getString("time");
//                        bookedAppointmentsTimeArray[i] = bookedAppointmentTime;
//                        System.out.println("bookedAppointmentTimeSlots " + bookedAppointmentTime);
//                    }
//                    System.out.println("booked Appointment Date Array is:" + Arrays.toString(bookedAppointmentsDateArray));
//                    System.out.println("booked Appointment Time Array is:" + Arrays.toString(bookedAppointmentsTimeArray));
//
//                }
//
//                catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        }) {
//            @Nullable
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> data = new HashMap<>();
//
//                data.put("doctor_id", doctor_id);
//                return data;
//            }
//        };
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        requestQueue.add(stringRequest);
//    }

    private int position(String date2 ) {
        int positionOfItem = 0;
        try {
            String format = "dd-MM-yyyy HH:mm";
            SimpleDateFormat sdf = new SimpleDateFormat(format);

            Date dateObj2 = null;
            Date dateObj1 = sdf.parse(date2 + " " + "11:00");
            dateObj2 = sdf.parse(date2 + " " + "24:00");
            long dif = dateObj1.getTime();
            while (dif < dateObj2.getTime()) {
                positionOfItem = positionOfItem + 1;
                dif += Integer.parseInt("15") * 60000;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return positionOfItem;
    }

    private void getSlots(String date1) {

        int positionOfItem = position(date);
        timeModelList = new ArrayList<>();
        thirdList= new ArrayList<>();
        try {

            String format = "dd-MM-yyyy HH:mm";
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Date dateObj2 = null;
            Date dateObj1 = sdf.parse(date1 + " " + "11:00");
            dateObj2 = sdf.parse(date1 + " " + "24:00");

            long dif = dateObj1.getTime();
            while (dif < dateObj2.getTime()) {

                Date slot = new Date(dif);
                String sformat = "HH:mm";
                SimpleDateFormat dateFormat = new SimpleDateFormat(sformat, Locale.US);
                String timesSlots = dateFormat.format(slot);
                String color = "white";

                TimeModel timeModel = new TimeModel(timesSlots,color);
                timeModelList.add(timeModel);
                totalTimeSlots = timeModelList.size();
                dif += Integer.parseInt("15") * 60000;
            }

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    thirdList.clear();
                    try {
                        JSONArray bookedSlots = new JSONArray(response);
                        bookedAppointmentsDateArray = new String[bookedSlots.length()];
                        bookedAppointmentsTimeArray = new String[bookedSlots.length()];
                        for (int i = 0; i < bookedSlots.length(); i++) {
                            JSONObject object = bookedSlots.getJSONObject(i);

                            String bookedAppointmentDate = object.getString("date");
                            bookedAppointmentsDateArray[i] = bookedAppointmentDate;
                            String bookedAppointmentTime = object.getString("time");
                            bookedAppointmentsTimeArray[i] = bookedAppointmentTime;

                            System.out.println("bookedAppointmentTimeSlots " + bookedAppointmentTime);
                        }
                        System.out.println("checking length of time array"+bookedAppointmentsTimeArray.length);
                        if(bookedAppointmentsTimeArray.length != 0)
                        {
                            for (int x = 0; x < bookedAppointmentsTimeArray.length; x++) {

                                for (int j = 0; j < positionOfItem; j++) {

                                    if ((timeModelList.get(j).getTimeOfAppointment().equals(bookedAppointmentsTimeArray[x])) && (date1.equals(bookedAppointmentsDateArray[x])))
                                    {
                                        timeModelList.set(j,new TimeModel(timeModelList.get(j).getTimeOfAppointment(),"red"));
                                    }
                                    else
                                    {
                                        timeModelList.set(j,new TimeModel(timeModelList.get(j).getTimeOfAppointment(),"white"));
                                    }
                                }
                            }
                        }
//                        else
//                        {
//                            for (int a = 0; a < positionOfItem; a++) {
//                                TimeModel timeModel = new TimeModel(timeModelList.get(a).getTimeOfAppointment(),"white");
//                                    timeModelList.add(timeModel);
////                                ColorOfTime colorOfTime = new ColorOfTime("white");
////                                thirdList.add(colorOfTime);
//                            }
//                        }

                        TimeAdapter timeAdapter = new TimeAdapter(BookAppointment.this, timeModelList, new TimeAdapter.ItemClickListener() {
                            @Override
                            public void onItemClick(TimeModel timeModel) {

                                appointmentTime = timeModel.getTimeOfAppointment();
                                Toast.makeText(BookAppointment.this, appointmentTime, Toast.LENGTH_SHORT).show();
                            }
                        });
                        recyclerView.setAdapter(timeAdapter);
                    }

                    catch (Exception e) {
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

                    data.put("doctor_id", doctor_id);
                    return data;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);



        } catch (
                ParseException e) {
            e.printStackTrace();
        }


    }

}
