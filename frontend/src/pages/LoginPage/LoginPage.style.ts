import styled from '@emotion/styled';

import { Flex, Heading } from '@/components';

export const LoginPageContainer = styled(Flex)`
  @media (max-width: 768px) {
    width: 90%;
    padding: 0 1rem;
  }
`;

export const LoginForm = styled.form`
  display: flex;
  flex-direction: column;
  gap: 1rem;

  width: 100%;
  height: 100%;

  @media (max-width: 768px) {
    gap: 0.75rem;
  }
`;

export const ResponsiveHeading = styled(Heading.Large)`
  @media (max-width: 768px) {
    font-size: 2rem;
  }
`;

export const ResponsiveFlex = styled(Flex)`
  @media (max-width: 768px) {
    gap: 1rem;
    align-items: center;
  }
`;
