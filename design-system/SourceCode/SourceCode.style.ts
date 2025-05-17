import { EditorView } from '@uiw/react-codemirror';

export const CustomEditorTheme = EditorView.theme({
  '.cm-activeLine': { backgroundColor: `rgba(0, 0, 0, 0.1) !important` },
  '.cm-activeLineGutter': { backgroundColor: `rgba(0, 0, 0, 0.1) !important` },
});

export const CustomViewerTheme = EditorView.theme({
  '.cm-activeLine': { backgroundColor: `transparent !important` },
  '.cm-activeLineGutter': { backgroundColor: `transparent !important` },
});
