package com.example.communityapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.communityapp.Model.Comment;
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
import java.util.List;
import java.util.Locale;

public class post_Details extends AppCompatActivity {

    ImageView imgPost, imgUserPost, imgCurrentUser;
    TextView txtPostDesc, txtPostDateName, txtPostTitle;
    EditText editTextComment;
    Button addCommentBtn, editPostBtn;
    String post_Key;

    FirebaseAuth firebaseAuth;
    FirebaseUser fbUser;
    FirebaseDatabase fbDatabase;
    DatabaseReference postRef;

    RecyclerView CommentRV;
    CommentAdapter commentAdapter;
    List<Comment> listComment;
    static  String COMMENT_KEY = "Comment";

    Dialog updatepost;
    TextView updatePostTitle, updatePostDescription;
    ImageView updateBtn, upDateUserImg;
    ProgressBar updatePBar;


    String postImage, postTitle, userpostImg, postDescription, postUserID;

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

        fbDatabase = FirebaseDatabase.getInstance();
        postRef = fbDatabase.getReference("Posts");

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

                String key = commentRef.getKey();
                comment.setPostKey(key);

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

        showPostDate();

        inRVComment();

        updateEvent();

    }

    private void showPostDate() {
//        getting post image from adapter
        postImage = getIntent().getExtras().getString("postImage");
        Glide.with(this).load(postImage).into(imgPost);

//        getting post title from adapter
        postTitle = getIntent().getExtras().getString("title");
        txtPostTitle.setText(postTitle);

//        getting post creator's image from adapter
        userpostImg = getIntent().getExtras().getString("userPhoto");
        Glide.with(this).load(userpostImg).into(imgUserPost);

//        getting post description from adapter
        postDescription = getIntent().getExtras().getString("description");
        txtPostDesc.setText(postDescription);

//
//        getting post creator's id from adapter
        postUserID = getIntent().getExtras().getString("userId");

//       setting commenting user image
        Glide.with(this).load(fbUser.getPhotoUrl()).into(imgCurrentUser);

//        getting post's key from adapter
        post_Key = getIntent().getExtras().getString("postKey");

//        getting post creation date from adapter
        String datePost = timeStampToString(getIntent().getExtras().getLong("postDate"));
        txtPostDateName.setText(datePost);

//        getting current user id
        String fbUserid = fbUser.getUid();

//        comparing current user with post creator id to display edit button accordingly
        if (fbUserid.equals(postUserID)){
            editPostBtn.setVisibility(View.VISIBLE);
        }

        else {
            editPostBtn.setVisibility(View.INVISIBLE);
        }
    }

//    updating post
    private void updateEvent() {

        updatepost = new Dialog(this);
        updatepost.setContentView(R.layout.update_dialog);
        updatepost.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        updatepost.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        updatepost.getWindow().getAttributes().gravity = Gravity.TOP;

        updatePostTitle = updatepost.findViewById(R.id.popup_title);
        updatePostDescription = updatepost.findViewById(R.id.popup_description);
        upDateUserImg = updatepost.findViewById(R.id.popup_user_image);

        updateBtn = updatepost.findViewById(R.id.popup_update);
        updatePBar = updatepost.findViewById(R.id.popup_progressBar);

//          putting listener to update button
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateBtn.setVisibility(View.INVISIBLE);
                updatePBar.setVisibility(View.VISIBLE);
                if (isTitlechanged() || ispostDescriptionChanged()){
                    Toast.makeText(post_Details.this, "Data is changed", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(post_Details.this, "Data is same", Toast.LENGTH_SHORT).show();
                    updatePBar.setVisibility(View.INVISIBLE);
                    updateBtn.setVisibility(View.VISIBLE);
                }
            }
        });
    }

//    check if description has changed
    private boolean ispostDescriptionChanged() {
        if(!postDescription.equals(updatePostDescription.getEditableText().toString())){
            postRef.child(post_Key).child("description").setValue(updatePostDescription.getEditableText().toString());
            updatepost.dismiss();
            return true;
        }
        else {
            return false;
        }
    }

//    check if title has changed
    private boolean isTitlechanged() {
        if(!postTitle.equals(updatePostTitle.getEditableText().toString())){
            postRef.child(post_Key).child("title").setValue(updatePostTitle.getEditableText().toString());
            updatepost.dismiss();
            return true;
        }
        else {
            return false;
        }
    }


//    to fill the comment recycler view
    private void inRVComment() {
        CommentRV.setLayoutManager(new LinearLayoutManager(this));

        DatabaseReference commentRef = fbDatabase.getReference(COMMENT_KEY).child(post_Key);
        commentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listComment = new ArrayList<>();
                for (DataSnapshot snap:snapshot.getChildren()){
                    Comment comment = snap.getValue(Comment.class);
                    comment.setPostKey(snap.getKey());
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

//   to show message for error
    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

    }

//    to get the timestamp of post created
    private String timeStampToString(long time){
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        String date = DateFormat.format("dd-MM-yyyy", calendar).toString();
        return date;
    }

//      method put into update button
    public void updatePost(View view) {
        updatepost.show();

    }
}