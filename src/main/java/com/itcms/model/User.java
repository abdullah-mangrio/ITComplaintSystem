package com.itcms.model;

public class User {
    private int id;
    private String name;
    private String username;
    private String password;
    private String email;
    private String occupation;
    private String role; // USER, ADMIN, IT_STAFF, TECHNICIAN

    public User(int id, String name, String username, String password, String email, String occupation, String role) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.email = email;
        this.occupation = occupation;
        this.role = role;
    }

    public boolean login(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    public void register() {}

    public void viewComplaints() {}

    public void trackComplaintStatus(int complaintId) {}

    public void giveFeedback(Feedback f) {}

    public void displayInfo() {
        System.out.println("User: " + name + " | Role: " + role);
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getOccupation() { return occupation; }
    public void setOccupation(String occupation) { this.occupation = occupation; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    @Override
    public String toString() { return name + " (" + role + ")"; }
}
