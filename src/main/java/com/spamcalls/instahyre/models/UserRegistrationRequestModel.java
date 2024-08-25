package com.spamcalls.instahyre.models;

public class UserRegistrationRequestModel {

    private String userName;
    private String phoneNumber;
    private String password;
    private String emailId;

    public UserRegistrationRequestModel() {
    }

    public UserRegistrationRequestModel(String userName, String phoneNumber, String password, String emailId) {
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.emailId = emailId;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }
}
