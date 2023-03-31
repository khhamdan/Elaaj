package com.hamdan.elaajmedicalapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.hamdan.elaajmedicalapp.DoctorVoiceNoteForPatient;
import com.hamdan.elaajmedicalapp.PatientReceiveDoctorVoiceNote;
import com.hamdan.elaajmedicalapp.R;
import com.hamdan.elaajmedicalapp.models.Doctor_Details;
import com.hamdan.elaajmedicalapp.models.IpAddress;
import com.hamdan.elaajmedicalapp.models.Prescription;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PatientReceivePrescriptionAdapter extends RecyclerView.Adapter<PatientReceivePrescriptionAdapter.ViewHolder> {
    Context context;
    List<Prescription> prescriptionList = new ArrayList<>();
    IpAddress ipAddress;

    public PatientReceivePrescriptionAdapter(Context context, List<Prescription> prescriptionList) {
        this.context = context;
        this.prescriptionList = prescriptionList;
    }

    @NonNull
    @Override
    public PatientReceivePrescriptionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_recieving_prescription_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientReceivePrescriptionAdapter.ViewHolder holder, int position)
    {
        Prescription prescription  = prescriptionList.get(position);
        holder.nameText.setText(prescription.getName());
        holder.appointmentDate.setText(prescription.getDate());
        holder.appointmentTime.setText(prescription.getTime());
        holder.appointmentTime.setText(prescription.getTime());
        Picasso.get().load("http://"+ipAddress.ip+":8000/profiles/users/" + prescription.getProfile_pic()).into(holder.profilePicText);

        holder.prescriptionImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fileName2 =  prescription.getPrescriptions();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse("http://"+ipAddress.ip+":8000/pdfs/doctorPrescriptions/"+fileName2), "image/*");
                view.getContext().startActivity(intent);
            }
        });
        holder.voice_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String audioFileName = prescription.getVoice_note();
                Intent intent = new Intent(view.getContext(), PatientReceiveDoctorVoiceNote.class);
                intent.putExtra("recordedAudio", audioFileName);
                view.getContext().startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return prescriptionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameText,appointmentDate,appointmentTime;
        AppCompatButton prescriptionImage;
        CircleImageView profilePicText;
        ImageView voice_note;
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            nameText = itemView.findViewById(R.id.nameText);
            appointmentDate = itemView.findViewById(R.id.appointmentDate);
            appointmentTime = itemView.findViewById(R.id.appointmentTime);
            prescriptionImage = itemView.findViewById(R.id.prescriptionImage);
            profilePicText = itemView.findViewById(R.id.profilePicText);
            voice_note = itemView.findViewById(R.id.voice_note);
        }


    }
}
