package com.example.communityapp.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.communityapp.Model.Note;
import com.example.communityapp.R;
import com.example.communityapp.adapter.NotifAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
 * Use the {@link NotifFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotifFragment extends Fragment {

    View rootview;
    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference nRef;

    private RecyclerView notifRV;
    NotifAdapter notifAdapter;

    List<Note> notesList;


    public NotifFragment() {
        // Required empty public constructor
    }


    public static NotifFragment newInstance(String param1, String param2) {

        return new NotifFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();

//        firebase referencing to add value to note model
        nRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                notesList = new ArrayList<>();
                for (DataSnapshot nSnap : snapshot.getChildren()){
                    Note note = nSnap.getValue(Note.class);
                    note.setPostKey(nSnap.getKey());
                    notesList.add(note);
                }
                notifAdapter = new NotifAdapter(getActivity(), notesList);
                notifRV.setAdapter(notifAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_notif, container, false);

        firebaseDatabase = FirebaseDatabase.getInstance();
        nRef = FirebaseDatabase.getInstance().getReference();

        notifRV = rootview.findViewById(R.id.notif_RV);
        notifRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        notifRV.setHasFixedSize(true);
        nRef = firebaseDatabase.getReference("Notifs");

        return rootview;
    }


}