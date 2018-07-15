package com.cfa.chyrellos.chatfa.Model;

import java.util.Date;

/**
 * Created by chyrellos on 07.05.2017.
 */

public class ChatMessage {
    private String roomID;
    private String messageText;
    private String messageSenderUID;
    private long messageTime;

    public ChatMessage(String roomID, String messageText, String messageSenderUID) {
        this.roomID = roomID;
        this.messageText = messageText;
        this.messageSenderUID = messageSenderUID;
        messageTime = new Date().getTime();
    }

    public ChatMessage(){

    }

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageSenderUID() {
        return messageSenderUID;
    }

    public void setMessageSenderUID(String messageSenderUID) {
        this.messageSenderUID = messageSenderUID;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }
}
