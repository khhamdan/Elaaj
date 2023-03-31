package com.hamdan.elaajmedicalapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hamdan.elaajmedicalapp.R;
import com.hamdan.elaajmedicalapp.models.Appointments;
import com.hamdan.elaajmedicalapp.models.Doctor_Details;
import com.hamdan.elaajmedicalapp.models.Reviews;

import java.util.ArrayList;
import java.util.List;

public class PatientViewDoctorReviewsAdapter extends RecyclerView.Adapter<PatientViewDoctorReviewsAdapter.ViewHolder>
{
    Context context;
    List<Reviews> reviewsList = new ArrayList<>();

    public PatientViewDoctorReviewsAdapter(Context context, List<Reviews> reviewsList) {
        this.context = context;
        this.reviewsList = reviewsList;
    }

    @NonNull
    @Override
    public PatientViewDoctorReviewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reviews_by_patient_lists, parent, false);
        return new ViewHolder(view);

    }


    @Override
    public void onBindViewHolder(@NonNull PatientViewDoctorReviewsAdapter.ViewHolder holder, int position) {
        Reviews reviews = reviewsList.get(position);
        holder.comments.setText(reviews.getComments());
        holder.name.setText(reviews.getName());
        holder.ratingBar.setRating(Float.parseFloat(reviews.getRatings()));

    }


    @Override
    public int getItemCount() {
        return reviewsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RatingBar ratingBar;
        TextView comments,name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            comments = itemView.findViewById(R.id.comments);
            name = itemView.findViewById(R.id.name);
            ratingBar = itemView.findViewById(R.id.ratingBar);
        }
    }
}
