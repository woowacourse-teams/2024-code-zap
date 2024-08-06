import styled from '@emotion/styled';

export const DropdownContainer = styled.div`
  font-size: '0.875rem';
`;

export const Wrapper = styled.div`
  width: 10rem;
  background-color: white;
  border: 1px solid #788496;
  border-radius: 8px;
`;

export const SelectedButton = styled.button`
  display: flex;
  align-items: center;
  justify-content: space-between;

  width: 100%;
  padding: 0.75rem;

  border-radius: 8px;
`;

export const OptionList = styled.ul`
  position: absolute;
  z-index: 1;

  width: 10rem;
  margin: 2px 0 0 0;

  background-color: white;
  border: 1px solid #788496;
  border-radius: 8px;
`;

export const Option = styled.li`
  width: 100%;
  padding: 0.75rem;
  border-radius: 7px;
  &:hover {
    color: ${({ theme }) => theme.color.light.white};
    background-color: ${({ theme }) => theme.color.light.primary_500};
  }
`;
