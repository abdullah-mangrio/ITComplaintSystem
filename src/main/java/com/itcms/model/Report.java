package com.itcms.model;

public class Report {
    private int reportId;
    private String generatedAt;
    private int totalComplaints;
    private int pendingComplaints;
    private int resolvedComplaints;
    private static int counter = 100;

    public Report(int total, int pending, int resolved) {
        this.reportId = ++counter;
        this.totalComplaints = total;
        this.pendingComplaints = pending;
        this.resolvedComplaints = resolved;
        this.generatedAt = java.time.LocalDateTime.now()
            .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    public void generateReport() {}
    public void exportReport() {}
    public void displayReport() {
        System.out.println("Report #" + reportId + ": Total=" + totalComplaints);
    }

    public int getReportId() { return reportId; }
    public String getGeneratedAt() { return generatedAt; }
    public int getTotalComplaints() { return totalComplaints; }
    public int getPendingComplaints() { return pendingComplaints; }
    public int getResolvedComplaints() { return resolvedComplaints; }
}
