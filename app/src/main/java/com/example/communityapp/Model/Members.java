package com.example.communityapp.Model;

public class Members {
    private String memberID;
    private String communId;
    private String postKey;

    public Members(String memberID, String communId) {
        this.memberID = memberID;
        this.communId = communId;
    }

    public Members() {
    }

    public String getMemberID() {
        return memberID;
    }

    public void setMemberID(String memberID) {
        this.memberID = memberID;
    }

    public String getCommunId() {
        return communId;
    }

    public void setCommunId(String communId) {
        this.communId = communId;
    }

    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }
}
