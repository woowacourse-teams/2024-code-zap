import { useRef, useState } from 'react';

import { PlusIcon } from '@/assets/images';
import {
  Button,
  CategoryDropdown,
  Input,
  LoadingBall,
  Radio,
  SelectList,
  SourceCodeEditor,
  TagInput,
  Text,
  Textarea,
} from '@/components';
import { useCustomNavigate, useInput, useSelectList, useToast } from '@/hooks';
import { useAuth } from '@/hooks/authentication';
import { useCategory } from '@/hooks/category';
import { useSourceCode, useTag } from '@/hooks/template';
import { useTemplateUploadMutation } from '@/queries/templates';
import { trackClickTemplateSave, useTrackPageViewed } from '@/service/amplitude';
import { DEFAULT_TEMPLATE_VISIBILITY, TEMPLATE_VISIBILITY, convertToKorVisibility } from '@/service/constants';
import { generateUniqueFilename, isFilenameEmpty } from '@/service/generateUniqueFilename';
import { validateTemplate } from '@/service/validates';
import { ICON_SIZE } from '@/style/styleConstants';
import { theme } from '@/style/theme';
import { TemplateUploadRequest } from '@/types';
import { SourceCodes, TemplateVisibility } from '@/types/template';
import { getLanguageForAutoTag } from '@/utils';

import * as S from './TemplateUploadPage.style';

const TemplateUploadPage = () => {
  useTrackPageViewed({ eventName: '[Viewed] 템플릿 업로드 페이지' });

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
  const [isSaving, setIsSaving] = useState(false);
  const isSavingRef = useRef(false);

  const { currentOption: currentFile, linkedElementRefs: sourceCodeRefs, handleSelectOption } = useSelectList();

  const { mutateAsync: uploadTemplate, error } = useTemplateUploadMutation();

  const handleVisibility = (visibility: TemplateVisibility) => {
    setVisibility(visibility);
  };

  const handleCancelButton = () => {
    navigate(-1);
  };

  const handleSaveButtonClick = async () => {
    if (isSavingRef.current) {
      return;
    }

    if (!canSaveTemplate()) {
      return;
    }

    isSavingRef.current = true;
    setIsSaving(true);

    try {
      const processedSourceCodes = generateProcessedSourceCodes();

      const newTemplate: TemplateUploadRequest = {
        title,
        description,
        sourceCodes: processedSourceCodes,
        thumbnailOrdinal: 1,
        categoryId: categoryProps.currentValue.id,
        tags: tagProps.tags,
        visibility,
      };

      const response = await uploadTemplate(newTemplate);

      if (response.ok) {
        trackTemplateSaveSuccess();
      }
    } finally {
      isSavingRef.current = false;
      setIsSaving(false);
    }
  };

  const canSaveTemplate = (): boolean => {
    if (categoryProps.isCategoryQueryFetching) {
      failAlert('카테고리 목록을 불러오는 중입니다. 잠시 후 다시 시도해주세요.');

      return false;
    }

    const errorMessage = validateTemplate(title, sourceCodes);

    if (errorMessage) {
      failAlert(errorMessage);

      return false;
    }

    return true;
  };

  const generateProcessedSourceCodes = (): SourceCodes[] =>
    sourceCodes.map((sourceCode, index): SourceCodes => {
      const { filename } = sourceCode;

      return {
        ...sourceCode,
        ordinal: index + 1,
        filename: isFilenameEmpty(filename) ? generateUniqueFilename() : filename,
      };
    });

  const trackTemplateSaveSuccess = () => {
    trackClickTemplateSave({
      templateTitle: title,
      sourceCodeCount: sourceCodes.length,
      visibility,
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

        <Radio
          options={[...TEMPLATE_VISIBILITY]}
          currentValue={visibility}
          handleCurrentValue={handleVisibility}
          getOptionLabel={(option: TemplateVisibility) => convertToKorVisibility[option]}
        />

        {isSaving ? (
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

export default TemplateUploadPage;
