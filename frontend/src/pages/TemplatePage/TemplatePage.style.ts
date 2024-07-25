import styled from '@emotion/styled';

import { Button } from '@/components';

export const MainContainer = styled.main`
  display: flex;
  flex-direction: column;
  gap: 2rem;

  width: 100%;
  max-width: 80rem;
`;

export const SidebarContainer = styled.aside`
  position: fixed;
  top: 20rem;
  right: 2rem;
`;

export const EditButton = styled(Button)``;

export const DeleteButton = styled(Button)``;
