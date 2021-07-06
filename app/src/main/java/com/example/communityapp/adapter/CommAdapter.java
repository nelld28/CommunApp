package com.example.communityapp.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.communityapp.Model.Members;
import com.example.communityapp.Model.group_intro;
import com.example.communityapp.R;
import com.example.communityapp.adminActivity;
import com.example.communityapp.group_detail;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class CommAdapter extends RecyclerView.Adapter<CommAdapter.MyViewHolder> {
    Context mContext;
    List<group_intro> mData;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = firebaseAuth.getCurrentUser();

    public CommAdapter(Context mContext, List<group_intro> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.list_card_row, parent, false);

        return new MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.communityName
                .setText(mData
                        .get(position)
                        .getCommName());

        Glide.with(mContext).load(mData.get(position).getCommImg()).into(holder.ctImg);
        Glide.with(mContext).load(mData.get(position).getCommUserImg()).into(holder.ctUserImg);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{


        TextView communityName;
        ImageView ctImg, ctUserImg;
        String memberId;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            communityName = itemView.findViewById(R.id.card_group_name);
            ctImg = itemView.findViewById(R.id.card_group_img);
            ctUserImg = itemView.findViewById(R.id.commmunity_creator_img);


            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    int position = getAdapterPosition();
                    String commId = mData.get(position).getCommUserId();

                    String id = currentUser.getUid();

                    if(id.equals(commId)){
                        DatabaseReference delComm = FirebaseDatabase.getInstance()
                                .getReference().child("Communities")
                                .child(mData.get(position).getPostKey());
                        delComm.removeValue();

                        notifyItemRemoved(position);

                        Toast.makeText(mContext.getApplicationContext(), "Deleted",
                                Toast.LENGTH_SHORT).show();

                    }
                    else {
                        Toast.makeText(mContext.getApplicationContext(), "No Delete allowed",
                                Toast.LENGTH_SHORT).show();

                    }


                    return true;
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    int position = getAdapterPosition();
                    String cuUID1 = currentUser.getUid();

                    String commId = mData.get(position).getCommUserId();


//                    HashMap<String, String> hm = mData.get(position).getMembers();
//
//                    Iterator myIterator =hm.keySet().iterator();
//                    while (myIterator.hasNext()){
//                        String key = (String) myIterator.next();
//                        String value = (String)hm.get(key);
////                                    Toast.makeText(mContext.getApplicationContext(), "key:"+ key + " "
////                                            + "value:" +value, Toast.LENGTH_SHORT).show();
//
//                        Toast.makeText(mContext.getApplicationContext(), "" +hm.values().toString(), Toast.LENGTH_SHORT).show();
//                    }


                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference coRef = firebaseDatabase.getReference("Communities");

                    DatabaseReference memRef = coRef.child("Members");
                    memRef.child("MemberId").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                        @Override
                        public void onSuccess(DataSnapshot snapshot) {
                            int position = getAdapterPosition();
                            memberId = mData.get(position).getMemberIdKey();
                            Toast.makeText(mContext.getApplicationContext(), ""+memberId, Toast.LENGTH_SHORT).show();
                        }
                    });


//                    AlertDialog.Builder joinDialog = new AlertDialog.Builder(itemView.getContext());
//                        joinDialog.setMessage("Join Group?");
//                        joinDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                                int position = getAdapterPosition();
//
//                                String cUID = currentUser.getUid();
//
////                            TO ADD MEMBER
//                            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
//                            DatabaseReference coRef = firebaseDatabase.getReference("Communities");
//
//                            String coKey = mData.get(position).getPostKey();
//
//                            coRef.child(coKey).child("Members").child("MemberId").push().setValue(cUID);
//
////                            HashMap<String, String> hm = mData.get(position).getMembers();
////
////
////                                Iterator myIterator =hm.keySet().iterator();
////                                while (myIterator.hasNext()){
////                                    String key = (String) myIterator.next();
////                                    String value = (String)hm.get(key);
//////                                    Toast.makeText(mContext.getApplicationContext(), "key:"+ key + " "
//////                                            + "value:" +value, Toast.LENGTH_SHORT).show();
////
////                                    Toast.makeText(mContext.getApplicationContext(), "" +hm.values().toString(), Toast.LENGTH_SHORT).show();
////                                }
////
////                                hm.containsKey(cUID);
////
////                           END OF ADDING MEMBER
//
//
//                                Intent groupDetail = new Intent(mContext, group_detail.class);
//
//                                groupDetail.putExtra("groupName", mData.get(position).getCommName());
//                                groupDetail.putExtra("groupDesc", mData.get(position).getCommDesc());
//                                groupDetail.putExtra("groupImg", mData.get(position).getCommImg());
//                                groupDetail.putExtra("groupCreatorId", mData.get(position).getCommUserId());
//                                groupDetail.putExtra("groupId", mData.get(position).getPostKey());
//
//                                mContext.startActivity(groupDetail);
//
//
//
//                            }
//                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.cancel();
//                            }
//                        });
//
//                        AlertDialog alertDialog = joinDialog.create();
//                        alertDialog.show();

//                          END OF FIRST ALERTDIALOG


//                    Toast.makeText(mContext.getApplicationContext(), ""+ memberId, Toast.LENGTH_SHORT).show();
//                    if (cuUID1.equals(commId) || cuUID1.equals(memberId)){
//                        Intent groupDetail = new Intent(mContext, group_detail.class);
//
//                        groupDetail.putExtra("groupName", mData.get(position).getCommName());
//                        groupDetail.putExtra("groupDesc", mData.get(position).getCommDesc());
//                        groupDetail.putExtra("groupImg", mData.get(position).getCommImg());
//                        groupDetail.putExtra("groupCreatorId", mData.get(position).getCommUserId());
//                        groupDetail.putExtra("groupId", mData.get(position).getPostKey());
//
//                        mContext.startActivity(groupDetail);
//
//
//                    }
//                    else{
//                        //                    START OF ALERT DIALOG
//                        AlertDialog.Builder joinDialog = new AlertDialog.Builder(itemView.getContext());
//                        joinDialog.setMessage("Join Group?");
//                        joinDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                                int position = getAdapterPosition();
//
//                                String cUID = currentUser.getUid();
//
////                            TO ADD MEMBER
//                            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
//                            DatabaseReference coRef = firebaseDatabase.getReference("Communities");
//
//                            String coKey = mData.get(position).getPostKey();
//
//                            coRef.child(coKey).child("Members").push().setValue(cUID);
//
//                            HashMap<String, String> hm = mData.get(position).getMembers();
//
//
//                                Iterator myIterator =hm.keySet().iterator();
//                                while (myIterator.hasNext()){
//                                    String key = (String) myIterator.next();
//                                    String value = (String)hm.get(key);
////                                    Toast.makeText(mContext.getApplicationContext(), "key:"+ key + " "
////                                            + "value:" +value, Toast.LENGTH_SHORT).show();
//
//                                    Toast.makeText(mContext.getApplicationContext(), "" +hm.values().toString(), Toast.LENGTH_SHORT).show();
//                                }
//
//                                hm.containsKey(cUID);
////                                https://www.geeksforgeeks.org/hashmap-containskey-method-in-java/
////                            String gh = hm.put(memberId, );
//
//
//
////                                Toast.makeText(mContext.getApplicationContext(), "" + hm, Toast.LENGTH_SHORT).show();
//
//
////
//
////                            DatabaseReference doRef =coRef.child(coKey).child("Members");
////
////                            doRef.child("MemberID").setValue(cUID);
//
//
//
////                            DatabaseReference egRef = doRef.child("MemberId");
////                            String eKey = egRef.child("MemberId").toString();
//
//
////                            String eKey = mData.get(position).getMemberIdKey();
//
//
////                            String dKey = doRef.getKey();
//
////                            DatabaseReference mnRef = coRef.getRef().child("Members");
//
////                            group_intro groupIntro = new group_intro();
////                            Members members = new Members(currentUser.getUid(), coKey);
////                            String mKey = mnRef.child(coKey).getKey();
////                            members.setPostKey(mKey);
//
////                            String mKey = coRef.child(coKey).child("Members").child("MembersID").getKey();
//
//
////                            DatabaseReference mnRef = coRef.getRef().child("Members");
//
////                            group_intro groupIntro = new group_intro();
////                            Members members = new Members(currentUser.getUid(), coKey);
////                            String mKey = mnRef.child(coKey).getKey();
////                            members.setPostKey(mKey);
//
//
//
////                           END OF ADDING MEMBER
//
//
//                                Intent groupDetail = new Intent(mContext, group_detail.class);
//
//                                groupDetail.putExtra("groupName", mData.get(position).getCommName());
//                                groupDetail.putExtra("groupDesc", mData.get(position).getCommDesc());
//                                groupDetail.putExtra("groupImg", mData.get(position).getCommImg());
//                                groupDetail.putExtra("groupCreatorId", mData.get(position).getCommUserId());
//                                groupDetail.putExtra("groupId", mData.get(position).getPostKey());
//
//                                mContext.startActivity(groupDetail);
//
//
//
//                            }
//                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.cancel();
//                            }
//                        });
//
//                        AlertDialog alertDialog = joinDialog.create();
//                        alertDialog.show();
////                    END OF ALERT DIALOG
//
//
//                    }




//                    int position = getAdapterPosition();

//                        AlertDialog.Builder joinDialog = new AlertDialog.Builder(itemView.getContext());
//                        joinDialog.setMessage("Join group?");
//                        joinDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                int position = getAdapterPosition();
//                                String cUid = currentUser.getUid();
////                            String cName = currentUser.getDisplayName();
//
//                                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
//                                DatabaseReference mRef = firebaseDatabase.getReference("Communities");
//
//                                String pKey = mData.get(position).getPostKey();
//
//                                mRef.child(pKey).child("Members").child("memberID").push().setValue(cUid);
////                            mRef.child(pKey).child("Members").push().setValue(cName);
//
//                                Intent groupDetail = new Intent(mContext, group_detail.class);
//
//                                groupDetail.putExtra("groupName", mData.get(position).getCommName());
//                                groupDetail.putExtra("groupDesc", mData.get(position).getCommDesc());
//                                groupDetail.putExtra("groupImg", mData.get(position).getCommImg());
//                                groupDetail.putExtra("groupCreatorId", mData.get(position).getCommUserId());
//                                groupDetail.putExtra("groupId", mData.get(position).getPostKey());
//
//                                mContext.startActivity(groupDetail);
//
//                            }
//                        });
//
//                        joinDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.cancel();
//                            }
//                        });
//
//                        AlertDialog alertDialog = joinDialog.create();
//                        alertDialog.show();


//                    }




                }
            });

        }
    }




}
