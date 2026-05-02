package com.itcms.model;

import java.util.List;

public class Admin extends User {
    private int adminId;

    public Admin(int id, String name, String username, String password, String email) {
        super(id, name, username, password, email, "Administrator", "ADMIN");
        this.adminId = id;
    }

    public void manageUserAccounts() {}

    public void deleteInvalidComplaint(Complaint c, List<Complaint> complaints) {
        complaints.remove(c);
    }

    public Report generateReport(List<Complaint> complaints) {
        int total = complaints.size();
        long pending = complaints.stream().filter(c -> c.getStatus().equals("PENDING")).count();
        long resolved = complaints.stream().filter(c -> c.getStatus().equals("RESOLVED")).count();
        return new Report(total, (int) pending, (int) resolved);
    }

    @Override
    public void displayInfo() {
        System.out.println("Admin: " + getName());
    }

    public int getAdminId() { return adminId; }
}
