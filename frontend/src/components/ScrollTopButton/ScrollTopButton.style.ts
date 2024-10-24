import styled from '@emotion/styled';

import { theme } from '@/style/theme';

export const ScrollTopButtonContainer = styled.button<{ isVisible: boolean; isTemplateUpload: boolean }>`
  cursor: pointer;

  position: fixed;
  right: 2rem;
  bottom: ${({ isTemplateUpload }) => (isTemplateUpload ? '4rem' : '2rem')};
  transform: translateY(${({ isVisible }) => (isVisible ? '0' : '20px')});

  display: flex;
  align-items: center;
  justify-content: center;

  padding: 0.75rem;

  visibility: ${({ isVisible }) => (isVisible ? 'visible' : 'hidden')};
  opacity: ${({ isVisible }) => (isVisible ? 1 : 0)};
  background-color: ${theme.color.light.primary_500};
  border: none;
  border-radius: 100%;

  transition: all 0.3s ease-in-out;

  &:hover {
    transform: ${({ isVisible }) => (isVisible ? 'translateY(-5px)' : 'translateY(20px)')};
  }
`;

export const Sentinel = styled.div`
  pointer-events: none;

  position: absolute;
  top: 450px;
  left: 0;

  width: 100%;
  height: 1px;

  opacity: 0;
`;
