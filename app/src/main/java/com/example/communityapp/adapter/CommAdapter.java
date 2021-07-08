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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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


//            To delete a community by the community creator
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
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                                .getReference()
                                .child("Communities")
                                .child("Members");
                        Toast.makeText(mContext.getApplicationContext(), "No Delete allowed",
                                Toast.LENGTH_SHORT).show();

                    }


                    return true;
                }
            });

//            To join a group or not
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    int position = getAdapterPosition();
                    String cuUID1 = currentUser.getUid();

                    String commId = mData.get(position).getCommUserId();

                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference coRef = firebaseDatabase.getReference("Communities");

//                    THIS IS COMMUNITY ADAPTER
                    DatabaseReference memRef = coRef.child("Members");
                    memRef.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                        @Override
                        public void onSuccess(DataSnapshot snapshot) {
                            int position = getAdapterPosition();
                            String cuUID1 = currentUser.getUid();
                            memberId= String.valueOf(mData.get(position).getMembers());

                            boolean mem = memberId.contains(cuUID1);
//                            Toast.makeText(mContext.getApplicationContext(), ""+ mem, Toast.LENGTH_SHORT).show();

//                            user can enter community detail page if they have already joined the community previously
                            if (mem)
                            {
                                Intent groupDetail = new Intent(mContext, group_detail.class);

                                groupDetail.putExtra("groupName", mData.get(position).getCommName());
                                groupDetail.putExtra("groupDesc", mData.get(position).getCommDesc());
                                groupDetail.putExtra("groupImg", mData.get(position).getCommImg());
                                groupDetail.putExtra("groupCreatorId", mData.get(position).getCommUserId());
                                groupDetail.putExtra("groupId", mData.get(position).getPostKey());

                                mContext.startActivity(groupDetail);
                            }
//                            user can enter the community detail page if they are the community creator
                            else if (commId.equals(cuUID1)){
                                Intent groupDetail = new Intent(mContext, group_detail.class);

                                groupDetail.putExtra("groupName", mData.get(position).getCommName());
                                groupDetail.putExtra("groupDesc", mData.get(position).getCommDesc());
                                groupDetail.putExtra("groupImg", mData.get(position).getCommImg());
                                groupDetail.putExtra("groupCreatorId", mData.get(position).getCommUserId());
                                groupDetail.putExtra("groupId", mData.get(position).getPostKey());

                                mContext.startActivity(groupDetail);
                            }
//                            if they are not any of the above two then they have to join the community through the dialog box
                            else
                            {
                                AlertDialog.Builder joinDialog = new AlertDialog.Builder(itemView.getContext());
                                joinDialog.setMessage("Join Group?");
                                joinDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        int position = getAdapterPosition();

                                        String cUID = currentUser.getUid();

//                            Adding the member to firebase
                                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                                        DatabaseReference coRef = firebaseDatabase.getReference("Communities");

                                        String coKey = mData.get(position).getPostKey();

                                        coRef.child(coKey).child("Members").push().setValue(cUID);


                                        Intent groupDetail = new Intent(mContext, group_detail.class);

                                        groupDetail.putExtra("groupName", mData.get(position).getCommName());
                                        groupDetail.putExtra("groupDesc", mData.get(position).getCommDesc());
                                        groupDetail.putExtra("groupImg", mData.get(position).getCommImg());
                                        groupDetail.putExtra("groupCreatorId", mData.get(position).getCommUserId());
                                        groupDetail.putExtra("groupId", mData.get(position).getPostKey());

                                        mContext.startActivity(groupDetail);



                                    }
                                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });

                                AlertDialog alertDialog = joinDialog.create();
                                alertDialog.show();
                            }



                        }
                    });




                }
            });

        }
    }




}
