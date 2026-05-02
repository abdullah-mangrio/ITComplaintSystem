package com.itcms.model;

public class Technician extends User {
    private int technicianId;
    private String specialization;

    public Technician(int id, String name, String username, String password, String email, String specialization) {
        super(id, name, username, password, email, "Technician", "TECHNICIAN");
        this.technicianId = id;
        this.specialization = specialization;
    }

    public void viewAssignedComplaints() {}

    public void updateRepairStatus(Complaint c, String status) {
        c.setStatus(status);
    }

    public void addRepairNotes(Complaint c, String notes) {
        c.setRepairNotes(notes);
    }

    @Override
    public void displayInfo() {
        System.out.println("Technician: " + getName() + " | Spec: " + specialization);
    }

    public int getTechnicianId() { return technicianId; }
    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
}
