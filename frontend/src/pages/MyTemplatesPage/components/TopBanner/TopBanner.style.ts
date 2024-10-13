import styled from '@emotion/styled';

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
  align-items: center;
  margin-left: calc(12.5rem + clamp(1rem, calc(0.0888 * 100vw - 3.2618rem), 4.375rem));
`;
