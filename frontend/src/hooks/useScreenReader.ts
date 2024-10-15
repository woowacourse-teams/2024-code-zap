import { useCallback } from 'react';

export const useScreenReader = () => {
  const updateScreenReaderMessage = useCallback((message: string) => {
    const element = document.getElementById('screen-reader');

    if (element) {
      element.setAttribute('aria-hidden', 'false');
      element.textContent = message;

      setTimeout(() => {
        element.setAttribute('aria-hidden', 'true');
        element.textContent = '';
      });
    }
  }, []);

  return { updateScreenReaderMessage };
};
