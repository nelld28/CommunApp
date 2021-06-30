package com.example.communityapp.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.communityapp.MainActivity;
import com.example.communityapp.Model.group_intro;
import com.example.communityapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.app.Activity.RESULT_OK;
import static android.widget.Toast.*;
import static android.widget.Toast.LENGTH_SHORT;


public class add_group_intro extends Fragment {

    private TextView commName, commDesc;
    private ImageView commImg, commUserImg, commAddBtn, commLName;
    ProgressBar commProg;

    private static int PReqCode = 2;
    static int REQUESTCODE = 2;

    //    Image picker
    Uri pickedImgUri = null;

    FirebaseDatabase groupDatabase;
    DatabaseReference groupDataRef;
    FirebaseUser currentUser;

    group_intro groupIntro;

    public add_group_intro() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static add_group_intro newInstance() {
        return new add_group_intro();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootview;
        rootview =  inflater.inflate(R.layout.fragment_add_group_intro2, container, false);

        commName = rootview.findViewById(R.id.community_name);
        commDesc = rootview.findViewById(R.id.community_description);
        commUserImg = rootview.findViewById(R.id.community_creater_user_image);
        commImg = rootview.findViewById(R.id.community_img);
        commProg = rootview.findViewById(R.id.comm_progressBar);

        Glide.with(getActivity()).load(currentUser.getPhotoUrl())
                .into(commUserImg);

//        groupDatabase = FirebaseDatabase.getInstance();
//        groupDataRef = groupDatabase.getReference().child("group_intro");
        commAddBtn = rootview.findViewById(R.id.comm_add);

        setUpcommImageClick();

        commAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String groupName = commName.getText().toString();
//
//
//                if(TextUtils.isEmpty(groupName)){
//                    Toast.makeText(getActivity(), "Enter value", LENGTH_SHORT).show();
//
//                }
//                else
//                {
//                    addDataToFirebase(groupName);
//                }

                commAddBtn.setVisibility(View.INVISIBLE);
                commProg.setVisibility(View.VISIBLE);

                if(!commName.getText().toString().isEmpty()
                && !commDesc.getText().toString().isEmpty()
                && pickedImgUri != null){
                    StorageReference storageReference = FirebaseStorage.getInstance()
                            .getReference()
                            .child("image_posts");
                    final StorageReference imageFilePath = storageReference
                            .child(pickedImgUri.getLastPathSegment());
                    imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                           imageFilePath.getDownloadUrl().
                                   addOnSuccessListener(
                                           new OnSuccessListener<Uri>() {
                               @Override
                               public void onSuccess(Uri uri) {
                                   String imgDownloadLink = uri.toString();
                                   group_intro groupIntro =  new group_intro(
                                     commName.getText().toString(),
                                     commDesc.getText().toString(),
                                     imgDownloadLink,
                                     currentUser.getUid(),
                                     currentUser.getPhotoUrl().toString()
                                   );

                                   addComm(groupIntro);
                               }
                           }).addOnFailureListener(new OnFailureListener() {
                               @Override
                               public void onFailure(@NonNull Exception e) {
                                   showMessage(e.getMessage());
                                   commProg.setVisibility(View.INVISIBLE);
                                   commAddBtn.setVisibility(View.VISIBLE);
                               }
                           }) ;
                        }
                    });
                }

                else {
                    showMessage("Please fill all required fields");
                    commAddBtn.setVisibility(View.VISIBLE);
                    commProg.setVisibility(View.INVISIBLE);
                }
            }
        });


        return rootview;

    }

    private void setUpcommImageClick() {
        commImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndRequestForPermission();
            }
        });
    }

    private void checkAndRequestForPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    android.Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Toast.makeText(getActivity(),"Please accept for required permission",Toast.LENGTH_SHORT).show();

            }

            else
            {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{
                                Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);
            }

        }
        else

            openGallery();
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUESTCODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == REQUESTCODE && data !=null){
            pickedImgUri = data.getData();
            commImg.setImageURI(pickedImgUri);
        }


    }

    private void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

    }

    private void addComm(group_intro groupIntro) {
        groupDatabase = FirebaseDatabase.getInstance();
        groupDataRef = groupDatabase.getReference("group_intro").push();

        String key = groupDataRef.getKey();
        groupIntro.setPostKey(key);

        groupDataRef.setValue(groupIntro).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                showMessage("Post added successfully");
                commProg.setVisibility(View.INVISIBLE);
                commAddBtn.setVisibility(View.VISIBLE);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, groupFragment.newInstance())
                        .commitNow();
            }
        });
    }




}