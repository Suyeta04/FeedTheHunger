package com.example.feedthehunger.Admin;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

public class Admin_Registration_Activity extends AppCompatActivity {

    EditText admin_name, admin_email, admin_password, admin_contact, admin_location;
    Button admin_registerbtn;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_registration);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        admin_name = findViewById(R.id.admin_name);
        admin_email = findViewById(R.id.admin_email);
        admin_password = findViewById(R.id.admin_password);
        admin_contact = findViewById(R.id.admin_contact);
        admin_location = findViewById(R.id.admin_location);

        admin_registerbtn = findViewById(R.id.admin_registerbtn);

        admin_registerbtn.setOnClickListener(v -> {

            String name = admin_name.getText().toString().trim();
            String email = admin_email.getText().toString().trim();
            String password = admin_password.getText().toString().trim();
            String contact = admin_contact.getText().toString().trim();
            String location = admin_location.getText().toString().trim();

            if(name.isEmpty() || email.isEmpty() || password.isEmpty() || contact.isEmpty() || location.isEmpty()){
                Toast.makeText(this,"Please fill all fields",Toast.LENGTH_SHORT).show();
                return;
            }

            registerAdmin(name,email,password,contact,location);

        });

    }

    private void registerAdmin(String name,String email,String password,String contact,String location){

        String url= MyIP.IP_ADDRESS+ "admin/register";

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Registering...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest request=new StringRequest(Request.Method.POST,url,
                response -> {

                    progressDialog.dismiss();
                    Toast.makeText(this,"✅ Admin Registered Successfully",Toast.LENGTH_LONG).show();

                    admin_name.setText("");
                    admin_email.setText("");
                    admin_password.setText("");
                    admin_contact.setText("");
                    admin_location.setText("");

                },
                error -> {

                    progressDialog.dismiss();
                    Toast.makeText(this,"❌ Error: "+error.getMessage(),Toast.LENGTH_LONG).show();

                }){

            @Override
            protected Map<String, String> getParams(){

                Map<String,String> params=new HashMap<>();
                params.put("name",name);
                params.put("email",email);
                params.put("password",password);
                params.put("contact",contact);
                params.put("location",location);

                return params;
            }
        };

        RequestQueue queue= Volley.newRequestQueue(this);
        queue.add(request);
    }
}