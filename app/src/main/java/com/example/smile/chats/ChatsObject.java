package com.example.smile.chats;

public class ChatsObject {
    String userId;

    public ChatsObject(String userId, String name, String profileImage) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
