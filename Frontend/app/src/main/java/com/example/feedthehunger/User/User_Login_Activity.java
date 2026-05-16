package com.example.feedthehunger.User;

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

public class User_Login_Activity extends AppCompatActivity {

    EditText user_name, user_password;
    Button user_Loginbtn;
    TextView user_dont_have_an_acc, UserForgot;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        user_name = findViewById(R.id.admin_email);
        user_password = findViewById(R.id.admin_pass);
        user_Loginbtn = findViewById(R.id.Loginbtn);
        user_dont_have_an_acc = findViewById(R.id.admin_dont_have_an_acc);
        UserForgot = findViewById(R.id.UserForgot);

        user_Loginbtn.setOnClickListener(v -> {

            String email = user_name.getText().toString().trim();
            String password = user_password.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            loginUser(email, password);
        });

        user_dont_have_an_acc.setOnClickListener(v -> {
            startActivity(new Intent(User_Login_Activity.this, User_Registration_activity.class));
        });

        UserForgot.setOnClickListener(v -> {
            Intent intent = new Intent(User_Login_Activity.this, User_ForgetPassword_Activity.class);
            startActivity(intent);
        });
    }

    private void loginUser(String email, String password) {

        String url = MyIP.IP_ADDRESS+ "users/login";

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Logging in...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {

                    progressDialog.dismiss();

                    try {

                        JSONObject jsonObject = new JSONObject(response);

                        if (jsonObject.has("user")) {

                            JSONObject userObj = jsonObject.getJSONObject("user");

                            String userId = userObj.optString("_id", "");
                            String name = userObj.optString("name", "");
                            String emailResp = userObj.optString("email", "");
                            String contact = userObj.optString("contact", "");

                            if (!userId.isEmpty()) {

                                Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();

                                getSharedPreferences("UserData", MODE_PRIVATE)
                                        .edit()
                                        .putString("user_id", userId)
                                        .putString("name", name)
                                        .putString("email", emailResp)
                                        .putString("contact", contact)
                                        .apply();

                                // 👉 OPEN DASHBOARD
                                Intent intent = new Intent(User_Login_Activity.this, user_dashborad.class);
                                startActivity(intent);
                                finish();

                            } else {
                                Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(getApplicationContext(), "Invalid response", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Error parsing response", Toast.LENGTH_SHORT).show();
                    }

                },
                error -> {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_SHORT).show();
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