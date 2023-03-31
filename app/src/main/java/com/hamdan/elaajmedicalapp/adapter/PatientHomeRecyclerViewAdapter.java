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
import com.hamdan.elaajmedicalapp.PatientFindDoctor;
import com.hamdan.elaajmedicalapp.PatientAppointment;
import com.hamdan.elaajmedicalapp.PatientPhysicalAppointment;
import com.hamdan.elaajmedicalapp.R;

import java.util.List;

public class PatientHomeRecyclerViewAdapter extends RecyclerView.Adapter<PatientHomeRecyclerViewAdapter.ImageViewHolder>
{

    Context mContext;
    List<Row> mData;

    public PatientHomeRecyclerViewAdapter(Context mContext, List<Row> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 9- ImageView Holder - Binding views
        //10 - Creating Recycler View Item Layout
        View view = LayoutInflater.from(mContext).inflate(R.layout.home_recyclerview_items,parent,false);
        return new ImageViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        //Adding Glide Library to load the images faster
        //Binding image here
        //Using Glide Library
        Glide.with(mContext)
                .load(mData.get(position).getImg())
                .into(holder.img);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ImageViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{

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
                Intent intent = new Intent(view.getContext(), PatientFindDoctor.class);
                mContext.startActivity(intent);
            }
            else if(getLayoutPosition() == 1)
            {
                Intent intent = new Intent(view.getContext(), PatientAppointment.class);
                mContext.startActivity(intent);
            }
            else if(getLayoutPosition() == 2)
            {
                Intent intent = new Intent(view.getContext(), PatientPhysicalAppointment.class);
                mContext.startActivity(intent);
            }
        }
    }
}
