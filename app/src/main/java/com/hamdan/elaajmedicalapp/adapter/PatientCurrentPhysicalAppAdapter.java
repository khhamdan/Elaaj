package com.hamdan.elaajmedicalapp.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.hamdan.elaajmedicalapp.PatientReceivePrescriptionInAppointments;
import com.hamdan.elaajmedicalapp.R;
import com.hamdan.elaajmedicalapp.models.Appointments;
import com.hamdan.elaajmedicalapp.models.Doctor_Details;
import com.hamdan.elaajmedicalapp.models.IpAddress;
import com.hamdan.elaajmedicalapp.models.User_Details;
import com.hamdan.elaajmedicalapp.videoCall.PatientVideoCall;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PatientCurrentPhysicalAppAdapter extends RecyclerView.Adapter<PatientCurrentPhysicalAppAdapter.ViewHolder>
{
    Context context;
    FragmentActivity activity;
    User_Details user_details;
    Doctor_Details doctor_details;
    List<Appointments> appointmentsArrayList = new ArrayList<>();
    List<User_Details> user_detailsList = new ArrayList<>();
    List<Doctor_Details> arrayListDoctors = new ArrayList<>();
    private ItemClickListener mItemListener;
    IpAddress ipAddress;

    public PatientCurrentPhysicalAppAdapter(Context context, List<Appointments> appointmentsArrayList, List<User_Details> user_detailsList, List<Doctor_Details> arrayListDoctors, ItemClickListener mItemListener) {
        this.context = context;
        this.appointmentsArrayList = appointmentsArrayList;
        this.user_detailsList = user_detailsList;
        this.arrayListDoctors = arrayListDoctors;
        this.mItemListener = mItemListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.current_patient_physical_appointment_list, parent, false);
        return new ViewHolder(view);
    }

    public interface ItemClickListener
    {
        void onItemClick(Appointments appointments);

    }



    @Override
    public void onBindViewHolder(@NonNull PatientCurrentPhysicalAppAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Doctor_Details doctor_details = arrayListDoctors.get(position);
        Appointments appointments = appointmentsArrayList.get(position);
        User_Details user_details = user_detailsList.get(position);

        Picasso.get().load("http://"+ipAddress.ip+":8000/profiles/users/" + user_details.getProfile_pic()).into(holder.doctorProfilePic);
        holder.doctorName.setText(user_details.getName());
        holder.doctorsExperience.setText(doctor_details.getExpertise());
        holder.doctorsFees.setText(doctor_details.getFees());
        holder.appointmentDate.setText(appointments.getDate());
        holder.appointmentTime.setText(appointments.getTime());


        holder.cancelAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemListener.onItemClick(appointmentsArrayList.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        int size = appointmentsArrayList.size();
        return size;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView doctorName,doctorsExperience,appointmentDate,appointmentTime,doctorsFees;
        AppCompatButton cancelAppointment;
        CircleImageView doctorProfilePic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            doctorProfilePic = itemView.findViewById(R.id.doctorProfilePic);
            doctorName = itemView.findViewById(R.id.doctorName);
            doctorsExperience = itemView.findViewById(R.id.doctorsExperience);
            doctorsFees = itemView.findViewById(R.id.doctorsFees);
            appointmentDate = itemView.findViewById(R.id.appointmentDate);
            appointmentTime = itemView.findViewById(R.id.appointmentTime);
            cancelAppointment = itemView.findViewById(R.id.cancelAppointment);
        }
    }
}
