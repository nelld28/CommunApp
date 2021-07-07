package com.example.communityapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.example.communityapp.Model.Post;
import com.example.communityapp.Model.group_intro;
import com.example.communityapp.adapter.CommAdapter;
import com.example.communityapp.adapter.postAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.ls.LSException;

import java.util.ArrayList;
import java.util.List;

public class adminActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    RecyclerView postARV;
    com.example.communityapp.adapter.postAdapter pAdapter;

    List<Post> pList;
    TextView postNameAd;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onStart() {
        super.onStart();

        databaseReference.child("Post").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot snapshot) {

            }
        });




    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Posts");

        postNameAd = findViewById(R.id.over_view_item);

        postARV=findViewById(R.id.post_ad_rv);
        postARV.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        postARV.setHasFixedSize(true);


//        String postTitle = getIntent().getExtras().getString("postTitle");
//        postNameAd.setText(postTitle);

    }
}