import { useRef, useState } from 'react';

import { SourceCodes } from '@/types';
import { useScrollToTargetElement } from './useScrollToTargetElement';

export const useSelectList = (sourceCodes: SourceCodes[]) => {
  const scrollTo = useScrollToTargetElement();
  const [currentFile, setCurrentFile] = useState<number | null>(null);

  const sourceCodeRefs = useRef<(HTMLDivElement | null)[]>([]);

  const handleSelectOption = (index: number) => (event: React.MouseEvent<HTMLAnchorElement>) => {
    event.preventDefault();

    const targetElement = sourceCodeRefs.current[index];

    scrollTo(targetElement);

    const id = sourceCodes[index].id;

    if (!id) {
      console.error('id가 존재하지 않습니다.(useSelectList)');

      return;
    }

    setCurrentFile(id);
  };

  return { currentFile, setCurrentFile, sourceCodeRefs, handleSelectOption };
};
