package com.application.bahasa.arab.data.chats;

public class ModelChatList {
    public ModelChatList(String userId) {
        this.userId = userId;
    }

    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ModelChatList() {
    }
}