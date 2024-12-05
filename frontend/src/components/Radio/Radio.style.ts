import styled from '@emotion/styled';

export const RadioContainer = styled.div`
  display: flex;
  gap: 2rem;
`;

export const RadioOption = styled.button`
  cursor: pointer;

  display: flex;
  gap: 1rem;
  align-items: center;
  justify-content: center;
`;

export const RadioCircle = styled.div<{ isSelected: boolean }>`
  width: 1rem;
  height: 1rem;

  background-color: ${({ theme, isSelected }) =>
    isSelected ? theme.color.light.primary_500 : theme.color.light.secondary_300};
  border: ${({ theme }) => `0.18rem solid ${theme.color.light.secondary_300}`};
  border-radius: 100%;
`;
