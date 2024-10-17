import { useState } from 'react';

import { PlusIcon, PrivateIcon, PublicIcon } from '@/assets/images';
import { Button, Input, SelectList, SourceCodeEditor, Text, CategoryDropdown, TagInput, Toggle } from '@/components';
import { useInput, useSelectList } from '@/hooks';
import { useCategory } from '@/hooks/category';
import { useTag, useSourceCode } from '@/hooks/template';
import { useToast } from '@/hooks/useToast';
import { useTemplateEditMutation } from '@/queries/templates';
import { DEFAULT_TEMPLATE_VISIBILITY, TEMPLATE_VISIBILITY } from '@/service/constants';
import { ICON_SIZE } from '@/style/styleConstants';
import { theme } from '@/style/theme';
import type { Template, TemplateEditRequest } from '@/types';
import { TemplateVisibility } from '@/types/template';

import * as S from './TemplateEditPage.style';

interface Props {
  template: Template;
  toggleEditButton: () => void;
}

const TemplateEditPage = ({ template, toggleEditButton }: Props) => {
  const categoryProps = useCategory(template.category);

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

  const [visibility, setVisibility] = useState<TemplateVisibility>(DEFAULT_TEMPLATE_VISIBILITY);

  const { currentOption: currentFile, linkedElementRefs: sourceCodeRefs, handleSelectOption } = useSelectList();

  const { mutateAsync: updateTemplate, error } = useTemplateEditMutation(template.id);

  const { failAlert } = useToast();

  const validateTemplate = () => {
    if (!title) {
      return '제목을 입력해주세요';
    }

    if (sourceCodes.filter(({ filename }) => !filename || filename.trim() === '').length) {
      return '파일명을 입력해주세요';
    }

    if (sourceCodes.filter(({ content }) => !content || content.trim() === '').length) {
      return '소스코드 내용을 입력해주세요';
    }

    return '';
  };

  const handleCancelButton = () => {
    toggleEditButton();
  };

  const handleSaveButtonClick = async () => {
    if (validateTemplate()) {
      failAlert(validateTemplate());

      return;
    }

    const orderedSourceCodes = sourceCodes.map((sourceCode, index) => ({
      ...sourceCode,
      ordinal: index + 1,
    }));
    const createSourceCodes = orderedSourceCodes.filter((sourceCode) => !sourceCode.id);
    const updateSourceCodes = orderedSourceCodes.filter((sourceCode) => sourceCode.id);

    const templateUpdate: TemplateEditRequest = {
      title,
      description,
      createSourceCodes,
      updateSourceCodes,
      deleteSourceCodeIds,
      categoryId: categoryProps.currentValue.id,
      tags: tagProps.tags,
      visibility,
    };

    try {
      await updateTemplate({ id: template.id, template: templateUpdate });
      toggleEditButton();
    } catch (error) {
      console.error('Failed to update template:', error);
    }
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

        <Input size='large' variant='text'>
          <Input.TextField
            placeholder='이 템플릿을 언제 다시 쓸 것 같나요?'
            value={description}
            onChange={handleDescriptionChange}
          />
        </Input>

        {sourceCodes.map((sourceCode, index) => (
          <SourceCodeEditor
            key={index}
            sourceCodeRef={(el) => (sourceCodeRefs.current[index] = el)}
            filename={sourceCode.filename}
            content={sourceCode.content}
            isValidContentChange={isValidContentChange}
            onChangeContent={(newContent) => handleContentChange(newContent, index)}
            onChangeFilename={(newFilename) => handleFilenameChange(newFilename, index)}
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

        <S.ButtonGroup>
          <Button size='medium' variant='outlined' onClick={handleCancelButton}>
            취소
          </Button>
          <S.ToggleAndSaveButton>
            <Toggle
              options={[...TEMPLATE_VISIBILITY]}
              optionAdornments={[
                <PrivateIcon key={TEMPLATE_VISIBILITY[1]} width={ICON_SIZE.MEDIUM_SMALL} />,
                <PublicIcon key={TEMPLATE_VISIBILITY[0]} width={ICON_SIZE.MEDIUM_SMALL} />,
              ]}
              selectedOption={visibility}
              switchOption={setVisibility}
            />

            <Button
              size='medium'
              variant='contained'
              onClick={handleSaveButtonClick}
              disabled={sourceCodes.length === 0}
            >
              저장
            </Button>
          </S.ToggleAndSaveButton>
        </S.ButtonGroup>

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
