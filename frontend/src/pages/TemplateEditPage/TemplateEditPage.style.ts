import styled from '@emotion/styled';

import { Button } from '@/components';
import { theme } from '@/style/theme';

export const TemplateEditContainer = styled.div`
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
`;

export const MainContainer = styled.div`
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
  justify-content: center;

  width: 100%;
  max-width: 53rem;
  margin: 1rem 0 0 0;
  margin: auto;
  margin-top: 3rem;
`;

export const VisibilityContainer = styled.div`
  display: flex;
  gap: 2rem;
`;

export const VisibilityButton = styled.button`
  display: flex;
  gap: 1rem;
  align-items: center;
  justify-content: center;
`;

export const Radio = styled.div<{ isSelected: boolean }>`
  width: 1rem;
  height: 1rem;
  background-color: ${({ theme, isSelected }) =>
    isSelected ? theme.color.light.primary_500 : theme.color.light.secondary_200};
  border-radius: 100%;
`;

export const CancelButton = styled(Button)`
  background-color: white;
`;

export const ButtonGroup = styled.div`
  position: sticky;
  bottom: 0;

  display: flex;
  gap: 0.5rem;
  justify-content: flex-end;

  width: 100%;
  padding-top: 0.5rem;
  padding-bottom: 1rem;
`;

export const UnderlineInputWrapper = styled.div`
  width: 100%;
  border-bottom: 1px solid ${theme.color.light.tertiary_400};
`;

export const SidebarContainer = styled.aside`
  position: fixed;
  top: 20rem;
  right: 1.5rem;
  margin-left: -10rem;

  @media (min-width: 80rem) {
    position: sticky;
    top: 20rem;
    right: 0;
    align-self: flex-start;
  }
`;
