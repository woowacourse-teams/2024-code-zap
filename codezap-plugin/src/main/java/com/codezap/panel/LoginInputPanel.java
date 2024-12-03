package com.codezap.panel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.codezap.dto.request.LoginRequest;
import com.codezap.exception.ErrorType;
import com.codezap.exception.PluginException;
import com.intellij.util.ui.JBUI;

public class LoginInputPanel {

    private LoginInputPanel() {
    }

    public static LoginRequest inputLogin() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = JBUI.insets(2);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.2;
        panel.add(new JLabel("아이디:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.8;
        JTextField idField = new JTextField(20);
        panel.add(idField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.2;
        panel.add(new JLabel("비밀번호:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.8;
        JPasswordField passwordField = new JPasswordField(20);
        panel.add(passwordField, gbc);

        int option = JOptionPane.showConfirmDialog(
                null, panel, "로그인이 필요합니다.", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.CANCEL_OPTION) {
            throw new PluginException(ErrorType.CANCEL_TAP);
        }

        return new LoginRequest(idField.getText(), new String(passwordField.getPassword()));
    }
}
