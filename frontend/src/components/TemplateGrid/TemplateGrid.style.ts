import styled from '@emotion/styled';

export const TemplateContainer = styled.div<{ cols: number }>`
  display: grid;
  grid-gap: 1.6rem;
  grid-template-columns: repeat(${({ cols }) => cols}, 1fr);
  width: 101rem;
`;
