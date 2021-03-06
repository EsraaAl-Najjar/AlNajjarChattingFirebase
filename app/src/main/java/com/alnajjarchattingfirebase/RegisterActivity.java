package com.alnajjarchattingfirebase;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    MaterialEditText et_username,et_email,et_password;
    Button btn_register;

        FirebaseAuth auth;
        DatabaseReference DBreference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_register );
        FirebaseApp.initializeApp(this);
        Toolbar toolbar = findViewById( R.id.toolbarReg );
        setSupportActionBar( toolbar );
        getSupportActionBar().setTitle( "Register" );
        getSupportActionBar().setDisplayHomeAsUpEnabled( true );
        et_username = (MaterialEditText)findViewById( R.id.usernameReg );
        et_email =(MaterialEditText) findViewById( R.id.emailReg );
        et_password = (MaterialEditText)findViewById( R.id.passwordReg );
        btn_register = (Button)findViewById( R.id.btn_register_Reg );

        auth = FirebaseAuth.getInstance();
        btn_register.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_username = et_username.getText().toString();
                String txt_email = et_email.getText().toString();

                String txt_password = et_password.getText().toString();

                if (TextUtils.isEmpty( txt_username ) || TextUtils.isEmpty( txt_email ) || TextUtils.isEmpty( txt_password )){
                    Toast.makeText( RegisterActivity.this, "All Fields are Required", Toast.LENGTH_SHORT ).show();

                }else if (txt_password.length() <6){
                    Toast.makeText( RegisterActivity.this, "Password must be at least 6 Characters", Toast.LENGTH_SHORT ).show();
                }else{

                    register( txt_username,txt_email,txt_password );
                }

            }
        } );

    }

    private void register(final String username, String email, String password){

        auth.createUserWithEmailAndPassword( email,password )
                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            assert firebaseUser != null;
                            String userid = firebaseUser.getUid();
                            DBreference = FirebaseDatabase.getInstance().getReference("Users").child( userid );

                            HashMap<String,String> userhashmap = new HashMap<>();
                            userhashmap.put( "id",userid );
                            userhashmap.put( "username", username );
                            userhashmap.put( "imgURL", "def" );
                            userhashmap.put( "status", "offline" );
                            userhashmap.put( "search", username.toLowerCase() );
                            DBreference.setValue( userhashmap ).addOnCompleteListener( new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()){
                                        Intent intent = new Intent(RegisterActivity.this , MainActivity.class );
                                       intent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK );
                                       startActivity( intent );
                                       finish();
                                    }else{

                                        Toast.makeText( RegisterActivity.this, "Authentication", Toast.LENGTH_SHORT ).show();
                                    }
                                }
                            } );

                        }
                    }
                } );
    }
}
