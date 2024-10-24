import styled from '@emotion/styled';

export const PageTitle = styled.div`
  align-items: center;
  justify-content: center;

  width: 100%;
  padding: 5rem 1rem;

  white-space: wrap;
`;

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

export const TemplateListSectionWrapper = styled.div`
  position: relative;
  width: 100%;
`;
