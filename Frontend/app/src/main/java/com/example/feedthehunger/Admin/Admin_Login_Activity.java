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
import com.example.feedthehunger.MyIP;
import com.example.feedthehunger.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Admin_Login_Activity extends AppCompatActivity {

    EditText admin_email, admin_pass;
    Button admin_loginbtn;
    TextView admin_dont_have_an_acc;
    TextView forgotpass;

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
        admin_loginbtn = findViewById(R.id.Loginbtn);
        admin_dont_have_an_acc = findViewById(R.id.admin_dont_have_an_acc);
        forgotpass = findViewById(R.id.forgotpass);

        admin_loginbtn.setOnClickListener(v -> {

            String email = admin_email.getText().toString().trim();
            String password = admin_pass.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            loginAdmin(email, password);
        });

        admin_dont_have_an_acc.setOnClickListener(v -> {
            Intent intent = new Intent(Admin_Login_Activity.this, Admin_Registration_Activity.class);
            startActivity(intent);
            finish();
        });

        forgotpass.setOnClickListener(v -> {
            Intent intent = new Intent(Admin_Login_Activity.this, Admin_ForgetPassword_Activity.class);
            startActivity(intent);
        });
    }

    private void loginAdmin(String email, String password) {

        String url = MyIP.IP_ADDRESS+ "Admin/login";

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Logging in...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {

                    progressDialog.dismiss();

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        if (jsonObject.has("admin")) {

                            Toast.makeText(getApplicationContext(), "✅ Login Successful", Toast.LENGTH_SHORT).show();

                            admin_email.setText("");
                            admin_pass.setText("");

                            // SAVE ADMIN DATA IF NEEDED
                            JSONObject adminObj = jsonObject.getJSONObject("admin");
                            String adminId = adminObj.optString("_id", "");
                            String adminName = adminObj.optString("name", "");
                            String adminEmail = adminObj.optString("email", "");

                            getSharedPreferences("AdminData", MODE_PRIVATE)
                                    .edit()
                                    .putString("admin_id", adminId)
                                    .putString("admin_name", adminName)
                                    .putString("admin_email", adminEmail)
                                    .apply();

                            // OPEN DASHBOARD
                            Intent intent = new Intent(Admin_Login_Activity.this, Admin_Dashboard.class);
                            startActivity(intent);
                            finish();

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

                    String errorMessage = "Login Failed";
                    if (error.networkResponse != null && error.networkResponse.data != null) {
                        errorMessage = new String(error.networkResponse.data);
                    }

                    Toast.makeText(Admin_Login_Activity.this, errorMessage, Toast.LENGTH_LONG).show();
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