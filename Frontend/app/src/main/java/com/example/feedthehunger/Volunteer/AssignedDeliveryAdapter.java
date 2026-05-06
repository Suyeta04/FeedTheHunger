package com.example.feedthehunger.Volunteer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.feedthehunger.ApiClient;
import com.example.feedthehunger.ApiService;
import com.example.feedthehunger.R;
import com.example.feedthehunger.User.ReportModel;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AssignedDeliveryAdapter extends RecyclerView.Adapter<AssignedDeliveryAdapter.ViewHolder> {

    Context context;
    List<ReportModel> list;

    public AssignedDeliveryAdapter(Context context, List<ReportModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_assigned_delivery, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ReportModel model = list.get(position);

        holder.txtFoodName.setText(model.getFood_type());
        holder.txtAddress.setText(model.getAddress());
        holder.txtTime.setText(model.getUpload_date_time());

        Glide.with(context)
                .load(model.getImage())
                .placeholder(R.drawable.food)
                .into(holder.imgFood);

        holder.btnDelivered.setOnClickListener(v -> markDelivered(model));
    }

    private void markDelivered(ReportModel model) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ResponseBody> call = apiService.updateFoodStatus(model.get_id(), "Delivered");

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Marked as Delivered", Toast.LENGTH_SHORT).show();
                    removeItemById(model.get_id());
                } else {
                    Toast.makeText(context, "Failed to update", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void removeItemById(String id) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).get_id().equals(id)) {
                list.remove(i);
                notifyItemRemoved(i);
                notifyItemRangeChanged(i, list.size());
                return;
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgFood;
        TextView txtFoodName, txtAddress, txtTime;
        Button btnDelivered;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgFood = itemView.findViewById(R.id.imgFood);
            txtFoodName = itemView.findViewById(R.id.txtFoodName);
            txtAddress = itemView.findViewById(R.id.txtAddress);
            txtTime = itemView.findViewById(R.id.txtTime);
            btnDelivered = itemView.findViewById(R.id.btnDelivered);
        }
    }
}