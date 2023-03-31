package com.hamdan.elaajmedicalapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hamdan.elaajmedicalapp.models.DoctorReceivePrescriptionOfPatient;
import com.hamdan.elaajmedicalapp.R;
import com.hamdan.elaajmedicalapp.models.IpAddress;
import com.hamdan.elaajmedicalapp.models.Symptoms;

import java.util.ArrayList;
import java.util.List;

public class DoctorReceivePatientFilesAdapter extends RecyclerView.Adapter<DoctorReceivePatientFilesAdapter.ViewHolder> {
    Context context;
    List<DoctorReceivePrescriptionOfPatient> prescriptionOfPatientList = new ArrayList<>();
    List<Symptoms> symptomsList = new ArrayList<>();
    IpAddress ipAddress;

    public DoctorReceivePatientFilesAdapter(Context context, List<DoctorReceivePrescriptionOfPatient> prescriptionOfPatientList, List<Symptoms> symptomsList) {
        this.context = context;
        this.prescriptionOfPatientList = prescriptionOfPatientList;
        this.symptomsList = symptomsList;
    }


    @NonNull
    @Override
    public DoctorReceivePatientFilesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_recieve_patient_prescription, parent, false);
        return new DoctorReceivePatientFilesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorReceivePatientFilesAdapter.ViewHolder holder, int position) {
        DoctorReceivePrescriptionOfPatient doctorReceivePrescriptionOfPatient = prescriptionOfPatientList.get(position);
        Symptoms symptoms = symptomsList.get(position);
        holder.symptomsOfPatients.setText(symptoms.getSymptomsOfPatient());
        holder.noOfDays.setText(symptoms.getNoOfDays());
        holder.dateCreatedAt.setText(doctorReceivePrescriptionOfPatient.getCreated_at());
        holder.current_medications.setText(doctorReceivePrescriptionOfPatient.getCurrent_medication());
        holder.download_recent_prescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fileName =  doctorReceivePrescriptionOfPatient.getUrlOfRecentPrescription();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                if (fileName.contains(".pdf")) {
                    intent.setDataAndType(Uri.parse("http://" + ipAddress.ip + ":8000/pdfs/Prescription/" + fileName), "application/pdf");
                }
                else
                {
                    intent.setDataAndType(Uri.parse("http://" + ipAddress.ip + ":8000/pdfs/Prescription/" + fileName), "image/*");

                }
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //added this line
                view.getContext().startActivity(intent);
            }
        });
        holder.download_lab_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fileName2 =  doctorReceivePrescriptionOfPatient.getUrlOfLabTest();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                if (fileName2.contains(".pdf")) {
                    browserIntent.setDataAndType(Uri.parse("http://" + ipAddress.ip + ":8000/pdfs/Prescription/" + fileName2), "application/pdf");
                }
                else
                {
                    browserIntent.setDataAndType(Uri.parse("http://" + ipAddress.ip + ":8000/pdfs/Prescription/" + fileName2), "image/*");

                }
                Intent chooser = Intent.createChooser(browserIntent, "Your File");
                chooser.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // optional
//                browserIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //added this line
                view.getContext().startActivity(browserIntent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return prescriptionOfPatientList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView symptomsOfPatients, download_recent_prescription, download_lab_test,current_medications,noOfDays,dateCreatedAt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            download_recent_prescription = itemView.findViewById(R.id.download_recent_prescription);
            download_lab_test = itemView.findViewById(R.id.download_lab_test);
            symptomsOfPatients = itemView.findViewById(R.id.symptomsOfPatients);
            current_medications = itemView.findViewById(R.id.current_medications);
            noOfDays = itemView.findViewById(R.id.noOfDays);
            dateCreatedAt = itemView.findViewById(R.id.dateCreatedAt);
        }
    }
}
