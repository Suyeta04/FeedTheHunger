package com.example.feedthehunger.Admin;

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

public class Admin_Login_Activity extends AppCompatActivity {

    EditText admin_email, admin_pass, admin_confirm_pass, admin_location;
    Button admin_loginbtn;
    TextView admin_dont_have_an_acc;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        admin_email = findViewById(R.id.admin_email);
        admin_pass = findViewById(R.id.admin_pass);
        admin_loginbtn = findViewById(R.id.admin_loginbtn);
        admin_dont_have_an_acc = findViewById(R.id.admin_dont_have_an_acc);

        admin_loginbtn.setOnClickListener(v -> {
            String name = admin_email.getText().toString().trim();
            String password = admin_pass.getText().toString().trim();
            String confirmPassword = admin_confirm_pass.getText().toString().trim();
            String location = admin_location.getText().toString().trim();

            if (name.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || location.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            loginAdmin(name, password, location);
        });

        admin_dont_have_an_acc.setOnClickListener(v -> {
            startActivity(new Intent(Admin_Login_Activity.this, Admin_Login_Activity.class));
            finish();
        });
    }

    private void loginAdmin(String name, String password, String location) {
        String url = "http://192.168.1.3:3000/admin/login";

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

                            admin_email.setText("");
                            admin_pass.setText("");
                            admin_confirm_pass.setText("");
                            admin_location.setText("");

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
                    Toast.makeText(Admin_Login_Activity.this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("password", password);
                params.put("location", location);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }
}
