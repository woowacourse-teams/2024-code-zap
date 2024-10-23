import styled from '@emotion/styled';

import { Button } from '@/components';

export const MainContainer = styled.main`
  display: flex;
  flex-direction: column;
  gap: 1.5rem;

  width: 100%;
  max-width: 53rem;
  margin: auto;
  margin-top: 3rem;
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

export const NoScrollbarContainer = styled.div`
  scrollbar-width: none;
  overflow: auto;
  width: 100%;

  -ms-overflow-style: none;
  &::-webkit-scrollbar {
    display: none;
  }
`;

export const EditButton = styled(Button)`
  padding: 0;
`;

export const DeleteButton = styled(Button)`
  padding: 0;
`;

export const PrivateWrapper = styled.div`
  flex-shrink: 0;
`;

export const AuthorInfoContainer = styled.div`
  cursor: pointer;

  display: flex;
  gap: 0.25rem;
  align-items: center;

  min-width: 0;
`;
