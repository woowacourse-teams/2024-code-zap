import { useState } from 'react';

import { PlusIcon, PrivateIcon, PublicIcon } from '@/assets/images';
import { Button, CategoryDropdown, Input, SelectList, SourceCodeEditor, TagInput, Text, Toggle } from '@/components';
import { useCustomNavigate, useInput, useSelectList } from '@/hooks';
import { useAuth } from '@/hooks/authentication';
import { useCategory } from '@/hooks/category';
import { useSourceCode, useTag } from '@/hooks/template';
import { useToast } from '@/hooks/useToast';
import { useTemplateUploadMutation } from '@/queries/templates';
import { END_POINTS } from '@/routes';
import { DEFAULT_TEMPLATE_VISIBILITY, TEMPLATE_VISIBILITY } from '@/service/constants';
import { ICON_SIZE } from '@/style/styleConstants';
import { theme } from '@/style/theme';
import { TemplateUploadRequest } from '@/types';
import { TemplateVisibility } from '@/types/template';

import * as S from './TemplateUploadPage.style';

const TemplateUploadPage = () => {
  const navigate = useCustomNavigate();
  const { failAlert } = useToast();

  const {
    memberInfo: { memberId },
  } = useAuth();

  const categoryProps = useCategory({ memberId: memberId! });

  const [title, handleTitleChange] = useInput('');
  const [description, handleDescriptionChange] = useInput('');

  const {
    sourceCodes,
    isValidContentChange,
    handleFilenameChange,
    handleContentChange,
    addNewEmptySourceCode,
    handleDeleteSourceCode,
  } = useSourceCode([
    {
      filename: '',
      content: '',
      ordinal: 1,
    },
  ]);

  const tagProps = useTag([]);

  const [visibility, setVisibility] = useState<TemplateVisibility>(DEFAULT_TEMPLATE_VISIBILITY);

  const { currentOption: currentFile, linkedElementRefs: sourceCodeRefs, handleSelectOption } = useSelectList();

  const { mutateAsync: uploadTemplate, error } = useTemplateUploadMutation();

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
    navigate(-1);
  };

  const handleSaveButtonClick = async () => {
    const errorMessage = validateTemplate();

    if (errorMessage) {
      failAlert(errorMessage);

      return;
    }

    const newTemplate: TemplateUploadRequest = {
      title,
      description,
      sourceCodes,
      thumbnailOrdinal: 1,
      categoryId: categoryProps.currentValue.id,
      tags: tagProps.tags,
      visibility,
    };

    await uploadTemplate(newTemplate, {
      onSuccess: (res) => {
        if (res?.status === 400 || res?.status === 404) {
          failAlert('템플릿 생성에 실패했습니다. 다시 한 번 시도해주세요');

          return;
        }

        navigate(END_POINTS.memberTemplates(memberId!));
      },
    });
  };

  return (
    <S.TemplateEditContainer>
      <S.MainContainer>
        <S.CategoryAndVisibilityContainer>
          <CategoryDropdown {...categoryProps} />
          <Toggle
            showOptions={false}
            options={[...TEMPLATE_VISIBILITY]}
            optionAdornments={[
              <PrivateIcon key={TEMPLATE_VISIBILITY[1]} width={ICON_SIZE.MEDIUM_SMALL} />,
              <PublicIcon key={TEMPLATE_VISIBILITY[0]} width={ICON_SIZE.MEDIUM_SMALL} />,
            ]}
            selectedOption={visibility}
            switchOption={setVisibility}
          />
        </S.CategoryAndVisibilityContainer>

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
          <S.CancelButton size='medium' variant='outlined' onClick={handleCancelButton}>
            취소
          </S.CancelButton>
          <Button size='medium' variant='contained' onClick={handleSaveButtonClick} disabled={sourceCodes.length === 0}>
            저장
          </Button>
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

export default TemplateUploadPage;
