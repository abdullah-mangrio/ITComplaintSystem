package com.itcms.ui;

import com.itcms.model.*;
import com.itcms.service.DataStore;
import com.itcms.util.UITheme;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class ITStaffDashboard extends BaseDashboard {
    private JButton dashBtn, allComplaintsBtn, assignBtn, notifBtn;

    public ITStaffDashboard(ITStaff user) {
        super(user, "IT Staff Dashboard");
    }

    @Override
    protected void buildNavItems(JPanel sidebar) {
        dashBtn = addNavButton(sidebar, "🏠", "Dashboard", this::showDashboard);
        allComplaintsBtn = addNavButton(sidebar, "📋", "All Complaints", this::showAllComplaints);
        assignBtn = addNavButton(sidebar, "👷", "Assign Complaints", this::showAssignComplaint);
        notifBtn = addNavButton(sidebar, "🔔", "Notifications", this::showNotifications);
        setActiveNav(dashBtn);
        showDashboard();
    }

    private void showDashboard() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBackground(UITheme.BG_DARK);

        JPanel welcome = UITheme.createCard();
        welcome.setLayout(new BoxLayout(welcome, BoxLayout.Y_AXIS));
        welcome.add(UITheme.createLabel("IT Staff Panel — " + currentUser.getName(), UITheme.FONT_TITLE, UITheme.TEXT_PRIMARY));
        welcome.add(Box.createVerticalStrut(4));
        welcome.add(UITheme.createLabel("Manage complaints, assign technicians, and mark resolutions.", UITheme.FONT_BODY, UITheme.TEXT_SECONDARY));
        panel.add(welcome, BorderLayout.NORTH);

        List<Complaint> all = DataStore.getInstance().getComplaints();
        long pending = all.stream().filter(c -> c.getStatus().equals("PENDING")).count();
        long inProg = all.stream().filter(c -> c.getStatus().equals("IN_PROGRESS")).count();
        long resolved = all.stream().filter(c -> c.getStatus().equals("RESOLVED")).count();

        JPanel stats = new JPanel(new GridLayout(1, 4, 14, 0));
        stats.setBackground(UITheme.BG_DARK);
        stats.add(makeStatCard("Total", String.valueOf(all.size()), UITheme.ACCENT_BLUE, "📊"));
        stats.add(makeStatCard("Pending", String.valueOf(pending), UITheme.ACCENT_ORANGE, "⏳"));
        stats.add(makeStatCard("In Progress", String.valueOf(inProg), UITheme.ACCENT_BLUE, "🔧"));
        stats.add(makeStatCard("Resolved", String.valueOf(resolved), UITheme.ACCENT_GREEN, "✅"));
        panel.add(stats, BorderLayout.CENTER);

        showPanel(panel, "Dashboard");
    }

    private JPanel makeStatCard(String title, String value, Color accent, String icon) {
        JPanel card = UITheme.createCard();
        card.setLayout(new BorderLayout());
        JLabel iconLabel = new JLabel(icon + "  " + title);
        iconLabel.setFont(UITheme.FONT_SMALL);
        iconLabel.setForeground(UITheme.TEXT_SECONDARY);
        JLabel valLabel = new JLabel(value);
        valLabel.setFont(new Font("SansSerif", Font.BOLD, 34));
        valLabel.setForeground(accent);
        card.add(iconLabel, BorderLayout.NORTH);
        card.add(valLabel, BorderLayout.CENTER);
        return card;
    }

    private void showAllComplaints() {
        JPanel panel = new JPanel(new BorderLayout(0, 14));
        panel.setBackground(UITheme.BG_DARK);

        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setBackground(UITheme.BG_DARK);
        topRow.add(UITheme.createLabel("All Complaints", UITheme.FONT_SUBTITLE, UITheme.TEXT_PRIMARY), BorderLayout.WEST);

        List<Complaint> complaints = DataStore.getInstance().getComplaints();
        String[] cols = {"Ticket #", "Reporter", "Equipment", "Location", "Status", "Assigned To", "Date"};
        Object[][] data = buildComplaintData(complaints);

        JTable table = createTable(data, cols);
        table.getColumnModel().getColumn(4).setCellRenderer(new UserDashboard.StatusCellRenderer());

        JScrollPane scroll = UITheme.createScrollPane(table);
        panel.add(topRow, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        // Action panel
        JPanel actionCard = UITheme.createCard();
        actionCard.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 4));

        JButton markResolvedBtn = UITheme.createButton("Mark as Resolved ✓", UITheme.ACCENT_GREEN);
        JButton assignTechBtn = UITheme.createButton("Assign Technician →", UITheme.ACCENT_BLUE);
        JLabel actionStatus = UITheme.createLabel("", UITheme.FONT_SMALL, UITheme.ACCENT_GREEN);

        actionCard.add(UITheme.createLabel("Selected: ", UITheme.FONT_SMALL, UITheme.TEXT_SECONDARY));
        actionCard.add(markResolvedBtn);
        actionCard.add(assignTechBtn);
        actionCard.add(actionStatus);
        panel.add(actionCard, BorderLayout.SOUTH);

        markResolvedBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { actionStatus.setForeground(UITheme.ACCENT_RED); actionStatus.setText("Select a complaint first."); return; }
            Complaint c = complaints.get(row);
            if (c.getStatus().equals("RESOLVED")) { actionStatus.setForeground(UITheme.ACCENT_ORANGE); actionStatus.setText("Already resolved."); return; }
            ((ITStaff) currentUser).markResolved(c);
            DataStore.getInstance().addNotification(new Notification("Complaint #" + c.getComplaintId() + " marked as resolved by IT Staff."));
            refreshTable(table, buildComplaintData(complaints), cols);
            actionStatus.setForeground(UITheme.ACCENT_GREEN);
            actionStatus.setText("✓ Complaint #" + c.getComplaintId() + " marked resolved.");
        });

        assignTechBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { actionStatus.setForeground(UITheme.ACCENT_RED); actionStatus.setText("Select a complaint first."); return; }
            Complaint c = complaints.get(row);
            showAssignDialog(c, actionStatus, table, complaints, cols);
        });

        showPanel(panel, "All Complaints");
    }

    private void showAssignDialog(Complaint c, JLabel statusLabel, JTable table, List<Complaint> complaints, String[] cols) {
        List<Technician> techs = DataStore.getInstance().getTechnicians();
        if (techs.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No technicians available.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        String[] techNames = techs.stream().map(t -> t.getName() + " (" + t.getSpecialization() + ")").toArray(String[]::new);
        int choice = JOptionPane.showOptionDialog(this,
            "Assign complaint #" + c.getComplaintId() + " to:",
            "Assign Technician",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, techNames, techNames[0]);
        if (choice >= 0) {
            ((ITStaff) currentUser).assignComplaint(c, techs.get(choice));
            DataStore.getInstance().addNotification(new Notification("Complaint #" + c.getComplaintId() + " assigned to " + techs.get(choice).getName()));
            refreshTable(table, buildComplaintData(complaints), cols);
            statusLabel.setForeground(UITheme.ACCENT_GREEN);
            statusLabel.setText("✓ Assigned to " + techs.get(choice).getName());
        }
    }

    private void showAssignComplaint() {
        showAllComplaints();
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

    private JTable createTable(Object[][] data, String[] cols) {
        JTable table = new JTable(data, cols) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
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
        return table;
    }

    private void refreshTable(JTable table, Object[][] data, String[] cols) {
        DefaultTableModel model = new DefaultTableModel(data, cols) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table.setModel(model);
        table.getColumnModel().getColumn(4).setCellRenderer(new UserDashboard.StatusCellRenderer());
    }
}
