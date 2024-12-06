package com.codezap.panel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.codezap.dto.request.CreateSourceCodeRequest;
import com.codezap.dto.request.TemplateCreateRequest;
import com.codezap.dto.response.FindAllCategoriesResponse;
import com.codezap.dto.response.FindCategoryResponse;
import com.codezap.exception.ErrorType;
import com.codezap.exception.PluginException;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.ui.JBUI;

public class CreateTemplatePanel {

    private CreateTemplatePanel() {
    }

    public static TemplateCreateRequest inputCreateTemplate(
            String fileName, String content, FindAllCategoriesResponse findAllCategoriesResponse
    ) {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        addTitleField(panel, constraints, fileName);
        addCategoryComboBox(panel, constraints, findAllCategoriesResponse);
        addFileNameField(panel, constraints, fileName);
        addContentArea(panel, constraints, content);
        addPrivacyCheckbox(panel, constraints);

        int option = JOptionPane.showConfirmDialog(
                null, panel, "템플릿 생성", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.CANCEL_OPTION) {
            throw new PluginException(ErrorType.CANCEL_TAP);
        }

        long categoryId = findAllCategoriesResponse.getId((String) ((ComboBox) panel.getComponent(1)).getSelectedItem());
        return makeTemplateCreateRequest(
                ((JTextField) panel.getComponent(1)).getText(),
                fileName,
                content,
                categoryId,
                ((JCheckBox) panel.getComponent(4)).isSelected()
        );
    }

    private static void addTitleField(JPanel panel, GridBagConstraints constraints, String fileName) {
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 0.2;
        panel.add(new JLabel("템플릿 제목:"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.8;
        JTextField titleField = new JTextField(fileName, 20);
        panel.add(titleField, constraints);
    }

    private static void addCategoryComboBox(JPanel panel, GridBagConstraints constraints, FindAllCategoriesResponse findAllCategoriesResponse) {
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.weightx = 0.8;
        ComboBox<String> categoryComboBox = new ComboBox<>(
                findAllCategoriesResponse.categories().stream()
                        .map(FindCategoryResponse::name)
                        .toArray(String[]::new)
        );
        panel.add(categoryComboBox, constraints);
    }

    private static void addFileNameField(JPanel panel, GridBagConstraints constraints, String fileName) {
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.weightx = 0.8;
        panel.add(new JLabel("파일명:", SwingConstants.RIGHT), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.8;
        JTextField fileNameField = new JTextField(fileName, 20);
        fileNameField.setEditable(false);
        panel.add(fileNameField, constraints);
    }

    private static void addContentArea(JPanel panel, GridBagConstraints constraints, String content) {
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.weightx = 0.2;
        panel.add(new JLabel("소스코드:", SwingConstants.RIGHT), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.8;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weighty = 1.0;
        JTextArea contentArea = new JTextArea(10, 20);
        contentArea.setText(content);
        contentArea.setEditable(false);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JBScrollPane(contentArea);
        panel.add(scrollPane, constraints);
    }

    private static void addPrivacyCheckbox(JPanel panel, GridBagConstraints constraints) {
        constraints.gridx = 1;
        constraints.gridy = 4;
        constraints.weightx = 0.5;
        constraints.weighty = 0.0;
        JCheckBox isPrivateCheckBox = new JCheckBox("비공개");
        panel.add(isPrivateCheckBox, constraints);
    }

    private static TemplateCreateRequest makeTemplateCreateRequest(
            String title, String fileName, String content, long categoryId, boolean isPrivate
    ) {
        String visibility = isPrivate ? "PRIVATE" : "PUBLIC";
        return new TemplateCreateRequest(
                title,
                "",
                List.of(new CreateSourceCodeRequest(fileName, content, 1)),
                1,
                categoryId,
                List.of(),
                visibility
        );
    }
}
