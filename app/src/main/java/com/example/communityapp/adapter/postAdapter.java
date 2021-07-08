package com.example.communityapp.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.communityapp.MainActivity;
import com.example.communityapp.Model.Post;
import com.example.communityapp.R;
import com.example.communityapp.adminActivity;
import com.example.communityapp.post_Details;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class postAdapter extends RecyclerView.Adapter<postAdapter.MyViewHolder> {

    Context mContext;
    List<Post> mData;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = firebaseAuth.getCurrentUser();

    public postAdapter(Context mContext, List<Post> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View rootView = LayoutInflater.from(mContext).inflate(R.layout.row_post_list, parent, false);

        return new MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.tcTitle.setText(mData.get(position).getTitle());
        Glide.with(mContext).load(mData.get(position).getPicture()).into(holder.postImg);
        Glide.with(mContext).load(mData.get(position).getUserPhoto()).into(holder.imgPostProfile);


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tcTitle;
        ImageView postImg, imgPostProfile;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tcTitle = itemView.findViewById(R.id.row_post_title);
            postImg = itemView.findViewById(R.id.row_post_img);
            imgPostProfile = itemView.findViewById(R.id.row_post_profile);

//            to access post detail page
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent postDetailAct = new Intent(mContext, post_Details.class);
                    int position = getAdapterPosition();

                    postDetailAct.putExtra("title", mData.get(position).getTitle());
                    postDetailAct.putExtra("postImage", mData.get(position).getPicture());
                    postDetailAct.putExtra("description", mData.get(position).getDescription());
                    postDetailAct.putExtra("postKey", mData.get(position).getPostKey());
                    postDetailAct.putExtra("userPhoto", mData.get(position).getUserPhoto());
                    postDetailAct.putExtra("userId", mData.get(position).getUserId());
//                    postDetailAct.putExtra("userName", mData.get(position).getUser);
                    long timestamp = (long) mData.get(position).getTimeStamp();
                    postDetailAct.putExtra("postDate", timestamp);
                    mContext.startActivity(postDetailAct);
                }
            });

//            To delete the post
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = getAdapterPosition();

                    String postId = mData.get(position).getUserId();


                    String id = currentUser.getUid();


                    if (id.equals(postId)){
                        DatabaseReference delRef = FirebaseDatabase.getInstance()
                                .getReference().child("Posts").child(mData.get(position).getPostKey());
                        delRef.removeValue();

                        notifyItemRemoved(getAdapterPosition());

                        Toast.makeText(mContext.getApplicationContext(), "Post Deleted" , Toast.LENGTH_SHORT).show();

                    }

                    else {
                        Toast.makeText(mContext.getApplicationContext(), "Not allowed", Toast.LENGTH_SHORT).show();
                    }


                    return true;
                }
            });


        }
    }

}
