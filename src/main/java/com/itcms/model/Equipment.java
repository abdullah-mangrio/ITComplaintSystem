package com.itcms.model;

public class Equipment {
    private int id;
    private String type;  // PC, Printer, Projector, Network, Other
    private String defect;
    private String company;
    private int manufacturingYear;
    private String location;
    private String importanceLevel;
    private String status;

    public Equipment(int id, String type, String location) {
        this.id = id;
        this.type = type;
        this.location = location;
        this.status = "OPERATIONAL";
        this.defect = "";
        this.company = "";
        this.importanceLevel = "MEDIUM";
    }

    public boolean verifyCompany() { return company != null && !company.isEmpty(); }

    public void updateStatus(String newStatus) { this.status = newStatus; }

    public void displayInfo() {
        System.out.println("Equipment: " + type + " at " + location);
    }

    // Getters and setters
    public int getId() { return id; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getDefect() { return defect; }
    public void setDefect(String defect) { this.defect = defect; }
    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }
    public int getManufacturingYear() { return manufacturingYear; }
    public void setManufacturingYear(int year) { this.manufacturingYear = year; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getImportanceLevel() { return importanceLevel; }
    public void setImportanceLevel(String level) { this.importanceLevel = level; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() { return type + " (" + location + ")"; }
}
