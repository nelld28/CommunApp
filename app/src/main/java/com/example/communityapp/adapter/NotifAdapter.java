package com.example.communityapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.communityapp.Model.Note;
import com.example.communityapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Objects;

public class NotifAdapter extends RecyclerView.Adapter<NotifAdapter.MyViewHolder> {

    Context nContext;
    List<Note> nData;

    public NotifAdapter(Context nContext, List<Note> nData) {
        this.nContext = nContext;
        this.nData = nData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(nContext).inflate(R.layout.notif_list_rv, parent, false);

        return new MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.nCommName.setText(nData.get(position).getComName());
        holder.nInNum.setText(nData.get(position).getInfectecdNum());
        holder.nRecNum.setText(nData.get(position).getRecoveredNum());
        holder.nMatRec.setText(nData.get(position).getMaterial());

    }

    @Override
    public int getItemCount() {
        return nData.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView nCommName, nInNum, nRecNum, nMatRec;
            String priorityType;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            int position = getAdapterPosition();
             nCommName = itemView.findViewById(R.id.nl_comm_name);
             nInNum = itemView.findViewById(R.id.inf_Num);
             nRecNum = itemView.findViewById(R.id.nRec_Num);
             nMatRec = itemView.findViewById(R.id.rec_mat);



             FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference prRef = firebaseDatabase.getReference()
                    .child("Notifs");

            prRef.child("priority").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                @Override
                public void onSuccess(DataSnapshot snapshot) {
                    int position = getAdapterPosition();
                        priorityType = nData.get(position).getPriority();

                        if (priorityType.compareTo("Low")==0){
                            nMatRec.setBackgroundColor(Color.GREEN);
                        }
                        else if(priorityType.compareTo("High")==0)
                        {
                            nMatRec.setBackgroundColor(Color.RED);
                        }
                        else if (priorityType.compareTo("Medium")==0){
                            nMatRec.setBackgroundColor(Color.YELLOW);
                        }
                        else {
                            showMessage();
                        }
                }
            });






        }
    }

    private void showMessage() {
        Toast.makeText(nContext.getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
    }


}

//




