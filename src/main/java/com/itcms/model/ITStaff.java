package com.itcms.model;

public class ITStaff extends User {
    private int staffId;

    public ITStaff(int id, String name, String username, String password, String email) {
        super(id, name, username, password, email, "IT Staff", "IT_STAFF");
        this.staffId = id;
    }

    public void viewAllComplaints() {}

    public void assignComplaint(Complaint c, Technician t) {
        c.setAssignedTechnician(t);
        c.setStatus("IN_PROGRESS");
    }

    public void markResolved(Complaint c) {
        c.setStatus("RESOLVED");
    }

    @Override
    public void displayInfo() {
        System.out.println("IT Staff: " + getName());
    }

    public int getStaffId() { return staffId; }
}
