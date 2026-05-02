package com.itcms.ui;

import com.itcms.model.User;
import com.itcms.service.DataStore;
import com.itcms.util.UITheme;

import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JDialog {
    private JTextField nameField, usernameField, emailField;
    private JPasswordField passwordField, confirmField;
    private JComboBox<String> occupationCombo;

    public RegisterFrame(JFrame parent) {
        super(parent, "Create Account", true);
        setSize(460, 560);
        setLocationRelativeTo(parent);
        setResizable(false);
        buildUI();
    }

    private void buildUI() {
        JPanel main = new JPanel(new GridBagLayout());
        main.setBackground(UITheme.BG_DARK);
        main.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 5, 0);
        gbc.gridx = 0;
        int row = 0;

        JLabel titleLabel = UITheme.createLabel("Create Account", UITheme.FONT_TITLE, UITheme.TEXT_PRIMARY);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = row++;
        gbc.insets = new Insets(0, 0, 24, 0);
        main.add(titleLabel, gbc);

        gbc.insets = new Insets(4, 0, 4, 0);

        String[][] fields = {{"Full Name", "name"}, {"Username", "username"}, {"Email", "email"}};

        // Name
        main.add(UITheme.createLabel("Full Name", UITheme.FONT_SMALL, UITheme.TEXT_SECONDARY), makeGbc(row++));
        nameField = UITheme.createTextField("");
        main.add(nameField, makeGbc(row++));

        main.add(UITheme.createLabel("Username", UITheme.FONT_SMALL, UITheme.TEXT_SECONDARY), makeGbc(row++));
        usernameField = UITheme.createTextField("");
        main.add(usernameField, makeGbc(row++));

        main.add(UITheme.createLabel("Email", UITheme.FONT_SMALL, UITheme.TEXT_SECONDARY), makeGbc(row++));
        emailField = UITheme.createTextField("");
        main.add(emailField, makeGbc(row++));

        main.add(UITheme.createLabel("Password", UITheme.FONT_SMALL, UITheme.TEXT_SECONDARY), makeGbc(row++));
        passwordField = UITheme.createPasswordField();
        main.add(passwordField, makeGbc(row++));

        main.add(UITheme.createLabel("Confirm Password", UITheme.FONT_SMALL, UITheme.TEXT_SECONDARY), makeGbc(row++));
        confirmField = UITheme.createPasswordField();
        main.add(confirmField, makeGbc(row++));

        main.add(UITheme.createLabel("Occupation", UITheme.FONT_SMALL, UITheme.TEXT_SECONDARY), makeGbc(row++));
        occupationCombo = UITheme.createComboBox(new String[]{"Student", "Faculty", "Staff"});
        occupationCombo.setPreferredSize(new Dimension(340, 36));
        main.add(occupationCombo, makeGbc(row++));

        JButton registerBtn = UITheme.createButton("Create Account", UITheme.ACCENT_BLUE);
        registerBtn.setPreferredSize(new Dimension(340, 42));
        GridBagConstraints btnGbc = makeGbc(row++);
        btnGbc.insets = new Insets(16, 0, 4, 0);
        main.add(registerBtn, btnGbc);

        JButton cancelBtn = UITheme.createButton("Cancel", UITheme.BG_INPUT);
        cancelBtn.setPreferredSize(new Dimension(340, 38));
        main.add(cancelBtn, makeGbc(row++));

        registerBtn.addActionListener(e -> handleRegister());
        cancelBtn.addActionListener(e -> dispose());

        add(main);
    }

    private GridBagConstraints makeGbc(int row) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.insets = new Insets(3, 0, 3, 0);
        return gbc;
    }

    private void handleRegister() {
        String name = nameField.getText().trim();
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String pass = new String(passwordField.getPassword());
        String confirm = new String(confirmField.getPassword());
        String occupation = (String) occupationCombo.getSelectedItem();

        if (name.isEmpty() || username.isEmpty() || email.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!pass.equals(confirm)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!email.contains("@")) {
            JOptionPane.showMessageDialog(this, "Invalid email address.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (DataStore.getInstance().usernameExists(username)) {
            JOptionPane.showMessageDialog(this, "Username already taken.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id = DataStore.getInstance().getNextUserId();
        User user = new User(id, name, username, pass, email, occupation, "USER");
        DataStore.getInstance().addUser(user);

        JOptionPane.showMessageDialog(this, "Account created successfully! You can now log in.", "Success", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }
}
