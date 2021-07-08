package com.example.communityapp;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.provider.CalendarContract;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;


public class calendar_fragment extends Fragment {

    EditText title, location, description;
    Button addBtn;

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

        title = rootView.findViewById(R.id.cal_Title);
        location = rootView.findViewById(R.id.cal_Loc);
        description = rootView.findViewById(R.id.cal_desc);
        addBtn =rootView.findViewById(R.id.call_add_btn);

//        to add the calendar event
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if all the detail fields are filled then calendar event is created and
//                sent to calendar using intent
                if(!title.getText().toString().isEmpty() &&
                !location.getText().toString().isEmpty() &&
                !description.getText().toString().isEmpty()){

                    Intent intent =new Intent(Intent.ACTION_INSERT);
                    intent.setData(CalendarContract.Events.CONTENT_URI);
                    intent.putExtra(CalendarContract.Events.TITLE, title.getText()
                    .toString());
                    intent.putExtra(CalendarContract.Events.EVENT_LOCATION, location.getText()
                            .toString());
                    intent.putExtra(CalendarContract.Events.DESCRIPTION, description.getText()
                    .toString());
                    intent.putExtra(CalendarContract.Events.ALL_DAY, "true");
                    intent.putExtra(Intent.EXTRA_EMAIL, "");

                    startActivity(intent);

                }
                else{
                    Toast.makeText(getActivity(), "Fill the values.", Toast.LENGTH_SHORT).show();
                }
            }
        });



        return rootView;    }


}