import { useMemo, useRef, CSSProperties } from 'react';

export const useScreenReader = () => {
  const elementRef = useRef<HTMLDivElement | null>(null);

  const updateScreenReaderMessage = (message: string) => {
    if (elementRef.current) {
      elementRef.current.textContent = message;
    }
  };

  const visuallyHiddenStyle: CSSProperties = {
    position: 'absolute',
    width: '1px',
    height: '1px',
    margin: '-1px',
    padding: '0',
    border: '0',
    overflow: 'hidden',
    clip: 'rect(0, 0, 0, 0)',
    whiteSpace: 'nowrap',
    wordWrap: 'normal',
  };

  const visuallyHiddenProps = useMemo(
    () => ({
      ref: elementRef,
      style: visuallyHiddenStyle,
      'aria-live': 'polite' as const,
      'aria-hidden': false,
    }),
    [],
  );

  return { visuallyHiddenProps, updateScreenReaderMessage };
};
