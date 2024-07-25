import styled from '@emotion/styled';

import { Button } from '@/components';

export const MainContainer = styled.main`
  display: flex;
  flex-direction: column;
  gap: 2rem;

  width: 100%;
  max-width: 80rem;
`;

export const DeleteButton = styled(Button)`
  position: absolute;
  top: 0.3rem;
  right: 0.4rem;
  height: 2.4rem;
`;
