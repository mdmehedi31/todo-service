package com.todom.library.dto;

import java.time.Instant;

public class UserRegistrationDTO extends AbstractIdAndUserTimeZone {

    private String fullName;
    private String email;
    private String password;
    private String phoneNo;
    private Byte image;
    private String googleAccessToken;
    private String googleRefreshToken;
    private Instant googleTokenExpiredAt;
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public Byte getImage() {
        return image;
    }

    public void setImage(Byte image) {
        this.image = image;
    }

    public String getGoogleAccessToken() {
        return googleAccessToken;
    }

    public void setGoogleAccessToken(String googleAccessToken) {
        this.googleAccessToken = googleAccessToken;
    }

    public String getGoogleRefreshToken() {
        return googleRefreshToken;
    }

    public void setGoogleRefreshToken(String googleRefreshToken) {
        this.googleRefreshToken = googleRefreshToken;
    }
    public void setGoogleTokenExpiredAt(Instant googleTokenExpiredAt) {
        this.googleTokenExpiredAt = googleTokenExpiredAt;
    }

    public Instant getGoogleTokenExpiredAt() {
        return googleTokenExpiredAt;
    }
}
