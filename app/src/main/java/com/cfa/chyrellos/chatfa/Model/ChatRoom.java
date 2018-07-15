package com.cfa.chyrellos.chatfa.Model;

import java.util.Date;

/**
 * Created by chyrellos on 09.05.2017.
 */

public class ChatRoom {


    private String userUID;
    private String userName;
    private String lastMessage;
    private long messageTime;

    public ChatRoom( String userUID, String userName, String lastMessage) {

        this.userUID = userUID;
        this.userName = userName;
        if(lastMessage.length() > 10)
            this.lastMessage = lastMessage.substring(0,10);
        else
            this.lastMessage = lastMessage;
        this.messageTime = new Date().getTime();
    }

    public ChatRoom(){

    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }
}
