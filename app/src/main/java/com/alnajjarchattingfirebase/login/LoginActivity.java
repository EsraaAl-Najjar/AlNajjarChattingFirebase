package com.alnajjarchattingfirebase.login;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alnajjarchattingfirebase.MainActivity;
import com.alnajjarchattingfirebase.R;
import com.alnajjarchattingfirebase.RegisterActivity;
import com.alnajjarchattingfirebase.ResetPasswordActivity;
import com.alnajjarchattingfirebase.databinding.ActivityLoginBinding;

import com.alnajjarchattingfirebase.model.Users;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.rengwuxian.materialedittext.MaterialEditText;

public class LoginActivity extends AppCompatActivity implements  LoginContract.ViewModel {

    private static final String TAG = "esraaYouCanDisplayItNow ==";
    ActivityLoginBinding activityLoginBinding;
    MaterialEditText et_email,et_password;
    ProgressDialog mProgressDialog;
    private LoginPresenter mLoginPresenter;
    Users users;
    LoginModel loginModel;

  //  private LoginContract.onLoginListener mOnLoginListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        //setContentView( R.layout.activity_login );

        activityLoginBinding = DataBindingUtil.setContentView(this,R.layout.activity_login);
        FirebaseApp.initializeApp(this);
        //setSupportActionBar(activityLoginBinding.toolbarLog);

       // Toolbar toolbar = findViewById( R.id.toolbarLog );
        Toolbar toolbar = (Toolbar) activityLoginBinding.toolbarLog;
        setSupportActionBar( toolbar );
        getSupportActionBar().setTitle( "LogIn" );
        getSupportActionBar().setDisplayHomeAsUpEnabled( true );


        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Please wait, Logging in..");
        mLoginPresenter = new LoginPresenter( this);
        users = new Users();
        loginModel = new LoginModel(  );

       // activityLoginBinding.getUsers();
        activityLoginBinding.setLoginModel( loginModel );
        activityLoginBinding.setPresenter( mLoginPresenter );

//        auth = FirebaseAuth.getInstance();
//        btn_login.setOnClickListener( new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String txt_email = et_email.getText().toString();
//
//                String txt_password = et_password.getText().toString();
//
//                if ( TextUtils.isEmpty( txt_email ) || TextUtils.isEmpty( txt_password )){
//                    Toast.makeText( LoginActivity.this, "All Fields are Required", Toast.LENGTH_SHORT ).show();
//
//                }else {
//                    auth.signInWithEmailAndPassword( txt_email,txt_password )
//                            .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
//                                @Override
//                                public void onComplete(@NonNull Task<AuthResult> task) {
//                                    Toast.makeText( LoginActivity.this, "in log ", Toast.LENGTH_SHORT ).show();
//
//                                    if (task.isSuccessful()){
//                                        Intent intent = new Intent( LoginActivity.this , MainActivity.class );
//                                        intent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK );
//                                        startActivity( intent );
//                                        finish();
//                                    }
//                                }
//                            } );
//                }
//            }
//        } );


    }
    /*@SuppressLint("LongLogTag")
    public void onButtonClick(){
        checkLoginDetails();
    }
*/


    private void checkLoginDetails() {
        if(!TextUtils.isEmpty(et_email.getText().toString()) && !TextUtils.isEmpty(et_password.getText().toString())){
           // initLogin(et_email.getText().toString(), et_password.getText().toString());
        }else{
            if(TextUtils.isEmpty(et_email.getText().toString())){
                et_email.setError("Please enter a valid email");
            }if(TextUtils.isEmpty(et_password.getText().toString())){
                et_password.setError("Please enter password");
            }
        }
    }

    @Override
    public void performFirebaseLogin(LoginModel loginModel) {
        if(!TextUtils.isEmpty(loginModel.getEmail().toString()) && !TextUtils.isEmpty(loginModel.getPassword().toString())  ){

            // initLogin(et_email.getText().toString(), et_password.getText().toString());
            mProgressDialog.show();

            Toast.makeText( this, "emai=="+loginModel.getEmail() + "===" + loginModel.getPassword(), Toast.LENGTH_SHORT ).show();
            FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword( loginModel.getEmail(),loginModel.getPassword() )

                    .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()){
                                Toast.makeText( LoginActivity.this, "success", Toast.LENGTH_SHORT ).show();
                                Intent intent = new Intent( LoginActivity.this, MainActivity.class );
                                intent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK );
                                startActivity( intent );
                                finish();
                            }else {
                                mProgressDialog.dismiss();
                                Toast.makeText( LoginActivity.this, "failed", Toast.LENGTH_SHORT ).show();
                            }
                        }
                    } );
        }else{
            if(TextUtils.isEmpty(loginModel.getEmail().toString())){
                Toast.makeText( LoginActivity.this, "Please enter a valid email", Toast.LENGTH_SHORT ).show();
                mProgressDialog.dismiss();
            }
            if(TextUtils.isEmpty(loginModel.getPassword().toString())){
                Toast.makeText( LoginActivity.this, "Please enter password", Toast.LENGTH_SHORT ).show();
                mProgressDialog.dismiss();

            }else if (loginModel.getPassword().toString().length() <6 ){
                mProgressDialog.dismiss();
                Toast.makeText( LoginActivity.this, "password must not be less than 6 digits", Toast.LENGTH_SHORT ).show();

            }
        }
    }

    @Override
    public void performRegister() {
        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    public void performForgetPassword() {
        startActivity( new Intent( LoginActivity.this, ResetPasswordActivity.class ) );
        startActivity( new Intent( LoginActivity.this, ResetPasswordActivity.class ) );
    }


//    public class MyClickHandlers {
//
//        Context context;
//
//        public MyClickHandlers(Context context) {
//            this.context = context;
//        }
//
//        public void onFabClicked(View view) {
//            users.set("Ravi");
//            user.email.set("ravi8x@gmail.com");
//            Toast.makeText(getApplicationContext(), "FAB clicked!", Toast.LENGTH_SHORT).show();
//        }
//
//        public void onButtonClick(View view) {
//            Toast.makeText(getApplicationContext(), "Button clicked!", Toast.LENGTH_SHORT).show();
//        }
//
//        public void onButtonClickWithParam(View view, User user) {
//            Toast.makeText(getApplicationContext(), "Button clicked! Name: " + user.getName(), Toast.LENGTH_SHORT).show();
//        }
//
//        public boolean onButtonLongPressed(View view) {
//            Toast.makeText(getApplicationContext(), "Button long pressed!", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//    }

}
