package com.application.bahasa.arab.data.chats;

public class ModelChat {
    private String sender;
    private String receiver;
    private String message;
    private boolean readChats;

    public ModelChat(String sender, String receiver, String message, boolean readChats) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.readChats = readChats;
    }

    public ModelChat() {
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isReadChats() {
        return readChats;
    }

    public void setReadChats(boolean readChats) {
        this.readChats = readChats;
    }
}
