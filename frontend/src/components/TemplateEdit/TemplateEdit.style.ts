import styled from '@emotion/styled';

import { Button } from '@/components';
import { theme } from '@/style/theme';

export const TemplateEditContainer = styled.div`
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
  align-items: flex-start;
  justify-content: center;

  max-width: 53rem;
  margin: 1rem 0 0 0;
  margin: auto;
  margin-top: 3rem;
`;

export const DeleteButton = styled(Button)`
  position: absolute;
  top: 0.3rem;
  right: 0.4rem;
  height: 2.4rem;
`;

export const UnderlineInputWrapper = styled.div`
  width: 100%;
  border-bottom: 1px solid ${theme.color.light.tertiary_400};
`;
