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
import androidx.recyclerview.widget.RecyclerView;

import com.hamdan.elaajmedicalapp.DoctorReceivePatientFiles;
import com.hamdan.elaajmedicalapp.DoctorTodayPhysicalAppointmentFragment;
import com.hamdan.elaajmedicalapp.R;
import com.hamdan.elaajmedicalapp.models.Appointments;
import com.hamdan.elaajmedicalapp.models.IpAddress;
import com.hamdan.elaajmedicalapp.models.Payments;
import com.hamdan.elaajmedicalapp.models.User_Details;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorTodayPhysicalAppointmentAdapter extends RecyclerView.Adapter<DoctorTodayPhysicalAppointmentAdapter.ViewHolder>
{
    Context context;
    List<Appointments> appointmentsArrayList = new ArrayList<>();
    List<User_Details> user_detailsList = new ArrayList<>();
    List<Payments> paymentsArrayList = new ArrayList<>();
    private ItemClickListener mItemListener;
    IpAddress ipAddress;

    public DoctorTodayPhysicalAppointmentAdapter(Context context, List<Appointments> appointmentsArrayList, List<User_Details> user_detailsList, List<Payments> paymentsArrayList, ItemClickListener mItemListener) {
        this.context = context;
        this.appointmentsArrayList = appointmentsArrayList;
        this.user_detailsList = user_detailsList;
        this.paymentsArrayList = paymentsArrayList;
        this.mItemListener = mItemListener;
    }

    @NonNull
    @Override
    public DoctorTodayPhysicalAppointmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_today_physical_appointment_list, parent, false);
        return new ViewHolder(view);
    }

    public interface ItemClickListener
    {
        void onItemClick(Appointments appointments);
    }


    @Override
    public void onBindViewHolder(@NonNull DoctorTodayPhysicalAppointmentAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Appointments appointments = appointmentsArrayList.get(position);
        User_Details user_details = user_detailsList.get(position);
        Payments payments = paymentsArrayList.get(position);

        holder.patientName.setText(user_details.getName());
        Picasso.get().load("http://"+ipAddress.ip+":8000/profiles/users/" + user_details.getProfile_pic()).into(holder.doctorProfilePicText);
        holder.appointmentDate.setText("Appointment Data "+appointments.getDate());
        holder.appointmentTime.setText("Appointment Time "+appointments.getTime());
        holder.doctorsFees.setText(payments.getAmount());
        holder.patientDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DoctorReceivePatientFiles.class);
                intent.putExtra("patient_id", appointments.getPatient_id());
                intent.putExtra("patientName", user_details.getName());
                intent.putExtra("appointmentDate", appointments.getDate());
                intent.putExtra("appointmentTime", appointments.getTime());

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                view.getContext().startActivity(intent);
            }
        });

        holder.cancelButton.setOnClickListener(new View.OnClickListener() {
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView patientName,appointmentDate,appointmentTime,doctorsFees;
        AppCompatButton patientDetails,cancelButton;
        CircleImageView doctorProfilePicText;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            patientName = itemView.findViewById(R.id.patientName);
            doctorProfilePicText = itemView.findViewById(R.id.doctorProfilePicText);
            appointmentDate = itemView.findViewById(R.id.appointmentDate);
            appointmentTime = itemView.findViewById(R.id.appointmentTime);
            patientDetails = itemView.findViewById(R.id.patientDetails);
            doctorsFees = itemView.findViewById(R.id.doctorsFees);
            cancelButton = itemView.findViewById(R.id.cancelButton);
        }
    }
}
