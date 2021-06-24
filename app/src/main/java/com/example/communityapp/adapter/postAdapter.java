package com.example.communityapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.communityapp.Model.Post;
import com.example.communityapp.R;
import com.example.communityapp.post_Details;

import java.util.List;

public class postAdapter extends RecyclerView.Adapter<postAdapter.MyViewHolder> {

    Context mContext;
    List<Post> mData;

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
//                    postDetailAct.putExtra("userName", mData.get(position).getUser);
                    long timestamp = (long) mData.get(position).getTimeStamp();
                    postDetailAct.putExtra("postDate", timestamp);
                    mContext.startActivity(postDetailAct);
                }
            });
        }
    }
}