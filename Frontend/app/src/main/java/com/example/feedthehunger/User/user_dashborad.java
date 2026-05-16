package com.example.feedthehunger.User;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.feedthehunger.R;

public class user_dashborad extends AppCompatActivity {

    LinearLayout donatediv, reportSts, profileDiv, btnLogout;
    //Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_dashborad);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 🔹 Find Views
        donatediv = findViewById(R.id.donatediv);
        reportSts = findViewById(R.id.ReportSts); // 👈 your id
        profileDiv = findViewById(R.id.profileDiv);
        btnLogout = findViewById(R.id.btnLogout);

        // 🔹 Upload Food Click
        donatediv.setOnClickListener(v -> {
            Intent intent = new Intent(user_dashborad.this, UploadFoodActivity.class);
            startActivity(intent);
        });

        // 🔥 🔥 NEW: Report Status Click
        reportSts.setOnClickListener(v -> {
            Intent intent = new Intent(user_dashborad.this, Report_List.class);
            startActivity(intent);
        });

        // 👉 OPEN PROFILE
        profileDiv.setOnClickListener(v -> {
            Intent intent = new Intent(user_dashborad.this, User_Profile.class);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {

            // 🧹 Clear user session
            getSharedPreferences("UserData", MODE_PRIVATE)
                    .edit()
                    .clear()
                    .apply();

            // 🔄 Go to Login Screen
            Intent intent = new Intent(user_dashborad.this, User_Login_Activity.class);

            // ❌ Prevent back to dashboard
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);

            Toast.makeText(this, "Logout Successful", Toast.LENGTH_SHORT).show();
        });
    }
}