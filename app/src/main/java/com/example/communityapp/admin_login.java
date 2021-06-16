package com.example.communityapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

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

        checkField(adminName);
        checkField(adminPassword);
    }

    private boolean checkField(EditText adminName) {
        if (adminName.getText().toString().isEmpty() ){
            adminName.setError("Error");
            valid = false;
        }

        else{
            valid = true;
        }

        return valid;
    }


}