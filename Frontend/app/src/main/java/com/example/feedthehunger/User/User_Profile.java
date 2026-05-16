package com.example.feedthehunger.User;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.feedthehunger.R;

public class User_Profile extends AppCompatActivity {

    TextView txtName, txtEmail;
    EditText edtContact, edtAddress;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        txtName = findViewById(R.id.txtName);
        txtEmail = findViewById(R.id.txtEmail);
        edtContact = findViewById(R.id.edtContact);
        edtAddress = findViewById(R.id.edtAddress);
        btnSave = findViewById(R.id.btnSave);

        // Load saved data
        SharedPreferences sp = getSharedPreferences("UserData", MODE_PRIVATE);

        txtName.setText(sp.getString("name", ""));
        txtEmail.setText(sp.getString("email", ""));
        edtContact.setText(sp.getString("contact", ""));
        edtAddress.setText(sp.getString("address", ""));

        // Save updated data
        btnSave.setOnClickListener(v -> {

            String contact = edtContact.getText().toString();
            String address = edtAddress.getText().toString();

            sp.edit()
                    .putString("contact", contact)
                    .putString("address", address)
                    .apply();

            Toast.makeText(this, "Profile Updated ✅", Toast.LENGTH_SHORT).show();
        });
    }
}