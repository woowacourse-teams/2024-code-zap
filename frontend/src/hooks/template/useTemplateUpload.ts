import { useCallback, useState } from 'react';
import { useNavigate } from 'react-router-dom';

import { ToastContext } from '@/contexts';
import { useTemplateUploadMutation } from '@/queries/template';
import { END_POINTS } from '@/routes';
import type { SourceCodes, TemplateUploadRequest } from '@/types';
import { useCategory } from '../category';
import { useCustomContext, useInput, useInputWithValidate } from '../utils';

export const useTemplateUpload = () => {
  const navigate = useNavigate();
  const { failAlert } = useCustomContext(ToastContext);

  const categoryProps = useCategory();

  const [title, handleTitleChange] = useInput('');
  const [description, handleDescriptionChange] = useInput('');

  const [sourceCodes, setSourceCodes] = useState<SourceCodes[]>([
    {
      filename: '',
      content: '',
      ordinal: 1,
    },
  ]);

  const { value, handleChange: handleValue, resetValue } = useInputWithValidate('');

  const [tags, setTags] = useState<string[]>([]);

  const { mutateAsync: uploadTemplate, error } = useTemplateUploadMutation();

  const handleCodeChange = useCallback((newContent: string, idx: number) => {
    setSourceCodes((prevSourceCodes) =>
      prevSourceCodes.map((sourceCode, index) => (index === idx ? { ...sourceCode, content: newContent } : sourceCode)),
    );
  }, []);

  const handleFileNameChange = useCallback((newFileName: string, idx: number) => {
    setSourceCodes((prevSourceCodes) =>
      prevSourceCodes.map((sourceCode, index) =>
        index === idx ? { ...sourceCode, filename: newFileName } : sourceCode,
      ),
    );
  }, []);

  const handleAddButtonClick = () => {
    setSourceCodes((prevSourceCodes) => [
      ...prevSourceCodes,
      {
        filename: '',
        content: '',
        ordinal: prevSourceCodes.length + 1,
      },
    ]);
  };

  const handleDeleteSourceCode = (index: number) => {
    if (!sourceCodes[index]) {
      console.error('존재하지 않는 소스코드 인덱스입니다.');
    }

    setSourceCodes((prevSourceCodes) => prevSourceCodes.filter((_, idx) => index !== idx));
  };

  const handleCancelButton = () => {
    navigate(-1);
  };

  const validateTemplate = () => {
    if (!title) {
      return '제목을 입력해주세요';
    }

    if (sourceCodes.filter((sourceCode) => !sourceCode.filename).length) {
      return '파일명을 입력해주세요';
    }

    if (sourceCodes.filter((sourceCode) => !sourceCode.content).length) {
      return '소스코드 내용을 입력해주세요';
    }

    return '';
  };

  const handleSaveButtonClick = async () => {
    if (validateTemplate()) {
      failAlert(validateTemplate());

      return;
    }

    const newTemplate: TemplateUploadRequest = {
      title,
      description,
      sourceCodes,
      thumbnailOrdinal: 1,
      categoryId: categoryProps.currentValue.id,
      tags,
    };

    await uploadTemplate(newTemplate, {
      onSuccess: () => {
        navigate(END_POINTS.MY_TEMPLATES);
      },
    });
  };

  return {
    title,
    description,
    sourceCodes,
    error,
    handleDescriptionChange,
    handleTitleChange,
    handleCodeChange,
    handleFileNameChange,
    handleAddButtonClick,
    handleDeleteSourceCode,
    handleCancelButton,
    handleSaveButtonClick,

    categoryProps,
    tagProps: {
      value,
      handleValue,
      resetValue,
      tags,
      setTags,
    },
  };
};
