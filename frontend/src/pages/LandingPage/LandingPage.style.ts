import styled from '@emotion/styled';
import { EditorView } from '@uiw/react-codemirror';

export const Container = styled.div`
  max-width: 1200px;
  margin: auto;
  padding: 1.25rem;
`;

export const ContentSection = styled.div`
  display: flex;
  gap: 3rem;
  align-items: center;
  justify-content: space-between;

  width: 100%;
  margin-top: 4rem;

  @media (max-width: 768px) {
    flex-direction: column-reverse;
    text-align: center;
  }
`;

export const TextContent = styled.div`
  display: flex;
  flex-direction: column;
  gap: 2rem;
  justify-content: space-between;

  @media (max-width: 768px) {
    align-items: center;
    justify-content: center;
  }
`;

export const ImageWrapper = styled.div`
  display: flex;
  align-items: center;
  justify-content: center;
`;

export const CardSection = styled.div`
  display: flex;
  justify-content: space-between;
  padding: 3rem 0;

  @media (max-width: 768px) {
    flex-direction: column;
  }
`;

export const Card = styled.div`
  display: flex;
  flex: 1;
  flex-direction: column;
  gap: 0.5rem;

  margin: 0 10px;
  padding: 20px;

  text-align: center;

  background-color: white;
  border-radius: 8px;
  box-shadow: 1px 2px 8px 1px #00000030;
  &:hover {
    bottom: 0.5rem;
    transform: scale(1.025);
  }

  @media (max-width: 768px) {
    align-items: center;
    margin: 10px 0;
  }
`;

export const TemplateSection = styled.div`
  display: flex;
  align-items: center;
  justify-content: space-between;

  width: 100%;
  margin-top: 3rem;
  padding: 4rem 0;

  @media (max-width: 768px) {
    flex-direction: column;
  }
`;

export const CodeSection = styled.div`
  margin-right: 2rem;

  @media (max-width: 768px) {
    width: 100%;
  }
`;

export const SyntaxHighlighterWrapper = styled.div<{ isOpen: boolean }>`
  overflow: hidden;
  max-height: ${({ isOpen }) => (isOpen ? '1000rem' : '0')};
  animation: ${({ isOpen }) => (!isOpen ? 'collapse' : 'expand')} 0.7s ease-in-out forwards;
`;

export const CustomCodeMirrorTheme = EditorView.theme({
  '.cm-activeLine': { backgroundColor: `transparent !important` },
  '.cm-activeLineGutter': { backgroundColor: `transparent !important` },
});
