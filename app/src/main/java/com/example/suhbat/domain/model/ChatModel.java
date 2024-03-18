package com.example.suhbat.domain.model;

public class ChatModel {
    String chatKey;
    String messageKey;
    String message;
    String senderKey;
    String receiverKey;
    String timeStamp;
    boolean readStatus;

    public ChatModel() {
    }


    public ChatModel(String chatKey, String messageKey, String message, String senderKey, String receiverKey, String timeStamp, boolean readStatus) {
        this.chatKey = chatKey;
        this.messageKey = messageKey;
        this.message = message;
        this.senderKey = senderKey;
        this.receiverKey = receiverKey;
        this.timeStamp = timeStamp;
        this.readStatus = readStatus;
    }

    public boolean isReadStatus() {
        return readStatus;
    }

    public void setReadStatus(boolean readStatus) {
        this.readStatus = readStatus;
    }

    public String getChatKey() {
        return chatKey;
    }

    public void setChatKey(String chatKey) {
        this.chatKey = chatKey;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderKey() {
        return senderKey;
    }

    public void setSenderKey(String senderKey) {
        this.senderKey = senderKey;
    }

    public String getReceiverKey() {
        return receiverKey;
    }

    public void setReceiverKey(String receiverKey) {
        this.receiverKey = receiverKey;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
