package com.example.smile.matches;

public class MatchesObject {
    String name;
    String userId;
    String userProfile;

    public MatchesObject(String userId, String name, String profileImage) {
        this.name = name;
        this.userId = userId;
        this.userProfile = profileImage;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(String userProfile) {
        this.userProfile = userProfile;
    }
}
