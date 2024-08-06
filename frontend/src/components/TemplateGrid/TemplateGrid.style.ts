import styled from '@emotion/styled';

export const TemplateContainer = styled.div<{ cols: number }>`
  display: grid;
  grid-gap: 1rem;
  grid-template-columns: repeat(${({ cols }) => cols}, 1fr);

  width: 100%;
  max-width: 63.125rem;
`;
