import { Children, HTMLAttributes, InputHTMLAttributes, isValidElement, PropsWithChildren, ReactNode } from 'react';

import * as S from './style';

export interface BaseProps extends HTMLAttributes<HTMLDivElement> {
  size?: 'small' | 'medium';
  variant?: 'filled' | 'outlined' | 'text';
  isValid?: boolean;
}
export interface TextFieldProps extends InputHTMLAttributes<HTMLInputElement> {
  inputSize?: 'small' | 'medium';
}
export interface AdornmentProps extends HTMLAttributes<HTMLDivElement> {}

export interface HelperTextProps extends HTMLAttributes<HTMLSpanElement> {}

const getChildOfType = (children: ReactNode, type: unknown) => {
  const childrenArray = Children.toArray(children);

  return childrenArray.find((child) => isValidElement(child) && child.type === type);
};

const getChildrenWithoutType = (children: ReactNode, type: unknown) => {
  const childrenArray = Children.toArray(children);

  return childrenArray.filter((child) => !(isValidElement(child) && child.type === type));
};

const TextField = ({ ...rests }: TextFieldProps) => <S.TextField {...rests} />;

const Adornment = ({ children, ...rests }: PropsWithChildren<AdornmentProps>) => (
  <S.Adornment {...rests}>{children}</S.Adornment>
);

const HelperText = ({ children, ...rests }: PropsWithChildren<HelperTextProps>) => (
  <S.HelperText {...rests}>{children}</S.HelperText>
);

const HelperTextType = (<HelperText />).type;

const Base = ({
  variant = 'filled',
  size = 'medium',
  isValid = true,
  children,
  ...rests
}: PropsWithChildren<BaseProps>) => {
  const inputWithAdornment = getChildrenWithoutType(children, HelperTextType);
  const helperText = getChildOfType(children, HelperTextType);

  return (
    <S.Wrapper>
      <S.Base variant={variant} size={size} isValid={isValid} {...rests}>
        {inputWithAdornment}
      </S.Base>
      {helperText}
    </S.Wrapper>
  );
};

const Input = Object.assign(Base, {
  TextField,
  Adornment,
  HelperText,
});

export default Input;
