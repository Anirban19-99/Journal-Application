package com.example.journalapp.util;

import android.app.Application;

public class Journalapi extends Application {

    private String username;
    private String userid;
    private String name;
    private String profilephotourl;

    private String email;
    private static Journalapi instance;

    public static Journalapi getInstance() {
       if(instance==null)
       {
           instance=new Journalapi();

       }
        return instance;
    }


    public Journalapi()
    {

    }

    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilephotourl() {
        return profilephotourl;
    }

    public void setProfilephotourl(String profilephotourl) {
        this.profilephotourl = profilephotourl;
    }

    public static void setInstance(Journalapi instance) {
        Journalapi.instance = instance;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
