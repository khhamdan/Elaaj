package com.hamdan.elaajmedicalapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.hamdan.elaajmedicalapp.PatientReceivePrescriptionInAppointments;
import com.hamdan.elaajmedicalapp.models.Appointments;
import com.hamdan.elaajmedicalapp.models.Doctor_Details;
import com.hamdan.elaajmedicalapp.models.IpAddress;
import com.hamdan.elaajmedicalapp.models.User_Details;
import com.hamdan.elaajmedicalapp.R;
import com.hamdan.elaajmedicalapp.videoCall.PatientVideoCall;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PatientAppointmentDetailsAdapter extends RecyclerView.Adapter<PatientAppointmentDetailsAdapter.ViewHolder>
{
    Context context;
    User_Details user_details;
    Doctor_Details doctor_details;
    Appointments appointments;
    List<Appointments> appointmentsArrayList = new ArrayList<>();
    List<User_Details> user_detailsList = new ArrayList<>();
    List<Doctor_Details> arrayListDoctors = new ArrayList<>();
    IpAddress ipAddress;



    public PatientAppointmentDetailsAdapter(Context context, List<Appointments> appointmentsArrayList, List<User_Details> user_detailsList, List<Doctor_Details> arrayListDoctors) {
        this.context = context;
        this.appointmentsArrayList = appointmentsArrayList;
        this.user_detailsList = user_detailsList;
        this.arrayListDoctors = arrayListDoctors;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointment_detail_frame, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Doctor_Details doctor_details = arrayListDoctors.get(position);
        Appointments appointments = appointmentsArrayList.get(position);
        User_Details user_details = user_detailsList.get(position);

        Picasso.get().load("http://"+ipAddress.ip+":8000/profiles/users/" + user_details.getProfile_pic()).into(holder.doctorProfilePic);
        holder.patientName.setText(user_details.getPatientName());
        holder.doctorName.setText(user_details.getDoctorName());
        holder.doctorExpertise.setText(doctor_details.getExpertise());
        holder.docFees.setText(doctor_details.getFees());
        holder.appointmentDate.setText(appointments.getDate());
        holder.appointmentTime.setText(appointments.getTime());
        holder.my_prescription_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), PatientReceivePrescriptionInAppointments.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("appointmentTime",appointments.getTime() );
                intent.putExtra("appointmentDate",appointments.getDate() );
                intent.putExtra("doctor_id",doctor_details.getId() );

                view.getContext().startActivity(intent);

            }
        });
        holder.videoCall_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), PatientVideoCall.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                view.getContext().startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        int size = user_detailsList.size();
        return size;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView patientName,doctorName,doctorExpertise,appointmentDate,appointmentTime,docFees;
        AppCompatButton my_prescription_btn,videoCall_btn;
        CircleImageView doctorProfilePic;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            doctorProfilePic = itemView.findViewById(R.id.doctorProfilePic);
            patientName = itemView.findViewById(R.id.patientName);
            doctorName = itemView.findViewById(R.id.doctorName);
            docFees =itemView.findViewById(R.id.doctorFees);
            doctorExpertise = itemView.findViewById(R.id.doctorExpertise);
            appointmentDate = itemView.findViewById(R.id.appointmentDate);
            appointmentTime = itemView.findViewById(R.id.appointmentTime);
            my_prescription_btn = itemView.findViewById(R.id.my_prescription_btn);
            videoCall_btn = itemView.findViewById(R.id.videoCall_btn);

        }
    }
}
