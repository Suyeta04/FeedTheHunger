package com.example.feedthehunger.Admin;

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

public class Admin_Dashboard extends AppCompatActivity {

    CardView manageUsers, manageVolunteers, reportsAdmin, logoutAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_dashboard);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        manageUsers = findViewById(R.id.manage_users);
        manageVolunteers = findViewById(R.id.manage_volunteers);
        reportsAdmin = findViewById(R.id.reports_admin);
        logoutAdmin = findViewById(R.id.logout_admin);

        manageUsers.setOnClickListener(v -> {
            Intent intent = new Intent(Admin_Dashboard.this, Manage_Users.class);
            startActivity(intent);
        });

        manageVolunteers.setOnClickListener(v -> {
            Intent intent = new Intent(Admin_Dashboard.this, Manage_Volunteers.class);
            startActivity(intent);
        });

        reportsAdmin.setOnClickListener(v -> {
            Intent intent = new Intent(Admin_Dashboard.this, Admin_Reports.class);
            startActivity(intent);
        });

        logoutAdmin.setOnClickListener(v -> {
            new AlertDialog.Builder(Admin_Dashboard.this)
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to logout?")
                    .setPositiveButton("Yes", (dialog, which) -> {

                        getSharedPreferences("AdminData", MODE_PRIVATE)
                                .edit()
                                .clear()
                                .apply();

                        Intent intent = new Intent(Admin_Dashboard.this, Admin_Login_Activity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }
}