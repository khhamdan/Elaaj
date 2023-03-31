
package com.hamdan.elaajmedicalapp;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hamdan.elaajmedicalapp.adapter.PatientTodayAppointmentAdapter;
import com.hamdan.elaajmedicalapp.adapter.PatientTodayPhysicalAdapter;
import com.hamdan.elaajmedicalapp.interfaces.RecyclerViewInterface;
import com.hamdan.elaajmedicalapp.models.Appointments;
import com.hamdan.elaajmedicalapp.models.Doctor_Details;
import com.hamdan.elaajmedicalapp.models.IpAddress;
import com.hamdan.elaajmedicalapp.models.User_Details;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class PatientTodayPhysicalAppFragment extends Fragment {
    IpAddress ipAddress;
    String url = "http://" + ipAddress.ip + ":8000/api/showPatientTodayPhysicalAppointments";
    String url2 = "http://"+ipAddress.ip+":8000/api/cancelPatientAppointment";
    User_Details user_details;
    Doctor_Details doctor_details;
    Appointments appointments;
    private List<User_Details> user_detailsList;
    private List<Doctor_Details> doctor_detailsList;
    private List<Appointments> appointmentsList;
    SharedPreferences sharedPreferences;
    String  appointment_id;
    String userPatientId,patientName,patientEmail;
    private RecyclerView recyclerView;
    private RecyclerViewInterface recyclerViewInterface;
    SwipeRefreshLayout swipeRefreshLayout;
    PatientTodayPhysicalAdapter patientTodayPhysicalAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.patient_today_physical_app, container, false);

        user_detailsList = new ArrayList<>();
        doctor_detailsList = new ArrayList<>();
        appointmentsList = new ArrayList<>();
        recyclerView = root.findViewById(R.id.todayPhysicalAppointmentRecyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        swipeRefreshLayout = root.findViewById(R.id.swipeRefreshTodayPhysicalAppointments);

        sharedPreferences = getActivity().getSharedPreferences("credentials", getActivity().MODE_PRIVATE);
        patientName = sharedPreferences.getString("nameOfUser", "");
        userPatientId = sharedPreferences.getString("userId", "");
        patientEmail = sharedPreferences.getString("email", "");

        getTodayAppointmentList();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getTodayAppointmentList();
                getTodayAppointmentList();
                patientTodayPhysicalAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });


        return root;
    }
    private void getTodayAppointmentList() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                user_detailsList.clear();
                doctor_detailsList.clear();
                appointmentsList.clear();
                try {
                    JSONArray appointmentsDetails = new JSONArray(response);
                    for (int i = 0; i < appointmentsDetails.length(); i++) {
                        JSONObject object = appointmentsDetails.getJSONObject(i);

                        String doctorName = object.getString("name");
                        String doctorProfilePic = object.getString("profile_pic");
                        String doctorEmail = object.getString("email");
                        String appointmentDate = object.getString("date");
                        String appointmentTime = object.getString("time");
                        String appointmentType = object.getString("type");
                        String doctorFees = object.getString("fee");
                        String doctorExpertise = object.getString("expertise");
                        String user_details_id_of_doctor = object.getString("appointment_doctor_id");
                        appointment_id = object.getString("appointment_id");
                        String doctor_details_id = object.getString("doctor_details_id");


                        User_Details user_details = new User_Details(doctorName, doctorProfilePic);
                        Doctor_Details doctor_details = new Doctor_Details(doctor_details_id, doctorExpertise, doctorFees);
                        Appointments appointments = new Appointments(appointment_id, appointmentDate, appointmentTime, appointmentType, user_details_id_of_doctor);
                        user_detailsList.add(user_details);
                        doctor_detailsList.add(doctor_details);
                        appointmentsList.add(appointments);
                        patientTodayPhysicalAdapter =new PatientTodayPhysicalAdapter(getActivity(), appointmentsList, user_detailsList, doctor_detailsList, new PatientTodayPhysicalAdapter.ItemClickListener() {
                            @Override
                            public void onItemClick(Appointments appointments) {
                                String appointment_id1 = appointments.getAppointment_id();
                                Toast.makeText(getActivity(), appointment_id1, Toast.LENGTH_SHORT).show();
                                cancelAppointment(appointment_id1);
                                sendEmail(patientEmail,doctorName);
                                sendEmail2(doctorEmail,doctorName);

                            }
                        });
                        recyclerView.setAdapter(patientTodayPhysicalAdapter);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
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

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

    }
    private void cancelAppointment(String appointment_id2) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("appointment_id", appointment_id2);
                return data;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }
    private void sendEmail(String userEmailInput,String doctorName) {
        try {
            String stringSenderEmail = "f2018-443@bnu.edu.pk";
            String stringReceiverEmail = userEmailInput;
            String stringPasswordSenderEmail = "hamsab123";

            String stringHost = "smtp.gmail.com";

            Properties properties = System.getProperties();

            properties.put("mail.smtp.host", stringHost);
            properties.put("mail.smtp.port", "465");
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.auth", "true");

            javax.mail.Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(stringSenderEmail, stringPasswordSenderEmail);
                }
            });

            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(stringReceiverEmail));

            mimeMessage.setSubject("Subject: Elaaj Appointments");
            mimeMessage.setText("Dear "+patientName+", \n\n You have cancelled your appointment with Dr."+doctorName+" \n\n Regards !\nElaaj");

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Transport.send(mimeMessage);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();

        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
    private void sendEmail2(String userEmailInput,String doctorName) {
        try {
            String stringSenderEmail = "f2018-443@bnu.edu.pk";
            String stringReceiverEmail = userEmailInput;
            String stringPasswordSenderEmail = "hamsab123";

            String stringHost = "smtp.gmail.com";

            Properties properties = System.getProperties();

            properties.put("mail.smtp.host", stringHost);
            properties.put("mail.smtp.port", "465");
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.auth", "true");

            javax.mail.Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(stringSenderEmail, stringPasswordSenderEmail);
                }
            });

            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(stringReceiverEmail));

            mimeMessage.setSubject("Subject: Elaaj Appointments");
            mimeMessage.setText("Dear Dr."+doctorName+", \n\n Your appointment have been cancelled by the patient-"+patientName+"  \n\n Regards !\nElaaj");

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Transport.send(mimeMessage);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();

        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}