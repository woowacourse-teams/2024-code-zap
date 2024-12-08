package com.codezap.panel;

import java.awt.GridBagLayout;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.codezap.dto.request.CreateSourceCodeRequest;
import com.codezap.dto.request.TemplateCreateRequest;
import com.codezap.dto.response.FindAllCategoriesResponse;
import com.codezap.exception.ErrorType;
import com.codezap.exception.PluginException;

public class CreateTemplatePanel {

    private static final SwingComponentAdder componentAdder = SwingComponentAdder.getInstance();

    private CreateTemplatePanel() {
    }

    public static TemplateCreateRequest inputCreateTemplate(
            String fileName, String content, FindAllCategoriesResponse findAllCategoriesResponse
    ) {
        JPanel panel = new JPanel(new GridBagLayout());
        componentAdder.addLabel(panel, 0, "템플릿 제목:");
        JTextField titleField = componentAdder.addTextField(panel, 0, fileName, false);
        JComboBox<String> categoryComboBox =
                componentAdder.addComboBox(panel, findAllCategoriesResponse.getCategoryNames());
        componentAdder.addLabel(panel, 2, "파일명:");
        componentAdder.addTextField(panel, 2, fileName, true);
        componentAdder.addLabel(panel, 3, "소스코드:");
        componentAdder.addScrollPane(panel, 3, content);
        JCheckBox isPrivateCheckBox = componentAdder.addCheckbox(panel, 4, "비공개");

        int option = JOptionPane.showConfirmDialog(
                null, panel, "템플릿 생성", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.CANCEL_OPTION) {
            throw new PluginException(ErrorType.CANCEL_TAP);
        }

        long categoryId = findAllCategoriesResponse.getId((String) categoryComboBox.getSelectedItem());
        return makeTemplateCreateRequest(
                titleField.getText(), fileName, content, categoryId, isPrivateCheckBox.isSelected());
    }

    private static TemplateCreateRequest makeTemplateCreateRequest(
            String title, String fileName, String content, long categoryId, boolean isPrivate
    ) {
        String visibility = isPrivate ? "PRIVATE" : "PUBLIC";
        List<CreateSourceCodeRequest> sourceCodes = List.of(new CreateSourceCodeRequest(fileName, content, 1));
        return new TemplateCreateRequest(
                title, "", sourceCodes, 1, categoryId, List.of(), visibility);
    }
}
