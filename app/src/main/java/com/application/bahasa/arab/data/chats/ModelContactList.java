package com.application.bahasa.arab.data.chats;

public class ModelContactList {

    private String userId;
    private String userName;
    private String phoneNumber;
    private String profilePicture;

    public ModelContactList(String userId, String userName, String phoneNumber, String profilePicture) {
        this.userId = userId;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.profilePicture = profilePicture;
    }

    public ModelContactList() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
}
