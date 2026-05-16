package com.example.feedthehunger.Volunteer;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.feedthehunger.R;

public class Volunteer_Dashboard extends AppCompatActivity {

    CardView Profile, availablePickups, assignedDelivery, completedDelivery, logoutVolunteer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_volunteer_dashboard);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Profile = findViewById(R.id.profile_volunteer);
        availablePickups = findViewById(R.id.available_pickups);
        assignedDelivery = findViewById(R.id.assigned_delivery);
        completedDelivery = findViewById(R.id.completed_delivery);
        logoutVolunteer = findViewById(R.id.logout_volunteer);

        Profile.setOnClickListener(v -> {
            Intent intent = new Intent(Volunteer_Dashboard.this, Volunteer_Profile.class);
            startActivity(intent);
        });

        availablePickups.setOnClickListener(v -> {
            Intent intent = new Intent(Volunteer_Dashboard.this, Available_Pickups.class);
            startActivity(intent);
        });

        assignedDelivery.setOnClickListener(v -> {
            Intent intent = new Intent(Volunteer_Dashboard.this, Assigned_Delivery.class);
            startActivity(intent);
        });

        completedDelivery.setOnClickListener(v -> {
            Intent intent = new Intent(Volunteer_Dashboard.this, Completed_Delivery.class);
            startActivity(intent);
        });

        logoutVolunteer.setOnClickListener(v -> {
            new AlertDialog.Builder(Volunteer_Dashboard.this)
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to logout?")
                    .setPositiveButton("Yes", (dialog, which) -> {

                        getSharedPreferences("VolunteerData", MODE_PRIVATE)
                                .edit()
                                .clear()
                                .apply();

                        Intent intent = new Intent(Volunteer_Dashboard.this, Volunteer_Login_Activity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }
}