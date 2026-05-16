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

public class VolunteerPickupAdapter extends RecyclerView.Adapter<VolunteerPickupAdapter.ViewHolder> {

    Context context;
    List<ReportModel> list;

    public VolunteerPickupAdapter(Context context, List<ReportModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_item_available_pickup, parent, false);
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

        holder.btnAccept.setOnClickListener(v -> acceptFood(model));
        holder.btnReject.setOnClickListener(v -> rejectFood(model));
    }

    private void acceptFood(ReportModel model) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ResponseBody> call = apiService.updateFoodStatus(model.get_id(), "Accepted");

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Successfully Accepted", Toast.LENGTH_SHORT).show();
                    //removeItemById(model.get_id());
                } else {
                    Toast.makeText(context, "Accept failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void rejectFood(ReportModel model) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ResponseBody> call = apiService.updateFoodStatus(model.get_id(), "Rejected");

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Rejected and deleted", Toast.LENGTH_SHORT).show();
                    //removeItemById(model.get_id());
                } else {
                    Toast.makeText(context, "Reject failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtFoodName, txtAddress, txtTime;
        ImageView imgFood;
        Button btnAccept, btnReject;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtFoodName = itemView.findViewById(R.id.txtFoodName);
            txtAddress = itemView.findViewById(R.id.txtAddress);
            txtTime = itemView.findViewById(R.id.txtTime);
            imgFood = itemView.findViewById(R.id.imgFood);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnReject = itemView.findViewById(R.id.btnReject);
        }
    }
}