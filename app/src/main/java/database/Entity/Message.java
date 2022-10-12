package database.Entity;

import java.util.Date;

public class Message {
    private String message;
    private String sender;
    private String roomID;
    private Long date;
    public Message(String message, String sender, String roomID) {
        this.message = message;
        this.sender = sender;
        this.roomID = roomID;
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
