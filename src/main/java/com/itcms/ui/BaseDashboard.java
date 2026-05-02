package com.itcms.ui;

import com.itcms.model.User;
import com.itcms.util.UITheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public abstract class BaseDashboard extends JFrame {
    protected User currentUser;
    protected JPanel contentArea;
    protected JPanel sidebarPanel;
    protected JLabel pageTitle;
    private JButton activeButton;
    private boolean initialized = false;

    public BaseDashboard(User user, String title) {
        this.currentUser = user;
        setTitle("IT Complaint System - " + title);
        setSize(1100, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(900, 600));
        buildLayout();
    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
        if (b && !initialized) {
            initialized = true;
            // Now window is on screen — safe to build nav and show initial content
            buildNavItems(sidebarPanel);
            sidebarPanel.revalidate();
            sidebarPanel.repaint();
        }
    }

    private void buildLayout() {
        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(UITheme.BG_DARK);

        sidebarPanel = buildSidebar();
        root.add(sidebarPanel, BorderLayout.WEST);

        JPanel rightPanel = new JPanel(new BorderLayout(0, 0));
        rightPanel.setBackground(UITheme.BG_DARK);
        rightPanel.add(buildTopBar(), BorderLayout.NORTH);

        contentArea = new JPanel(new BorderLayout());
        contentArea.setBackground(UITheme.BG_DARK);
        contentArea.setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));
        rightPanel.add(contentArea, BorderLayout.CENTER);

        root.add(rightPanel, BorderLayout.CENTER);
        add(root);
    }

    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(UITheme.BG_CARD);
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, UITheme.BORDER_COLOR));

        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 18, 20));
        logoPanel.setBackground(UITheme.BG_CARD);
        logoPanel.setMaximumSize(new Dimension(220, 70));
        JLabel icon = new JLabel("⚙");
        icon.setFont(new Font("SansSerif", Font.PLAIN, 24));
        icon.setForeground(UITheme.ACCENT_CYAN);
        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setBackground(UITheme.BG_CARD);
        JLabel l1 = new JLabel("IT Complaint");
        l1.setFont(new Font("SansSerif", Font.BOLD, 13));
        l1.setForeground(UITheme.TEXT_PRIMARY);
        JLabel l2 = new JLabel("Management System");
        l2.setFont(UITheme.FONT_SMALL);
        l2.setForeground(UITheme.TEXT_SECONDARY);
        textPanel.add(l1);
        textPanel.add(l2);
        logoPanel.add(icon);
        logoPanel.add(textPanel);
        sidebar.add(logoPanel);
        sidebar.add(createHRule());

        JLabel navLabel = UITheme.createLabel("  NAVIGATION", new Font("SansSerif", Font.BOLD, 10), UITheme.TEXT_SECONDARY);
        navLabel.setBorder(BorderFactory.createEmptyBorder(14, 16, 8, 0));
        navLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(navLabel);

        // placeholder so glue index is stable
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(createHRule());

        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 12));
        userPanel.setBackground(UITheme.BG_CARD);
        userPanel.setMaximumSize(new Dimension(220, 60));
        JLabel avatar = new JLabel(currentUser.getName().substring(0, 1).toUpperCase());
        avatar.setFont(new Font("SansSerif", Font.BOLD, 14));
        avatar.setForeground(UITheme.BG_DARK);
        avatar.setOpaque(true);
        avatar.setBackground(UITheme.ACCENT_BLUE);
        avatar.setPreferredSize(new Dimension(34, 34));
        avatar.setHorizontalAlignment(SwingConstants.CENTER);
        JPanel namePanel = new JPanel(new GridLayout(2, 1));
        namePanel.setBackground(UITheme.BG_CARD);
        namePanel.add(UITheme.createLabel(currentUser.getName(), UITheme.FONT_SMALL, UITheme.TEXT_PRIMARY));
        namePanel.add(UITheme.createLabel(currentUser.getRole(), new Font("SansSerif", Font.PLAIN, 10), UITheme.ACCENT_CYAN));
        userPanel.add(avatar);
        userPanel.add(namePanel);
        sidebar.add(userPanel);

        return sidebar;
    }

    protected abstract void buildNavItems(JPanel sidebar);

    protected JButton addNavButton(JPanel sidebar, String icon, String label, Runnable action) {
        JButton btn = new JButton("  " + icon + "  " + label);
        btn.setFont(UITheme.FONT_BODY);
        btn.setForeground(UITheme.TEXT_SECONDARY);
        btn.setBackground(UITheme.BG_CARD);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        btn.setMaximumSize(new Dimension(220, 42));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setOpaque(true);
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (btn != activeButton) { btn.setBackground(new Color(30, 38, 60)); btn.setForeground(UITheme.TEXT_PRIMARY); }
            }
            public void mouseExited(MouseEvent e) {
                if (btn != activeButton) { btn.setBackground(UITheme.BG_CARD); btn.setForeground(UITheme.TEXT_SECONDARY); }
            }
        });
        btn.addActionListener(e -> { setActiveNav(btn); action.run(); });

        // Insert before the glue (index 3 = after logo, hRule, navLabel)
        // Find glue position dynamically
        int glueIndex = 3;
        for (int i = 0; i < sidebar.getComponentCount(); i++) {
            if (sidebar.getComponent(i) instanceof Box.Filler) { glueIndex = i; break; }
        }
        sidebar.add(btn, glueIndex);
        return btn;
    }

    protected void setActiveNav(JButton btn) {
        if (activeButton != null) {
            activeButton.setBackground(UITheme.BG_CARD);
            activeButton.setForeground(UITheme.TEXT_SECONDARY);
        }
        activeButton = btn;
        btn.setBackground(new Color(37, 99, 235, 60));
        btn.setForeground(UITheme.ACCENT_CYAN);
    }

    private JPanel buildTopBar() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(UITheme.BG_CARD);
        topBar.setPreferredSize(new Dimension(0, 60));
        topBar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, UITheme.BORDER_COLOR),
            BorderFactory.createEmptyBorder(0, 28, 0, 28)
        ));
        pageTitle = UITheme.createLabel("Dashboard", UITheme.FONT_SUBTITLE, UITheme.TEXT_PRIMARY);
        topBar.add(pageTitle, BorderLayout.WEST);
        JButton logoutBtn = UITheme.createButton("Logout", new Color(127, 29, 29));
        logoutBtn.setFont(UITheme.FONT_SMALL);
        logoutBtn.setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 14));
        logoutBtn.addActionListener(e -> {
            com.itcms.service.DataStore.getInstance().setCurrentUser(null);
            dispose();
            new LoginFrame().setVisible(true);
        });
        topBar.add(logoutBtn, BorderLayout.EAST);
        return topBar;
    }

    protected void showPanel(JPanel panel, String title) {
        pageTitle.setText(title);
        contentArea.removeAll();
        contentArea.add(panel, BorderLayout.CENTER);
        contentArea.revalidate();
        contentArea.repaint();
    }

    private JPanel createHRule() {
        JPanel p = new JPanel();
        p.setBackground(UITheme.BORDER_COLOR);
        p.setMaximumSize(new Dimension(220, 1));
        p.setPreferredSize(new Dimension(220, 1));
        return p;
    }
}
