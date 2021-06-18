package com.example.communityapp.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.communityapp.Model.group_intro;
import com.example.communityapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.widget.Toast.*;
import static android.widget.Toast.LENGTH_SHORT;


public class add_group_intro extends Fragment {

    private EditText groupNameEt;
    private ImageView groupAdd, groupCancel;

    FirebaseDatabase groupDatabase;

    DatabaseReference groupDataRef;

    group_intro groupIntro;

    public add_group_intro() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static add_group_intro newInstance() {
        return new add_group_intro();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootview;
        rootview =  inflater.inflate(R.layout.fragment_add_group_intro2, container, false);

        groupNameEt = rootview.findViewById(R.id.group_name_text);
        groupDatabase = FirebaseDatabase.getInstance();
        groupDataRef = groupDatabase.getReference().child("group_intro");
        groupAdd = rootview.findViewById(R.id.group_add);
        groupCancel = rootview.findViewById(R.id.group_cancel);

        groupIntro = new group_intro();

        groupAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String groupName = groupNameEt.getText().toString();

                if(TextUtils.isEmpty(groupName)){
                    Toast.makeText(getActivity(), "Enter value", LENGTH_SHORT).show();
                    
                }
                else
                {
                    addDataToFirebase(groupName);
                }
            }
        });

        groupCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, fragment_homePage.newInstance())
                        .commitNow();
            }
        });
        return rootview;

    }

    private void addDataToFirebase(String groupName) {
        groupIntro.setGroupName(groupName);

        groupDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                groupDataRef.setValue(groupIntro);
                Toast.makeText(getActivity(), "data added", LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Failed to add data", LENGTH_SHORT).show();
            }
        });
    }


}