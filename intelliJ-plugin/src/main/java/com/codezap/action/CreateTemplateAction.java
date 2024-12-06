package com.codezap.action;

import static com.codezap.message.PrintMessage.FAIL_TEMPLATE_UPLOAD;
import static com.codezap.message.PrintMessage.NEED_FILE_SELECT;
import static com.codezap.message.PrintMessage.NEED_FILE_SELECT_MESSAGE;
import static com.codezap.message.PrintMessage.SERVER_ERROR_MESSAGE;
import static com.codezap.message.PrintMessage.SUCCESS_TEMPLATE_UPLOAD;
import static com.codezap.message.PrintMessage.SUCCESS_TEMPLATE_UPLOAD_MESSAGE;

import java.io.IOException;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;

import com.codezap.dto.request.TemplateCreateRequest;
import com.codezap.exception.ErrorType;
import com.codezap.exception.PluginException;
import com.codezap.panel.CreateTemplatePanel;
import com.codezap.service.CategoryService;
import com.codezap.service.LoginService;
import com.codezap.service.TemplateService;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;

public class CreateTemplateAction extends AnAction {

    private final LoginService loginService = new LoginService();
    private final TemplateService templateService = new TemplateService();
    private final CategoryService categoryService = new CategoryService();

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        if (!loginService.login()) {
            return;
        }

        VirtualFile virtualFile = event.getData(CommonDataKeys.VIRTUAL_FILE);
        if (virtualFile == null) {
            Messages.showWarningDialog(NEED_FILE_SELECT_MESSAGE.getMessage(), NEED_FILE_SELECT.getMessage());
            return;
        }

        try {
            String fileName = virtualFile.getName();
            String content = findContents(virtualFile, event.getData(CommonDataKeys.EDITOR));

            TemplateCreateRequest request = CreateTemplatePanel.inputCreateTemplate(
                    fileName, content, categoryService.getCategories(loginService.getMemberId()));

            templateService.createTemplate(request);
            Messages.showInfoMessage(SUCCESS_TEMPLATE_UPLOAD_MESSAGE.getMessage(),
                    SUCCESS_TEMPLATE_UPLOAD.getMessage());
        } catch (IOException ignored) {
            Messages.showInfoMessage(SERVER_ERROR_MESSAGE.getMessage(), FAIL_TEMPLATE_UPLOAD.getMessage());
        } catch (PluginException e) {
            if (!e.matchErrorType(ErrorType.CANCEL_TAP)) {
                Messages.showInfoMessage(e.getMessage(), FAIL_TEMPLATE_UPLOAD.getMessage());
            }
        }
    }

    private String findContents(VirtualFile virtualFile, Editor editor) {
        return Optional.ofNullable(editor)
                .map(e -> e.getSelectionModel().getSelectedText())
                .orElseGet(() -> readFileContents(virtualFile));
    }

    private String readFileContents(VirtualFile virtualFile) {
        try {
            return new String(virtualFile.contentsToByteArray());
        } catch (IOException ex) {
            throw new PluginException("파일이 선택되지 않았습니다.", ErrorType.SERVER_ERROR);
        }
    }
}
