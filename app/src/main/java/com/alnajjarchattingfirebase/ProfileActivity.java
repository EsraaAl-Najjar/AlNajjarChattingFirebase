package com.alnajjarchattingfirebase;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.TextView;
import android.widget.Toast;

import com.alnajjarchattingfirebase.model.Users;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    CircleImageView cimgProfile;
    TextView tv_username;

    DatabaseReference dbReference;
    FirebaseUser firebaseUser;

    StorageReference storageReference;
    private static final int IMAGE_REQUEST = 1;
    private Uri imgUri;
    private StorageTask storageTaskUpload;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_profile );
        Toolbar toolbar = findViewById( R.id.toolbarprofile);
        setSupportActionBar( toolbar );
        getSupportActionBar().setTitle( "Profile" );
        getSupportActionBar().setDisplayHomeAsUpEnabled( true );

        toolbar.setNavigationOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( ProfileActivity.this , MainActivity.class);
                startActivity( intent );
            }
        } );
        cimgProfile = findViewById( R.id.prof_pic );
        tv_username = findViewById( R.id.tv_username );

        storageReference = FirebaseStorage.getInstance().getReference("uploads");


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        dbReference = FirebaseDatabase.getInstance().getReference("Users").child( firebaseUser.getUid() );
        dbReference.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users users = dataSnapshot.getValue(Users.class);
                tv_username.setText( users.getUsername() );
                if (users.getImgURL().equals( "def" ) ){
                    Toast.makeText( ProfileActivity.this, "entered"+users.getImgURL(), Toast.LENGTH_SHORT ).show();
                    //img_uri!=null&&!img_uri.isEmpty()&&!img_uri.equals("null")&&img_uri.equals("image")
                    cimgProfile.setImageResource( R.drawable.sendbtn );
                }
                else{
                    Toast.makeText( ProfileActivity.this, "entered2", Toast.LENGTH_SHORT ).show();
                    // Glide.with( MainActivity.this ).load( user.getImgURL()).into( profile_img );
                    Picasso.get().load(  users.getImgURL() ).into( cimgProfile );

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );


        cimgProfile.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImage();

            }
        } );


    }

    private  void openImage(){

        Intent intent = new Intent(  );
        intent.setType( "image/*" );
        intent.setAction( Intent.ACTION_GET_CONTENT );
        startActivityForResult( intent , IMAGE_REQUEST );
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK &&
                data != null && data.getData() != null){
            /*if (storageTaskUpload != null && storageTaskUpload.isInProgress()){
                Toast.makeText( this, "Upload in progress", Toast.LENGTH_SHORT ).show();
            } else {*/
            imgUri = data.getData();

                uploadImage();
           // }
        }
    }
    private  String getFileExtension(Uri uri){

        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType( contentResolver.getType( uri ) );
    }

    private void  uploadImage(){
        final ProgressDialog pd = new ProgressDialog( this );
        pd.setMessage( "Uploading" );
        pd.show();
        Toast.makeText( this, "img"+imgUri, Toast.LENGTH_SHORT ).show();

        if((imgUri != null)){
            final StorageReference fileStorageReference = storageReference.child(
                    System.currentTimeMillis() + "."+getFileExtension( imgUri ) );
            Toast.makeText( this, "img"+imgUri, Toast.LENGTH_SHORT ).show();
            storageTaskUpload = fileStorageReference.putFile( imgUri );
            storageTaskUpload.continueWithTask( new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task task) throws Exception {
                    if (task.isSuccessful()){
//                        throw task.getException();
                    }
                    Toast.makeText( ProfileActivity.this, "file"+fileStorageReference.getDownloadUrl(), Toast.LENGTH_SHORT ).show();

                    return fileStorageReference.getDownloadUrl();
                }
            } ).addOnCompleteListener( new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        String mUri = downloadUri.toString();

                        dbReference = FirebaseDatabase.getInstance().getReference("Users").child( firebaseUser.getUid() );

                        HashMap<String, Object>  map = new HashMap<>(  );
                        map.put( "imgURL", mUri );
                        dbReference.updateChildren( map );
                        pd.dismiss();
                    } else {
                        Toast.makeText( ProfileActivity.this, "Failed", Toast.LENGTH_SHORT ).show();
                        //Log.d("fail message image", e.getMessage());

                        pd.dismiss();
                    }
                }
            } ).addOnFailureListener( new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText( ProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT ).show();
                    pd.dismiss();
                }
            } );
        } else {
            Toast.makeText( this, "No image selected", Toast.LENGTH_SHORT ).show();
            pd.dismiss();

        }
    }


}
