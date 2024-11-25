import { useEffect, useRef, useState } from 'react';

import { useScrollToTargetElement } from '@/hooks';

export const useSelectList = () => {
  const scrollTo = useScrollToTargetElement();

  const [currentOption, setCurrentOption] = useState<number | null>(null);

  const linkedElementRefs = useRef<(HTMLDivElement | null)[]>([]);

  useEffect(() => {
    if (!currentOption) {
      setCurrentOption(0);
    }
  }, [currentOption, setCurrentOption]);

  const scrollToLinkedElement = (index: number) => {
    const targetLinkedElement = linkedElementRefs.current[index];

    scrollTo(targetLinkedElement);
  };

  const handleSelectOption = (index: number) => (e: React.MouseEvent<HTMLAnchorElement>) => {
    e.preventDefault();

    scrollToLinkedElement(index);

    setCurrentOption(index);
  };

  return { currentOption, linkedElementRefs, handleSelectOption };
};
