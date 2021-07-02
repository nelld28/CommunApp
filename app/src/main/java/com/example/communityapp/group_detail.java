package com.example.communityapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.communityapp.Model.Note;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class group_detail extends AppCompatActivity {

    Dialog popNotif;
    private TextView comName, inNum, recNum, matReq;
    private RadioButton lowButton, medButton, highButton;
    private ImageView addNotifBtn;



    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;

    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);

        FloatingActionButton fab_notif = (FloatingActionButton) findViewById(R.id.add_new_notif);
        fab_notif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popNotif.show();
            }
        });

        addNotif();

    }

    private void addNotif() {

        popNotif = new Dialog(this);
        popNotif.setContentView(R.layout.notif_dialog);
        popNotif.getWindow().setBackgroundDrawable(new ColorDrawable(Color
                .GRAY));
        popNotif.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams
                .WRAP_CONTENT);
        popNotif.getWindow().getAttributes().gravity = Gravity.CENTER;

        comName = popNotif.findViewById(R.id.com_Name);
        inNum = popNotif.findViewById(R.id.in_Num);
        recNum = popNotif.findViewById(R.id.rec_Num);
        matReq = popNotif.findViewById(R.id.mat_Req);
        lowButton = popNotif.findViewById(R.id.low_Button);
        medButton = popNotif.findViewById(R.id.med_Button);
        highButton = popNotif.findViewById(R.id.high_Button);
        addNotifBtn = popNotif.findViewById(R.id.add_Notif);

        

        addNotifBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Note note = new Note();
                String communityName = comName.getText().toString();
                String infectNum = inNum.getText().toString();
                String recoverNum = recNum.getText().toString();
                String materialReq = matReq.getText().toString();
                String lowPriority = lowButton.getText().toString();
                String medPriority = medButton.getText().toString();
                String highPriority = highButton.getText().toString();

                if (lowButton.isChecked()){
                    note.setPriority(lowPriority);
                }
                else if (medButton.isChecked()){
                    note.setPriority(medPriority);
                }
                else if(highButton.isChecked()){
                    note.setPriority(highPriority);
                }
                else {
                    Toast.makeText(group_detail.this, "Wrong buttons", Toast.LENGTH_SHORT).show();
                }

                note.setComName(communityName);
                note.setInfectecdNum(infectNum);
                note.setRecoveredNum(recoverNum);
                note.setMaterial(materialReq);
                

                addToFb(note);

                getiingCName();
            }
        });
    }

    private void getiingCName() {

    }

    private void addToFb(Note note) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference nRef = firebaseDatabase.getReference("Notifs").push();
        
        String nKey = nRef.getKey();
        note.setPostKey(nKey);
        
        nRef.setValue(note).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                popNotif.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(group_detail.this, "Not added", Toast.LENGTH_SHORT).show();
            }
        });
        
    }
}