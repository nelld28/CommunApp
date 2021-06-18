package com.example.communityapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.communityapp.Model.Post;
import com.example.communityapp.fragment.fragment_homePage;
import com.example.communityapp.fragment.groupFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private String[] mNavigationDrawerItemTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    Toolbar toolbar;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    ActionBarDrawerToggle mDrawerToggle;
    FirebaseUser currentUser;

    static int PReqCode = 1;
    static int REQUESTCODE = 1;

//    Image picker
    Uri pickedImgUri = null;

//    Popup Post
    Dialog popUpAddPost;
    ImageView popUpUserImage, popUpPostImage, popUpAddBtn;
    TextView popUpTitle, popUpDescription;
    ProgressBar popUpClickProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

//        Toast.makeText(MainActivity.this, "Firebase connected", Toast.LENGTH_SHORT).show();

        Log.d("tag", "onCreate: " + firebaseAuth.getCurrentUser().getEmail() +firebaseAuth.getCurrentUser().getDisplayName());

        mTitle = mDrawerTitle =getTitle();
        mNavigationDrawerItemTitles = getResources().getStringArray(R.array.navigation_drawer_items_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView)findViewById(R.id.left_drawer);


        setupToolbar();

//        Content titles to be navigated
        DataModel[] drawerItem = new DataModel[4];

        drawerItem[0] = new DataModel(R.drawable.calendar_dra, "calendar");
        drawerItem[1] = new DataModel(R.drawable.com_loc, "location");
        drawerItem[2] = new DataModel(R.drawable.login_logo, "logout");
        drawerItem[3] = new DataModel(R.drawable.login_logo, "homepage");


        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        getSupportActionBar().setHomeButtonEnabled(true);

        DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this, R.layout.list_view_item_row, drawerItem);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        setupDrawerToggle();

//        adapter for group left


//        popup
        Popup();
        setupPopupImageClick();




//        fab Button
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                popUpAddPost.show();
//            }
//        });

      }

//          END OF ONCREATE



    //      to set up image in popup post
    private void setupPopupImageClick() {
        popUpPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndRequestForPermission();
            }
        });
    }

    private void checkAndRequestForPermission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Toast.makeText(MainActivity.this,"Please accept for required permission",Toast.LENGTH_SHORT).show();

            }

            else
            {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{
                                Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);
            }

        }
        else

            openGallery();
    }

//    Image Gallery
    private void openGallery() {

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("Image/*");
        startActivityForResult(galleryIntent, REQUESTCODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == REQUESTCODE && data !=null){

            pickedImgUri = data.getData();
            popUpPostImage.setImageURI(pickedImgUri);

        }
    }

    //  popup add post
    private void Popup() {
        popUpAddPost = new Dialog(this);
        popUpAddPost.setContentView(R.layout.add_post);
        popUpAddPost.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popUpAddPost.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        popUpAddPost.getWindow().getAttributes().gravity = Gravity.TOP;

//    popUp widgets
        popUpUserImage = popUpAddPost.findViewById(R.id.popup_user_image);
        popUpAddBtn = popUpAddPost.findViewById(R.id.popup_add);
        popUpTitle = popUpAddPost.findViewById(R.id.popup_title);
        popUpDescription = popUpAddPost.findViewById(R.id.popup_description);
        popUpPostImage = popUpAddPost.findViewById(R.id.popup_img);
        popUpClickProgress = popUpAddPost.findViewById(R.id.popup_progressBar);

//        user profile pic
        Glide.with(MainActivity.this).load(currentUser.getPhotoUrl()).into(popUpUserImage);


//        post click listener
        popUpAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUpAddBtn.setVisibility(View.INVISIBLE);
                popUpClickProgress.setVisibility(View.VISIBLE);

                if(!popUpTitle.getText().toString().isEmpty()
                        && !popUpDescription.getText().toString().isEmpty()){

                    StorageReference storageReference = FirebaseStorage.getInstance()
                            .getReference()
                            .child("images");
                    final StorageReference imageFilePath = storageReference.child(pickedImgUri.getLastPathSegment());
                    imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imgDownloadLink = uri.toString();
                                    Post post = new Post(popUpTitle.getText().toString() ,
                                            popUpDescription.getText().toString(),
                                            imgDownloadLink,
                                            currentUser.getUid(),
                                            currentUser.getPhotoUrl().toString());

//                                    add post to firebase
                                    addPost(post);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
//                                    something goes wrong in uploading picture
                                    showMessage(e.getMessage());
                                    popUpClickProgress.setVisibility(View.INVISIBLE);
                                    popUpAddBtn.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    });

                }

                else {
                    showMessage("Please fill all required fields");
                    popUpAddBtn.setVisibility(View.VISIBLE);
                    popUpClickProgress.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void addPost(Post post) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Posts").push();

//        get post unique id and  update post key
        String key = myRef.getKey();
        post.setPostKey(key);


//        add post data to firebase database
        myRef.setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                showMessage("Post added successfully");
                popUpClickProgress.setVisibility(View.INVISIBLE);
                popUpAddBtn.setVisibility(View.VISIBLE);
                popUpAddPost.dismiss();
            }
        });

    }

    private void showMessage(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();

    }




//    Side Navigation
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        Fragment fragment = null;

        switch (position){
            case 0:
                fragment = new calendar_fragment();
                break;
            case 1:
                fragment = new groupFragment();
                break;

            case 2:
                logout();
                break;

            case 3:
                fragment = new fragment_homePage();

            default:
                break;
        }

        if (fragment != null){
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .add(R.id.content_frame, fragment)
                    .addToBackStack("fragment")
                    .commit();

            mDrawerList.setItemChecked(position, true);

            mDrawerList.setSelection(position);

            setTitle(mNavigationDrawerItemTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
        }
        else {
            Log.e("MainActivity", "Error om creating fragment" );
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    //    could be deleted later
    private void logout() {
        logout();
    }


    //    LOGOUT FROM APP
    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();

        GoogleSignIn.getClient(this, new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .build())
                .signOut()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        startActivity(new Intent(view.getContext()
                                ,login_activity.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Failed LogOut", Toast.LENGTH_SHORT)
                        .show();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setTitle(CharSequence title){
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    void setupDrawerToggle() {
        mDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout,
                toolbar,
                R.string.app_name,
                R.string.app_name);
        mDrawerToggle.syncState();
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    void setupToolbar() {
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }


}