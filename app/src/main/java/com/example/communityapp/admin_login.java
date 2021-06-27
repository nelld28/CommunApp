package com.example.communityapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class admin_login extends AppCompatActivity {

    EditText adminName, adminPassword;
    Button adminLogin;
    boolean valid = true;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        adminName = findViewById(R.id.admin_name);
        adminPassword = findViewById(R.id.admin_password);
        adminLogin = findViewById(R.id.admin_login);

        adminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adminName.getText().toString().equals("admin") &&
                adminPassword.getText().toString().equals("admin")){
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
                else
                {
                    Toast.makeText(admin_login.this, "Wrong credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }




}