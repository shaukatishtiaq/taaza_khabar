package dev.shaukat.Taaza_Khabar.api.entity;

import java.util.Date;

public class User {
    private Long id;
    private String email;
    private Date createdAt;
    private boolean isVerified;

    public User(Long id, String email, Date createdAt, boolean isVerified) {
        this.id = id;
        this.email = email;
        this.createdAt = createdAt;
        this.isVerified = isVerified;
    }

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", validUpto=" + createdAt +
                ", isVerified=" + isVerified +
                '}';
    }
}
