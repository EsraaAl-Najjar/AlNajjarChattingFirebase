package com.alnajjarchattingfirebase.login;

import android.app.Activity;
import android.databinding.ObservableField;
import android.text.TextUtils;


public class LoginPresenter implements LoginContract.Presenter {

    private LoginContract.ViewModel mLoginView;
   // private LoginInteractor mLoginInteractor;

    public LoginPresenter(LoginContract.ViewModel mLoginView) {
        this.mLoginView = mLoginView;

        //mLoginInteractor = new LoginInteractor(this);
    }

    @Override
    public void onLogInClicked(LoginModel loginModel) {
        mLoginView.performFirebaseLogin( loginModel );
    }

    @Override
    public void onButtonClickRegister() {
        mLoginView.performRegister();
    }

    @Override
    public void onButtonClickForget() {
        mLoginView.performForgetPassword();
    }

   /* @Override
    public void Login(Activity activity, String email, String password) {
        mLoginInteractor.performFirebaseLogin(activity, email, password);

    }*/


}
