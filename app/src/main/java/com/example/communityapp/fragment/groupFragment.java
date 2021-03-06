package com.example.communityapp.fragment;

import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.communityapp.Model.Members;
import com.example.communityapp.Model.group_intro;
import com.example.communityapp.R;
import com.example.communityapp.adapter.CommAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link groupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class groupFragment extends Fragment {


    FirebaseDatabase firebaseDatabaseRV;
    DatabaseReference databaseReferenceRV;
//    DatabaseReference databaseReferenceRVM;

    private RecyclerView commRV;
    CommAdapter commAdapter;

    List<group_intro> commList;
//    List<Members> memList;

    FirebaseUser currentUser;
//    String currentUserId, commUserId;





    public groupFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static groupFragment newInstance() {

        return new groupFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();

//        firebase referencing to set community
        databaseReferenceRV.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                commList = new ArrayList<>();
                for (DataSnapshot commSnap : snapshot.getChildren()){
                    group_intro group = commSnap.getValue(group_intro.class);
                    group.setPostKey(commSnap.getKey());
                    commList.add(group);


                }

                commAdapter = new CommAdapter(getActivity(), commList);
                commRV.setAdapter(commAdapter);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_group, container, false);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

        firebaseDatabaseRV = FirebaseDatabase.getInstance();
        databaseReferenceRV = firebaseDatabaseRV.getReference();


        commRV = rootview.findViewById(R.id.community_groupRV);
        commRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        commRV.setHasFixedSize(true);
        databaseReferenceRV = firebaseDatabaseRV.getReference("Communities");
//         databaseReferenceRVM= databaseReferenceRV.child("Members");

        currentUser.getUid();

        return rootview;
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}