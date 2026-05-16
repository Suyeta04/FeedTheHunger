package com.example.feedthehunger.User;

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

import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ViewHolder> {

    Context context;
    List<ReportModel> list;

    public ReportAdapter(Context context, List<ReportModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_report_status_dashboard, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ReportModel model = list.get(position);

        holder.txtFoodName.setText(model.getFood_type());
        holder.txtAddress.setText(model.getAddress());
        holder.txtStatus.setText("Status: " + model.getStatus());

        String imageUrl = model.getImage();

        Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.food)
                .into(holder.imgFood);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtFoodName, txtAddress, txtStatus;
        ImageView imgFood;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtFoodName = itemView.findViewById(R.id.txtFoodName);
            txtAddress = itemView.findViewById(R.id.txtAddress);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            imgFood = itemView.findViewById(R.id.imgFood);
        }
    }
}