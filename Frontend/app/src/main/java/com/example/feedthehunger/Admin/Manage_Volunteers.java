package com.example.feedthehunger.Admin;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.feedthehunger.ApiClient;
import com.example.feedthehunger.ApiService;
import com.example.feedthehunger.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Manage_Volunteers extends AppCompatActivity {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_volunteers);

        recyclerView = findViewById(R.id.volunteerRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadVolunteers();
    }

    private void loadVolunteers() {

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        Call<List<VolunteerModel>> call = apiService.getAllVolunteers();

        call.enqueue(new Callback<List<VolunteerModel>>() {
            @Override
            public void onResponse(Call<List<VolunteerModel>> call, Response<List<VolunteerModel>> response) {

                if (response.isSuccessful() && response.body() != null) {

                    VolunteerAdapter adapter =
                            new VolunteerAdapter(Manage_Volunteers.this, response.body());

                    recyclerView.setAdapter(adapter);

                } else {
                    Toast.makeText(Manage_Volunteers.this, "No Data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<VolunteerModel>> call, Throwable t) {
                Toast.makeText(Manage_Volunteers.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}