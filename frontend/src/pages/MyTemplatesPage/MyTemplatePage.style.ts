import styled from '@emotion/styled';

import { Input } from '@/components';

export const MyTemplatePageContainer = styled.div`
  display: flex;
  flex-direction: column;
`;

export const MainContainer = styled.main`
  display: flex;
  gap: 4.375rem;

  @media (max-width: 1376px) {
    gap: clamp(1rem, calc(0.0888 * 100vw - 3.2618rem), 4.375rem);
  }

  @media (max-width: 768px) {
    gap: 0;
  }
`;

export const TopBannerContainer = styled.div`
  display: flex;
  align-items: center;

  width: 100%;
  height: 10.25rem;

  white-space: nowrap;
`;

export const TopBannerTextWrapper = styled.div`
  display: flex;
  gap: 0.5rem;
  margin-left: calc(12.5rem + clamp(1rem, calc(0.0888 * 100vw - 3.2618rem), 4.375rem));
`;

export const SearchInput = styled(Input)`
  box-shadow: inset 1px 2px 8px #00000030;
`;
