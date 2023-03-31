package com.hamdan.elaajmedicalapp.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.hamdan.elaajmedicalapp.R;
import com.hamdan.elaajmedicalapp.models.DoctorPayments;

import java.util.ArrayList;
import java.util.List;

public class DoctorPaymentHistoryAdapter extends RecyclerView.Adapter<DoctorPaymentHistoryAdapter.ViewHolder> {
    Context context;
    List<DoctorPayments> doctorPaymentsList = new ArrayList<>();

    public DoctorPaymentHistoryAdapter(Context context, List<DoctorPayments> doctorPaymentsList) {
        this.context = context;
        this.doctorPaymentsList = doctorPaymentsList;
    }

    @NonNull
    @Override
    public DoctorPaymentHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_payment_history_list, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull DoctorPaymentHistoryAdapter.ViewHolder holder, int position) {
        DoctorPayments doctorPayments = doctorPaymentsList.get(position);

        holder.date.setText(doctorPayments.getDate());
        holder.amount.setText(doctorPayments.getAmountReceived());

    }
    @Override
    public int getItemCount() {
        return doctorPaymentsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView date,amount;
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            amount = itemView.findViewById(R.id.amount);
        }
    }
}
