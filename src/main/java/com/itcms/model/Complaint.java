package com.itcms.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Complaint {
    private int complaintId;
    private String description;
    private String image;
    private String location;
    private String status; // PENDING, IN_PROGRESS, RESOLVED, CLOSED
    private String createdAt;
    private String resolvedAt;
    private String repairNotes;
    private User reporter;
    private Equipment equipment;
    private Technician assignedTechnician;

    private static int idCounter = 1000;

    public Complaint(String description, String location, User reporter, Equipment equipment) {
        this.complaintId = generateTicketId();
        this.description = description;
        this.location = location;
        this.reporter = reporter;
        this.equipment = equipment;
        this.status = "PENDING";
        this.createdAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.repairNotes = "";
        this.image = "";
    }

    public int generateTicketId() {
        return ++idCounter;
    }

    public void addRepairNotes(String notes) {
        this.repairNotes = notes;
    }

    public void displayInfo() {
        System.out.println("Complaint #" + complaintId + " - " + status);
    }

    // Getters and setters
    public int getComplaintId() { return complaintId; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getStatus() { return status; }
    public void setStatus(String status) {
        this.status = status;
        if (status.equals("RESOLVED")) {
            this.resolvedAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        }
    }
    public String getCreatedAt() { return createdAt; }
    public String getResolvedAt() { return resolvedAt; }
    public String getRepairNotes() { return repairNotes; }
    public void setRepairNotes(String repairNotes) { this.repairNotes = repairNotes; }
    public User getReporter() { return reporter; }
    public Equipment getEquipment() { return equipment; }
    public Technician getAssignedTechnician() { return assignedTechnician; }
    public void setAssignedTechnician(Technician technician) { this.assignedTechnician = technician; }

    @Override
    public String toString() {
        return "#" + complaintId + " - " + (equipment != null ? equipment.getType() : "N/A") + " - " + status;
    }
}
