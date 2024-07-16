import { ChangeEvent } from 'react';
import ReactCodeMirror from '@uiw/react-codemirror';
import { vscodeDark } from '@uiw/codemirror-theme-vscode';
import { javascript } from '@codemirror/lang-javascript';
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
      <S.SnippetTitleInput value={fileName} onChange={handleFileNameChange} />
      <ReactCodeMirror
        value={content}
        height='200px'
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
