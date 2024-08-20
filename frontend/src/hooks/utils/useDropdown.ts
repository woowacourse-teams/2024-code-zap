import { useCallback, useEffect, useRef, useState } from 'react';

export const useDropdown = <T>(initValue: T) => {
  const [isOpen, setIsOpen] = useState(false);
  const [currentValue, setCurrentValue] = useState<T>(initValue);
  const dropdownRef = useRef<HTMLDivElement | null>(null);

  const toggleDropdown = useCallback(() => {
    setIsOpen((prev) => !prev);
  }, []);

  const handleCurrentValue = useCallback((newValue: T) => {
    setCurrentValue(newValue);
    setIsOpen(false);
  }, []);

  const handleClickOutside = useCallback(
    (event: MouseEvent) => {
      if (dropdownRef.current && !dropdownRef.current.contains(event.target as Node) && isOpen) {
        setIsOpen(false);
      }
    },
    [isOpen],
  );

  useEffect(() => {
    document.addEventListener('mousedown', handleClickOutside);

    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, [handleClickOutside]);

  return { isOpen, toggleDropdown, currentValue, handleCurrentValue, dropdownRef };
};
