package com.example.communityapp.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toolbar;

import com.example.communityapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_homePage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_homePage extends Fragment {

    View rootview;

    private FloatingActionButton fab_g;

    public static fragment_homePage newInstance() {
        return new fragment_homePage();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

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
        return rootview;
    }


}