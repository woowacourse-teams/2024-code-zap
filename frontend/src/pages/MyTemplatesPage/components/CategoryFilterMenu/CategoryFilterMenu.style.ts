import styled from '@emotion/styled';

import { theme } from '@design/style/theme';

export const CategoryContainer = styled.div<{ isMenuOpen: boolean }>`
  display: flex;
  flex-direction: column;
  gap: 0.6rem;

  @media (max-width: 768px) {
    position: fixed;
    z-index: 52;
    top: 0;
    bottom: 0;
    left: ${({ isMenuOpen }) => (isMenuOpen ? '0' : '-15rem')};

    width: 15rem;
    padding: 1rem;

    background-color: white;
    box-shadow: 2px 0 8px rgba(0, 0, 0, 0.2);

    transition: left 0.3s ease-out;
  }
`;

export const CategoryListContainer = styled.div`
  position: relative;
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
`;

export const CategoryButtonContainer = styled.div`
  position: relative;
  display: flex;
  align-items: center;
  width: 12.5rem;
`;

export const CategoryButtonWrapper = styled.button`
  cursor: pointer;

  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;

  width: 100%;
  height: 3rem;
  padding: 0.5rem;

  text-overflow: ellipsis;
  white-space: nowrap;

  background-color: white;
  border-radius: 8px;
  box-shadow: 1px 2px 8px #00000020;
`;

export const IconButtonWrapper = styled.button<{ isMenuOpen: boolean }>`
  cursor: pointer;

  align-self: flex-end;

  padding: 0;

  opacity: 0.7;
  background: none;
  border: none;

  &:hover {
    transform: scale(1.15);
    opacity: 1;
  }

  @media (max-width: 768px) {
    display: ${({ isMenuOpen }) => (isMenuOpen ? 'block' : 'none')};
  }
`;

export const HighlightBox = styled.div<{ selectedIndex: number; categoryCount: number; isMenuOpen: boolean }>`
  position: absolute;
  top: ${({ selectedIndex }) => `calc(${selectedIndex} * 3.75rem)`};
  left: 0;

  width: 12.5rem;
  height: 3rem;

  border: 3px solid ${theme.color.light.primary_500};
  border-radius: 8px;

  transition: top 0.25s ease-out;

  @media (max-width: 768px) {
    display: ${({ isMenuOpen }) => (isMenuOpen ? 'block' : 'none')};
    transition: none;
  }
`;

export const ToggleMenuButton = styled.button<{ isMenuOpen: boolean }>`
  cursor: pointer;

  position: fixed;
  z-index: 50;
  top: 10%;
  left: 0;

  display: ${({ isMenuOpen }) => (isMenuOpen ? 'none' : 'flex')};
  gap: 0.5rem;
  align-items: center;

  height: 3.75rem;
  padding: 0 0.5rem 0 1.25rem;

  background-color: white;
  border: 2px solid ${theme.color.light.primary_600};
  border-left: none;
  border-radius: 0 8px 8px 0;

  transition: left 0.3s ease-out;

  &:hover {
    opacity: 0.7;
  }

  @media (min-width: 769px) {
    display: none;
  }
`;

export const Backdrop = styled.div`
  @media (max-width: 768px) {
    position: fixed;
    z-index: 51;
    top: 0;
    left: 0;

    width: 100vw;
    height: 100vh;

    background-color: rgba(0, 0, 0, 0.5);
  }
`;
