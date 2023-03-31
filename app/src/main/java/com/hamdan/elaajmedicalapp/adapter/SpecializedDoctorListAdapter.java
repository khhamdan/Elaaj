package com.hamdan.elaajmedicalapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.hamdan.elaajmedicalapp.PatientPrescriptionPhysicalAppointment;
import com.hamdan.elaajmedicalapp.PatientViewDoctorReviews;
import com.hamdan.elaajmedicalapp.models.Doctor_Details;
import com.hamdan.elaajmedicalapp.models.IpAddress;
import com.hamdan.elaajmedicalapp.models.User_Details;
import com.hamdan.elaajmedicalapp.PatientPrescriptions;
import com.hamdan.elaajmedicalapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SpecializedDoctorListAdapter extends RecyclerView.Adapter<SpecializedDoctorListAdapter.MyViewHolder> implements Filterable {
    Context context;
    User_Details user_details;
    Doctor_Details doctor_details;
    List<User_Details> arrayListUsers = new ArrayList<>();
    List<Doctor_Details> arrayListDoctors = new ArrayList<>();
    List<User_Details> doctorListAll;
    IpAddress ipAddress;

    public SpecializedDoctorListAdapter(Context context, List<User_Details> arrayListUsers, List<Doctor_Details> arrayListDoctors) {
        this.context = context;
        this.arrayListUsers = arrayListUsers;
        this.arrayListDoctors = arrayListDoctors;
        this.doctorListAll = new ArrayList<>(arrayListUsers);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.specialized_doctor_list, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
         User_Details user_details = arrayListUsers.get(position);
         Doctor_Details doctor_details = arrayListDoctors.get(position);

        holder.doctorNameText.setText(user_details.getName());
        holder.doctorExpertiseText.setText(doctor_details.getExpertise());
        holder.doctorFees.setText(doctor_details.getFees());
        holder.doctorsExperience.setText(doctor_details.getExperience());
//        Picasso.get().load("http://192.168.56.1/FYP/public/profiles/users/" + user_details.getProfile_pic()).into(holder.doctorProfilePic);
        Picasso.get().load("http://"+ipAddress.ip+":8000/profiles/users/" + user_details.getProfile_pic()).into(holder.doctorProfilePic);
        //Glide.with(context).load(doctorModel.getProfile_pic()).into(holder.doctorProfilePic);
//        Glide.with(holder.doctorProfilePic.getContext()).load(new File("http://192.168.56.1/profiles/users/" + user_details.getProfile_pic())).into(holder.doctorProfilePic);
        holder.bookAppointmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), PatientPrescriptions.class);
                intent.putExtra("doctor_id", doctor_details.getId());
                intent.putExtra("doctorName", user_details.getName());
                intent.putExtra("doctorExpertise", doctor_details.getExpertise());
                intent.putExtra("doctorProfilePicture", user_details.getProfile_pic());
                intent.putExtra("docFees", doctor_details.getFees());
                intent.putExtra("doctorExperience", doctor_details.getExperience());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                view.getContext().startActivity(intent);
            }
        });
        holder.view_Reviews_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), PatientViewDoctorReviews.class);
                intent.putExtra("doctor_id", doctor_details.getId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                view.getContext().startActivity(intent);
            }
        });
//        holder.bookPhysicalAppointmentButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(view.getContext(), PatientPrescriptionPhysicalAppointment.class);
//                intent.putExtra("doctor_id", doctor_details.getId());
//                intent.putExtra("doctorName", user_details.getName());
//                intent.putExtra("doctorExpertise", doctor_details.getExpertise());
//                intent.putExtra("doctorProfilePicture", user_details.getProfile_pic());
//                intent.putExtra("docFees", doctor_details.getFees());
//                intent.putExtra("doctorExperience", doctor_details.getExperience());
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                view.getContext().startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        int size = arrayListUsers.size();
        return size;
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        //background thread
        @Override
        protected FilterResults performFiltering(CharSequence keyword)
        {
            ArrayList<User_Details> filteredData = new ArrayList<>();
            if (keyword.toString().isEmpty()) {
                filteredData.addAll(doctorListAll);
            } else {
                for (User_Details user_details : doctorListAll) {
                    if (user_details.getName().toString().toLowerCase().contains(keyword.toString().toLowerCase())) {
                        filteredData.add(user_details);
                    }
                }

            }
            FilterResults results = new FilterResults();
            results.values = filteredData;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults results) {
            arrayListUsers.clear();
            arrayListUsers.addAll((ArrayList<User_Details>)results.values);
            notifyDataSetChanged();
        }
    };

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView doctorNameText, doctorExpertiseText,doctorFees,doctorsExperience;
        private CircleImageView doctorProfilePic;
        private AppCompatButton bookAppointmentBtn,view_Reviews_btn,bookPhysicalAppointmentButton;
        public MyViewHolder(View view)
        {super(view);

            doctorProfilePic = view.findViewById(R.id.doctorProfilePic);
            doctorNameText = view.findViewById(R.id.doctorNameText);
            doctorExpertiseText = view.findViewById(R.id.doctorExpertiseText);
            doctorsExperience = view.findViewById(R.id.doctorsExperience);
            doctorFees = view.findViewById(R.id.doctorsFees);
            bookAppointmentBtn =  view.findViewById(R.id.bookAppointmentButton);
            view_Reviews_btn =  view.findViewById(R.id.view_Reviews_btn);
//            bookPhysicalAppointmentButton =  view.findViewById(R.id.bookPhysicalAppointmentButton);


        }
    }
}
