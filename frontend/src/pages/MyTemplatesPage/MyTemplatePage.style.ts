import styled from '@emotion/styled';

import { Input } from '@/components';
import { theme } from '@design/style/theme';

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

export const SearchKeywordPlaceholder = styled.div`
  height: 1.5rem;
`;

export const SearchInput = styled(Input)`
  box-shadow: inset 1px 2px 8px #00000030;
`;

export const NewTemplateButton = styled.button`
  cursor: pointer;

  display: flex;
  flex-direction: column;
  gap: 1.5rem;
  align-items: center;
  justify-content: center;

  width: 100%;
  height: 19.75rem;
  padding: 1.5rem;

  border: 3px dashed ${theme.color.light.primary_500};
  border-radius: 8px;
  &:hover {
    background-color: ${theme.color.light.primary_50};
  }
`;

export const TemplateListSectionWrapper = styled.div`
  position: relative;
  width: 100%;
`;
