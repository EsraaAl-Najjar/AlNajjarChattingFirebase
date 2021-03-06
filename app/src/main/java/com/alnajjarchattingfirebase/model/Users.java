package com.alnajjarchattingfirebase.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;


public class Users extends BaseObservable {
    String id;
    String username;
    String imgURL;
    String status;
    String search;



    public Users(String id, String username, String imgURL, String status, String search) {
        this.id = id;
        this.username = username;
        this.imgURL = imgURL;
        this.status = status;
        this.search = search;
    }

    public Users() {
    }
    @Bindable
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Bindable
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
        //notifyPropertyChanged( com.alnajjarchattingfirebase.BR.username);
    }

    @Bindable
    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    @Bindable
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Bindable
    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}
