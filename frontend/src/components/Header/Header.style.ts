import { css } from '@emotion/react';
import styled from '@emotion/styled';

import { theme } from '@/style/theme';

export const HeaderContainer = styled.nav`
  position: fixed;
  z-index: 10;
  left: 0;

  display: flex;
  justify-content: center;

  width: 100%;
  padding: 0 2rem;

  background: white;
  border-bottom: 2px solid ${theme.color.light.secondary_200};
`;

export const HeaderContentContainer = styled.div`
  display: flex;
  gap: 3.75rem;
  align-items: center;
  justify-content: space-between;

  width: 80rem;
  max-width: 80rem;
  height: 4rem;

  white-space: nowrap;
`;

export const HeaderMenu = styled.div<{ menuOpen: boolean }>`
  display: flex;
  justify-content: space-between;
  width: 100%;

  @media (max-width: 768px) {
    position: fixed;
    z-index: 100;
    top: var(--header-height);
    right: 0;

    display: none;
    flex-direction: column;
    flex-wrap: nowrap;
    align-items: center;
    justify-content: flex-start;

    width: 75%;
    height: 100%;

    background-color: white;

    transition: transform 0.3s ease-in-out;

    ${({ menuOpen }) =>
      menuOpen
        ? css`
            transform: translateX(0);
            display: flex;
          `
        : css`
            transform: translateX(100%);
          `}
  }
`;

export const HamburgerIconWrapper = styled.div`
  display: none;

  @media (max-width: 768px) {
    display: flex;
  }
`;

export const NavOptionButton = styled.button`
  cursor: pointer;
  background: none;
`;
export const NewTemplateButton = styled.button`
  cursor: pointer;

  display: flex;
  gap: 0.5rem;
  align-items: center;
  justify-content: center;

  width: 7.5rem;
  height: 2.375rem;

  background-color: white;
  border: 2px solid ${theme.color.light.primary_500};
  border-radius: 8px;
`;
export const UserMenuButton = styled.button`
  cursor: pointer;

  width: 2.375rem;
  height: 2.375rem;

  color: ${theme.color.light.primary_500};

  object-fit: contain;
  background-color: white;
  border-radius: 50%;
`;
