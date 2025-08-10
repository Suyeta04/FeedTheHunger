package com.example.feedthehunger.Volunteer;

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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.feedthehunger.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Volunteer_Registration_Activity extends AppCompatActivity {

    EditText vol_name, vol_email, vol_pass, vol_contact, vol_location;
    Button vol_regisbtn;
    TextView vol_already_have_an_acc;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_volunteer_registration);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        vol_name = findViewById(R.id.vol_name);
        vol_email = findViewById(R.id.admin_email);
        vol_pass = findViewById(R.id.vol_pass);
        vol_contact = findViewById(R.id.vol_contact);
        vol_location = findViewById(R.id.admin_location);
        vol_regisbtn = findViewById(R.id.vol_regisbtn);
        vol_already_have_an_acc = findViewById(R.id.admin_already_have_an_acc);

        vol_regisbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = vol_name.getText().toString();
                String email = vol_email.getText().toString();
                String password = vol_pass.getText().toString();
                String contact = vol_contact.getText().toString();
                String location = vol_location.getText().toString();

                // ⚠️ Update your server's IP here
                String url = "http://192.168.0.177:3000/volunteer/register";

                insertVolunteerData(url, name, email, password, contact, location);
            }
        });
    }

    private void insertVolunteerData(String url, String name, String email, String password, String contact, String location) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.has("_id")) {
                            Toast.makeText(getApplicationContext(), "✅ Volunteer Registered", Toast.LENGTH_LONG).show();
                            vol_name.setText("");
                            vol_email.setText("");
                            vol_pass.setText("");
                            vol_contact.setText("");
                            vol_location.setText("");
                        } else {
                            Toast.makeText(getApplicationContext(), "❌ Registration failed", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "❌ Error parsing server response", Toast.LENGTH_LONG).show();
                    }
                },
                error -> {
                    progressDialog.dismiss();
                    Toast.makeText(Volunteer_Registration_Activity.this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("email", email);
                params.put("password", password);
                params.put("contact", contact);
                params.put("location", location);

                return params;
            }

        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }
}
