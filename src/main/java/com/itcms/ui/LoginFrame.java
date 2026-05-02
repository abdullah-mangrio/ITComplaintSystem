package com.itcms.ui;

import com.itcms.model.*;
import com.itcms.service.DataStore;
import com.itcms.util.UITheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel statusLabel;

    public LoginFrame() {
        setTitle("IT Equipment Complaint Management System");
        setSize(500, 680);
        setMinimumSize(new Dimension(460, 520));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        buildUI();
    }

    private void buildUI() {
        JPanel main = new JPanel(new GridBagLayout());
        main.setBackground(UITheme.BG_DARK);
        main.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 0, 6, 0);
        gbc.gridx = 0;
        int row = 0;

        // Logo/Header
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(UITheme.BG_DARK);
        JLabel iconLabel = new JLabel("⚙");
        iconLabel.setFont(new Font("SansSerif", Font.PLAIN, 48));
        iconLabel.setForeground(UITheme.ACCENT_CYAN);
        headerPanel.add(iconLabel);
        gbc.gridy = row++;
        gbc.insets = new Insets(0, 0, 8, 0);
        main.add(headerPanel, gbc);

        // Title
        JLabel titleLabel = UITheme.createLabel("IT Complaint System", UITheme.FONT_TITLE, UITheme.TEXT_PRIMARY);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = row++;
        gbc.insets = new Insets(0, 0, 4, 0);
        main.add(titleLabel, gbc);

        JLabel subtitleLabel = UITheme.createLabel("University IT Management Portal", UITheme.FONT_SMALL, UITheme.TEXT_SECONDARY);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = row++;
        gbc.insets = new Insets(0, 0, 30, 0);
        main.add(subtitleLabel, gbc);

        // Card
        JPanel card = UITheme.createCard();
        card.setLayout(new GridBagLayout());
        GridBagConstraints cbc = new GridBagConstraints();
        cbc.fill = GridBagConstraints.HORIZONTAL;
        cbc.insets = new Insets(5, 0, 5, 0);
        cbc.gridx = 0;
        int crow = 0;

        JLabel signInLabel = UITheme.createLabel("Sign In", UITheme.FONT_SUBTITLE, UITheme.TEXT_PRIMARY);
        cbc.gridy = crow++;
        cbc.insets = new Insets(0, 0, 16, 0);
        card.add(signInLabel, cbc);

        // Username
        JLabel usernameLabel = UITheme.createLabel("Username", UITheme.FONT_SMALL, UITheme.TEXT_SECONDARY);
        cbc.gridy = crow++;
        cbc.insets = new Insets(4, 0, 4, 0);
        card.add(usernameLabel, cbc);
        usernameField = UITheme.createTextField("username");
        cbc.gridy = crow++;
        card.add(usernameField, cbc);

        // Password
        JLabel passLabel = UITheme.createLabel("Password", UITheme.FONT_SMALL, UITheme.TEXT_SECONDARY);
        cbc.gridy = crow++;
        cbc.insets = new Insets(10, 0, 4, 0);
        card.add(passLabel, cbc);
        passwordField = UITheme.createPasswordField();
        cbc.gridy = crow++;
        cbc.insets = new Insets(4, 0, 16, 0);
        card.add(passwordField, cbc);

        // Status label
        statusLabel = UITheme.createLabel("", UITheme.FONT_SMALL, UITheme.ACCENT_RED);
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        cbc.gridy = crow++;
        cbc.insets = new Insets(0, 0, 8, 0);
        card.add(statusLabel, cbc);

        // Login button
        JButton loginBtn = UITheme.createButton("Sign In →", UITheme.ACCENT_BLUE);
        loginBtn.setPreferredSize(new Dimension(340, 42));
        cbc.gridy = crow++;
        cbc.insets = new Insets(4, 0, 12, 0);
        card.add(loginBtn, cbc);

        // Divider
        JLabel orLabel = UITheme.createLabel("─────  or  ─────", UITheme.FONT_SMALL, UITheme.TEXT_SECONDARY);
        orLabel.setHorizontalAlignment(SwingConstants.CENTER);
        cbc.gridy = crow++;
        card.add(orLabel, cbc);

        // Register link
        JButton registerBtn = UITheme.createButton("Create New Account", UITheme.BG_INPUT);
        registerBtn.setPreferredSize(new Dimension(340, 38));
        cbc.gridy = crow++;
        cbc.insets = new Insets(12, 0, 0, 0);
        card.add(registerBtn, cbc);

        gbc.gridy = row++;
        gbc.insets = new Insets(0, 0, 20, 0);
        main.add(card, gbc);

        // Demo accounts hint
        JPanel hintCard = new JPanel(new GridLayout(0, 1, 2, 2));
        hintCard.setBackground(new Color(20, 27, 45));
        hintCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UITheme.BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(10, 14, 10, 14)
        ));
        JLabel hintTitle = UITheme.createLabel("Demo Accounts:", UITheme.FONT_SMALL, UITheme.ACCENT_CYAN);
        hintCard.add(hintTitle);
        String[] hints = {"admin / admin123  (Admin)", "ali / ali123  (IT Staff)", "kamran / kamran123  (Technician)", "sara / sara123  (User)"};
        for (String hint : hints) {
            JLabel h = UITheme.createLabel("• " + hint, UITheme.FONT_SMALL, UITheme.TEXT_SECONDARY);
            hintCard.add(h);
        }
        gbc.gridy = row++;
        main.add(hintCard, gbc);

        // Actions
        loginBtn.addActionListener(e -> handleLogin());
        passwordField.addActionListener(e -> handleLogin());
        registerBtn.addActionListener(e -> openRegister());

        JScrollPane scrollPane = new JScrollPane(main);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBackground(UITheme.BG_DARK);
        scrollPane.getViewport().setBackground(UITheme.BG_DARK);
        add(scrollPane);
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Please enter username and password.");
            return;
        }

        User user = DataStore.getInstance().authenticate(username, password);
        if (user == null) {
            statusLabel.setText("Invalid credentials. Try again.");
            passwordField.setText("");
            return;
        }

        DataStore.getInstance().setCurrentUser(user);
        final User loggedInUser = user;
        dispose();

        // Open role-specific dashboard — invokeLater ensures dispose() fully completes first
        SwingUtilities.invokeLater(() -> {
            JFrame dashboard;
            switch (loggedInUser.getRole()) {
                case "ADMIN":
                    dashboard = new AdminDashboard((Admin) loggedInUser);
                    break;
                case "IT_STAFF":
                    dashboard = new ITStaffDashboard((ITStaff) loggedInUser);
                    break;
                case "TECHNICIAN":
                    dashboard = new TechnicianDashboard((Technician) loggedInUser);
                    break;
                default:
                    dashboard = new UserDashboard(loggedInUser);
            }
            dashboard.setVisible(true);
            dashboard.toFront();
            dashboard.requestFocus();
        });
    }

    private void openRegister() {
        new RegisterFrame(this).setVisible(true);
    }
}
