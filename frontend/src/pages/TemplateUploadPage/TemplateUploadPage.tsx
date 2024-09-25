import { useNavigate } from 'react-router-dom';

import { PlusIcon } from '@/assets/images';
import { Button, CategoryDropdown, Input, SelectList, SourceCodeEditor, TagInput, Text } from '@/components';
import { useInput, useSelectList } from '@/hooks';
import { useCategory } from '@/hooks/category';
import { useSourceCode, useTag } from '@/hooks/template';
import { useToast } from '@/hooks/useToast';
import { useTemplateUploadMutation } from '@/queries/templates';
import { END_POINTS } from '@/routes';
import { theme } from '@/style/theme';
import { TemplateUploadRequest } from '@/types';

import * as S from './TemplateUploadPage.style';

const TemplateUploadPage = () => {
  const navigate = useNavigate();
  const { failAlert } = useToast();

  const categoryProps = useCategory();

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
    };

    await uploadTemplate(newTemplate, {
      onSuccess: (res) => {
        if (res?.status === 400 || res?.status === 404) {
          failAlert('템플릿 생성에 실패했습니다. 다시 한 번 시도해주세요');

          return;
        }

        navigate(END_POINTS.MY_TEMPLATES);
      },
    });
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
          <PlusIcon width={14} height={14} aria-label='소스코드 추가' />
        </Button>

        <TagInput {...tagProps} />

        <S.ButtonGroup>
          <Button size='medium' variant='outlined' onClick={handleCancelButton}>
            취소
          </Button>
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
