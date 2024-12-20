import styled from '@emotion/styled';

export const TopBannerContainer = styled.div`
  display: flex;
  flex-shrink: 0;
  align-items: center;

  width: 100%;
  height: 10.25rem;

  white-space: nowrap;
`;

export const TopBannerTextWrapper = styled.div`
  display: flex;
  gap: 0.5rem;
  align-items: center;

  margin-left: calc(12.5rem + clamp(1rem, calc(0.0888 * 100vw - 3.2618rem), 4.375rem));

  white-space: normal;

  @media (max-width: 768px) {
    flex-direction: column;
    align-items: flex-start;
    justify-content: flex-start;
    margin-left: 5rem;
  }
`;
