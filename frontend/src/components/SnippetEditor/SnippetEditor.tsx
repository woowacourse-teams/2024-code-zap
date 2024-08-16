import { javascript } from '@codemirror/lang-javascript';
import { vscodeDark } from '@uiw/codemirror-theme-vscode';
import ReactCodeMirror from '@uiw/react-codemirror';
import { ChangeEvent } from 'react';

import * as S from './style';

interface Props {
  fileName: string;
  content: string;
  onChangeContent: (newContent: string) => void;
  onChangeFileName: (newFileName: string) => void;
}

const SnippetEditor = ({ fileName, content, onChangeContent, onChangeFileName }: Props) => {
  const handleFileNameChange = (event: ChangeEvent<HTMLInputElement>) => {
    onChangeFileName(event.target.value);
  };

  const handleContentChange = (value: string) => {
    onChangeContent(value);
  };

  return (
    <S.SnippetEditorContainer>
      <S.SnippetFileNameInput value={fileName} onChange={handleFileNameChange} placeholder={'파일명.js'} />
      <ReactCodeMirror
        placeholder={'// 코드를 입력해주세요'}
        value={content}
        height='100%'
        minHeight='10rem'
        maxHeight='40rem'
        style={{ width: '100%' }}
        theme={vscodeDark}
        extensions={[javascript({ jsx: true })]}
        onChange={handleContentChange}
        basicSetup={{ highlightActiveLine: false }}
      />
    </S.SnippetEditorContainer>
  );
};

export default SnippetEditor;
