package com.example.communityapp.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toolbar;

import com.example.communityapp.Model.group_intro;
import com.example.communityapp.R;
import com.example.communityapp.adapter.homePage;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_homePage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_homePage extends Fragment {

    View rootview;

//    floating action button for adding community
    private FloatingActionButton fab_g;

    private CardView groupCard;

    public static fragment_homePage newInstance() {
        return new fragment_homePage();
    }

//    Recycler view
    private RecyclerView recyclerView;
    homePage homePageAdapter;
    DatabaseReference RVdatabaseReference;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }


    @Override
    public void onStart() {
        super.onStart();
        homePageAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        homePageAdapter.stopListening();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_home_page, container, false);

        fab_g = rootview.findViewById(R.id.fab_group);

        fab_g.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.homePage_frag, add_group_intro.newInstance())
                        .commitNow();
            }
        });
        // end of fab

//        RECYCLERvIEW
        RVdatabaseReference = FirebaseDatabase.getInstance().getReference();

        recyclerView = rootview.findViewById(R.id.group_recycler_view);

        recyclerView.setLayoutManager(
                new LinearLayoutManager(getActivity()));

        FirebaseRecyclerOptions<group_intro> options =
                new FirebaseRecyclerOptions.Builder<group_intro>()
                .setQuery(RVdatabaseReference, group_intro.class)
                .build();
        homePageAdapter = new homePage(options);
        recyclerView.setAdapter(homePageAdapter);

//        card group
        groupCard = rootview.findViewById(R.id.group_card);



//        groupCard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getActivity().getSupportFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.content_frame, groupFragment.newInstance())
//                        .addToBackStack(null)
//                        .commitNow();
//            }
//        });

        return rootview;
    }




}