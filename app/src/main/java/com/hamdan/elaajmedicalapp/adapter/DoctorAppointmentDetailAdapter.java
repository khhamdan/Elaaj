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

import com.hamdan.elaajmedicalapp.models.Appointments;
import com.hamdan.elaajmedicalapp.models.Doctor_Details;
import com.hamdan.elaajmedicalapp.models.User_Details;
import com.hamdan.elaajmedicalapp.R;
import com.hamdan.elaajmedicalapp.videoCall.PatientVideoCall;

import java.util.ArrayList;
import java.util.List;

public class DoctorAppointmentDetailAdapter extends RecyclerView.Adapter<DoctorAppointmentDetailAdapter.ViewHolder> {

    Context context;
    User_Details user_details;
    Doctor_Details doctor_details;
    Appointments appointments;
    List<Appointments> appointmentsArrayList = new ArrayList<>();
    List<User_Details> user_detailsList = new ArrayList<>();

    public DoctorAppointmentDetailAdapter(Context context, List<Appointments> appointmentsArrayList, List<User_Details> user_detailsList) {
        this.context = context;
        this.appointmentsArrayList = appointmentsArrayList;
        this.user_detailsList = user_detailsList;
    }


    @NonNull
    @Override
    public DoctorAppointmentDetailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_appointment_details_frame, parent, false);
        return new DoctorAppointmentDetailAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull DoctorAppointmentDetailAdapter.ViewHolder holder, int position) {
        Appointments appointments = appointmentsArrayList.get(position);
        User_Details user_details = user_detailsList.get(position);

        holder.patientName.setText(user_details.getPatientName());
        holder.doctorName.setText(user_details.getDoctorName());
        holder.appointmentDate.setText(appointments.getDate());
        holder.appointmentTime.setText(appointments.getTime());
        holder.videoCallButton.setOnClickListener(new View.OnClickListener() {
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
        TextView patientName,doctorName,appointmentDate,appointmentTime;
        AppCompatButton videoCallButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            patientName = itemView.findViewById(R.id.patientName);
            doctorName = itemView.findViewById(R.id.doctorName);
            appointmentDate = itemView.findViewById(R.id.appointmentDate);
            appointmentTime = itemView.findViewById(R.id.appointmentTime);
            videoCallButton = itemView.findViewById(R.id.videoCall);
        }
    }
}
