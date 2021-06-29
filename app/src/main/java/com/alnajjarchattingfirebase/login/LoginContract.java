package com.alnajjarchattingfirebase.login;

import android.app.Activity;


public interface LoginContract {
    interface ViewModel{
        void performFirebaseLogin( LoginModel loginModel);
        void performRegister();
        void performForgetPassword();
       /* void onLoginSuccess(String message);
        void onLoginFailure(String message);*/

    }
    interface Presenter{
        void onLogInClicked(LoginModel loginModel);
        void onButtonClickRegister();
        void onButtonClickForget();
    }

   /* interface Intractor{
        void performFirebaseLogin(Activity activity, String email, String password);
    }

    interface onLoginListener{
        void onSuccess(String message);
        void onFailure(String message);
    }*/

}
