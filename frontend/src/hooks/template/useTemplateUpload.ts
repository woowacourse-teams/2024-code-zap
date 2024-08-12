import { useCallback, useState } from 'react';
import { useNavigate } from 'react-router-dom';

import { ToastContext } from '@/contexts';
import { useTemplateUploadMutation } from '@/queries/template';
import type { Snippet, TemplateUploadRequest } from '@/types';
import { useCategory } from '../category';
import { useCustomContext, useInput } from '../utils';

export const useTemplateUpload = () => {
  const navigate = useNavigate();
  const { failAlert } = useCustomContext(ToastContext);

  const categoryProps = useCategory();

  const [title, handleTitleChange] = useInput('');
  const [description, handleDescriptionChange] = useInput('');

  const [snippets, setSnippets] = useState<Snippet[]>([
    {
      filename: '',
      content: '',
      ordinal: 1,
    },
  ]);

  const [value, handleValue, resetValue] = useInput('');
  const [tags, setTags] = useState<string[]>([]);

  const { mutateAsync: uploadTemplate, error } = useTemplateUploadMutation();

  const handleCodeChange = useCallback((newContent: string, idx: number) => {
    setSnippets((prevSnippets) =>
      prevSnippets.map((snippet, index) => (index === idx ? { ...snippet, content: newContent } : snippet)),
    );
  }, []);

  const handleFileNameChange = useCallback((newFileName: string, idx: number) => {
    setSnippets((prevSnippets) =>
      prevSnippets.map((snippet, index) => (index === idx ? { ...snippet, filename: newFileName } : snippet)),
    );
  }, []);

  const handleAddButtonClick = () => {
    setSnippets((prevSnippets) => [
      ...prevSnippets,
      {
        filename: '',
        content: '',
        ordinal: prevSnippets.length + 1,
      },
    ]);
  };

  const handleDeleteSnippet = (index: number) => {
    if (!snippets[index]) {
      console.error('존재하지 않는 스니펫 인덱스입니다.');
    }

    setSnippets((prevSnippets) => prevSnippets.filter((_, idx) => index !== idx));
  };

  const handleCancelButton = () => {
    navigate(-1);
  };

  const validateTemplate = () => {
    if (!title) {
      return '제목을 입력해주세요';
    }

    if (snippets.filter((snippet) => !snippet.filename).length) {
      return '파일 명을 입력해주세요';
    }

    if (snippets.filter((snippet) => !snippet.content).length) {
      return '스니펫 내용을 입력해주세요';
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
      snippets,
      categoryId: categoryProps.currentValue.id,
      tags,
    };

    await uploadTemplate(newTemplate, {
      onSuccess: () => {
        navigate('/');
      },
    });
  };

  return {
    title,
    description,
    snippets,
    error,
    handleDescriptionChange,
    handleTitleChange,
    handleCodeChange,
    handleFileNameChange,
    handleAddButtonClick,
    handleDeleteSnippet,
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
