package com.itcms.ui;

import com.itcms.model.*;
import com.itcms.service.DataStore;
import com.itcms.util.UITheme;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class TechnicianDashboard extends BaseDashboard {
    private JButton dashBtn, assignedBtn, updateBtn;

    public TechnicianDashboard(Technician user) {
        super(user, "Technician Dashboard");
    }

    @Override
    protected void buildNavItems(JPanel sidebar) {
        dashBtn = addNavButton(sidebar, "🏠", "Dashboard", this::showDashboard);
        assignedBtn = addNavButton(sidebar, "🔧", "Assigned Complaints", this::showAssignedComplaints);
        updateBtn = addNavButton(sidebar, "📝", "Update Status", this::showAssignedComplaints);
        setActiveNav(dashBtn);
        showDashboard();
    }

    private void showDashboard() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBackground(UITheme.BG_DARK);

        JPanel welcome = UITheme.createCard();
        welcome.setLayout(new BoxLayout(welcome, BoxLayout.Y_AXIS));
        welcome.add(UITheme.createLabel("Technician Panel — " + currentUser.getName(), UITheme.FONT_TITLE, UITheme.TEXT_PRIMARY));
        welcome.add(Box.createVerticalStrut(4));
        Technician tech = (Technician) currentUser;
        welcome.add(UITheme.createLabel("Specialization: " + tech.getSpecialization(), UITheme.FONT_BODY, UITheme.ACCENT_CYAN));
        panel.add(welcome, BorderLayout.NORTH);

        List<Complaint> mine = DataStore.getInstance().getComplaintsForTechnician((Technician) currentUser);
        long pending = mine.stream().filter(c -> c.getStatus().equals("PENDING")).count();
        long inProg = mine.stream().filter(c -> c.getStatus().equals("IN_PROGRESS")).count();
        long resolved = mine.stream().filter(c -> c.getStatus().equals("RESOLVED")).count();

        JPanel stats = new JPanel(new GridLayout(1, 3, 14, 0));
        stats.setBackground(UITheme.BG_DARK);
        stats.add(makeStatCard("Assigned", String.valueOf(mine.size()), UITheme.ACCENT_BLUE, "📋"));
        stats.add(makeStatCard("In Progress", String.valueOf(inProg), UITheme.ACCENT_ORANGE, "🔧"));
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

    private void showAssignedComplaints() {
        JPanel panel = new JPanel(new BorderLayout(0, 14));
        panel.setBackground(UITheme.BG_DARK);
        panel.add(UITheme.createLabel("Assigned Complaints", UITheme.FONT_SUBTITLE, UITheme.TEXT_PRIMARY), BorderLayout.NORTH);

        List<Complaint> complaints = DataStore.getInstance().getComplaintsForTechnician((Technician) currentUser);
        String[] cols = {"Ticket #", "Equipment", "Location", "Description", "Status", "Assigned On"};
        Object[][] data = buildData(complaints);

        JTable table = buildTable(data, cols);
        table.getColumnModel().getColumn(4).setCellRenderer(new UserDashboard.StatusCellRenderer());
        panel.add(UITheme.createScrollPane(table), BorderLayout.CENTER);

        // Update panel
        JPanel updateCard = UITheme.createCard();
        updateCard.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        updateCard.add(UITheme.createLabel("Update Selected Complaint", UITheme.FONT_SUBTITLE, UITheme.TEXT_PRIMARY), makeGbc(0, 0, 8));

        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        row1.setBackground(UITheme.BG_CARD);
        row1.add(UITheme.createLabel("New Status:", UITheme.FONT_SMALL, UITheme.TEXT_SECONDARY));
        JComboBox<String> statusCombo = UITheme.createComboBox(new String[]{"IN_PROGRESS", "RESOLVED"});
        row1.add(statusCombo);
        gbc.gridy = 1; gbc.insets = new Insets(8, 0, 6, 0);
        updateCard.add(row1, gbc);

        updateCard.add(UITheme.createLabel("Repair Notes:", UITheme.FONT_SMALL, UITheme.TEXT_SECONDARY), makeGbc(2, 0, 4));
        JTextArea notesArea = UITheme.createTextArea(3, 40);
        updateCard.add(UITheme.createScrollPane(notesArea), makeGbc(3, 0, 4));

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btnRow.setBackground(UITheme.BG_CARD);
        JButton updateBtn2 = UITheme.createButton("Update Complaint", UITheme.ACCENT_BLUE);
        JLabel statusLabel = UITheme.createLabel("", UITheme.FONT_SMALL, UITheme.ACCENT_GREEN);
        btnRow.add(statusLabel);
        btnRow.add(updateBtn2);
        gbc.gridy = 4; gbc.insets = new Insets(10, 0, 0, 0);
        updateCard.add(btnRow, gbc);

        panel.add(updateCard, BorderLayout.SOUTH);

        updateBtn2.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { statusLabel.setForeground(UITheme.ACCENT_RED); statusLabel.setText("Select a complaint first."); return; }
            Complaint c = complaints.get(row);
            String newStatus = (String) statusCombo.getSelectedItem();
            String notes = notesArea.getText().trim();
            ((Technician) currentUser).updateRepairStatus(c, newStatus);
            if (!notes.isEmpty()) ((Technician) currentUser).addRepairNotes(c, notes);
            DataStore.getInstance().addNotification(new Notification("Complaint #" + c.getComplaintId() + " updated to " + newStatus + " by " + currentUser.getName()));

            Object[][] newData = buildData(complaints);
            DefaultTableModel model = new DefaultTableModel(newData, cols) {
                public boolean isCellEditable(int r2, int c2) { return false; }
            };
            table.setModel(model);
            table.getColumnModel().getColumn(4).setCellRenderer(new UserDashboard.StatusCellRenderer());
            statusLabel.setForeground(UITheme.ACCENT_GREEN);
            statusLabel.setText("✓ Updated to " + newStatus);
            notesArea.setText("");
        });

        showPanel(panel, "Assigned Complaints");
    }

    private Object[][] buildData(List<Complaint> complaints) {
        Object[][] data = new Object[complaints.size()][6];
        for (int i = 0; i < complaints.size(); i++) {
            Complaint c = complaints.get(i);
            data[i] = new Object[]{
                "#" + c.getComplaintId(),
                c.getEquipment() != null ? c.getEquipment().getType() : "N/A",
                c.getLocation(),
                c.getDescription().length() > 40 ? c.getDescription().substring(0, 40) + "..." : c.getDescription(),
                c.getStatus(),
                c.getCreatedAt()
            };
        }
        return data;
    }

    private JTable buildTable(Object[][] data, String[] cols) {
        JTable table = new JTable(data, cols) { public boolean isCellEditable(int r, int c) { return false; } };
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

    private GridBagConstraints makeGbc(int row, int gridx, int topInset) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = gridx;
        gbc.gridy = row;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(topInset, 0, 4, 0);
        return gbc;
    }
}
