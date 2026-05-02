package com.itcms.model;

public class Notification {
    private int notificationId;
    private String message;
    private String sentAt;
    private boolean isRead;
    private static int counter = 300;

    public Notification(String message) {
        this.notificationId = ++counter;
        this.message = message;
        this.isRead = false;
        this.sentAt = java.time.LocalDateTime.now()
            .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    public void sendNotification() {}
    public void markAsRead() { this.isRead = true; }
    public void displayInfo() {}

    public int getNotificationId() { return notificationId; }
    public String getMessage() { return message; }
    public String getSentAt() { return sentAt; }
    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { this.isRead = read; }
}
