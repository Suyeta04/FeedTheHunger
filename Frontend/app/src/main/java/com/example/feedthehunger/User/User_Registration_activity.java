package com.example.feedthehunger.User;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.feedthehunger.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class User_Registration_activity extends AppCompatActivity {
    EditText user_name,user_email,user_pass,user_contact;
    Button user_regisbtn;
    TextView user_already_have_an_acc;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_registration);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        user_name=findViewById(R.id.admin_email);
        user_email=findViewById(R.id.user_email);
        user_pass=findViewById(R.id.admin_pass);
        user_contact=findViewById(R.id.user_contact);

        user_regisbtn=findViewById(R.id.admin_signupbtn);

        user_already_have_an_acc=findViewById(R.id.user_already_have_an_acc);

        user_regisbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String un= user_name.getText().toString();
                String ue= user_email.getText().toString();
                String up= user_pass.getText().toString();
                String uc= user_contact.getText().toString();

                String url = "http://192.168.0.177:3000/users/register";

                insertData(url, un, ue, up, uc);
            }
        });

    }

    private void insertData(String url, String un, String ue, String up, String uc) {
        // Initialize progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait...");
        progressDialog.setCancelable(false);
        // Show progress dialog
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Hide progress dialog
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.has("user")){
                                JSONObject user =
                                        jsonObject.getJSONObject("user");
                                String userId = user.optString("_id");
                                if (!userId.isEmpty()) {
                                    Toast.makeText(getApplicationContext(), "✅ Registration Success", Toast.LENGTH_LONG).show();
                                            // Optionally, save token or user data here
                                            //reset Data or send to LoginActivity
                                            user_name.setText("");
                                            user_email.setText("");
                                            user_pass.setText("");
                                            user_contact.setText("");
                                } else {
                                    Toast.makeText(getApplicationContext(), "❌Registration Failed", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "❌Invalid response from server", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "❌ Errorparsing server response", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(User_Registration_activity.this, "Error: " +
                                error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("name", un);
                params.put("email", ue);
                params.put("password", up);
                params.put("contact", uc);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }
}