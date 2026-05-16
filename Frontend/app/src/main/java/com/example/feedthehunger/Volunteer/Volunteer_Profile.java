package com.example.feedthehunger.Volunteer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.feedthehunger.R;
import com.example.feedthehunger.ChooseActivity;

public class Volunteer_Profile extends AppCompatActivity {

    TextView txtVolunteerName, txtVolunteerEmail;
    EditText edtVolunteerContact, edtVolunteerAddress;
    Button btnSaveVolunteer, btnLogoutVolunteer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_profile);

        // 🔹 Bind views
        txtVolunteerName = findViewById(R.id.txtVolunteerName);
        txtVolunteerEmail = findViewById(R.id.txtVolunteerEmail);
        edtVolunteerContact = findViewById(R.id.edtVolunteerContact);
        edtVolunteerAddress = findViewById(R.id.edtVolunteerAddress);
        btnSaveVolunteer = findViewById(R.id.btnSaveVolunteer);
        btnLogoutVolunteer = findViewById(R.id.btnLogoutVolunteer);

        // 🔹 Get SharedPreferences
        SharedPreferences sp = getSharedPreferences("VolunteerData", MODE_PRIVATE);

        // 🔹 Load data
        txtVolunteerName.setText(sp.getString("name", "Volunteer Name"));
        txtVolunteerEmail.setText(sp.getString("email", "email@example.com"));
        edtVolunteerContact.setText(sp.getString("contact", ""));
        edtVolunteerAddress.setText(sp.getString("address", ""));

        // 🔹 Save updated data
        btnSaveVolunteer.setOnClickListener(v -> {

            String contact = edtVolunteerContact.getText().toString().trim();
            String address = edtVolunteerAddress.getText().toString().trim();

            sp.edit()
                    .putString("contact", contact)
                    .putString("address", address)
                    .apply();

            Toast.makeText(this, "Profile Updated ✅", Toast.LENGTH_SHORT).show();
        });

        // 🔹 Logout
        btnLogoutVolunteer.setOnClickListener(v -> {

            // Clear data
            sp.edit().clear().apply();

            // Go to ChooseActivity
            Intent intent = new Intent(Volunteer_Profile.this, ChooseActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
        });
    }
}