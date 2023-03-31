package com.hamdan.elaajmedicalapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hamdan.elaajmedicalapp.models.PatientWalletModel;
import com.hamdan.elaajmedicalapp.R;

import java.util.ArrayList;
import java.util.List;

public class PatientWalletAdapter extends RecyclerView.Adapter<PatientWalletAdapter.ViewHolder> {
    Context context;
    List<PatientWalletModel> patientWalletList = new ArrayList<>();

    public PatientWalletAdapter(Context context, List<PatientWalletModel> patientWallets) {
        this.context = context;
        this.patientWalletList = patientWallets;
    }

    @NonNull
    @Override
    public PatientWalletAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_wallet_list, parent, false);
        return new PatientWalletAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull PatientWalletAdapter.ViewHolder holder, int position) {
        PatientWalletModel patientWalletModel = patientWalletList.get(position);
        if (patientWalletModel.getAppointmentWalletFees().contains("-"))
        {
            holder.transactionAmount.setTextColor(context.getResources().getColor(R.color.red));
            holder.transactionAmount.setText(patientWalletModel.getAppointmentWalletFees());

        }
        else {
            holder.transactionAmount.setText(patientWalletModel.getAppointmentWalletFees());
        }
        holder.transactionDoctorName.setText(patientWalletModel.getAppointmentWalletDoctorName());
        holder.transactionTime.setText(patientWalletModel.getAppointmentWalletTime());
        holder.transactionDate.setText(patientWalletModel.getAppointmentWalletDate());
        holder.status.setText(patientWalletModel.getAppointmentStatus());
    }


    @Override
    public int getItemCount() {
        return patientWalletList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView status, transactionAmount, transactionTime, transactionDate, transactionDoctorName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            status = itemView.findViewById(R.id.status);
            transactionAmount = itemView.findViewById(R.id.transactionAmount);
            transactionTime = itemView.findViewById(R.id.transactionTime);
            transactionDate = itemView.findViewById(R.id.transactionDate);
            transactionDoctorName = itemView.findViewById(R.id.transactionDoctorName);
        }

    }
}
