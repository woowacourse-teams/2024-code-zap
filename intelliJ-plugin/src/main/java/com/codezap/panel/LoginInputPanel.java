package com.codezap.panel;

import java.awt.GridBagLayout;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.codezap.dto.request.LoginRequest;
import com.codezap.exception.ErrorType;
import com.codezap.exception.PluginException;

public class LoginInputPanel {

    private static final SwingComponentAdder componentAdder = SwingComponentAdder.getInstance();

    private LoginInputPanel() {
    }

    public static LoginRequest inputLogin() {
        JPanel panel = new JPanel(new GridBagLayout());
        componentAdder.addLabel(panel, 0, "아이디:");
        JTextField idField = componentAdder.addTextField(panel, 0, "", true);
        componentAdder.addLabel(panel, 1, "비밀번호:");
        JPasswordField passwordField = componentAdder.addPasswordField(panel, 1);

        int option = JOptionPane.showConfirmDialog(
                null, panel, "로그인이 필요합니다.", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.CANCEL_OPTION) {
            throw new PluginException(ErrorType.CANCEL_TAP);
        }

        return new LoginRequest(idField.getText(), new String(passwordField.getPassword()));
    }
}
