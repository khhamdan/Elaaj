package com.hamdan.elaajmedicalapp.adapter;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.hamdan.elaajmedicalapp.models.DoctorSpeciality;
import com.hamdan.elaajmedicalapp.R;
import com.hamdan.elaajmedicalapp.SpecializedDoctorList;

import java.util.List;


public class DoctorSpecialityAdapter extends RecyclerView.Adapter<DoctorSpecialityAdapter.ViewHolder>
{
//    DoctorSpeciality[] doctorsSpeciality;
    private List<DoctorSpeciality> doctorSpecialityList;
    Context context;
    SharedPreferences sharedPreferences;

    public DoctorSpecialityAdapter(List<DoctorSpeciality> doctorSpecialityList, Context context) {
        this.doctorSpecialityList = doctorSpecialityList;
        this.context = context;
    }

    public void setFilteredList(List<DoctorSpeciality> filteredList)
    {
        this.doctorSpecialityList = filteredList;
        notifyDataSetChanged();

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.doctor_speciality_list,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
//        final DoctorSpeciality doctorSpecialityList = doctorsSpeciality[position];
        final DoctorSpeciality doctorSpeciality = doctorSpecialityList.get(position);
        holder.doctorSpecialityIcon.setImageResource(doctorSpecialityList.get(position).getSpecialityIcon());
        holder.doctorsSpecialityText.setText(doctorSpecialityList.get(position).getDoctorSpecialityText());

        holder.doctorSpecialityView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                SharedPreferences sp = context.getSharedPreferences("doctorData",MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("doctorSpeciality",doctorSpeciality.getDoctorSpecialityText());
                editor.apply();
                Intent intent = new Intent(context, SpecializedDoctorList.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return doctorSpecialityList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView doctorSpecialityIcon;
        TextView doctorsSpecialityText;
        CardView doctorSpecialityView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            doctorSpecialityIcon = itemView.findViewById(R.id.imageView);
            doctorsSpecialityText = itemView.findViewById(R.id.specialityText);
            doctorSpecialityView = itemView.findViewById(R.id.doctorSpecialityView);

        }
    }
}
