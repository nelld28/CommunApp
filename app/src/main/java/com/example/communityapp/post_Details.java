package com.example.communityapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.media.Image;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.communityapp.Model.Comment;
import com.example.communityapp.Model.Post;
import com.example.communityapp.R;
import com.example.communityapp.adapter.CommentAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class post_Details extends AppCompatActivity {

    ImageView imgPost, imgUserPost, imgCurrentUser;
    TextView txtPostDesc, txtPostDateName, txtPostTitle;
    EditText editTextComment;
    Button addCommentBtn, editPostBtn;
    String post_Key;

    FirebaseAuth firebaseAuth;
    FirebaseUser fbUser;
    FirebaseDatabase fbDatabase;

    RecyclerView CommentRV;
    CommentAdapter commentAdapter;
    List<Comment> listComment;
    static  String COMMENT_KEY = "Comment";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post__details);

        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        CommentRV = findViewById(R.id.commentRV );
        imgPost = findViewById(R.id.post_detail_img);
        imgUserPost = findViewById(R.id.post_detail_user_img);
        imgCurrentUser = findViewById(R.id.post_detail_currentUser_img);

        txtPostTitle = findViewById(R.id.post_detail_title);
        txtPostDesc = findViewById(R.id.post_detail_desc);
        txtPostDateName = findViewById(R.id.post_date_name);

        editTextComment = findViewById(R.id.post_detail_comment);
        addCommentBtn = findViewById(R.id.add_comment_btn);

//        to edit post
        editPostBtn = findViewById(R.id.Edit_post);

        firebaseAuth = FirebaseAuth.getInstance();
        fbUser = firebaseAuth.getCurrentUser();
        fbDatabase = FirebaseDatabase.getInstance();


        addCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCommentBtn.setVisibility(View.INVISIBLE);
                DatabaseReference commentRef = fbDatabase.getReference(COMMENT_KEY).child(post_Key).push();
                String comment_content = editTextComment.getText().toString();
                String uid = fbUser.getUid();
                String uname = fbUser.getDisplayName();
                String uimg = fbUser.getPhotoUrl().toString();
                Comment comment = new Comment(comment_content, uid, uimg, uname);

                commentRef.setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        showMessage("comment added");
                        editTextComment.setText("");
                        addCommentBtn.setVisibility(View.VISIBLE);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showMessage("Failed to add comment:" +e.getMessage());
                    }
                });
            }
        });


        String postImage = getIntent().getExtras().getString("postImage");
        Glide.with(this).load(postImage).into(imgPost);

        String postTitle = getIntent().getExtras().getString("title");
        txtPostTitle.setText(postTitle);

        String userpostImg = getIntent().getExtras().getString("userPhoto");
        Glide.with(this).load(userpostImg).into(imgUserPost);

       String postDescription = getIntent().getExtras().getString("description");
       txtPostDesc.setText(postDescription);

//       setting commenting user image
        Glide.with(this).load(fbUser.getPhotoUrl()).into(imgCurrentUser);

        post_Key = getIntent().getExtras().getString("postKey");

        String datePost = timeStampToString(getIntent().getExtras().getLong("postDate"));
        txtPostDateName.setText(datePost);

        inRVComment();

        editPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }



    private void inRVComment() {
        CommentRV.setLayoutManager(new LinearLayoutManager(this));

        DatabaseReference commentRef = fbDatabase.getReference(COMMENT_KEY).child(post_Key);
        commentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listComment = new ArrayList<>();
                for (DataSnapshot snap:snapshot.getChildren()){
                    Comment comment = snap.getValue(Comment.class);
                    listComment.add(comment);

                }

                commentAdapter = new CommentAdapter(getApplicationContext(), listComment);
                CommentRV.setAdapter(commentAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

    }

    private String timeStampToString(long time){
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        String date = DateFormat.format("dd-MM-yyyy", calendar).toString();
        return date;
    }
}