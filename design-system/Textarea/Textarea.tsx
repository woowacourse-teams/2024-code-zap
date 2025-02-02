import { PropsWithChildren, useEffect, useRef } from 'react';

import { getChildOfType, getChildrenWithoutTypes } from '@/utils';

import * as S from './Textarea.style';

export interface BaseProps extends React.HTMLAttributes<HTMLDivElement> {
  size?: 'small' | 'medium' | 'large' | 'xlarge';
  variant?: 'filled' | 'outlined' | 'text';
  isValid?: boolean;
  inputColor?: string;
}

export interface TextFieldProps extends React.TextareaHTMLAttributes<HTMLTextAreaElement> {
  size?: 'small' | 'medium' | 'large' | 'xlarge';
  minRows?: number;
  maxRows?: number;
  placeholderColor?: string;
}

export interface LabelProps extends React.LabelHTMLAttributes<HTMLLabelElement> {}

export interface HelperTextProps extends React.HTMLAttributes<HTMLSpanElement> {}

const Label = ({ children, ...rests }: PropsWithChildren<LabelProps>) => <S.Label {...rests}>{children}</S.Label>;

const HelperText = ({ children, ...rests }: PropsWithChildren<HelperTextProps>) => (
  <S.HelperText {...rests}>{children}</S.HelperText>
);

const LabelType = (<Label />).type;
const HelperTextType = (<HelperText />).type;

const Base = ({
  variant = 'filled',
  size = 'medium',
  isValid = true,
  children,
  ...rests
}: PropsWithChildren<BaseProps>) => {
  const textarea = getChildrenWithoutTypes(children, [HelperTextType, LabelType]);
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
        {textarea}
      </S.Base>
      {helperText}
    </S.Container>
  );
};

const fontSize = {
  small: 14,
  medium: 16,
  large: 18,
  xlarge: 32,
} as const;

const TextField = ({ size = 'medium', minRows = 1, maxRows = 1, placeholderColor, ...rest }: TextFieldProps) => {
  const textareaRef = useRef<HTMLTextAreaElement | null>(null);

  const adjustHeight = () => {
    if (textareaRef.current) {
      textareaRef.current.style.height = 'auto';
      const scrollHeight = textareaRef.current.scrollHeight;

      textareaRef.current.style.height = `${Math.min(scrollHeight, maxRows * (fontSize[size] * 1.5))}px`;
    }
  };

  useEffect(() => {
    adjustHeight();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  return (
    <S.TextareaField
      ref={textareaRef}
      rows={minRows}
      maxRows={maxRows}
      onInput={adjustHeight}
      placeholderColor={placeholderColor}
      {...rest}
    />
  );
};

const Textarea = Object.assign(Base, {
  TextField,
  Label,
  HelperText,
});

export default Textarea;
