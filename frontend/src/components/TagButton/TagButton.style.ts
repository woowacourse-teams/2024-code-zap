import styled from '@emotion/styled';

export const TagButtonWrapper = styled.button<{ bgColor: string; borderColor: string }>`
  cursor: pointer;

  box-sizing: border-box;
  height: 2.8rem;
  padding: 0 1.2rem;

  background-color: ${({ bgColor }) => bgColor};
  border: 2px solid ${({ borderColor }) => borderColor};
  border-radius: 2.5rem;

  &:not(:disabled):hover {
    box-shadow: 0 0.2rem 0.5rem #00000040;
  }
`;
