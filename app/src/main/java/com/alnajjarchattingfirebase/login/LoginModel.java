package com.alnajjarchattingfirebase.login;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableField;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;

import com.alnajjarchattingfirebase.model.Users;

import java.util.regex.Pattern;

public class LoginModel extends BaseObservable {

    //String email,password;
     String email;
     String password;

    public LoginModel() {
    }

    public LoginModel(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    @Bindable
    public String getEmail() {
        return email;
    }
    @Bindable
    public String getPassword() {
        return password;
    }
}
