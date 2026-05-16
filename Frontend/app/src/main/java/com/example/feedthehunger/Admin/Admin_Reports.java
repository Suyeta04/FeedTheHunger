package com.example.feedthehunger.Admin;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.feedthehunger.ApiClient;
import com.example.feedthehunger.ApiService;
import com.example.feedthehunger.R;
import com.example.feedthehunger.User.FoodResponse;
import com.example.feedthehunger.User.ReportModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Admin_Reports extends AppCompatActivity {

    RecyclerView adminReportsRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_reports);

        adminReportsRecycler = findViewById(R.id.adminReportsRecycler);
        adminReportsRecycler.setLayoutManager(new LinearLayoutManager(this));

        loadReports();
    }

    private void loadReports() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        Call<FoodResponse> call = apiService.getAllFood();

        call.enqueue(new Callback<FoodResponse>() {
            @Override
            public void onResponse(Call<FoodResponse> call, Response<FoodResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ReportModel> reportList = response.body().getFoods();

                    AdminReportsAdapter adapter = new AdminReportsAdapter(Admin_Reports.this, reportList);
                    adminReportsRecycler.setAdapter(adapter);
                } else {
                    Toast.makeText(Admin_Reports.this, "No reports found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FoodResponse> call, Throwable t) {
                Toast.makeText(Admin_Reports.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}