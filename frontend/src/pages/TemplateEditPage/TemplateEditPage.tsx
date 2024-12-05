import { useState } from 'react';

import { PlusIcon } from '@/assets/images';
import {
  Button,
  Input,
  SelectList,
  SourceCodeEditor,
  Text,
  CategoryDropdown,
  TagInput,
  LoadingBall,
  Textarea,
  Radio,
} from '@/components';
import { useInput, useSelectList, useToast } from '@/hooks';
import { useAuth } from '@/hooks/authentication';
import { useCategory } from '@/hooks/category';
import { useTag, useSourceCode } from '@/hooks/template';
import { useTemplateEditMutation } from '@/queries/templates';
import { useTrackPageViewed } from '@/service/amplitude';
import { VISIBILITY_OPTIONS } from '@/service/constants';
import { generateUniqueFilename, isFilenameEmpty } from '@/service/generateUniqueFilename';
import { validateTemplate } from '@/service/validates';
import { ICON_SIZE } from '@/style/styleConstants';
import { theme } from '@/style/theme';
import type { Template, TemplateEditRequest } from '@/types';
import { TemplateVisibility } from '@/types/template';
import { getLanguageForAutoTag } from '@/utils';

import * as S from './TemplateEditPage.style';

interface Props {
  template: Template;
  toggleEditButton: () => void;
}

const TemplateEditPage = ({ template, toggleEditButton }: Props) => {
  useTrackPageViewed({ eventName: '[Viewed] 템플릿 편집 페이지' });

  const {
    memberInfo: { memberId },
  } = useAuth();
  const categoryProps = useCategory({ memberId: memberId!, initCategory: template.category });
  const [title, handleTitleChange] = useInput(template.title);
  const [description, handleDescriptionChange] = useInput(template.description);

  const {
    sourceCodes,
    deleteSourceCodeIds,
    isValidContentChange,
    handleFilenameChange,
    handleContentChange,
    addNewEmptySourceCode,
    handleDeleteSourceCode,
  } = useSourceCode(template.sourceCodes);

  const initTags = template.tags.map((tag) => tag.name);
  const tagProps = useTag(initTags);

  const [visibility, setVisibility] = useState<TemplateVisibility>(template.visibility);

  const { currentOption: currentFile, linkedElementRefs: sourceCodeRefs, handleSelectOption } = useSelectList();

  const { mutateAsync: updateTemplate, isPending, error } = useTemplateEditMutation(template.id);

  const { failAlert } = useToast();

  const handleCancelButton = () => {
    toggleEditButton();
  };

  const handleSaveButtonClick = async () => {
    if (!canSaveTemplate()) {
      return;
    }

    const { createSourceCodes, updateSourceCodes } = generateProcessedSourceCodes();

    const templateUpdate: TemplateEditRequest = {
      id: template.id,
      title,
      description,
      createSourceCodes,
      updateSourceCodes,
      deleteSourceCodeIds,
      categoryId: categoryProps.currentValue.id,
      tags: tagProps.tags,
      visibility,
    };

    await updateTemplate(templateUpdate);
    toggleEditButton();
  };

  const canSaveTemplate = () => {
    const errorMessage = validateTemplate(title, sourceCodes);

    if (errorMessage) {
      failAlert(errorMessage);

      return false;
    }

    return true;
  };

  const generateProcessedSourceCodes = () => {
    const processSourceCodes = sourceCodes.map((sourceCode, index) => {
      const { filename } = sourceCode;

      return {
        ...sourceCode,
        ordinal: index + 1,
        filename: isFilenameEmpty(filename) ? generateUniqueFilename() : filename,
      };
    });

    const createSourceCodes = processSourceCodes.filter((sourceCode) => !sourceCode.id);
    const updateSourceCodes = processSourceCodes.filter((sourceCode) => sourceCode.id);

    return { createSourceCodes, updateSourceCodes };
  };

  return (
    <S.TemplateEditContainer>
      <S.MainContainer>
        <CategoryDropdown {...categoryProps} />

        <S.UnderlineInputWrapper>
          <Input size='xlarge' variant='text'>
            <Input.TextField placeholder='제목을 입력해주세요' value={title} onChange={handleTitleChange} />
          </Input>
        </S.UnderlineInputWrapper>

        <Textarea size='medium' variant='text'>
          <Textarea.TextField
            placeholder='이 템플릿을 언제 다시 쓸 것 같나요?'
            minRows={1}
            maxRows={5}
            value={description}
            onChange={handleDescriptionChange}
          />
        </Textarea>

        {sourceCodes.map((sourceCode, index) => (
          <SourceCodeEditor
            key={index}
            sourceCodeRef={(el) => (sourceCodeRefs.current[index] = el)}
            filename={sourceCode.filename}
            content={sourceCode.content}
            isValidContentChange={isValidContentChange}
            onChangeContent={(newContent) => handleContentChange(newContent, index)}
            onChangeFilename={(newFilename) => handleFilenameChange(newFilename, index)}
            onBlurFilename={(newFilename) => tagProps.addTag(getLanguageForAutoTag(newFilename))}
            handleDeleteSourceCode={() => handleDeleteSourceCode(index)}
            filenameAutoFocus={index !== 0}
          />
        ))}

        <Button
          size='medium'
          variant='contained'
          buttonColor={theme.color.light.primary_50}
          fullWidth
          onClick={addNewEmptySourceCode}
        >
          <PlusIcon width={ICON_SIZE.X_SMALL} height={ICON_SIZE.X_SMALL} aria-label='소스코드 추가' />
        </Button>

        <TagInput {...tagProps} />

        <Radio options={VISIBILITY_OPTIONS} currentValue={visibility} handleCurrentValue={setVisibility} />

        {isPending ? (
          <LoadingBall />
        ) : (
          <S.ButtonGroup>
            <S.CancelButton size='medium' variant='outlined' onClick={handleCancelButton}>
              취소
            </S.CancelButton>
            <Button
              size='medium'
              variant='contained'
              onClick={handleSaveButtonClick}
              disabled={sourceCodes.length === 0}
            >
              저장
            </Button>
          </S.ButtonGroup>
        )}

        {error && <Text.Medium color={theme.color.light.analogous_primary_400}>Error: {error.message}</Text.Medium>}
      </S.MainContainer>

      <S.SidebarContainer>
        <SelectList>
          {sourceCodes.map((sourceCode, index) => (
            <SelectList.Option key={index} onClick={handleSelectOption(index)} isSelected={currentFile === index}>
              {sourceCode.filename}
            </SelectList.Option>
          ))}
        </SelectList>
      </S.SidebarContainer>
    </S.TemplateEditContainer>
  );
};

export default TemplateEditPage;
