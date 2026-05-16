package com.example.feedthehunger.Admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.feedthehunger.ApiClient;
import com.example.feedthehunger.ApiService;
import com.example.feedthehunger.R;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VolunteerAdapter extends RecyclerView.Adapter<VolunteerAdapter.ViewHolder> {

    Context context;
    List<VolunteerModel> list;

    public VolunteerAdapter(Context context, List<VolunteerModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_volunteer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        VolunteerModel model = list.get(position);

        holder.txtName.setText(model.getName());
        holder.txtEmail.setText(model.getEmail());
        holder.txtContact.setText(model.getContact());

        holder.btnDelete.setOnClickListener(v -> {

            int currentPosition = holder.getAdapterPosition();

            if (currentPosition != RecyclerView.NO_POSITION) {

                VolunteerModel currentModel = list.get(currentPosition);

                ApiService apiService = ApiClient.getClient().create(ApiService.class);

                Call<ResponseBody> call =
                        apiService.deleteVolunteer(currentModel.get_id());

                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        if (response.isSuccessful()) {

                            Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show();

                            // 🔥 REMOVE ITEM FROM LIST
                            list.remove(currentPosition);
                            notifyItemRemoved(currentPosition);
                            notifyItemRangeChanged(currentPosition, list.size());

                        } else {
                            Toast.makeText(context, "Delete failed", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtName, txtEmail, txtContact;
        Button btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txtName);
            txtEmail = itemView.findViewById(R.id.txtEmail);
            txtContact = itemView.findViewById(R.id.txtContact);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}