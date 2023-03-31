package com.hamdan.elaajmedicalapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.hamdan.elaajmedicalapp.models.ColorOfTime;
import com.hamdan.elaajmedicalapp.models.TimeModel;
import com.hamdan.elaajmedicalapp.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.ViewHolder>{
    int row_index=-1;
    public int highlightedItemPosition;
    Context context;
    TimeModel time;
    List<TimeModel> timeList = new ArrayList<>();
//    List<ColorOfTime> colorList = new ArrayList<>();
    private ItemClickListener mItemListener;

    public TimeAdapter(Context context, List<TimeModel> timeList, ItemClickListener mItemListener) {
        this.context = context;
        this.timeList = timeList;
        this.mItemListener = mItemListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.timelists, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position)
    {
        TimeModel timeModel = timeList.get(position);

        if (timeModel.getColorOfResource().equals("red"))
        {
            holder.timeText.setEnabled(false);
            holder.timeText.setText(timeModel.getTimeOfAppointment());
            holder.timeText.setTextColor(context.getResources().getColor(R.color.white));
            holder.linearLayout.setBackgroundColor(context.getResources().getColor(R.color.red));
            holder.timeCardView.setCardBackgroundColor(context.getResources().getColor(R.color.red));
        }
else {

            holder.timeText.setText(timeModel.getTimeOfAppointment());
            holder.timeText.setTextColor(context.getResources().getColor(R.color.black));
            holder.linearLayout.setBackgroundColor(context.getResources().getColor(R.color.white));
            holder.timeCardView.setCardBackgroundColor(context.getResources().getColor(R.color.white));

            holder.timeText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemListener.onItemClick(timeList.get(position));
                    row_index = position;
                    notifyDataSetChanged();
                }
            });
            if (row_index == position)
            {
                holder.linearLayout.setBackgroundColor(context.getResources().getColor(R.color.skyBlue));
                holder.timeCardView.setCardBackgroundColor(context.getResources().getColor(R.color.skyBlue));
                holder.timeText.setTextColor(context.getResources().getColor(R.color.white));
            }
        }
//        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                row_index = position;
//                notifyDataSetChanged();
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return timeList.size();
    }

    public interface ItemClickListener
    {
        void onItemClick(TimeModel timeModel);

    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView timeText;
        CardView timeCardView;
        LinearLayout linearLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            timeText = itemView.findViewById(R.id.timeText);
            timeCardView = itemView.findViewById(R.id.timeCardView);
            linearLayout = itemView.findViewById(R.id.linearLayout);
        }
    }
}
