package com.example.communityapp.Model;

public class Note {
    private String postKey;
    private String commId;
    private String comName;
    private String infectecdNum;
    private String recoveredNum;
    private String material;
    private String priority;

    public Note() {
    }

    public Note(String commId, String comName, String infectecdNum, String recoveredNum, String material, String priority) {
        this.commId = commId;
        this.comName = comName;
        this.infectecdNum = infectecdNum;
        this.recoveredNum = recoveredNum;
        this.material = material;
        this.priority = priority;
    }

    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public String getCommId() {
        return commId;
    }

    public void setCommId(String commId) {
        this.commId = commId;
    }

    public String getComName() {
        return comName;
    }

    public void setComName(String comName) {
        this.comName = comName;
    }

    public String getInfectecdNum() {
        return infectecdNum;
    }

    public void setInfectecdNum(String infectecdNum) {
        this.infectecdNum = infectecdNum;
    }

    public String getRecoveredNum() {
        return recoveredNum;
    }

    public void setRecoveredNum(String recoveredNum) {
        this.recoveredNum = recoveredNum;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}

