package com.example.communityapp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.communityapp.Model.Note;

import java.util.List;

public class NotifAdapter extends RecyclerView.Adapter<NotifAdapter.NotifHolder> {

    private Context nContext;
    private List<Note> nData;

    @NonNull
    @Override
    public NotifAdapter.NotifHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull NotifAdapter.NotifHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return nData.size();
    }

    public class NotifHolder extends RecyclerView.ViewHolder{

        public NotifHolder(@NonNull View itemView) {
            super(itemView);


        }
    }
}
