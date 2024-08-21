import { HTMLAttributes, PropsWithChildren } from 'react';
import { createPortal } from 'react-dom';

import { theme } from '../../style/theme';
import Heading from '../Heading/Heading';
import * as S from './Modal.style';

export type ModalSize = 'xsmall' | 'small' | 'medium' | 'large';

export interface BaseProps extends HTMLAttributes<HTMLDivElement> {
  isOpen: boolean;
  toggleModal: () => void;
  size?: ModalSize;
}

const Base = ({ isOpen, toggleModal, size = 'small', children, ...props }: PropsWithChildren<BaseProps>) => {
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

const Title = ({ children }: { children: string }) => (
  <S.TitleWrapper>
    <Heading.XSmall color={theme.color.light.secondary_900}>{children}</Heading.XSmall>
  </S.TitleWrapper>
);

const Body = ({ children, ...props }: PropsWithChildren<HTMLAttributes<HTMLDivElement>>) => (
  <S.BodyContainer {...props}>{children}</S.BodyContainer>
);

const Footer = ({ children, ...props }: PropsWithChildren<HTMLAttributes<HTMLDivElement>>) => (
  <S.FooterContainer {...props}>{children}</S.FooterContainer>
);

const Modal = Object.assign(Base, {
  Title,
  Body,
  Footer,
});

export default Modal;
