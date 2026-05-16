package com.example.feedthehunger.User;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.feedthehunger.ApiClient;
import com.example.feedthehunger.ApiService;
import com.example.feedthehunger.R;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Report_List extends AppCompatActivity {

    RecyclerView recyclerView;
    ReportAdapter adapter;
    List<ReportModel> reportList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_list);

        recyclerView = findViewById(R.id.reportRecyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        reportList = new ArrayList<>();

        // Sample Data
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        Call<FoodResponse> call = apiService.getAllFood();


        call.enqueue(new Callback<FoodResponse>() {
            @Override
            public void onResponse(Call<FoodResponse> call, Response<FoodResponse> response) {

                if(response.isSuccessful()){

                    FoodResponse data = response.body();

                    List<ReportModel> foods = data.getFoods();


                    ReportAdapter adapter = new ReportAdapter(Report_List.this,  foods);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<FoodResponse> call, Throwable t) {

                t.printStackTrace();
            }
        });
    }
}