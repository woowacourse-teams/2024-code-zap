import { HTMLAttributes, InputHTMLAttributes, LabelHTMLAttributes, PropsWithChildren } from 'react';

import { getChildOfType, getChildrenWithoutTypes } from '@/utils';

import * as S from './Input.style';

export interface BaseProps extends HTMLAttributes<HTMLDivElement> {
  size?: 'small' | 'medium' | 'large' | 'xlarge';
  variant?: 'filled' | 'outlined' | 'text';
  isValid?: boolean;
  inputColor?: string;
}
export interface TextFieldProps extends InputHTMLAttributes<HTMLInputElement> {
  inputSize?: 'small' | 'medium' | 'large';
  placeholderColor?: string;
}

export interface LabelProps extends LabelHTMLAttributes<HTMLLabelElement> {}

export interface AdornmentProps extends HTMLAttributes<HTMLDivElement> {
  as?: 'div' | 'button';
}

export interface HelperTextProps extends HTMLAttributes<HTMLSpanElement> {}

const TextField = ({ ...rests }: TextFieldProps) => <S.TextField {...rests} />;

const Label = ({ children, ...rests }: PropsWithChildren<LabelProps>) => <S.Label {...rests}>{children}</S.Label>;

const Adornment = ({ children, as, ...rests }: PropsWithChildren<AdornmentProps>) => {
  const buttonProps = as === 'button' ? { type: 'button' } : {};

  return (
    <S.Adornment as={as} {...rests} {...buttonProps} data-adornment>
      {children}
    </S.Adornment>
  );
};
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

  const handleFocusInput = (e: React.MouseEvent<HTMLDivElement>) => {
    const target = e.target as HTMLElement;
    const isAdornmentClicked = target.closest('[data-adornment]');
    const isInputClicked = target.tagName === 'INPUT';

    if (isAdornmentClicked) {
      return;
    }

    const input = e.currentTarget.querySelector('input');

    if (input && !isInputClicked) {
      input.focus();

      const length = input.value.length;

      input.setSelectionRange(length, length);
    }
  };

  return (
    <S.Container>
      {label}
      <S.Base variant={variant} size={size} isValid={isValid} {...rests} onClick={handleFocusInput}>
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
