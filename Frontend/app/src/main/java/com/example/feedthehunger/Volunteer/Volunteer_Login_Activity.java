package com.example.feedthehunger.Volunteer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.feedthehunger.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Volunteer_Login_Activity extends AppCompatActivity {

    EditText volunteer_email, volunteer_password;
    Button volunteer_loginbtn;
    TextView volunteer_dont_have_an_acc;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_volunteer_login); // Your XML file name
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        volunteer_email = findViewById(R.id.volunteer_email);
        volunteer_password = findViewById(R.id.volunteer_password);
        volunteer_loginbtn = findViewById(R.id.volunteer_loginbtn);
        volunteer_dont_have_an_acc = findViewById(R.id.volunteer_dont_have_an_acc);

        volunteer_loginbtn.setOnClickListener(v -> {
            String email = volunteer_email.getText().toString().trim();
            String password = volunteer_password.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            loginVolunteer(email, password);
        });

        volunteer_dont_have_an_acc.setOnClickListener(v -> {
            startActivity(new Intent(Volunteer_Login_Activity.this, Volunteer_Registration_Activity.class));
            finish();
        });
    }

    private void loginVolunteer(String email, String password) {
        String url = "http://192.168.1.3:3000/volunteers/login";

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Logging in...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.has("token")) {
                            Toast.makeText(getApplicationContext(), "✅ Login Successful", Toast.LENGTH_SHORT).show();

                            // Clear fields
                            volunteer_email.setText("");
                            volunteer_password.setText("");

                            // You can also navigate to a new activity if needed here
                        } else {
                            Toast.makeText(getApplicationContext(), "❌ Login Failed", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "❌ Error parsing server response", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    progressDialog.dismiss();
                    Toast.makeText(Volunteer_Login_Activity.this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }
}
