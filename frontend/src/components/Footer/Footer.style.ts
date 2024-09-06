import styled from '@emotion/styled';

import { theme } from '@/style/theme';

export const FooterContainer = styled.footer`
  display: flex;
  gap: 2rem;
  align-items: center;
  justify-content: space-between;

  padding-top: 4rem;

  color: ${theme.color.light.secondary_500};
`;

export const ContactEmail = styled.a`
  text-decoration: none;

  &:hover {
    text-decoration: underline;
  }
`;
