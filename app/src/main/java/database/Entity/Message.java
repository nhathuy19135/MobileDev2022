package database.Entity;

import java.util.Date;
import java.util.UUID;

public class Message {
    private String message;
    private String sender;
    private String roomID;
    private Long date;
    private String messageID;
    public Message(String message, String sender, String roomID) {
        this.message = message;
        this.sender = sender;
        this.roomID = roomID;
        this.date=  new Date().getTime();
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public String getMessageID() {
        return messageID;
    }

    public Long getDate() {
        return date;
    }

    public Message() {

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public String toString1() {
        return this.message+" " + this.roomID;
    }
}
