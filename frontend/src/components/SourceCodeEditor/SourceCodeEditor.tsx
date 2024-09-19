import { ViewUpdate } from '@codemirror/view';
import { ChangeEvent, useRef } from 'react';

import { TrashcanIcon } from '@/assets/images';
import { SourceCode } from '@/components';
import { useToast } from '@/hooks/useToast';
import { validateFilename, validateSourceCode } from '@/service';
import { getLanguageByFilename } from '@/utils';

import * as S from './SourceCodeEditor.style';

interface Props {
  filename: string;
  filenameAutoFocus?: boolean;
  content: string;
  onChangeFilename: (newFileName: string) => void;
  onChangeContent: (newContent: string) => void;
  handleDeleteSourceCode: () => void;
  sourceCodeRef?: React.Ref<HTMLInputElement> | null;
}

const SourceCodeEditor = ({
  filename,
  filenameAutoFocus = false,
  content,
  onChangeFilename,
  onChangeContent,
  handleDeleteSourceCode,
  sourceCodeRef = null,
}: Props) => {
  const previousContentRef = useRef<string>(content);
  const { failAlert } = useToast();
  //TODO: 파일명과 소스코드 검증을 이 컴포넌트에서 하고 있는데 괜찮은가? 이 컴포넌트의 책임이 맞는가?

  const handleFilenameChange = (event: ChangeEvent<HTMLInputElement>) => {
    const newFilename = event.target.value;

    const errorMessage = validateFilename(newFilename);

    if (errorMessage) {
      failAlert(errorMessage);

      return;
    }

    onChangeFilename(newFilename);
  };

  const handleContentChange = (value: string, viewUpdate: ViewUpdate) => {
    const errorMessage = validateSourceCode(value);

    if (errorMessage) {
      failAlert(errorMessage);

      const previousContent = previousContentRef.current;
      const transaction = viewUpdate.state.update({
        changes: { from: 0, to: value.length, insert: previousContent },
      });

      viewUpdate.view.dispatch(transaction);
    } else {
      onChangeContent(value);
      previousContentRef.current = value;
    }
  };

  return (
    <S.SourceCodeEditorContainer ref={sourceCodeRef}>
      <S.FilenameInput
        value={filename}
        onChange={handleFilenameChange}
        placeholder={'파일명.[확장자]'}
        autoFocus={filenameAutoFocus}
      />
      <SourceCode
        mode='edit'
        language={getLanguageByFilename(filename)}
        content={content}
        handleContentChange={handleContentChange}
      />
      <S.DeleteButton size='small' variant='text' onClick={handleDeleteSourceCode}>
        <TrashcanIcon width={24} height={24} aria-label='템플릿 삭제' />
      </S.DeleteButton>
    </S.SourceCodeEditorContainer>
  );
};

export default SourceCodeEditor;
