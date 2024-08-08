import React, { createContext, useState, ReactNode, useRef } from 'react';

import { Toast } from '@/components';

type ToastContextType = {
  failAlert: (message: string) => void;
  successAlert: (message: string) => void;
  infoAlert: (message: string) => void;
};

export const ToastContext = createContext<ToastContextType | undefined>(undefined);

export const ToastProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
  const [toastList, setToastList] = useState<{ id: number; type: 'success' | 'fail' | 'info'; message: string }[]>([]);
  const nextId = useRef(0);

  const removeToast = (id: number) => {
    setToastList((prev) => prev.filter((toast) => toast.id !== id));
  };

  const failAlert = (message: string) => {
    const id = nextId.current;

    nextId.current += 1;

    setToastList((prev) => [...prev, { id, type: 'fail', message }]);
    setTimeout(() => removeToast(id), 4000);
  };

  const successAlert = (message: string) => {
    const id = nextId.current;

    nextId.current += 1;

    setToastList((prev) => [...prev, { id, type: 'success', message }]);
    setTimeout(() => removeToast(id), 4000);
  };

  const infoAlert = (message: string) => {
    const id = nextId.current;

    nextId.current += 1;

    setToastList((prev) => [...prev, { id, type: 'info', message }]);
    setTimeout(() => removeToast(id), 4000);
  };

  return (
    <ToastContext.Provider value={{ failAlert, successAlert, infoAlert }}>
      {toastList.map((toast) => (
        <Toast key={toast.id} type={toast.type}>
          {toast.message}
        </Toast>
      ))}
      {children}
    </ToastContext.Provider>
  );
};
