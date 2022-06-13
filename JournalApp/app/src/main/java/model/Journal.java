package model;

import com.google.firebase.Timestamp;

public class Journal {
    private String caption;
    private String docid;
    private String thougts;
    private String userid;
    private String username;
    private Timestamp time;
    private String imageurl;
    private String profileurl;

    public Journal() {
    }

    public Journal(String caption, String thougts, String userid, String username, Timestamp time, String imageurl, String docid,String email,String profileurl) {
        this.caption = caption;
        this.docid=docid;
        this.thougts = thougts;
        this.userid = userid;
        this.username = username;
        this.time = time;
        this.imageurl = imageurl;
        this.profileurl=profileurl;
    }


    public String getProfileurl() {
        return profileurl;
    }

    public void setProfileurl(String profileurl) {
        this.profileurl = profileurl;
    }

    public String getCaption() {
        return caption;
    }




    public String getDocid() {
        return docid;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public void setDocid(String docid) {
        this.docid = docid;
    }

    public String getThougts() {
        return thougts;
    }

    public void setThougts(String thougts) {
        this.thougts = thougts;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }


    @Override
    public String toString() {
        return "Journal{" +
                "caption='" + caption + '\'' +
                ", docid='" + docid + '\'' +
                ", thougts='" + thougts + '\'' +
                ", userid='" + userid + '\'' +
                ", username='" + username + '\'' +
                ", time=" + time +
                ", imageurl='" + imageurl + '\'' +
                '}';
    }
}
