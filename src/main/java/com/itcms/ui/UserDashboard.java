package com.itcms.ui;

import com.itcms.model.*;
import com.itcms.service.DataStore;
import com.itcms.util.UITheme;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class UserDashboard extends BaseDashboard {
    private JButton dashBtn, submitBtn, myComplaintsBtn, notificationsBtn, feedbackBtn;

    public UserDashboard(User user) {
        super(user, "User Dashboard");
    }

    @Override
    protected void buildNavItems(JPanel sidebar) {
        dashBtn = addNavButton(sidebar, "🏠", "Dashboard", this::showDashboard);
        submitBtn = addNavButton(sidebar, "📝", "Submit Complaint", this::showSubmitComplaint);
        myComplaintsBtn = addNavButton(sidebar, "📋", "My Complaints", this::showMyComplaints);
        notificationsBtn = addNavButton(sidebar, "🔔", "Notifications", this::showNotifications);
        feedbackBtn = addNavButton(sidebar, "⭐", "Give Feedback", this::showFeedback);

        setActiveNav(dashBtn);
        showDashboard();
    }

    private void showDashboard() {
        pageTitle.setText("Dashboard");
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBackground(UITheme.BG_DARK);

        // Welcome
        JPanel welcomeCard = UITheme.createCard();
        welcomeCard.setLayout(new BoxLayout(welcomeCard, BoxLayout.Y_AXIS));
        JLabel welcome = UITheme.createLabel("Welcome back, " + currentUser.getName() + " 👋", UITheme.FONT_TITLE, UITheme.TEXT_PRIMARY);
        JLabel sub = UITheme.createLabel("Report IT equipment issues quickly and track your complaints.", UITheme.FONT_BODY, UITheme.TEXT_SECONDARY);
        welcomeCard.add(welcome);
        welcomeCard.add(Box.createVerticalStrut(6));
        welcomeCard.add(sub);
        panel.add(welcomeCard, BorderLayout.NORTH);

        // Stats
        List<Complaint> mine = DataStore.getInstance().getComplaintsForUser(currentUser);
        long pending = mine.stream().filter(c -> c.getStatus().equals("PENDING")).count();
        long inProg = mine.stream().filter(c -> c.getStatus().equals("IN_PROGRESS")).count();
        long resolved = mine.stream().filter(c -> c.getStatus().equals("RESOLVED")).count();

        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 16, 0));
        statsPanel.setBackground(UITheme.BG_DARK);
        statsPanel.add(createStatCard("Total Complaints", String.valueOf(mine.size()), UITheme.ACCENT_BLUE, "📊"));
        statsPanel.add(createStatCard("Pending", String.valueOf(pending), UITheme.ACCENT_ORANGE, "⏳"));
        statsPanel.add(createStatCard("Resolved", String.valueOf(resolved), UITheme.ACCENT_GREEN, "✅"));
        panel.add(statsPanel, BorderLayout.CENTER);

        // Quick actions
        JPanel actionsCard = UITheme.createCard();
        actionsCard.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 0));
        JLabel actLabel = UITheme.createLabel("Quick Actions:", UITheme.FONT_SUBTITLE, UITheme.TEXT_PRIMARY);

        JButton quickSubmit = UITheme.createButton("+ Submit New Complaint", UITheme.ACCENT_BLUE);
        quickSubmit.addActionListener(e -> { setActiveNav(submitBtn); showSubmitComplaint(); });

        JButton quickView = UITheme.createButton("View My Complaints", UITheme.BG_INPUT);
        quickView.addActionListener(e -> { setActiveNav(myComplaintsBtn); showMyComplaints(); });

        actionsCard.add(actLabel);
        actionsCard.add(Box.createHorizontalStrut(10));
        actionsCard.add(quickSubmit);
        actionsCard.add(quickView);
        panel.add(actionsCard, BorderLayout.SOUTH);

        showPanel(panel, "Dashboard");
    }

    private JPanel createStatCard(String title, String value, Color accent, String icon) {
        JPanel card = UITheme.createCard();
        card.setLayout(new BorderLayout());

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        top.setBackground(UITheme.BG_CARD);
        JLabel iconLabel = new JLabel(icon + "  ");
        iconLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));
        JLabel titleLabel = UITheme.createLabel(title, UITheme.FONT_SMALL, UITheme.TEXT_SECONDARY);
        top.add(iconLabel);
        top.add(titleLabel);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("SansSerif", Font.BOLD, 36));
        valueLabel.setForeground(accent);

        card.add(top, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }

    private void showSubmitComplaint() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(UITheme.BG_DARK);

        JPanel card = UITheme.createCard();
        card.setLayout(new GridBagLayout());
        card.setPreferredSize(new Dimension(600, 500));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 0, 6, 0);
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        int row = 0;

        card.add(UITheme.createLabel("Submit New Complaint", UITheme.FONT_SUBTITLE, UITheme.TEXT_PRIMARY), makeGbc(row++, 12));

        card.add(UITheme.createLabel("Equipment Type *", UITheme.FONT_SMALL, UITheme.TEXT_SECONDARY), makeGbc(row++, 6));
        JComboBox<String> equipCombo = UITheme.createComboBox(new String[]{"PC / Computer", "Printer", "Projector", "Network Device", "Scanner", "Other"});
        card.add(equipCombo, makeGbc(row++, 4));

        card.add(UITheme.createLabel("Location *", UITheme.FONT_SMALL, UITheme.TEXT_SECONDARY), makeGbc(row++, 6));
        JComboBox<String> locationCombo = UITheme.createComboBox(new String[]{
            "Lab A", "Lab B", "Lab C", "Room 101", "Room 201", "Room 301",
            "Office A", "Office B", "Library", "Admin Block"
        });
        card.add(locationCombo, makeGbc(row++, 4));

        card.add(UITheme.createLabel("Problem Description *", UITheme.FONT_SMALL, UITheme.TEXT_SECONDARY), makeGbc(row++, 6));
        JTextArea descArea = UITheme.createTextArea(5, 30);
        JScrollPane descScroll = UITheme.createScrollPane(descArea);
        descScroll.setPreferredSize(new Dimension(500, 110));
        card.add(descScroll, makeGbc(row++, 4));

        card.add(UITheme.createLabel("Image Path (optional)", UITheme.FONT_SMALL, UITheme.TEXT_SECONDARY), makeGbc(row++, 6));
        JPanel imgPanel = new JPanel(new BorderLayout(8, 0));
        imgPanel.setBackground(UITheme.BG_CARD);
        JTextField imgField = UITheme.createTextField("Path to image...");
        JButton browseBtn = UITheme.createButton("Browse", UITheme.BG_INPUT);
        browseBtn.setFont(UITheme.FONT_SMALL);
        browseBtn.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                imgField.setText(fc.getSelectedFile().getAbsolutePath());
            }
        });
        imgPanel.add(imgField, BorderLayout.CENTER);
        imgPanel.add(browseBtn, BorderLayout.EAST);
        card.add(imgPanel, makeGbc(row++, 4));

        JLabel statusLabel = UITheme.createLabel("", UITheme.FONT_SMALL, UITheme.ACCENT_RED);
        card.add(statusLabel, makeGbc(row++, 8));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btnPanel.setBackground(UITheme.BG_CARD);
        JButton cancelBtn = UITheme.createButton("Cancel", UITheme.BG_INPUT);
        JButton submitBtn2 = UITheme.createButton("Submit Complaint →", UITheme.ACCENT_BLUE);
        btnPanel.add(cancelBtn);
        btnPanel.add(submitBtn2);
        GridBagConstraints btnGbc = makeGbc(row++, 8);
        card.add(btnPanel, btnGbc);

        cancelBtn.addActionListener(e -> { setActiveNav(dashBtn); showDashboard(); });

        submitBtn2.addActionListener(e -> {
            String desc = descArea.getText().trim();
            String loc = (String) locationCombo.getSelectedItem();
            String equipType = (String) equipCombo.getSelectedItem();

            if (desc.isEmpty()) {
                statusLabel.setText("Please enter a problem description.");
                statusLabel.setForeground(UITheme.ACCENT_RED);
                return;
            }
            if (desc.length() < 10) {
                statusLabel.setText("Description must be at least 10 characters.");
                statusLabel.setForeground(UITheme.ACCENT_RED);
                return;
            }

            Equipment equipment = new Equipment(DataStore.getInstance().getComplaints().size() + 1, equipType, loc);
            Complaint complaint = new Complaint(desc, loc, currentUser, equipment);
            complaint.setImage(imgField.getText());
            DataStore.getInstance().addComplaint(complaint);

            statusLabel.setForeground(UITheme.ACCENT_GREEN);
            statusLabel.setText("✓ Complaint #" + complaint.getComplaintId() + " submitted successfully!");
            descArea.setText("");
            imgField.setText("");
        });

        panel.add(card);
        showPanel(panel, "Submit Complaint");
    }

    private GridBagConstraints makeGbc(int row, int topInset) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(topInset, 0, 4, 0);
        return gbc;
    }

    private void showMyComplaints() {
        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.setBackground(UITheme.BG_DARK);

        JLabel header = UITheme.createLabel("My Complaints", UITheme.FONT_SUBTITLE, UITheme.TEXT_PRIMARY);
        panel.add(header, BorderLayout.NORTH);

        List<Complaint> complaints = DataStore.getInstance().getComplaintsForUser(currentUser);
        String[] cols = {"Ticket #", "Equipment", "Location", "Description", "Status", "Date"};
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

        JTable table = createStyledTable(data, cols);
        table.getColumnModel().getColumn(4).setCellRenderer(new StatusCellRenderer());

        JScrollPane scroll = UITheme.createScrollPane(table);
        panel.add(scroll, BorderLayout.CENTER);

        // Detail panel
        JPanel detailCard = UITheme.createCard();
        detailCard.setLayout(new BoxLayout(detailCard, BoxLayout.Y_AXIS));
        JLabel detailTitle = UITheme.createLabel("Select a complaint to view details", UITheme.FONT_SMALL, UITheme.TEXT_SECONDARY);
        detailCard.add(detailTitle);
        detailCard.setPreferredSize(new Dimension(0, 90));
        panel.add(detailCard, BorderLayout.SOUTH);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) {
                Complaint c = complaints.get(table.getSelectedRow());
                detailCard.removeAll();
                detailCard.add(UITheme.createLabel("Ticket #" + c.getComplaintId() + " — " + c.getStatus(), UITheme.FONT_SUBTITLE, UITheme.TEXT_PRIMARY));
                detailCard.add(UITheme.createLabel("Equipment: " + (c.getEquipment() != null ? c.getEquipment().getType() : "N/A") + " | Location: " + c.getLocation(), UITheme.FONT_SMALL, UITheme.TEXT_SECONDARY));
                detailCard.add(UITheme.createLabel("Description: " + c.getDescription(), UITheme.FONT_BODY, UITheme.TEXT_PRIMARY));
                if (!c.getRepairNotes().isEmpty()) {
                    detailCard.add(UITheme.createLabel("Repair Notes: " + c.getRepairNotes(), UITheme.FONT_SMALL, UITheme.ACCENT_GREEN));
                }
                detailCard.add(UITheme.createLabel("Submitted: " + c.getCreatedAt() + (c.getResolvedAt() != null ? "  |  Resolved: " + c.getResolvedAt() : ""), UITheme.FONT_SMALL, UITheme.TEXT_SECONDARY));
                detailCard.revalidate();
                detailCard.repaint();
            }
        });

        showPanel(panel, "My Complaints");
    }

    private void showNotifications() {
        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.setBackground(UITheme.BG_DARK);
        panel.add(UITheme.createLabel("Notifications", UITheme.FONT_SUBTITLE, UITheme.TEXT_PRIMARY), BorderLayout.NORTH);

        JPanel notifList = new JPanel();
        notifList.setLayout(new BoxLayout(notifList, BoxLayout.Y_AXIS));
        notifList.setBackground(UITheme.BG_DARK);

        List<com.itcms.model.Notification> notifications = DataStore.getInstance().getNotifications();
        for (com.itcms.model.Notification n : notifications) {
            JPanel item = UITheme.createCard();
            item.setLayout(new BorderLayout(8, 0));
            item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

            JLabel msgLabel = UITheme.createLabel((n.isRead() ? "  " : "● ") + n.getMessage(), UITheme.FONT_BODY, n.isRead() ? UITheme.TEXT_SECONDARY : UITheme.TEXT_PRIMARY);
            JLabel timeLabel = UITheme.createLabel(n.getSentAt(), UITheme.FONT_SMALL, UITheme.TEXT_SECONDARY);

            item.add(msgLabel, BorderLayout.CENTER);
            item.add(timeLabel, BorderLayout.EAST);
            n.markAsRead();

            notifList.add(item);
            notifList.add(Box.createVerticalStrut(6));
        }

        panel.add(UITheme.createScrollPane(notifList), BorderLayout.CENTER);
        showPanel(panel, "Notifications");
    }

    private void showFeedback() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(UITheme.BG_DARK);

        JPanel card = UITheme.createCard();
        card.setPreferredSize(new Dimension(500, 420));
        card.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        int row = 0;

        card.add(UITheme.createLabel("Submit Feedback", UITheme.FONT_SUBTITLE, UITheme.TEXT_PRIMARY), makeGbc(row++, 8));

        card.add(UITheme.createLabel("Select Complaint *", UITheme.FONT_SMALL, UITheme.TEXT_SECONDARY), makeGbc(row++, 10));

        List<Complaint> resolved = DataStore.getInstance().getComplaintsForUser(currentUser);
        resolved.removeIf(c -> !c.getStatus().equals("RESOLVED"));
        if (resolved.isEmpty()) {
            JLabel noLabel = UITheme.createLabel("No resolved complaints to give feedback on.", UITheme.FONT_BODY, UITheme.TEXT_SECONDARY);
            card.add(noLabel, makeGbc(row++, 8));
            panel.add(card);
            showPanel(panel, "Give Feedback");
            return;
        }

        String[] cOptions = resolved.stream().map(c -> "#" + c.getComplaintId() + " - " + c.getDescription().substring(0, Math.min(30, c.getDescription().length()))).toArray(String[]::new);
        JComboBox<String> complaintCombo = UITheme.createComboBox(cOptions);
        card.add(complaintCombo, makeGbc(row++, 4));

        card.add(UITheme.createLabel("Rating (1-5 stars) *", UITheme.FONT_SMALL, UITheme.TEXT_SECONDARY), makeGbc(row++, 10));
        JComboBox<String> ratingCombo = UITheme.createComboBox(new String[]{"⭐ 1 - Poor", "⭐⭐ 2 - Fair", "⭐⭐⭐ 3 - Good", "⭐⭐⭐⭐ 4 - Very Good", "⭐⭐⭐⭐⭐ 5 - Excellent"});
        ratingCombo.setSelectedIndex(4);
        card.add(ratingCombo, makeGbc(row++, 4));

        card.add(UITheme.createLabel("Comments", UITheme.FONT_SMALL, UITheme.TEXT_SECONDARY), makeGbc(row++, 10));
        JTextArea commentsArea = UITheme.createTextArea(4, 30);
        card.add(UITheme.createScrollPane(commentsArea), makeGbc(row++, 4));

        JLabel statusLabel = UITheme.createLabel("", UITheme.FONT_SMALL, UITheme.ACCENT_GREEN);
        card.add(statusLabel, makeGbc(row++, 8));

        JButton submitBtn = UITheme.createButton("Submit Feedback", UITheme.ACCENT_BLUE);
        card.add(submitBtn, makeGbc(row++, 4));

        submitBtn.addActionListener(e -> {
            int rating = ratingCombo.getSelectedIndex() + 1;
            String comments = commentsArea.getText().trim();
            Complaint chosen = resolved.get(complaintCombo.getSelectedIndex());
            Feedback fb = new Feedback(rating, comments, chosen);
            DataStore.getInstance().addFeedback(fb);
            statusLabel.setText("✓ Feedback submitted! Thank you.");
            commentsArea.setText("");
        });

        panel.add(card);
        showPanel(panel, "Give Feedback");
    }

    private JTable createStyledTable(Object[][] data, String[] cols) {
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

    static class StatusCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            label.setForeground(UITheme.getStatusColor(String.valueOf(value)));
            label.setFont(new Font("SansSerif", Font.BOLD, 12));
            return label;
        }
    }
}
