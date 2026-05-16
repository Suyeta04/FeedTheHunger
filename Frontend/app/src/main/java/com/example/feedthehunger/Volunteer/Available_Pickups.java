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

public class Available_Pickups extends AppCompatActivity {

    RecyclerView pickupRecycler;
    VolunteerPickupAdapter adapter;
    List<ReportModel> pickupList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_available_pickups);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        pickupRecycler = findViewById(R.id.pickupRecycler);
        pickupRecycler.setLayoutManager(new LinearLayoutManager(this));

        pickupList = new ArrayList<>();

        loadAvailablePickups();
    }

    private void loadAvailablePickups() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        Call<FoodResponse> call = apiService.getAvailablePickups();

        call.enqueue(new Callback<FoodResponse>() {
            @Override
            public void onResponse(Call<FoodResponse> call, Response<FoodResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    FoodResponse data = response.body();
                    pickupList = data.getFoods();

                    adapter = new VolunteerPickupAdapter(Available_Pickups.this, pickupList);
                    pickupRecycler.setAdapter(adapter);

                } else {
                    Toast.makeText(Available_Pickups.this, "No data found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FoodResponse> call, Throwable t) {
                Toast.makeText(Available_Pickups.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });
    }
}