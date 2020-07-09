package com.application.bahasa.arab.data.chats;

public class DataModelProfile {

    private String myId;
    private String myEmail;
    private String myPhoneNumber;
    private String myStudentName;
    private String myStudentIdNumber;
    private String myProfilePictureInTheURL;

    public DataModelProfile(String myId, String myEmail, String myPhoneNumber, String myStudentName, String myStudentIdNumber, String myProfilePictureInTheURL) {
        this.myId = myId;
        this.myEmail = myEmail;
        this.myPhoneNumber = myPhoneNumber;
        this.myStudentName = myStudentName;
        this.myStudentIdNumber = myStudentIdNumber;
        this.myProfilePictureInTheURL = myProfilePictureInTheURL;
    }

    public String getMyId() {
        return myId;
    }

    public void setMyId(String myId) {
        this.myId = myId;
    }

    public String getMyEmail() {
        return myEmail;
    }

    public void setMyEmail(String myEmail) {
        this.myEmail = myEmail;
    }

    public String getMyPhoneNumber() {
        return myPhoneNumber;
    }

    public void setMyPhoneNumber(String myPhoneNumber) {
        this.myPhoneNumber = myPhoneNumber;
    }

    public String getMyStudentName() {
        return myStudentName;
    }

    public void setMyStudentName(String myStudentName) {
        this.myStudentName = myStudentName;
    }

    public String getMyStudentIdNumber() {
        return myStudentIdNumber;
    }

    public void setMyStudentIdNumber(String myStudentIdNumber) {
        this.myStudentIdNumber = myStudentIdNumber;
    }

    public String getMyProfilePictureInTheURL() {
        return myProfilePictureInTheURL;
    }

    public void setMyProfilePictureInTheURL(String myProfilePictureInTheURL) {
        this.myProfilePictureInTheURL = myProfilePictureInTheURL;
    }
}
