package com.hamdan.elaajmedicalapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.FragmentActivity;
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

public class PatientCurrentAppointmentAdapter extends RecyclerView.Adapter<PatientCurrentAppointmentAdapter.ViewHolder>{
    Context context;
    FragmentActivity activity;
    User_Details user_details;
    Doctor_Details doctor_details;
    List<Appointments> appointmentsArrayList = new ArrayList<>();
    List<User_Details> user_detailsList = new ArrayList<>();
    List<Doctor_Details> arrayListDoctors = new ArrayList<>();
    private ItemClickListener mItemListener;
    IpAddress ipAddress;


    public PatientCurrentAppointmentAdapter(Context context, List<Appointments> appointmentsArrayList, List<User_Details> user_detailsList, List<Doctor_Details> arrayListDoctors, ItemClickListener mItemListener)
    {
        this.context = context;
        this.appointmentsArrayList = appointmentsArrayList;
        this.user_detailsList = user_detailsList;
        this.arrayListDoctors = arrayListDoctors;
        this.mItemListener = mItemListener;

    }
    public interface ItemClickListener
    {
        void onItemClick(Appointments appointments);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.current_patient_appointment_list, parent, false);
        return new ViewHolder(view);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Doctor_Details doctor_details = arrayListDoctors.get(position);
        Appointments appointments = appointmentsArrayList.get(position);
        User_Details user_details = user_detailsList.get(position);

        Picasso.get().load("http://"+ipAddress.ip+":8000/profiles/users/" + user_details.getProfile_pic()).into(holder.doctorProfilePic);
        holder.doctorName.setText(user_details.getName());
        holder.doctorsExperience.setText(doctor_details.getExpertise());
        holder.doctorsFees.setText(doctor_details.getFees());
        holder.appointmentDate.setText(appointments.getDate());
        holder.appointmentTime.setText(appointments.getTime());

        if(appointments.getStatus().equals("completed"))
        {
            holder.videoCall_btn.setBackgroundColor(context.getResources().getColor(R.color.teal_700));
            holder.videoCall_btn.setEnabled(false);
            holder.cancelAppointment.setVisibility(View.GONE);


        }
        else
        {
        holder.videoCall_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), PatientVideoCall.class);
//                intent.putExtra("doctor_id", doctor_details.getId());
//                intent.putExtra("patientName", user_details.getName());
//                intent.putExtra("appointmentDate", appointments.getDate());
//                intent.putExtra("appointmentTime", appointments.getTime());
//                intent.putExtra("appointmentFees", doctor_details.getFees());
//                intent.putExtra("profile_pic", user_details.getProfile_pic());
//                intent.putExtra("doctorSpeciality", doctor_details.getExpertise());
//                intent.putExtra("doctorName", user_details.getName());
                intent.putExtra("appointment_id", appointments.getAppointment_id());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                view.getContext().startActivity(intent);
            }
        });
            holder.cancelAppointment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemListener.onItemClick(appointmentsArrayList.get(position));
                }
            });

        }


        holder.my_prescription_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), PatientReceivePrescriptionInAppointments.class);
                intent.putExtra("doctor_id", appointments.getDoctor_id());
                intent.putExtra("patientName", user_details.getName());
                intent.putExtra("appointmentDate", appointments.getDate());
                intent.putExtra("appointmentTime", appointments.getTime());
                view.getContext().startActivity(intent);


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
        AppCompatButton videoCall_btn,cancelAppointment,my_prescription_btn;
        CircleImageView doctorProfilePic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            doctorProfilePic = itemView.findViewById(R.id.doctorProfilePic);
            doctorName = itemView.findViewById(R.id.doctorName);
            doctorsExperience = itemView.findViewById(R.id.doctorsExperience);
            doctorsFees = itemView.findViewById(R.id.doctorsFees);
            videoCall_btn = itemView.findViewById(R.id.videoCall_btn);
            appointmentDate = itemView.findViewById(R.id.appointmentDate);
            appointmentTime = itemView.findViewById(R.id.appointmentTime);
            cancelAppointment = itemView.findViewById(R.id.cancelAppointment);
            my_prescription_btn = itemView.findViewById(R.id.my_prescription_btn);

        }
    }
}
