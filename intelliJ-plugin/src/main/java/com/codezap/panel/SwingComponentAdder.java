package com.codezap.panel;

import java.awt.GridBagConstraints;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.ui.JBUI;

public class SwingComponentAdder {

    private static final SwingComponentAdder swingComponentAdder = new SwingComponentAdder();

    public static SwingComponentAdder getInstance() {
        return swingComponentAdder;
    }

    private final GridBagConstraints constraints = new GridBagConstraints();

    private SwingComponentAdder() {
        constraints.insets = JBUI.insets(2);
        constraints.fill = GridBagConstraints.HORIZONTAL;
    }

    public void addLabel(JPanel panel, int gridY, String text) {
        constraints.gridx = 0;
        constraints.gridy = gridY;
        constraints.weightx = 0.2;
        panel.add(new JLabel(text), constraints);
    }

    public JTextField addTextField(JPanel panel, int gridY, String text, boolean isEditable) {
        constraints.gridx = 1;
        constraints.gridy = gridY;
        constraints.weightx = 0.8;
        JTextField field = new JTextField(text, 20);
        field.setEditable(isEditable);
        panel.add(field, constraints);
        return field;
    }

    public JPasswordField addPasswordField(JPanel panel, int gridY) {
        constraints.gridx = 1;
        constraints.gridy = gridY;
        constraints.weightx = 0.8;
        JPasswordField field = new JPasswordField(20);
        panel.add(field, constraints);
        return field;
    }

    public JComboBox<String> addComboBox(JPanel panel, String[] names) {
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.weightx = 0.8;
        JComboBox<String> comboBox = new ComboBox<>(names);
        panel.add(comboBox, constraints);
        return comboBox;
    }

    public void addScrollPane(JPanel panel, int gridY, String content) {
        constraints.gridx = 1;
        constraints.gridy = gridY;
        constraints.weightx = 0.8;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weighty = 1.0;
        JTextArea contentArea = new JTextArea(10, 20);
        contentArea.setText(content);
        contentArea.setEditable(false);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        panel.add(new JBScrollPane(contentArea), constraints);
    }

    public JCheckBox addCheckbox(JPanel panel, int gridY, String text) {
        constraints.gridx = 1;
        constraints.gridy = gridY;
        constraints.weightx = 0.5;
        constraints.weighty = 0.0;
        JCheckBox checkBox = new JCheckBox(text);
        panel.add(checkBox, constraints);
        return checkBox;
    }
}
