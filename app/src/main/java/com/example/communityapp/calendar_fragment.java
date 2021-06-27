package com.example.communityapp;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;


public class calendar_fragment extends Fragment {

    CalendarView eventCal;
    Dialog popUpCal;

    public calendar_fragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_calendar_fragment, container, false);

        eventCal = rootView.findViewById(R.id.event_cal);

        eventCal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                popUpCal.show();
            }
        });

        popUpAddCal();

        return rootView;    }

    private void popUpAddCal() {
        popUpCal = new Dialog(getContext());
        popUpCal.setContentView(R.layout.fragment_add_group_intro2);
        popUpCal.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popUpCal.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        popUpCal.getWindow().getAttributes().gravity = Gravity.TOP;


    }
}