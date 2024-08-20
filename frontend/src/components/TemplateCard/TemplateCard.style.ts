import styled from '@emotion/styled';

import { theme } from '@/style/theme';

export const TemplateCardContainer = styled.div`
  cursor: pointer;

  display: flex;
  flex-direction: column;
  justify-content: space-between;

  box-sizing: border-box;
  width: 100%;
  height: 8rem;
  padding: 1rem;

  background: ${theme.color.light.white};
  border-radius: 8px;
  box-shadow: 1px 2px 8px 1px #00000020;

  transition: 0.1s ease;

  &:hover {
    bottom: 0.5rem;
    transform: scale(1.025);
    box-shadow: 1px 2px 8px 1px #00000030;
  }
`;
