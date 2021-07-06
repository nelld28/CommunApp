package com.example.communityapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class group_detail extends AppCompatActivity {

    Dialog popNotif;
    TextView comName, inNum, recNum, matReq;
     RadioButton lowButton, medButton, highButton;
     ImageView addNotifBtn;

     TextView CommunitName, CommunityDesc;
    private RecyclerView groupRV;

    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);

        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);


        CommunitName = findViewById(R.id.community_Name);
        CommunityDesc = findViewById(R.id.group_desc);
        groupRV = findViewById(R.id.gr_RV);

        String groupName = getIntent().getExtras().getString("groupName");
        CommunitName.setText(groupName);

        String groupDesc = getIntent().getExtras().getString("groupDesc");
        CommunityDesc.setText(groupDesc);
        CommunityDesc.setTextColor(Color.BLACK);



//        String community_Cr_ID = getIntent().getExtras().getString("groupCreatorId");

        FloatingActionButton fab_notif = (FloatingActionButton) findViewById(R.id.add_new_notif);

        fab_notif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popNotif.show();
            }
        });

//        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
//        DatabaseReference cNameRef = firebaseDatabase.getReference().child("Communities");
//
//        String key = cNameRef.getKey();
//
//        cNameRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot snapshot1 : snapshot.getChildren()){
//                    String gettingCName = snapshot1.child("commName")
//                            .getValue(String.class);
//                    Note note = new Note();
//                    note.setComName(gettingCName);
//                }
//
//                Toast.makeText(group_detail.this, "" + comName, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

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
        recNum = popNotif.findViewById(R.id.nRec_Num);
        matReq = popNotif.findViewById(R.id.mat_Req);
        lowButton = popNotif.findViewById(R.id.low_Button);
        medButton = popNotif.findViewById(R.id.med_Button);
        highButton = popNotif.findViewById(R.id.high_Button);
        addNotifBtn = popNotif.findViewById(R.id.add_Notif);

        String groupName = getIntent().getExtras().getString("groupName");
        comName.setText(groupName);

        addNotifBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Note note = new Note();
                if(!inNum.getText().toString().isEmpty() &&
                    !recNum.getText().toString().isEmpty() &&
                    !matReq.getText().toString().isEmpty()
                    ) {

                    String communityName = comName.getText().toString();
                    String infectNum = inNum.getText().toString();
                    String recoverNum = recNum.getText().toString();
                    String materialReq = matReq.getText().toString();
                    String lowPriority = lowButton.getText().toString();
                    String medPriority = medButton.getText().toString();
                    String highPriority = highButton.getText().toString();

                    if (lowButton.isChecked()) {
                        note.setPriority(lowPriority);

                    } else if (medButton.isChecked()) {
                        note.setPriority(medPriority);
                    } else if (highButton.isChecked()) {
                        note.setPriority(highPriority);
                    } else {
                        Toast.makeText(group_detail.this, "Wrong buttons", Toast.LENGTH_SHORT).show();
                    }

                    note.setComName(communityName);
                    note.setInfectecdNum(infectNum);
                    note.setRecoveredNum(recoverNum);
                    note.setMaterial(materialReq);

                    addToFb(note);
                }
                else{
                    showMessage("Please fill all required fields");
                }

            }
        });
    }

    private void showMessage(String message) {
        Toast.makeText(group_detail.this, message, Toast.LENGTH_SHORT).show();

    }


//    private void getiingCName() {
//        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
//
//        DatabaseReference cNameRef = firebaseDatabase.getReference();
//
//        cNameRef.child("Communities")
//
////        ValueEventListener cNameListener = new ValueEventListener() {
////            @Override
////            public void onDataChange(@NonNull DataSnapshot snapshot) {
////                group_intro groupIntro = snapshot.getValue(group_intro.class);
////
////            }
////
////            @Override
////            public void onCancelled(@NonNull DatabaseError error) {
////                Log.d(TAG, "onCancelled: ", error.toException());
////
////            }
////        };
//
//    }

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