import styled from '@emotion/styled';

import { theme } from '@/style/theme';

export const HeaderContainer = styled.nav`
  position: fixed;
  z-index: 1;
  left: 0;

  display: flex;
  justify-content: center;

  width: 100%;

  background: white;
  border-bottom: 0.125rem solid ${theme.color.light.secondary_200};
`;

export const HeaderContentContainer = styled.div`
  display: flex;
  gap: 60px;
  align-items: center;

  width: 1280px;
  max-width: 1280px;
  height: 64px;
  padding: 30px;

  white-space: nowrap;
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

  width: 120px;
  height: 38px;

  background-color: white;
  border: 0.125rem solid ${theme.color.light.primary_800};
  border-radius: 0.5rem;
`;

export const UserMenuButton = styled.button`
  cursor: pointer;

  width: 38px;
  height: 38px;

  color: ${theme.color.light.primary_800};

  object-fit: contain;
  background-color: white;
  border-radius: 50%;
`;
