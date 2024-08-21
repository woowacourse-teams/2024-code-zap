import { type LanguageName, loadLanguage } from '@uiw/codemirror-extensions-langs';
import { quietlight } from '@uiw/codemirror-theme-quietlight';
import CodeMirror, { ReactCodeMirrorRef } from '@uiw/react-codemirror';
import { ChangeEvent, useRef } from 'react';

import { ToastContext } from '@/contexts';
import { useCustomContext } from '@/hooks/utils';
import { validateFileName } from '@/service/validates';
import { getLanguageByFilename } from '@/utils';
import * as S from './SourceCodeEditor.style';

interface Props {
  index: number;
  fileName: string;
  content: string;
  onChangeContent: (newContent: string) => void;
  onChangeFileName: (newFileName: string) => void;
}

const SourceCodeEditor = ({ index, fileName, content, onChangeContent, onChangeFileName }: Props) => {
  const codeMirrorRef = useRef<ReactCodeMirrorRef>(null);
  const { failAlert } = useCustomContext(ToastContext);

  const focusCodeMirror = () => {
    if (!codeMirrorRef.current) {
      return;
    }

    if (codeMirrorRef.current?.view) {
      codeMirrorRef.current.view.focus();
    }
  };

  const handleFileNameChange = (event: ChangeEvent<HTMLInputElement>) => {
    const errorMessage = validateFileName(event.target.value);

    if (errorMessage) {
      failAlert(errorMessage);

      return;
    }

    onChangeFileName(event.target.value);
  };

  const handleContentChange = (value: string) => {
    onChangeContent(value);
  };

  return (
    <S.SourceCodeEditorContainer>
      <S.SourceCodeFileNameInput
        value={fileName}
        onChange={handleFileNameChange}
        placeholder={'파일명.js'}
        autoFocus={index !== 0 ? true : false}
      />
      <CodeMirror
        ref={codeMirrorRef}
        placeholder={'// 코드를 입력해주세요'}
        value={content}
        height='100%'
        minHeight='10rem'
        maxHeight='40rem'
        style={{ fontSize: '1rem' }}
        theme={quietlight}
        onChange={handleContentChange}
        extensions={[loadLanguage(getLanguageByFilename(fileName) as LanguageName) || [], S.CustomTheme]}
        onClick={focusCodeMirror}
        basicSetup={{ foldGutter: true, foldKeymap: true }}
      />
    </S.SourceCodeEditorContainer>
  );
};

export default SourceCodeEditor;
