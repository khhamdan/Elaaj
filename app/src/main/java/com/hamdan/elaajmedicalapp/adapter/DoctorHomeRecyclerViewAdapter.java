package com.hamdan.elaajmedicalapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hamdan.elaajmedicalapp.DoctorOnlineAppointment;
import com.hamdan.elaajmedicalapp.DoctorPhysicalAppointments;
import com.hamdan.elaajmedicalapp.DoctorPreviousAppointments;
import com.hamdan.elaajmedicalapp.R;

import java.util.List;

public class DoctorHomeRecyclerViewAdapter extends RecyclerView.Adapter<DoctorHomeRecyclerViewAdapter.ImageViewHolder>{

    Context mContext;
    List<Row> mData;

    public DoctorHomeRecyclerViewAdapter(Context mContext, List<Row> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public DoctorHomeRecyclerViewAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.home_recyclerview_items,parent,false);
        return new DoctorHomeRecyclerViewAdapter.ImageViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull DoctorHomeRecyclerViewAdapter.ImageViewHolder holder, int position) {
        Glide.with(mContext)
                .load(mData.get(position).getImg())
                .into(holder.img);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView img;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imageView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (getLayoutPosition() == 0)
            {
                Intent intent = new Intent(view.getContext(), DoctorOnlineAppointment.class);
                mContext.startActivity(intent);
            }
            else if (getLayoutPosition() == 1)
            {
                Intent intent = new Intent(view.getContext(), DoctorPreviousAppointments.class);
                mContext.startActivity(intent);
            }
            else if (getLayoutPosition() == 2)
            {
                Intent intent = new Intent(view.getContext(), DoctorPhysicalAppointments.class);
                mContext.startActivity(intent);
            }
        }
    }
}
