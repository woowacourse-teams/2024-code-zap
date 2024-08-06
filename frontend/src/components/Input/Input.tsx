import {
  Children,
  HTMLAttributes,
  InputHTMLAttributes,
  isValidElement,
  LabelHTMLAttributes,
  PropsWithChildren,
  ReactNode,
} from 'react';

import * as S from './Input.style';

export interface BaseProps extends HTMLAttributes<HTMLDivElement> {
  size?: 'small' | 'medium' | 'xlarge';
  variant?: 'filled' | 'outlined' | 'text';
  isValid?: boolean;
}
export interface TextFieldProps extends InputHTMLAttributes<HTMLInputElement> {
  inputSize?: 'small' | 'medium';
}

export interface LabelProps extends LabelHTMLAttributes<HTMLLabelElement> {}

export interface AdornmentProps extends HTMLAttributes<HTMLDivElement> {}

export interface HelperTextProps extends HTMLAttributes<HTMLSpanElement> {}

const getChildOfType = (children: ReactNode, type: unknown) => {
  const childrenArray = Children.toArray(children);

  return childrenArray.find((child) => isValidElement(child) && child.type === type);
};

const getChildrenWithoutTypes = (children: ReactNode, types: unknown[]) => {
  const childrenArray = Children.toArray(children);

  return childrenArray.filter((child) => !(isValidElement(child) && types.includes(child.type)));
};

const TextField = ({ ...rests }: TextFieldProps) => <S.TextField {...rests} />;

const Label = ({ children, ...rests }: PropsWithChildren<LabelProps>) => <S.Label {...rests}>{children}</S.Label>;

const Adornment = ({ children, ...rests }: PropsWithChildren<AdornmentProps>) => (
  <S.Adornment {...rests}>{children}</S.Adornment>
);

const HelperText = ({ children, ...rests }: PropsWithChildren<HelperTextProps>) => (
  <S.HelperText {...rests}>{children}</S.HelperText>
);

const HelperTextType = (<HelperText />).type;
const LabelType = (<Label />).type;

const Base = ({
  variant = 'filled',
  size = 'medium',
  isValid = true,
  children,
  ...rests
}: PropsWithChildren<BaseProps>) => {
  const inputWithAdornment = getChildrenWithoutTypes(children, [HelperTextType, LabelType]);
  const helperText = getChildOfType(children, HelperTextType);
  const label = getChildOfType(children, LabelType);

  return (
    <S.Container>
      {label}
      <S.Base variant={variant} size={size} isValid={isValid} {...rests}>
        {inputWithAdornment}
      </S.Base>
      {helperText}
    </S.Container>
  );
};

const Input = Object.assign(Base, {
  TextField,
  Label,
  Adornment,
  HelperText,
});

export default Input;
