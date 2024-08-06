import { useCallback, useState } from 'react';
import { useNavigate } from 'react-router-dom';

import { ToastContext } from '@/context/ToastContext';
import { Snippet, TemplateUploadRequest } from '@/types/template';
import { useCategory } from '../category/useCategory';
import useCustomContext from '../utils/useCustomContext';
import { useInput } from '../utils/useInput';
import { useTemplateUploadQuery } from './useTemplateUploadQuery';

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

  const { mutateAsync: uploadTemplate, error } = useTemplateUploadQuery();

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

    try {
      await uploadTemplate(newTemplate, {
        onSuccess: (res) => {
          navigate(res.headers.get('Location') || '/');
        },
      });
    } catch (error) {
      console.error('Failed to update template:', error);
    }
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
