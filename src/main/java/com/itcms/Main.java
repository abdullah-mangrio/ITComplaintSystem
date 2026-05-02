package com.itcms;

import com.itcms.ui.LoginFrame;
import com.itcms.util.UITheme;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        UITheme.applyTheme();
        SwingUtilities.invokeLater(() -> {
            LoginFrame login = new LoginFrame();
            login.setVisible(true);
        });
    }
}
