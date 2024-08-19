import styled from '@emotion/styled';

export const Tag = styled.span`
  display: flex;
  gap: 0.25rem;
  align-items: center;
  justify-content: center;

  max-width: 100%;
  padding: 0.3rem 0.75rem;

  word-break: break-word;

  background-color: ${({ theme }) => theme.color.light.tertiary_50};
  border: 1px solid ${({ theme }) => theme.color.light.tertiary_200};
  border-radius: 40px;
`;
