package com.itcms.service;

import com.itcms.model.*;
import java.util.ArrayList;
import java.util.List;

public class DataStore {
    private static DataStore instance;

    private List<User> users = new ArrayList<>();
    private List<Complaint> complaints = new ArrayList<>();
    private List<Notification> notifications = new ArrayList<>();
    private List<Feedback> feedbacks = new ArrayList<>();
    private List<Technician> technicians = new ArrayList<>();
    private List<ITStaff> itStaffList = new ArrayList<>();
    private User currentUser;

    private DataStore() {
        seedData();
    }

    public static DataStore getInstance() {
        if (instance == null) instance = new DataStore();
        return instance;
    }

    private void seedData() {
        // Admin
        Admin admin = new Admin(1, "Admin User", "admin", "admin123", "admin@university.edu");
        users.add(admin);

        // IT Staff
        ITStaff staff1 = new ITStaff(2, "Ali Hassan", "ali", "ali123", "ali@university.edu");
        users.add(staff1);
        itStaffList.add(staff1);

        // Technicians
        Technician tech1 = new Technician(3, "Kamran Ahmed", "kamran", "kamran123", "kamran@university.edu", "Hardware");
        Technician tech2 = new Technician(4, "Bilal Raza", "bilal", "bilal123", "bilal@university.edu", "Networking");
        users.add(tech1);
        users.add(tech2);
        technicians.add(tech1);
        technicians.add(tech2);

        // Regular users
        User user1 = new User(5, "Sara Khan", "sara", "sara123", "sara@university.edu", "Student", "USER");
        User user2 = new User(6, "Usman Malik", "usman", "usman123", "usman@university.edu", "Faculty", "USER");
        users.add(user1);
        users.add(user2);

        // Sample complaints
        Equipment eq1 = new Equipment(1, "PC", "Lab A");
        Equipment eq2 = new Equipment(2, "Projector", "Room 301");
        Equipment eq3 = new Equipment(3, "Printer", "Office B");

        Complaint c1 = new Complaint("PC is not starting, black screen on boot", "Lab A - Computer 5", user1, eq1);
        Complaint c2 = new Complaint("Projector displaying distorted image", "Room 301", user2, eq2);
        Complaint c3 = new Complaint("Printer paper jam and error light blinking", "Office B", user1, eq3);
        c2.setStatus("IN_PROGRESS");
        c2.setAssignedTechnician(tech1);
        c3.setStatus("RESOLVED");
        c3.setRepairNotes("Paper jam cleared, rollers cleaned.");

        complaints.add(c1);
        complaints.add(c2);
        complaints.add(c3);

        notifications.add(new Notification("New complaint #" + c1.getComplaintId() + " submitted by " + user1.getName()));
        notifications.add(new Notification("Complaint #" + c2.getComplaintId() + " assigned to " + tech1.getName()));
        notifications.add(new Notification("Complaint #" + c3.getComplaintId() + " has been resolved"));
    }

    public User authenticate(String username, String password) {
        return users.stream()
            .filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password))
            .findFirst().orElse(null);
    }

    public boolean usernameExists(String username) {
        return users.stream().anyMatch(u -> u.getUsername().equals(username));
    }

    public boolean emailExists(String email) {
        return users.stream().anyMatch(u -> u.getEmail().equals(email));
    }

    public void addUser(User user) { users.add(user); }
    public void addComplaint(Complaint c) {
        complaints.add(c);
        notifications.add(new Notification("New complaint #" + c.getComplaintId() + " submitted by " + c.getReporter().getName()));
    }
    public void addNotification(Notification n) { notifications.add(n); }
    public void addFeedback(Feedback f) { feedbacks.add(f); }
    public void removeComplaint(Complaint c) { complaints.remove(c); }

    public List<User> getUsers() { return users; }
    public List<Complaint> getComplaints() { return complaints; }
    public List<Notification> getNotifications() { return notifications; }
    public List<Feedback> getFeedbacks() { return feedbacks; }
    public List<Technician> getTechnicians() { return technicians; }
    public List<ITStaff> getItStaffList() { return itStaffList; }

    public User getCurrentUser() { return currentUser; }
    public void setCurrentUser(User user) { this.currentUser = user; }

    public List<Complaint> getComplaintsForUser(User user) {
        List<Complaint> result = new ArrayList<>();
        for (Complaint c : complaints) {
            if (c.getReporter().getId() == user.getId()) result.add(c);
        }
        return result;
    }

    public List<Complaint> getComplaintsForTechnician(Technician tech) {
        List<Complaint> result = new ArrayList<>();
        for (Complaint c : complaints) {
            if (c.getAssignedTechnician() != null && c.getAssignedTechnician().getId() == tech.getId()) result.add(c);
        }
        return result;
    }

    public int getNextUserId() { return users.size() + 1; }
}
