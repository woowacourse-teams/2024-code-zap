import styled from '@emotion/styled';

import { Input } from '@/components';

export const TemplateExplorePageContainer = styled.div<{ cols: number }>`
  display: grid;
  grid-gap: 1rem;
  grid-template-columns: repeat(${({ cols }) => cols}, minmax(0, 1fr));

  width: 100%;
  max-width: 80rem;
`;

export const MainContainer = styled.main`
  display: flex;

  @media (max-width: 1376px) {
    gap: clamp(1rem, calc(0.0888 * 100vw - 3.2618rem), 4.375rem);
  }

  @media (max-width: 768px) {
    gap: 0;
  }
`;

export const SearchInput = styled(Input)`
  box-shadow: inset 1px 2px 8px #00000030;
`;

export const TemplateListSectionWrapper = styled.div`
  position: relative;
  width: 100%;
`;
