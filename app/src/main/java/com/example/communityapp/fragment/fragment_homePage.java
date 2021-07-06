package com.example.communityapp.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.communityapp.Model.Post;
import com.example.communityapp.Model.group_intro;
import com.example.communityapp.R;
import com.example.communityapp.adapter.homePage;
import com.example.communityapp.adapter.postAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_homePage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_homePage extends Fragment {

    View rootview;



    public static fragment_homePage newInstance() {
        return new fragment_homePage();
    }

//    Recycler view
//    private RecyclerView recyclerView;
//    homePage homePageAdapter;

    FirebaseDatabase fbDatabase;
    DatabaseReference RVdatabaseReference;

    private RecyclerView postRV;
    postAdapter postAdapterRV;

    List<Post> postList;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }


    @Override
    public void onStart() {
        super.onStart();
//        homePageAdapter.startListening();
        RVdatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList = new ArrayList<>();
                for (DataSnapshot postsnap : snapshot.getChildren()){
                    Post post = postsnap.getValue(Post.class);
                    post.setPostKey(postsnap.getKey());
                    postList.add(post);

                }

                postAdapterRV = new postAdapter(getActivity(), postList);
                postRV.setAdapter(postAdapterRV);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });


    }

    @Override
    public void onStop() {
        super.onStop();
//        homePageAdapter.stopListening();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_home_page, container, false);


        RVdatabaseReference = FirebaseDatabase.getInstance().getReference();

        postRV = rootview.findViewById(R.id.postRV_list);
        postRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        postRV.setHasFixedSize(true);
        fbDatabase = FirebaseDatabase.getInstance();
        RVdatabaseReference = fbDatabase.getReference("Posts");


        FirebaseRecyclerOptions<Post> options = new FirebaseRecyclerOptions.Builder<Post>()
                .setQuery(RVdatabaseReference, Post.class)
                .build();

//        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT||ItemTouchHelper.RIGHT) {
//            @Override
//            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
//                return false;
//            }
//
//            @Override
//            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//
//            }
//        })

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Toast.makeText(getActivity(), "Item has been swiped", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(postRV);


//        RECYCLERvIEW
//        RVdatabaseReference = FirebaseDatabase.getInstance().getReference();
//
//        recyclerView = rootview.findViewById(R.id.group_recycler_view);
//
//        recyclerView.setLayoutManager(
//                new LinearLayoutManager(getActivity()));
//
//        FirebaseRecyclerOptions<group_intro> options =
//                new FirebaseRecyclerOptions.Builder<group_intro>()
//                .setQuery(RVdatabaseReference, group_intro.class)
//                .build();
//        homePageAdapter = new homePage(options);
//        recyclerView.setAdapter(homePageAdapter);

//        card group
//        groupCard = rootview.findViewById(R.id.group_card);
//
//        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
//            @Override
//            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
//                return false;
//            }
//
//            @Override
//            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
//                getActivity().getSupportFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.content_frame, groupFragment.newInstance())
//                        .addToBackStack(null)
//                        .commitNow();
//            }
//
//            @Override
//            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
//
//            }
//        });




        if(container != null){
            container.removeAllViews();
        }

        return rootview;
    }




}