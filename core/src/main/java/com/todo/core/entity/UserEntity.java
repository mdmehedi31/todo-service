package com.todo.core.entity;


import com.todo.core.utils.DBUtils;
import jakarta.persistence.*;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity(name = DBUtils.TB_USER)
public class UserEntity extends AbstractIdEntity {

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "email")
    private String email;

    @Column(name = "created_on")
    private Instant createdOn;

    @Column(name = "password")
    private String password;

    @Column(name = "salt")
    private String salt;

    @Column(name = "image")
    private Byte image;

    @Column(name = "phone_no")
    private String phoneNo;

    @Column(name = "wrong_attempt")
    private Integer wrongAttempt;

    @Column(name = "is_blocked")
    private boolean isBlocked;

    @Column(name = "time_zone")
    private String timeZone;

    @Lob
    @Column(name = "google_refresh_token")
    private String googleResfreshToken;

    @Lob
    @Column(name = "google_access_token")
    private String googleAccessToken;

    @Column(name = "google_token_expired_at")
    private Instant googleTokenExpiresAt;

    @Column(name = "activated_code")
    private String activatedCode;

    @Column(name = "expired_at")
    private LocalDateTime expiresdAt;

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

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Byte getImage() {
        return image;
    }

    public void setImage(Byte image) {
        this.image = image;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public Integer getWrongAttempt() {
        return wrongAttempt;
    }

    public void setWrongAttempt(Integer wrongAttempt) {
        this.wrongAttempt = wrongAttempt;
    }

    public boolean getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    public Instant getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getGoogleAccessToken() {
        return googleAccessToken;
    }

    public void setGoogleAccessToken(String googleAccessToken) {
        this.googleAccessToken = googleAccessToken;
    }

    public String getGoogleResfreshToken() {
        return googleResfreshToken;
    }

    public void setGoogleResfreshToken(String googleResfreshToken) {
        this.googleResfreshToken = googleResfreshToken;
    }

    public Instant getGoogleTokenExpiresAt() {
        return googleTokenExpiresAt;
    }

    public void setGoogleTokenExpiresAt(Instant googleTokenExpiresAt) {
        this.googleTokenExpiresAt = googleTokenExpiresAt;
    }

    public String getActivatedCode() {
        return activatedCode;
    }

    public void setActivatedCode(String activatedCode) {
        this.activatedCode = activatedCode;
    }

    public LocalDateTime getExpiresdAt() {
        return expiresdAt;
    }

    public void setExpiresdAt(LocalDateTime expiresdAt) {
        this.expiresdAt = expiresdAt;
    }
}
