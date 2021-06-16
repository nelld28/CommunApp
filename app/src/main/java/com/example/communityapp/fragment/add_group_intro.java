package com.example.communityapp.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.communityapp.Model.group_intro;
import com.example.communityapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.widget.Toast.*;
import static android.widget.Toast.LENGTH_SHORT;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link add_group_intro#newInstance} factory method to
 * create an instance of this fragment.
 */
public class add_group_intro extends Fragment {

    private EditText groupNameEt;
    private ImageView groupAdd;

    FirebaseDatabase groupDatabase;

    DatabaseReference groupDataRef;

    public add_group_intro() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static add_group_intro newInstance() {
        add_group_intro fragment = new add_group_intro();

        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview =  inflater.inflate(R.layout.fragment_add_group_intro2, container, false);

        groupNameEt = rootview.findViewById(R.id.group_name);
        groupDatabase = FirebaseDatabase.getInstance();
        groupDataRef = groupDatabase.getReference().child("group_intro");
        groupAdd = rootview.findViewById(R.id.group_add);

        groupAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertData();
            }
        });
        return rootview;

    }

    private void insertData() {
        String groupName = groupNameEt.getText().toString();

        group_intro groupInt = new group_intro(groupName);

        groupDataRef.push().setValue(groupInt);

        Toast.makeText(getActivity(), "data inserted", LENGTH_SHORT).show();
    }


}