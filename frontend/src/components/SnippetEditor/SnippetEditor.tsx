import ReactCodeMirror from '@uiw/react-codemirror';
import { vscodeDark } from '@uiw/codemirror-theme-vscode';
import { javascript } from '@codemirror/lang-javascript';
import * as S from './style';

interface Props {
  content: string;
  onChange: (val: string) => void;
}

const SnippetEditor = ({ content, onChange }: Props) => {
  return (
    <S.SnippetEditorContainer>
      <S.SnippetTitleInput />
      <ReactCodeMirror
        value={content}
        height='200px'
        style={{ width: '100%' }}
        theme={vscodeDark}
        extensions={[javascript({ jsx: true })]}
        onChange={onChange}
        basicSetup={{ highlightActiveLine: false }}
      />
    </S.SnippetEditorContainer>
  );
};

export default SnippetEditor;
