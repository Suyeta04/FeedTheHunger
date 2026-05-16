package com.example.feedthehunger.Volunteer;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.feedthehunger.ApiClient;
import com.example.feedthehunger.ApiService;
import com.example.feedthehunger.R;
import com.example.feedthehunger.User.FoodResponse;
import com.example.feedthehunger.User.ReportModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Completed_Delivery extends AppCompatActivity {

    RecyclerView completedRecycler;
    CompletedDeliveryAdapter adapter;
    List<ReportModel> completedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_completed_delivery);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        completedRecycler = findViewById(R.id.completedRecycler);
        completedRecycler.setLayoutManager(new LinearLayoutManager(this));

        completedList = new ArrayList<>();
        loadCompletedDeliveries();
    }

    private void loadCompletedDeliveries() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        Call<FoodResponse> call = apiService.getCompletedDelivery();

        call.enqueue(new Callback<FoodResponse>() {
            @Override
            public void onResponse(Call<FoodResponse> call, Response<FoodResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    FoodResponse data = response.body();
                    List<ReportModel> allFoods = data.getFoods();

                    completedList.clear();
                    for (ReportModel item : allFoods) {
                        if ("Delivered".equalsIgnoreCase(item.getStatus())) {
                            completedList.add(item);
                        }
                    }

                    adapter = new CompletedDeliveryAdapter(Completed_Delivery.this, completedList);
                    completedRecycler.setAdapter(adapter);

                } else {
                    Toast.makeText(Completed_Delivery.this, "No completed deliveries", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FoodResponse> call, Throwable t) {
                Toast.makeText(Completed_Delivery.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}