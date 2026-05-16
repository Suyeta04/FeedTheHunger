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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.feedthehunger.MyIP;
import com.example.feedthehunger.R;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

public class Volunteer_Login_Activity extends AppCompatActivity {

    EditText volunteer_email, volunteer_password;
    Button volunteer_loginbtn;
    TextView volunteer_dont_have_an_acc, forgotpass;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_volunteer_login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        volunteer_email = findViewById(R.id.volunteer_email);
        volunteer_password = findViewById(R.id.volunteer_password);
        volunteer_loginbtn = findViewById(R.id.volunteer_loginbtn);
        volunteer_dont_have_an_acc = findViewById(R.id.volunteer_dont_have_an_acc);
        forgotpass = findViewById(R.id.forgotpass);

        volunteer_loginbtn.setOnClickListener(v -> {
            String email = volunteer_email.getText().toString().trim();
            String password = volunteer_password.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            loginVolunteer(email, password);
        });

        volunteer_dont_have_an_acc.setOnClickListener(v -> {
            startActivity(new Intent(Volunteer_Login_Activity.this, Volunteer_Registration_Activity.class));
            finish();
        });

        forgotpass.setOnClickListener(v -> {
            startActivity(new Intent(Volunteer_Login_Activity.this, Volunteer_ForgetPassword_Activity.class));
        });
    }

    private void loginVolunteer(String email, String password) {
        String url = MyIP.IP_ADDRESS+ "volunteer/login";

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Logging in...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("email", email);
            jsonBody.put("password", password);
        } catch (JSONException e) {
            progressDialog.dismiss();
            Toast.makeText(this, "JSON error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonBody,
                response -> {
                    progressDialog.dismiss();

                    try {
                        if (response.has("token")) {

                            FirebaseMessaging.getInstance()
                                    .subscribeToTopic("volunteers")
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(this, "Subscribed to notifications", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                            Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(Volunteer_Login_Activity.this, Volunteer_Dashboard.class);
                            startActivity(intent);
                            finish();

                        } else {
                            Toast.makeText(this, "Token not found in response", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(this, "Response parse error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                },
                error -> {
                    progressDialog.dismiss();

                    String message = "Login failed";
                    if (error.networkResponse != null && error.networkResponse.data != null) {
                        message = new String(error.networkResponse.data);
                    }

                    Toast.makeText(Volunteer_Login_Activity.this, message, Toast.LENGTH_LONG).show();
                    error.printStackTrace();
                }
        );

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }
}