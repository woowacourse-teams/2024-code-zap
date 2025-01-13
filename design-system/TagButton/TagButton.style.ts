import styled from '@emotion/styled';

export const TagButtonWrapper = styled.button<{ background: string; border: string; isFocused: boolean }>`
  cursor: pointer;

  display: flex;
  gap: 0.75rem;
  align-items: center;
  justify-content: center;

  box-sizing: border-box;
  height: 1.75rem;
  margin: 0.25rem;
  padding: 0 0.75rem;

  opacity: ${({ isFocused }) => (isFocused ? '0.99' : '0.85')};
  background-color: ${({ background }) => background};
  border-radius: 0.5rem;
  outline: ${({ isFocused }) => (isFocused ? '1.5' : '1')}px solid ${({ border }) => border};
  box-shadow: ${({ isFocused }) => isFocused && '0 0 3px #00000070'};

  &:disabled {
    cursor: text;
  }
`;
