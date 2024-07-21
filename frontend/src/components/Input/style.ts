import styled from '@emotion/styled';
import type { Props } from './Input';

interface StyleProps extends Pick<Props, 'width' | 'height' | 'fontSize' | 'fontWeight' | 'type'> {}

export const InputWrapper = styled.div`
  position: relative;
  display: inline-block;
`;

export const Input = styled.input<StyleProps>`
  width: ${({ width }) => width};
  height: ${({ height }) => height};
  padding: 1.4rem;
  padding-left: ${({ type }) => type === 'search' && '4.2rem;'};

  font-size: ${({ fontSize }) => fontSize};
  font-weight: ${({ fontWeight }) => fontWeight};

  background: #eee;
  border: 0.1rem solid #808080;
  border-radius: 8px;

  &::placeholder {
    color: #808080;
  }

  &:focus {
    border-color: black;
  }

  &:disabled {
    cursor: default;
    opacity: 0.6;
    background-color: #f5f5f5;
    border-color: #ddd;
  }
`;

export const SearchIcon = styled.img`
  position: absolute;
  top: 50%;
  left: 1.4rem;
  transform: translateY(-50%);

  width: 2rem;
  height: 2rem;
`;
