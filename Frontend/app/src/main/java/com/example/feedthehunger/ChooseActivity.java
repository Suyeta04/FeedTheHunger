package com.example.feedthehunger;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.feedthehunger.User.User_Login_Activity;
import com.example.feedthehunger.Admin.Admin_Login_Activity;
import com.example.feedthehunger.Volunteer.Volunteer_Login_Activity;

public class ChooseActivity extends AppCompatActivity {

    LinearLayout choosediv, Admindiv, Volunteerdiv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_choose);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // User card
        choosediv = findViewById(R.id.choosediv);

        // Admin card
        Admindiv = findViewById(R.id.Admindiv);

        // Volunteer card
        Volunteerdiv = findViewById(R.id.Volunteerdiv);

        // USER CLICK
        choosediv.setOnClickListener(v -> {
            Intent intent = new Intent(ChooseActivity.this, User_Login_Activity.class);
            startActivity(intent);
        });

        // ADMIN CLICK
        Admindiv.setOnClickListener(v -> {
            Intent intent = new Intent(ChooseActivity.this, Admin_Login_Activity.class);
            startActivity(intent);
        });

        // VOLUNTEER CLICK
        Volunteerdiv.setOnClickListener(v -> {
            Intent intent = new Intent(ChooseActivity.this, Volunteer_Login_Activity.class);
            startActivity(intent);
        });

    }
}