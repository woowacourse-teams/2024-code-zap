import styled from '@emotion/styled';

export const TagButtonWrapper = styled.button<{ bgColor: string; borderColor: string }>`
  cursor: pointer;

  box-sizing: border-box;
  height: 1.75rem;
  padding: 0 0.75rem;

  background-color: ${({ bgColor }) => bgColor};
  border: 1px solid ${({ borderColor }) => borderColor};
  border-radius: 2.5rem;

  &:not(:disabled):hover {
    box-shadow: 0 1px 4px #00000030;
  }
`;
