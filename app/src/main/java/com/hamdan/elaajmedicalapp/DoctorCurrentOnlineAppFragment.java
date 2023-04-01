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
import com.hamdan.elaajmedicalapp.adapter.DoctorCurrentOnlineAppointmentAdapter;
import com.hamdan.elaajmedicalapp.models.Appointments;
import com.hamdan.elaajmedicalapp.models.IpAddress;
import com.hamdan.elaajmedicalapp.models.Payments;
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


public class DoctorCurrentOnlineAppFragment extends Fragment {
    IpAddress ipAddress;
    String url="http://"+ipAddress.ip+":8000/api/showCurrentDoctorAppointments";
    String url2 = "http://"+ipAddress.ip+":8000/api/cancelPatientAppointment";
    String appointment_id;
    User_Details user_details;
    Appointments appointments;
    private List<User_Details> user_detailsList;
    private List<Appointments> appointmentsList;
    private List<Payments> paymentsList;
    SharedPreferences sharedPreferences;
    private RecyclerView recyclerView;
    String patient_id,appointmentDate,appointmentTime,appointmentType,patientName;
    String userDoctorId,doctorsName,userEmail;
    SwipeRefreshLayout swipeRefreshLayout;
    DoctorCurrentOnlineAppointmentAdapter doctorCurrentOnlineAppointmentAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.doctor_current_online_appointment, container, false);

        user_detailsList = new ArrayList<>();
        appointmentsList = new ArrayList<>();
        paymentsList = new ArrayList<>();
        recyclerView = root.findViewById(R.id.currentDoctorAppointmentRecyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        swipeRefreshLayout = root.findViewById(R.id.swipeRefreshCurrentDoctorAppointments);

        sharedPreferences = getActivity().getSharedPreferences("credentials", getActivity().MODE_PRIVATE);
        doctorsName = sharedPreferences.getString("nameOfUser","");
        userDoctorId = sharedPreferences.getString("userId","");
        userEmail = sharedPreferences.getString("email", "");

        getAppointmentList();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAppointmentList();
                getAppointmentList();
                doctorCurrentOnlineAppointmentAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return root;
    }
    private void getAppointmentList()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                user_detailsList.clear();
                appointmentsList.clear();
                try {
                    JSONArray appointmentsDetails = new JSONArray(response);
                    for (int i = 0; i < appointmentsDetails.length(); i++) {
                        JSONObject object = appointmentsDetails.getJSONObject(i);

                        String patientName = object.getString("name");
                        String patientProfilePic = object.getString("profile_pic");
                        String patientEmail = object.getString("email");
                        String appointmentDate = object.getString("date");
                        String appointmentTime = object.getString("time");
                        String appointmentType = object.getString("type");
                        String appointmentstatus = object.getString("status");
                        String appointment_doctor_id = object.getString("appointment_doctor_id");

                        appointment_id = object.getString("appointment_id");
                        String patient_id = object.getString("appointment_patient_id");
                        String amount  = object.getString("amount");


                        User_Details user_details = new User_Details(patientName, patientProfilePic);
                        Appointments appointments = new Appointments(appointment_id, appointmentDate, appointmentTime, appointmentType,appointmentstatus, patient_id,appointment_doctor_id);
                        Payments payments = new Payments(amount);
                        user_detailsList.add(user_details);
                        appointmentsList.add(appointments);
                        paymentsList.add(payments);
                        doctorCurrentOnlineAppointmentAdapter =new DoctorCurrentOnlineAppointmentAdapter(getActivity(), appointmentsList, user_detailsList, paymentsList, new DoctorCurrentOnlineAppointmentAdapter.ItemClickListener() {
                            @Override
                            public void onItemClick(Appointments appointments)
                            {
                                String appointmentId = appointments.getAppointment_id();
                                Toast.makeText(getActivity(), appointmentId, Toast.LENGTH_SHORT).show();
                                cancelAppointment(appointmentId);
                                sendEmail(userEmail,patientName);
                                sendEmail2(patientEmail,patientName);
                            }
                        });
                        recyclerView.setAdapter(doctorCurrentOnlineAppointmentAdapter);
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
                data.put("doctor_id", userDoctorId);
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
    private void sendEmail(String userEmailInput,String patientName) {
        try {
            String stringSenderEmail = "YOUR_Email";
            String stringReceiverEmail = userEmailInput;
            String stringPasswordSenderEmail = "PASSWORD";

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
            mimeMessage.setText("Dear Dr."+doctorsName+", \n\n You have cancelled your appointment with patient-"+patientName+"  \n\n Regards !\nElaaj");

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
    private void sendEmail2(String userEmailInput,String patientName) {
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
            mimeMessage.setText("Dear "+patientName+", \n\n Your appointment have been cancelled by the Dr."+doctorsName+"  \n\n Regards !\nElaaj");

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