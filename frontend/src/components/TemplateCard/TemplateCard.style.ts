import styled from '@emotion/styled';

export const TemplateCardContainer = styled.div`
  cursor: pointer;

  display: flex;
  flex-direction: column;
  justify-content: space-between;

  box-sizing: border-box;
  width: 100%;
  height: 12.8rem;
  padding: 1.6rem;

  background: #ffffff;
  border: 1px solid #afb6c1;
  border-radius: 0.8rem;
  box-shadow: 0 0.4rem 0.8rem #00000020;

  transition: 0.1s ease;

  &:hover {
    bottom: 0.8rem;
    transform: scale(1.04);
  }
`;
