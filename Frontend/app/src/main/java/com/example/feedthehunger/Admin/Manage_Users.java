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

public class Manage_Users extends AppCompatActivity {

    RecyclerView userRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);

        userRecycler = findViewById(R.id.userRecycler);
        userRecycler.setLayoutManager(new LinearLayoutManager(this));

        loadUsers();
    }

    private void loadUsers() {

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        Call<List<UserModel>> call = apiService.getAllUsers();

        call.enqueue(new Callback<List<UserModel>>() {
            @Override
            public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {

                if (response.isSuccessful()) {

                    List<UserModel> users = response.body();

                    UserAdapter adapter = new UserAdapter(Manage_Users.this, users);
                    userRecycler.setAdapter(adapter);

                } else {
                    Toast.makeText(Manage_Users.this, "No users found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<UserModel>> call, Throwable t) {
                Toast.makeText(Manage_Users.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}