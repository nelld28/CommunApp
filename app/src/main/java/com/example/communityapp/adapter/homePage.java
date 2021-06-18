package com.example.communityapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.communityapp.Model.group_intro;
import com.example.communityapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class homePage extends FirebaseRecyclerAdapter<group_intro, homePage.group_introViewHolder> {


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public homePage(@NonNull FirebaseRecyclerOptions<group_intro> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull homePage.group_introViewHolder holder, int position, @NonNull group_intro model) {

        holder.card_group_name.setText(model.getGroupName());
    }

    @NonNull
    @Override
    public homePage.group_introViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_card_row, parent, false);

        return new homePage.group_introViewHolder(view);
    }

//  to call from group_intro data model
    public class group_introViewHolder extends RecyclerView.ViewHolder {
        TextView card_group_name;
    public group_introViewHolder(@NonNull View itemView) {
        super(itemView);
        card_group_name = itemView.findViewById(R.id.card_group_name);

    }
}
}
