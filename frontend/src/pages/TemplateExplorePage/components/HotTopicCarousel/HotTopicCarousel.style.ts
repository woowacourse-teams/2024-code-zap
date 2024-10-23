import styled from '@emotion/styled';

import { theme } from '@/style/theme';

export const Topic = styled.button<{ background: string; border: string; isSelected: boolean }>`
  cursor: pointer;

  position: relative;

  display: flex;
  align-items: flex-end;

  width: 100%;
  height: 100%;
  padding: 1rem;

  background-image: url(${({ background }) => background});
  background-repeat: no-repeat;
  background-position: center;
  background-size: cover;
  border-radius: 12px;
  box-shadow: ${({ isSelected, border }) => (isSelected ? `0 0 0 2px ${border}` : 'none')};

  transition: box-shadow 0.2s ease-in-out;

  &::after {
    pointer-events: none;
    content: '';

    position: absolute;
    top: 0;
    right: 0;
    bottom: 0;
    left: 0;

    background-color: rgba(0, 0, 0, 0);
    border-radius: 12px;

    transition: background-color 0.3s;
  }

  &:hover::after {
    background-color: rgba(0, 0, 0, 0.2);
  }
`;

export const Content = styled.div`
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  justify-content: flex-start;
`;

export const Title = styled.div`
  width: fit-content;
  padding: 0.25rem 0.5rem;
  border: 1px solid ${theme.color.light.secondary_800};
  border-radius: 8px;
`;

export const Description = styled.div`
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
  align-items: flex-start;
  justify-content: flex-start;
`;
