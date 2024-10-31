import { type LanguageName, loadLanguage } from '@uiw/codemirror-extensions-langs';
import { quietlight } from '@uiw/codemirror-theme-quietlight';
import ReactCodeMirror, { EditorView, ReactCodeMirrorRef, type ViewUpdate } from '@uiw/react-codemirror';
import { useRef } from 'react';

import { useWindowWidth } from '@/hooks';

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
  const windowWidth = useWindowWidth();

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
      css={{
        width: '100%',
        fontSize: `${mode === 'detailView' && windowWidth <= 400 ? '0.65rem' : '1rem'}`,
        minHeight: `${mode === 'thumbnailView' ? '160px' : undefined}`,
        backgroundColor: `rgba(0, 0, 0, 0.1)`,
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
