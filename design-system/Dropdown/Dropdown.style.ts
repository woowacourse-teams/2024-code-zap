import styled from '@emotion/styled';

import { theme } from '@design/style/theme';

export const DropdownContainer = styled.div`
  font-size: '0.875rem';
`;

export const Wrapper = styled.div`
  width: 9.125rem;
  background-color: white;
  border-radius: 10px;
`;

export const SelectedButton = styled.button`
  display: flex;
  align-items: center;
  justify-content: space-between;

  width: 100%;
  height: 2.5rem;
  padding: 0.625rem;

  object-fit: contain;
  border: 1px solid ${theme.color.light.tertiary_100};
  border-radius: 10px;
`;

export const OptionList = styled.ul`
  position: absolute;
  z-index: 1;

  width: 9.125rem;
  margin: 2px 0 0 0;

  background-color: white;
  border: 1px solid ${theme.color.light.tertiary_100};
  border-radius: 10px;
`;

export const Option = styled.li`
  width: 100%;
  padding: 0.625rem;
  border-radius: 9px;
  &:hover {
    color: ${theme.color.light.white};
    background-color: ${theme.color.light.primary_200};
  }
`;
