import React, { useState, useEffect } from 'react';

import * as S from './Toast.style';

interface ToastProps {
  children: React.ReactNode;
  type: 'success' | 'fail' | 'info';
}

const Toast = ({ children, type }: ToastProps) => {
  const [visible, setVisible] = useState(true);

  useEffect(() => {
    const timer = setTimeout(() => {
      setVisible(false);
    }, 2000);

    return () => clearTimeout(timer);
  }, []);

  return (
    <S.BaseToast visible={visible} type={type}>
      {children}
    </S.BaseToast>
  );
};

export default Toast;
