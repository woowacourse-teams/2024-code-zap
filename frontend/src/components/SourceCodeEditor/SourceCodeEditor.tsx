import { ViewUpdate } from '@codemirror/view';
import { type LanguageName, loadLanguage } from '@uiw/codemirror-extensions-langs';
import { quietlight } from '@uiw/codemirror-theme-quietlight';
import CodeMirror, { ReactCodeMirrorRef } from '@uiw/react-codemirror';
import { ChangeEvent, useRef } from 'react';

import { ToastContext } from '@/contexts';
import { useCustomContext } from '@/hooks/utils';
import { validateFileName } from '@/service';
import { getByteSize, getLanguageByFilename } from '@/utils';
import * as S from './SourceCodeEditor.style';

const MAX_CONTENT_SIZE = 65535;

interface Props {
  index: number;
  fileName: string;
  content: string;
  onChangeContent: (newContent: string) => void;
  onChangeFileName: (newFileName: string) => void;
}

const SourceCodeEditor = ({ index, fileName, content, onChangeContent, onChangeFileName }: Props) => {
  const codeMirrorRef = useRef<ReactCodeMirrorRef>(null);
  const previousContentRef = useRef<string>(content);
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
    const newFileName = event.target.value;

    const errorMessage = validateFileName(newFileName);

    if (errorMessage) {
      failAlert(errorMessage);

      return;
    }

    onChangeFileName(newFileName);
  };

  const handleContentChange = (value: string, viewUpdate: ViewUpdate) => {
    const currentByteSize = getByteSize(value);

    if (currentByteSize > MAX_CONTENT_SIZE) {
      failAlert(`소스코드는 최대 ${MAX_CONTENT_SIZE} 바이트를 초과할 수 없습니다.`);

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
    <S.SourceCodeEditorContainer>
      <S.SourceCodeFileNameInput
        value={fileName}
        onChange={handleFileNameChange}
        placeholder={'파일명.js'}
        autoFocus={index !== 0}
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
        extensions={[loadLanguage(getLanguageByFilename(fileName) as LanguageName) || [], S.CustomCodeMirrorTheme]}
        onClick={focusCodeMirror}
        basicSetup={{ foldGutter: true, foldKeymap: true }}
      />
    </S.SourceCodeEditorContainer>
  );
};

export default SourceCodeEditor;
