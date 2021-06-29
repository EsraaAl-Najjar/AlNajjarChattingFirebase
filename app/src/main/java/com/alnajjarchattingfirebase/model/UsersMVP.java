package com.alnajjarchattingfirebase.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UsersMVP implements Serializable {

    @SerializedName("Id")
    String Id;

    @SerializedName("username")
    String username;
    @SerializedName("email")
    String email;
    @SerializedName("mobile")
    String mobile;
    @SerializedName("password")
    String password;
    @SerializedName("imgURL")
    String imgURL;
    @SerializedName("status")
    String status;
    @SerializedName("search")
    String search;

    public UsersMVP() {
    }

    public UsersMVP(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public UsersMVP(String username, String email, String mobile, String password, String imgURL, String status, String search) {
        this.username = username;
        this.email = email;
        this.mobile = mobile;
        this.password = password;
        this.imgURL = imgURL;
        this.status = status;
        this.search = search;
    }

    public UsersMVP(String id, String username, String email, String mobile, String password, String imgURL, String status, String search) {
        Id = id;
        this.username = username;
        this.email = email;
        this.mobile = mobile;
        this.password = password;
        this.imgURL = imgURL;
        this.status = status;
        this.search = search;
    }


    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}
