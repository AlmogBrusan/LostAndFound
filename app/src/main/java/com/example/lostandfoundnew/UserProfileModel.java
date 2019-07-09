package com.example.lostandfoundnew;

public class UserProfileModel {
    private String email;
    private String firstName;
    private String image;
    private String lastName;
     String phoneNumber;
    private String talkTo;
    private String userId;


    public UserProfileModel(String firstName, String lastName, String phoneNumber, String email, String image , String uid) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.image = image;
        this.userId = uid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getTalkTo() {
        return talkTo;
    }

    public void setTalkTo(String talkTo) {
        this.talkTo = talkTo;
    }

    public UserProfileModel(){}

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
