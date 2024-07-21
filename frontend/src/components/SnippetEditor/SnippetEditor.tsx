import { javascript } from '@codemirror/lang-javascript';
import { vscodeDark } from '@uiw/codemirror-theme-vscode';
import ReactCodeMirror from '@uiw/react-codemirror';
import { ChangeEvent } from 'react';
import { Input } from '../Input';
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
      <Input
        value={fileName}
        onChange={handleFileNameChange}
        type='text'
        width='100%'
        height='3rem'
        fontSize='14px'
        fontWeight='700'
      />
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
