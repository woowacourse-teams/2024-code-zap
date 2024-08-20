import { useEffect } from 'react';

/**
 * usePressESC - ESC 키 down에 대한 이벤트 리스너를 붙입니다.
 * @param {boolean} condition  - 이벤트 리스너가 붙여지는 조건입니다.
 * @param {Function} callback  - ESC 키가 눌렸을 때 호출되는 함수입니다.
 */
export const usePressESC = <T extends (...args: unknown[]) => void>(condition: boolean, callback: T) => {
  useEffect(() => {
    if (!condition) {
      return;
    }

    const handleKeyDown = (event: KeyboardEvent) => {
      if (event.key === 'Escape') {
        callback();
      }
    };

    if (condition) {
      document.addEventListener('keydown', handleKeyDown);
    }

    // eslint-disable-next-line consistent-return
    return () => {
      document.removeEventListener('keydown', handleKeyDown);
    };
  }, [condition, callback]);
};
