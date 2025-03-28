import { TrashcanIcon } from '@/assets/images';
import { SourceCode } from '@/components';
import { getLanguageByFilename } from '@/utils';
import { type ViewUpdate } from '@uiw/react-codemirror';
import { useRef } from 'react';
import { useTranslation } from 'react-i18next';

import { ICON_SIZE } from '@design/style/styleConstants';

import * as S from './SourceCodeEditor.style';

interface Props {
  filename: string;
  filenameAutoFocus?: boolean;
  content: string;
  onChangeFilename: (newFileName: string) => void;
  onChangeContent: (newContent: string) => void;
  onBlurFilename: (newFileName: string) => void;
  isValidContentChange?: (newContent: string) => boolean;
  handleDeleteSourceCode: () => void;
  sourceCodeRef?: React.Ref<HTMLInputElement> | null;
}

const SourceCodeEditor = ({
  filename,
  filenameAutoFocus = false,
  content,
  onChangeFilename,
  onBlurFilename,
  onChangeContent,
  isValidContentChange = () => true,
  handleDeleteSourceCode,
  sourceCodeRef = null,
}: Props) => {
  const previousContentRef = useRef<string>(content);
  const { t } = useTranslation('TemplateUploadPage');

  const handleFilenameChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    onChangeFilename(event.target.value);
  };

  const handleContentChange = (value: string, viewUpdate: ViewUpdate) => {
    const isValid = isValidContentChange(value);

    if (!isValid) {
      const previousContent = previousContentRef.current;
      const transaction = viewUpdate.state.update({
        changes: { from: 0, to: value.length, insert: previousContent },
      });

      viewUpdate.view.dispatch(transaction);

      return;
    }

    onChangeContent(value);
    previousContentRef.current = value;
  };

  const handleFilenameBlur = (e: React.FocusEvent<HTMLInputElement>) => {
    onBlurFilename(e.target.value);
  };

  return (
    <S.SourceCodeEditorContainer ref={sourceCodeRef}>
      <S.FilenameInput
        value={filename}
        onChange={handleFilenameChange}
        onBlur={handleFilenameBlur}
        placeholder={t('sourceCodeEditor.filenamePlaceholder')}
        autoFocus={filenameAutoFocus}
      />
      <SourceCode
        mode='edit'
        language={getLanguageByFilename(filename)}
        content={content}
        handleContentChange={handleContentChange}
      />
      <S.DeleteButton
        size='small'
        variant='text'
        onClick={handleDeleteSourceCode}
      >
        <TrashcanIcon
          width={ICON_SIZE.LARGE}
          height={ICON_SIZE.LARGE}
          aria-label={t('sourceCodeEditor.deleteAriaLabel')}
        />
      </S.DeleteButton>
    </S.SourceCodeEditorContainer>
  );
};

export default SourceCodeEditor;
