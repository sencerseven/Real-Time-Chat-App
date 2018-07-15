package com.cfa.chyrellos.chatfa.Model;

import java.util.Date;

/**
 * Created by chyrellos on 08.05.2017.
 */

public class User {
private String uID;
private String username;
private String email;
private boolean status;
private long statusDate;
private String userPhoto;


    public User(){

    }

    public User(String uID, String username, String email,boolean status, String userPhoto){
        this.uID = uID;
        this.username = username;
        this.email = email;
        this.status = status;
        statusDate = new Date().getTime();
        this.userPhoto = userPhoto;
    }



    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
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

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public long getStatusDate() {
        return statusDate;
    }

    public void setStatusDate(long statusDate) {
        this.statusDate = statusDate;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }
}
