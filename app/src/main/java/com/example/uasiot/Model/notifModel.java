package com.example.uasiot.Model;

public class notifModel {
    private String timestamp;
    private String message;
    private boolean isRead;

    // Constructor default
    public notifModel() {
    }

    // Constructor lengkap
    public notifModel(String timestamp, String message, boolean isRead) {
        this.timestamp = timestamp;
        this.message = message;
        this.isRead = isRead;
    }

    // Getter dan setter untuk timestamp
    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    // Getter dan setter untuk message
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    // Getter dan setter untuk isRead
    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }
}
