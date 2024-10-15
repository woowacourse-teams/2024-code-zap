import { useCallback, useState } from 'react';

import { validateFilename, validateSourceCode } from '@/service';
import { SourceCodes } from '@/types';

import { useToast } from '../useToast';

export const useSourceCode = (initSourceCode: SourceCodes[]) => {
  const [sourceCodes, setSourceCodes] = useState([...initSourceCode]);
  const [deleteSourceCodeIds, setDeleteSourceCodeIds] = useState<number[]>([]);

  const { failAlert } = useToast();

  const handleFilenameChange = useCallback(
    (newFilename: string, idx: number) => {
      const errorMessage = validateFilename(newFilename);

      if (errorMessage) {
        failAlert(errorMessage);

        return;
      }

      setSourceCodes((prevSourceCodes) =>
        prevSourceCodes.map((sourceCodes, index) =>
          index === idx ? { ...sourceCodes, filename: newFilename } : sourceCodes,
        ),
      );
    },
    [failAlert],
  );

  const handleContentChange = useCallback((newContent: string, idx: number) => {
    setSourceCodes((prevSourceCodes) =>
      prevSourceCodes.map((sourceCodes, index) =>
        index === idx ? { ...sourceCodes, content: newContent } : sourceCodes,
      ),
    );
  }, []);

  const isValidContentChange = useCallback(
    (newContent: string) => {
      const errorMessage = validateSourceCode(newContent);

      if (errorMessage) {
        failAlert(errorMessage);

        return false;
      }

      return true;
    },
    [failAlert],
  );

  const addNewEmptySourceCode = useCallback(() => {
    setSourceCodes((prevSourceCode) => [
      ...prevSourceCode,
      {
        filename: '',
        content: '',
        ordinal: prevSourceCode.length + 1,
      },
    ]);
  }, []);

  const handleDeleteSourceCode = useCallback(
    (index: number) => {
      const deletedSourceCodeId = sourceCodes[index].id;

      if (!sourceCodes[index]) {
        console.error('존재하지 않는 소스코드는 삭제할 수 없습니다. 삭제하려는 소스코드의 index를 다시 확인해주세요.');
      }

      if (deletedSourceCodeId) {
        setDeleteSourceCodeIds((prevSourceCodeId) => [...prevSourceCodeId, deletedSourceCodeId]);
      }

      setSourceCodes((prevSourceCodes) => prevSourceCodes.filter((_, idx) => index !== idx));
    },
    [sourceCodes],
  );

  return {
    sourceCodes,
    deleteSourceCodeIds,
    handleFilenameChange,
    isValidContentChange,
    handleContentChange,
    addNewEmptySourceCode,
    handleDeleteSourceCode,
  };
};
