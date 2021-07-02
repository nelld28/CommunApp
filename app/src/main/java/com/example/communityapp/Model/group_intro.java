package com.example.communityapp.Model;

public class group_intro {

    private String commName;
    private String commDesc;
    private String commImg;
    private String commUserId;
    private String commUserImg;
    private String postKey;

    public group_intro(String commName, String commDesc, String commImg, String commUserId, String commUserImg) {
        this.commName = commName;
        this.commDesc = commDesc;
        this.commImg = commImg;
        this.commUserId = commUserId;
        this.commUserImg = commUserImg;
    }

    public group_intro() {
    }

    public String getCommName() {
        return commName;
    }

    public void setCommName(String commName) {
        this.commName = commName;
    }

    public String getCommDesc() {
        return commDesc;
    }

    public void setCommDesc(String commDesc) {
        this.commDesc = commDesc;
    }

    public String getCommImg() {
        return commImg;
    }

    public void setCommImg(String commImg) {
        this.commImg = commImg;
    }

    public String getCommUserId() {
        return commUserId;
    }

    public void setCommUserId(String commUserId) {
        this.commUserId = commUserId;
    }

    public String getCommUserImg() {
        return commUserImg;
    }

    public void setCommUserImg(String commUserImg) {
        this.commUserImg = commUserImg;
    }

    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }
}
