package com.example.feedthehunger.Volunteer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.feedthehunger.R;
import com.example.feedthehunger.User.ReportModel;

import java.util.List;

public class CompletedDeliveryAdapter extends RecyclerView.Adapter<CompletedDeliveryAdapter.ViewHolder> {

    Context context;
    List<ReportModel> list;

    public CompletedDeliveryAdapter(Context context, List<ReportModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_completed_delivery, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ReportModel model = list.get(position);

        holder.txtFoodName.setText(model.getFood_type());
        holder.txtAddress.setText(model.getAddress());
        holder.txtTime.setText(model.getUpload_date_time());
        holder.txtStatus.setText(model.getStatus());

        Glide.with(context)
                .load(model.getImage())
                .placeholder(R.drawable.food)
                .into(holder.imgFood);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgFood;
        TextView txtFoodName, txtAddress, txtTime, txtStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgFood = itemView.findViewById(R.id.imgFood);
            txtFoodName = itemView.findViewById(R.id.txtFoodName);
            txtAddress = itemView.findViewById(R.id.txtAddress);
            txtTime = itemView.findViewById(R.id.txtTime);
            txtStatus = itemView.findViewById(R.id.txtStatus);
        }
    }
}