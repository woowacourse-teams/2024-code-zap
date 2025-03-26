import { theme } from '@design/style/theme';
import { useState } from 'react';
import { useTranslation } from 'react-i18next';

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
import {
  trackClickTemplateSave,
  useTrackPageViewed,
} from '@/service/amplitude';
import {
  DEFAULT_TEMPLATE_VISIBILITY,
  VISIBILITY_OPTIONS,
} from '@/service/constants';
import {
  generateUniqueFilename,
  isFilenameEmpty,
} from '@/service/generateUniqueFilename';
import { validateTemplate } from '@/service/validates';
import { ICON_SIZE } from '@/style/styleConstants';
import { TemplateUploadRequest } from '@/types';
import { SourceCodes, TemplateVisibility } from '@/types/template';
import { getLanguageForAutoTag } from '@/utils';

import * as S from './TemplateUploadPage.style';

const TemplateUploadPage = () => {
  useTrackPageViewed({ eventName: '[Viewed] 템플릿 업로드 페이지' });
  const { t } = useTranslation('TemplateUploadPage');

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

  const [visibility, setVisibility] = useState<TemplateVisibility>(
    DEFAULT_TEMPLATE_VISIBILITY,
  );

  const {
    currentOption: currentFile,
    linkedElementRefs: sourceCodeRefs,
    handleSelectOption,
  } = useSelectList();

  const {
    mutateAsync: uploadTemplate,
    isPending,
    error,
  } = useTemplateUploadMutation();

  const handleCancelButton = () => {
    navigate(-1);
  };

  const handleSaveButtonClick = async () => {
    if (!canSaveTemplate()) {
      return;
    }

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
  };

  const canSaveTemplate = (): boolean => {
    if (categoryProps.isCategoryQueryFetching) {
      failAlert(t('categoryLoadingError'));

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
        filename: isFilenameEmpty(filename)
          ? generateUniqueFilename()
          : filename,
      };
    });

  const trackTemplateSaveSuccess = () => {
    trackClickTemplateSave({
      templateTitle: title,
      sourceCodeCount: sourceCodes.length,
      visibility,
    });
  };

  const { t: tConstants } = useTranslation('Constants');

  const TRANS_VISIBILITY_OPTIONS: Record<string, string | number> = (() => {
    const keys = Object.keys(
      VISIBILITY_OPTIONS,
    ) as (keyof typeof VISIBILITY_OPTIONS)[];

    return keys.reduce(
      (acc, key) => ({ ...acc, [key]: tConstants(VISIBILITY_OPTIONS[key]) }),
      {},
    );
  })();

  return (
    <S.TemplateEditContainer>
      <S.MainContainer>
        <CategoryDropdown {...categoryProps} />

        <S.UnderlineInputWrapper>
          <Input size='xlarge' variant='text'>
            <Input.TextField
              placeholder={t('titlePlaceholder')}
              value={title}
              onChange={handleTitleChange}
            />
          </Input>
        </S.UnderlineInputWrapper>

        <Textarea size='medium' variant='text'>
          <Textarea.TextField
            placeholder={t('descriptionPlaceholder')}
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
            onChangeContent={(newContent) =>
              handleContentChange(newContent, index)
            }
            onChangeFilename={(newFilename) =>
              handleFilenameChange(newFilename, index)
            }
            onBlurFilename={(newFilename) =>
              tagProps.addTag(getLanguageForAutoTag(newFilename))
            }
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
          <PlusIcon
            width={ICON_SIZE.X_SMALL}
            height={ICON_SIZE.X_SMALL}
            aria-label={t('addSourcecodeAriaLabel')}
          />
        </Button>

        <TagInput {...tagProps} />

        <Radio
          options={TRANS_VISIBILITY_OPTIONS}
          currentValue={visibility}
          handleCurrentValue={setVisibility}
        />

        {isPending ? (
          <LoadingBall />
        ) : (
          <S.ButtonGroup>
            <S.CancelButton
              size='medium'
              variant='outlined'
              onClick={handleCancelButton}
            >
              {t('cancel')}
            </S.CancelButton>
            <Button
              size='medium'
              variant='contained'
              onClick={handleSaveButtonClick}
              disabled={sourceCodes.length === 0}
            >
              {t('save')}
            </Button>
          </S.ButtonGroup>
        )}

        {error && (
          <Text.Medium color={theme.color.light.analogous_primary_400}>
            {t('errorPrefix') + error.message}
          </Text.Medium>
        )}
      </S.MainContainer>

      <S.SidebarContainer>
        <SelectList>
          {sourceCodes.map((sourceCode, index) => (
            <SelectList.Option
              key={index}
              onClick={handleSelectOption(index)}
              isSelected={currentFile === index}
            >
              {sourceCode.filename}
            </SelectList.Option>
          ))}
        </SelectList>
      </S.SidebarContainer>
    </S.TemplateEditContainer>
  );
};

export default TemplateUploadPage;
