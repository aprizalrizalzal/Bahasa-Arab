package com.application.bahasa.arab.data.chats;

public class DataModelProfile {

    private String id;
    private String phoneNumber;
    private String studentName;
    private String studentIdNumber;
    private String profilePictureInTheURL;

    public DataModelProfile(String id, String phoneNumber, String studentName, String studentIdNumber, String profilePictureInTheURL) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.studentName = studentName;
        this.studentIdNumber = studentIdNumber;
        this.profilePictureInTheURL = profilePictureInTheURL;
    }

    public DataModelProfile() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentIdNumber() {
        return studentIdNumber;
    }

    public void setStudentIdNumber(String studentIdNumber) {
        this.studentIdNumber = studentIdNumber;
    }

    public String getProfilePictureInTheURL() {
        return profilePictureInTheURL;
    }

    public void setProfilePictureInTheURL(String profilePictureInTheURL) {
        this.profilePictureInTheURL = profilePictureInTheURL;
    }
}
