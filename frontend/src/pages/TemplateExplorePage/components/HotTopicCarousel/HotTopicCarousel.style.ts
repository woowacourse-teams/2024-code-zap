import styled from '@emotion/styled';

export const Topic = styled.button<{ background: string; border: string; isSelected: boolean }>`
  cursor: pointer; /* 커서 스타일 추가 */

  width: 100%;
  height: 100%;

  background-color: ${({ background }) => background};
  border: ${({ isSelected, border }) => (isSelected ? `2px solid ${border}` : 'none')};
  border-radius: 12px;

  &:hover {
    border: 2px solid ${({ border }) => border};
  }
`;
