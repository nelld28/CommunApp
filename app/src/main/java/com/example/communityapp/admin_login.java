package com.example.communityapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class admin_login extends AppCompatActivity {

//    EditText adminName, adminPassword;
//    Button adminLogin;
//    boolean valid = true;

    public static final int GOOGLE_SIGN_IN_CODE =10005;
    SignInButton adminLogInButton;
    GoogleSignInOptions gso;
    GoogleSignInClient signInClient;
    FirebaseAuth firebaseAuth;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        adminLogInButton = findViewById(R.id.admin_login);

//      Firebase initialization
        firebaseAuth = FirebaseAuth.getInstance();

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("745773994523-qnptj5jat1si2kvicsvr06tilopmg5eg.apps.googleusercontent.com")
                .requestEmail()
                .build();
        signInClient = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);

//        if sign in account is available
        if(signInAccount != null || firebaseAuth.getCurrentUser() != null )
        {
            startActivity(new Intent(this, adminActivity.class));
        }

        adminLogInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sign = signInClient.getSignInIntent();
                startActivityForResult(sign, GOOGLE_SIGN_IN_CODE);
            }
        });


//        adminName = findViewById(R.id.admin_name);
//        adminPassword = findViewById(R.id.admin_password);
//        adminLogin = findViewById(R.id.admin_login);
//
//        adminLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (adminName.getText().toString().equals("admin") &&
//                adminPassword.getText().toString().equals("admin")){
//                    startActivity(new Intent(getApplicationContext(), adminActivity.class));
//                }
//                else
//                {
//                    Toast.makeText(admin_login.this, "Wrong credentials", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GOOGLE_SIGN_IN_CODE){
            Task<GoogleSignInAccount> signInTask = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount signInAcc = signInTask.getResult(ApiException.class);

                AuthCredential authCredential = GoogleAuthProvider
                        .getCredential(signInAcc.getIdToken(), null);

                firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(getApplicationContext(), "Admin Login", Toast.LENGTH_SHORT)
                                .show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));

                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });


            }
            catch (ApiException e){
                e.printStackTrace();
            }
        }
    }




}