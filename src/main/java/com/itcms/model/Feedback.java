package com.itcms.model;

public class Feedback {
    private int feedbackId;
    private int rating;
    private String comments;
    private String submittedAt;
    private Complaint complaint;
    private static int counter = 200;

    public Feedback(int rating, String comments, Complaint complaint) {
        this.feedbackId = ++counter;
        this.rating = rating;
        this.comments = comments;
        this.complaint = complaint;
        this.submittedAt = java.time.LocalDateTime.now()
            .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    public void submitFeedback() {}
    public void displayInfo() {}

    public int getFeedbackId() { return feedbackId; }
    public int getRating() { return rating; }
    public String getComments() { return comments; }
    public String getSubmittedAt() { return submittedAt; }
    public Complaint getComplaint() { return complaint; }
}
