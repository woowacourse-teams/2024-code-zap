import { useEffect } from 'react';

/**
 * useScrollDisable - 조건에 따라 화면 body의 스크롤을 막는 훅입니다.
 * @param {boolean} condition - 스크롤을 막는 조건입니다.
 */
export const useScrollDisable = (condition: boolean) => {
  useEffect(() => {
    if (condition) {
      document.body.style.overflow = 'hidden';
    }

    return () => {
      document.body.style.overflow = 'unset';
    };
  }, [condition]);
};
