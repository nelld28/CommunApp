package com.example.communityapp.adapter;

import android.content.Context;
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
import com.example.communityapp.Model.group_intro;
import com.example.communityapp.R;
import com.example.communityapp.group_detail;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
                   Intent groupDetail = new Intent(mContext,
                           group_detail.class);
                   mContext.startActivity(groupDetail);

                }
            });

        }
    }


}
