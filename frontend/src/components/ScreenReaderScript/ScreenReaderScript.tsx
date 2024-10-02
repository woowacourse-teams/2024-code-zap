import { useEffect, useRef, useState } from 'react';

interface Props {
  message: string;
}

const ScreenReaderScript = ({ message }: Props) => {
  const announcerRef = useRef<HTMLDivElement>(null);
  const [isVisible, setIsVisible] = useState(false);

  useEffect(() => {
    if (announcerRef.current) {
      announcerRef.current.textContent = message;
    }

    setIsVisible(true);

    const timer = setTimeout(() => {
      setIsVisible(false);
      if (announcerRef.current) {
        announcerRef.current.textContent = '';
      }
    });

    return () => clearTimeout(timer);
  }, [message]);

  return (
    <div
      ref={announcerRef}
      aria-live='polite'
      aria-hidden={isVisible ? 'false' : 'true'}
      style={{
        position: 'absolute',
        width: '1px',
        height: '1px',
        overflow: 'hidden',
      }}
    />
  );
};

export default ScreenReaderScript;
