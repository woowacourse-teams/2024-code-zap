import { HTMLAttributes, PropsWithChildren, ReactNode } from 'react';
import { createPortal } from 'react-dom';

import { Heading } from '@/components';
import { usePressESC } from '@/hooks/usePressESC';
import { useScrollDisable } from '@/hooks/useScrollDisable';
import { theme } from '@/style/theme';

import * as S from './Modal.style';

export type ModalSize = 'xsmall' | 'small' | 'medium' | 'large';

export interface BaseProps extends HTMLAttributes<HTMLDivElement> {
  isOpen: boolean;
  toggleModal: () => void;
  size?: ModalSize;
}

const Base = ({ isOpen, toggleModal, size = 'small', children, ...props }: PropsWithChildren<BaseProps>) => {
  usePressESC(isOpen, toggleModal);
  useScrollDisable(isOpen);

  if (!isOpen) {
    return null;
  }

  return createPortal(
    <S.Base>
      <S.Backdrop onClick={toggleModal} />
      <S.ModalContainer size={size} {...props}>
        {children}
      </S.ModalContainer>
    </S.Base>,
    document.body,
  );
};

const Header = ({ children }: { children: ReactNode }) => (
  <S.HeaderWrapper>
    {typeof children === 'string' ? (
      <Heading.XSmall color={theme.color.light.secondary_900}>{children}</Heading.XSmall>
    ) : (
      children
    )}
  </S.HeaderWrapper>
);

const Body = ({ children, ...props }: PropsWithChildren<HTMLAttributes<HTMLDivElement>>) => (
  <S.BodyContainer {...props}>{children}</S.BodyContainer>
);

const Footer = ({ children, ...props }: PropsWithChildren<HTMLAttributes<HTMLDivElement>>) => (
  <S.FooterContainer {...props}>{children}</S.FooterContainer>
);

const Modal = Object.assign(Base, {
  Header,
  Body,
  Footer,
});

export default Modal;
