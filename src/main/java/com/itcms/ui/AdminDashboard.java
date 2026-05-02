package com.itcms.ui;

import com.itcms.model.*;
import com.itcms.service.DataStore;
import com.itcms.util.UITheme;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class AdminDashboard extends BaseDashboard {
    private JButton dashBtn, complaintsBtn, usersBtn, reportsBtn, notifBtn;

    public AdminDashboard(Admin user) {
        super(user, "Admin Dashboard");
    }

    @Override
    protected void buildNavItems(JPanel sidebar) {
        dashBtn = addNavButton(sidebar, "🏠", "Dashboard", this::showDashboard);
        complaintsBtn = addNavButton(sidebar, "📋", "Manage Complaints", this::showComplaints);
        usersBtn = addNavButton(sidebar, "👥", "Manage Users", this::showUsers);
        reportsBtn = addNavButton(sidebar, "📊", "Generate Reports", this::showReports);
        notifBtn = addNavButton(sidebar, "🔔", "Notifications", this::showNotifications);
        setActiveNav(dashBtn);
        showDashboard();
    }

    private void showDashboard() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBackground(UITheme.BG_DARK);

        JPanel welcome = UITheme.createCard();
        welcome.setLayout(new BoxLayout(welcome, BoxLayout.Y_AXIS));
        welcome.add(UITheme.createLabel("Admin Control Panel", UITheme.FONT_TITLE, UITheme.TEXT_PRIMARY));
        welcome.add(Box.createVerticalStrut(4));
        welcome.add(UITheme.createLabel("System overview and management tools", UITheme.FONT_BODY, UITheme.TEXT_SECONDARY));
        panel.add(welcome, BorderLayout.NORTH);

        com.itcms.service.DataStore ds = DataStore.getInstance();
        List<Complaint> all = ds.getComplaints();
        long pending = all.stream().filter(c -> c.getStatus().equals("PENDING")).count();
        long inProg = all.stream().filter(c -> c.getStatus().equals("IN_PROGRESS")).count();
        long resolved = all.stream().filter(c -> c.getStatus().equals("RESOLVED")).count();
        int totalUsers = ds.getUsers().size();
        int totalTechs = ds.getTechnicians().size();

        JPanel stats = new JPanel(new GridLayout(2, 3, 14, 14));
        stats.setBackground(UITheme.BG_DARK);
        stats.add(makeStatCard("Total Complaints", String.valueOf(all.size()), UITheme.ACCENT_BLUE, "📊"));
        stats.add(makeStatCard("Pending", String.valueOf(pending), UITheme.ACCENT_ORANGE, "⏳"));
        stats.add(makeStatCard("In Progress", String.valueOf(inProg), UITheme.ACCENT_BLUE, "🔧"));
        stats.add(makeStatCard("Resolved", String.valueOf(resolved), UITheme.ACCENT_GREEN, "✅"));
        stats.add(makeStatCard("Total Users", String.valueOf(totalUsers), UITheme.ACCENT_CYAN, "👥"));
        stats.add(makeStatCard("Technicians", String.valueOf(totalTechs), new Color(167, 139, 250), "👷"));
        panel.add(stats, BorderLayout.CENTER);

        showPanel(panel, "Dashboard");
    }

    private JPanel makeStatCard(String title, String value, Color accent, String icon) {
        JPanel card = UITheme.createCard();
        card.setLayout(new BorderLayout());
        JLabel top = new JLabel(icon + "  " + title);
        top.setFont(UITheme.FONT_SMALL);
        top.setForeground(UITheme.TEXT_SECONDARY);
        JLabel val = new JLabel(value);
        val.setFont(new Font("SansSerif", Font.BOLD, 34));
        val.setForeground(accent);
        card.add(top, BorderLayout.NORTH);
        card.add(val, BorderLayout.CENTER);
        return card;
    }

    private void showComplaints() {
        JPanel panel = new JPanel(new BorderLayout(0, 14));
        panel.setBackground(UITheme.BG_DARK);
        panel.add(UITheme.createLabel("Manage Complaints", UITheme.FONT_SUBTITLE, UITheme.TEXT_PRIMARY), BorderLayout.NORTH);

        List<Complaint> complaints = DataStore.getInstance().getComplaints();
        String[] cols = {"Ticket #", "Reporter", "Equipment", "Location", "Status", "Technician", "Date"};

        JTable table = buildComplaintsTable(complaints, cols);
        panel.add(UITheme.createScrollPane(table), BorderLayout.CENTER);

        JPanel actionCard = UITheme.createCard();
        actionCard.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 4));
        JButton deleteBtn = UITheme.createButton("Delete Invalid Complaint 🗑", UITheme.ACCENT_RED);
        JLabel statusLabel = UITheme.createLabel("", UITheme.FONT_SMALL, UITheme.ACCENT_GREEN);
        actionCard.add(deleteBtn);
        actionCard.add(statusLabel);
        panel.add(actionCard, BorderLayout.SOUTH);

        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { statusLabel.setForeground(UITheme.ACCENT_RED); statusLabel.setText("Select a complaint first."); return; }
            Complaint c = complaints.get(row);
            int confirm = JOptionPane.showConfirmDialog(this,
                "Delete complaint #" + c.getComplaintId() + "? This cannot be undone.",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                ((Admin) currentUser).deleteInvalidComplaint(c, complaints);
                Object[][] data = buildComplaintData(complaints);
                DefaultTableModel model = new DefaultTableModel(data, cols) { public boolean isCellEditable(int r, int col) { return false; } };
                table.setModel(model);
                table.getColumnModel().getColumn(4).setCellRenderer(new UserDashboard.StatusCellRenderer());
                statusLabel.setForeground(UITheme.ACCENT_GREEN);
                statusLabel.setText("✓ Complaint deleted.");
            }
        });

        showPanel(panel, "Manage Complaints");
    }

    private void showUsers() {
        JPanel panel = new JPanel(new BorderLayout(0, 14));
        panel.setBackground(UITheme.BG_DARK);
        panel.add(UITheme.createLabel("User Management", UITheme.FONT_SUBTITLE, UITheme.TEXT_PRIMARY), BorderLayout.NORTH);

        List<User> users = DataStore.getInstance().getUsers();
        String[] cols = {"ID", "Name", "Username", "Email", "Occupation", "Role"};
        Object[][] data = new Object[users.size()][6];
        for (int i = 0; i < users.size(); i++) {
            User u = users.get(i);
            data[i] = new Object[]{u.getId(), u.getName(), u.getUsername(), u.getEmail(), u.getOccupation(), u.getRole()};
        }

        JTable table = new JTable(data, cols) { public boolean isCellEditable(int r, int c) { return false; } };
        styleTable(table);
        table.getColumnModel().getColumn(5).setCellRenderer(new RoleCellRenderer());
        panel.add(UITheme.createScrollPane(table), BorderLayout.CENTER);

        // Add user card
        JPanel addCard = UITheme.createCard();
        addCard.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 4));
        addCard.add(UITheme.createLabel("Total Users: " + users.size(), UITheme.FONT_BODY, UITheme.TEXT_PRIMARY));

        showPanel(panel, "Manage Users");
    }

    private void showReports() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBackground(UITheme.BG_DARK);
        panel.add(UITheme.createLabel("Generate Reports", UITheme.FONT_SUBTITLE, UITheme.TEXT_PRIMARY), BorderLayout.NORTH);

        JPanel reportCard = UITheme.createCard();
        reportCard.setLayout(new BoxLayout(reportCard, BoxLayout.Y_AXIS));

        List<Complaint> all = DataStore.getInstance().getComplaints();
        Report report = ((Admin) currentUser).generateReport(all);

        reportCard.add(UITheme.createLabel("System Report — Generated: " + report.getGeneratedAt(), UITheme.FONT_SUBTITLE, UITheme.TEXT_PRIMARY));
        reportCard.add(Box.createVerticalStrut(16));

        reportCard.add(createReportRow("Total Complaints", String.valueOf(report.getTotalComplaints()), UITheme.ACCENT_BLUE));
        reportCard.add(Box.createVerticalStrut(8));
        reportCard.add(createReportRow("Pending Complaints", String.valueOf(report.getPendingComplaints()), UITheme.ACCENT_ORANGE));
        reportCard.add(Box.createVerticalStrut(8));
        reportCard.add(createReportRow("Resolved Complaints", String.valueOf(report.getResolvedComplaints()), UITheme.ACCENT_GREEN));
        reportCard.add(Box.createVerticalStrut(8));

        long inProg = all.stream().filter(c -> c.getStatus().equals("IN_PROGRESS")).count();
        reportCard.add(createReportRow("In Progress", String.valueOf(inProg), UITheme.ACCENT_BLUE));
        reportCard.add(Box.createVerticalStrut(16));

        double resolveRate = all.isEmpty() ? 0 : (report.getResolvedComplaints() * 100.0 / all.size());
        reportCard.add(createReportRow("Resolution Rate", String.format("%.1f%%", resolveRate), UITheme.ACCENT_CYAN));
        reportCard.add(Box.createVerticalStrut(16));

        // Equipment breakdown
        reportCard.add(UITheme.createLabel("Complaints by Equipment Type:", UITheme.FONT_SUBTITLE, UITheme.TEXT_PRIMARY));
        reportCard.add(Box.createVerticalStrut(8));
        String[] equipTypes = {"PC / Computer", "Printer", "Projector", "Network Device", "Scanner", "Other"};
        for (String type : equipTypes) {
            long count = all.stream().filter(c -> c.getEquipment() != null && c.getEquipment().getType().equals(type)).count();
            if (count > 0) {
                reportCard.add(createReportRow(type, String.valueOf(count), UITheme.TEXT_SECONDARY));
                reportCard.add(Box.createVerticalStrut(4));
            }
        }

        panel.add(UITheme.createScrollPane(reportCard), BorderLayout.CENTER);
        showPanel(panel, "Generate Reports");
    }

    private JPanel createReportRow(String label, String value, Color accent) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(UITheme.BG_CARD);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));

        JLabel labelL = UITheme.createLabel(label, UITheme.FONT_BODY, UITheme.TEXT_SECONDARY);
        JLabel valueL = new JLabel(value);
        valueL.setFont(new Font("SansSerif", Font.BOLD, 18));
        valueL.setForeground(accent);

        row.add(labelL, BorderLayout.WEST);
        row.add(valueL, BorderLayout.EAST);
        return row;
    }

    private void showNotifications() {
        JPanel panel = new JPanel(new BorderLayout(0, 14));
        panel.setBackground(UITheme.BG_DARK);
        panel.add(UITheme.createLabel("Notifications", UITheme.FONT_SUBTITLE, UITheme.TEXT_PRIMARY), BorderLayout.NORTH);

        JPanel list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setBackground(UITheme.BG_DARK);

        for (Notification n : DataStore.getInstance().getNotifications()) {
            JPanel item = UITheme.createCard();
            item.setLayout(new BorderLayout());
            item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));
            item.add(UITheme.createLabel("● " + n.getMessage(), UITheme.FONT_BODY, UITheme.TEXT_PRIMARY), BorderLayout.CENTER);
            item.add(UITheme.createLabel(n.getSentAt(), UITheme.FONT_SMALL, UITheme.TEXT_SECONDARY), BorderLayout.EAST);
            list.add(item);
            list.add(Box.createVerticalStrut(6));
        }

        panel.add(UITheme.createScrollPane(list), BorderLayout.CENTER);
        showPanel(panel, "Notifications");
    }

    private JTable buildComplaintsTable(List<Complaint> complaints, String[] cols) {
        Object[][] data = buildComplaintData(complaints);
        JTable table = new JTable(data, cols) { public boolean isCellEditable(int r, int c) { return false; } };
        styleTable(table);
        table.getColumnModel().getColumn(4).setCellRenderer(new UserDashboard.StatusCellRenderer());
        return table;
    }

    private Object[][] buildComplaintData(List<Complaint> complaints) {
        Object[][] data = new Object[complaints.size()][7];
        for (int i = 0; i < complaints.size(); i++) {
            Complaint c = complaints.get(i);
            data[i] = new Object[]{
                "#" + c.getComplaintId(),
                c.getReporter().getName(),
                c.getEquipment() != null ? c.getEquipment().getType() : "N/A",
                c.getLocation(),
                c.getStatus(),
                c.getAssignedTechnician() != null ? c.getAssignedTechnician().getName() : "Unassigned",
                c.getCreatedAt()
            };
        }
        return data;
    }

    private void styleTable(JTable table) {
        table.setBackground(UITheme.BG_CARD);
        table.setForeground(UITheme.TEXT_PRIMARY);
        table.setFont(UITheme.FONT_BODY);
        table.setRowHeight(36);
        table.setGridColor(UITheme.BORDER_COLOR);
        table.setSelectionBackground(new Color(37, 99, 235, 80));
        table.setSelectionForeground(UITheme.TEXT_PRIMARY);
        table.getTableHeader().setBackground(UITheme.BG_INPUT);
        table.getTableHeader().setForeground(UITheme.TEXT_SECONDARY);
        table.getTableHeader().setFont(UITheme.FONT_SMALL);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
    }

    static class RoleCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
            String role = String.valueOf(value);
            switch (role) {
                case "ADMIN": label.setForeground(new Color(167, 139, 250)); break;
                case "IT_STAFF": label.setForeground(UITheme.ACCENT_BLUE); break;
                case "TECHNICIAN": label.setForeground(UITheme.ACCENT_ORANGE); break;
                default: label.setForeground(UITheme.ACCENT_GREEN);
            }
            label.setFont(new Font("SansSerif", Font.BOLD, 12));
            return label;
        }
    }
}
