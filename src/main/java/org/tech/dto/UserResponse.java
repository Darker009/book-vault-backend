package org.tech.dto;

import org.tech.entity.UserProfile;

public class UserResponse {
    private Long id;
    private String email;
    private String role;
    private boolean profileUpdated;
    private UserProfile userProfile;

    public UserResponse() {}

    public UserResponse(Long id, String email, String role, boolean profileUpdated, UserProfile userProfile) {
        this.id = id;
        this.email = email;
        this.role = role;
        this.profileUpdated = profileUpdated;
        this.userProfile = userProfile;
    }

    // Getters and Setters

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
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public boolean isProfileUpdated() {
        return profileUpdated;
    }
    public void setProfileUpdated(boolean profileUpdated) {
        this.profileUpdated = profileUpdated;
    }
    public UserProfile getUserProfile() {
        return userProfile;
    }
    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }
}
