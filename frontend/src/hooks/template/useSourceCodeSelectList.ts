import { useEffect, useRef, useState } from 'react';

import { SourceCodes } from '@/types';

import { useScrollToTargetElement } from '../useScrollToTargetElement';

export const useSourceCodeSelectList = (sourceCodes: SourceCodes[]) => {
  const scrollTo = useScrollToTargetElement();

  const [currentFile, setCurrentFile] = useState<number | null>(sourceCodes[0]?.id ?? null);

  const sourceCodeRefs = useRef<(HTMLDivElement | null)[]>([]);

  useEffect(() => {
    if (sourceCodes.length) {
      setCurrentFile(0);
    }
  }, [sourceCodes, setCurrentFile]);

  const scrollToCurSourceCode = (index: number) => {
    const targetElement = sourceCodeRefs.current[index];

    scrollTo(targetElement);
  };

  const handleSelectOption = (index: number) => (e: React.MouseEvent<HTMLAnchorElement>) => {
    e.preventDefault();

    scrollToCurSourceCode(index);

    setCurrentFile(index);
  };

  return { currentFile, sourceCodeRefs, handleSelectOption };
};
