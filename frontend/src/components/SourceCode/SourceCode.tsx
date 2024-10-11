import { ViewUpdate } from '@codemirror/view';
import { type LanguageName, loadLanguage } from '@uiw/codemirror-extensions-langs';
import { quietlight } from '@uiw/codemirror-theme-quietlight';
import ReactCodeMirror, { EditorView, ReactCodeMirrorRef } from '@uiw/react-codemirror';
import { useRef } from 'react';

import * as S from './SourceCode.style';

export type SourceCodeMode = 'edit' | 'detailView' | 'thumbnailView';

interface Props {
  mode: SourceCodeMode;
  language: string;
  content: string;
  handleContentChange?: (value: string, viewUpdate: ViewUpdate) => void;
}

const SourceCode = ({ mode = 'detailView', language, content, handleContentChange }: Props) => {
  const codeMirrorRef = useRef<ReactCodeMirrorRef>(null);

  const focusCodeMirror = () => {
    if (!codeMirrorRef.current) {
      return;
    }

    if (codeMirrorRef.current?.view) {
      codeMirrorRef.current.view.focus();
    }
  };

  return (
    <ReactCodeMirror
      ref={codeMirrorRef}
      value={content}
      onClick={focusCodeMirror}
      onChange={handleContentChange}
      placeholder={'// 코드를 입력해주세요'}
      theme={quietlight}
      height={mode === 'thumbnailView' ? '10rem' : '100%'}
      minHeight={mode === 'edit' ? '10rem' : undefined}
      maxHeight={mode === 'edit' ? '40rem' : undefined}
      style={{ width: '100%', fontSize: '1rem' }}
      css={{
        '.cm-scroller': {
          padding: '1rem 0',
          overflowY: 'auto',
          height: '100%',
        },
      }}
      extensions={[
        loadLanguage(language as LanguageName) || [],
        mode === 'edit' ? [] : EditorView.editable.of(false),
        mode === 'edit' ? S.CustomEditorTheme : S.CustomViewerTheme,
      ]}
      basicSetup={{ foldGutter: true, foldKeymap: true }}
    />
  );
};

export default SourceCode;
