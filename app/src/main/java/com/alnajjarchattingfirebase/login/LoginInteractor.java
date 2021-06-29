package com.alnajjarchattingfirebase.login;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.alnajjarchattingfirebase.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginInteractor implements LoginContract {
  /* private LoginContract.onLoginListener mOnLoginListener;

    public LoginInteractor(LoginContract.onLoginListener mOnLoginListener) {
        this.mOnLoginListener = mOnLoginListener;
    }

    @Override
    public void performFirebaseLogin(final Activity activity, String email, String password) {
        FirebaseAuth.getInstance()
                .signInWithEmailAndPassword( email,password )

                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){
                            Toast.makeText( activity, "success", Toast.LENGTH_SHORT ).show();
                            mOnLoginListener.onSuccess( task.getResult().toString() );
                            Intent intent = new Intent( activity, MainActivity.class );
                            intent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK );
                            activity.startActivity( intent );
                            activity.finish();
                        }else {
                            mOnLoginListener.onFailure(task.getException().toString());
                            Toast.makeText( activity, "failed", Toast.LENGTH_SHORT ).show();

                        }
                    }
                } );
    }*/
}
