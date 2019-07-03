package com.example.lostandfoundnew;

public class UserProfileModel {
    public String email;
    public String firstName;
    public String image;
    public String lastName;
    public String phoneNumber;

    public UserProfileModel(String firstName, String lastName, String phonenumber, String email, String image) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phonenumber;
        this.email = email;
        this.image = image;
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

    public String getPhonenumber() {
        return this.phoneNumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phoneNumber = phonenumber;
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
