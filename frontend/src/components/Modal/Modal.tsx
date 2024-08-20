import { HTMLAttributes, PropsWithChildren } from 'react';
import { createPortal } from 'react-dom';

import * as S from './Modal.style';

export type ModalSize = 'xsmall' | 'small' | 'medium' | 'large';

export interface BaseProps extends HTMLAttributes<HTMLDivElement> {
  isOpen: boolean;
  toggleModal: () => void;
  size?: ModalSize;
}

const Base = ({
  isOpen,
  toggleModal,
  size = 'small',

  children,
  ...rests
}: PropsWithChildren<BaseProps>) => {
  if (!isOpen) {
    return null;
  }

  return createPortal(
    <S.Container>
      <S.Backdrop onClick={toggleModal} />
      <S.Base size={size} {...rests}>
        {children}
      </S.Base>
    </S.Container>,
    document.body,
  );
};

const Header = ({ children, ...props }: PropsWithChildren<HTMLAttributes<HTMLDivElement>>) => (
  <S.Header {...props}>{children}</S.Header>
);

const Body = ({ children, ...props }: PropsWithChildren<HTMLAttributes<HTMLDivElement>>) => (
  <S.Body {...props}>{children}</S.Body>
);

const Footer = ({ children, ...props }: PropsWithChildren<HTMLAttributes<HTMLDivElement>>) => (
  <S.Footer {...props}>{children}</S.Footer>
);

const Modal = Object.assign(Base, {
  Header,
  Body,
  Footer,
});

export default Modal;
