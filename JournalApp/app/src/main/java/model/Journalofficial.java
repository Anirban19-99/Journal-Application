package model;

import com.google.firebase.Timestamp;

public class Journalofficial {


    private String title;
    private String link;
    private String thougts;
    private String userid;
    private String username;
    private Timestamp time;
    private String fileurl;
    private String docid;

    public Journalofficial() {
    }


    public Journalofficial(String title, String link, String thougts, String userid, String username, Timestamp time, String fileurl, String docid) {
        this.title = title;
        this.link = link;
        this.thougts = thougts;
        this.userid = userid;
        this.username = username;
        this.time = time;
        this.fileurl = fileurl;
        this.docid = docid;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
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

    public String getFileurl() {
        return fileurl;
    }

    public void setFileurl(String fileurl) {
        this.fileurl = fileurl;
    }

    public String getDocid() {
        return docid;
    }

    public void setDocid(String docid) {
        this.docid = docid;
    }
}
