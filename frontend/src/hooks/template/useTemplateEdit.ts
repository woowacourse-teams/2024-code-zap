import { useCallback, useState } from 'react';

import { ToastContext } from '@/contexts';
import { useTemplateEditMutation } from '@/queries/template';
import type { Template, TemplateEditRequest } from '@/types';
import { useCategory } from '../category';
import { useCustomContext, useInput } from '../utils';

interface Props {
  template: Template;
  toggleEditButton: () => void;
}

export const useTemplateEdit = ({ template, toggleEditButton }: Props) => {
  const { failAlert } = useCustomContext(ToastContext);

  const [title, handleTitleChange] = useInput(template.title);
  const [description, handleDescriptionChange] = useInput(template.description);

  const [sourceCodes, setSourceCodes] = useState([...template.sourceCodes]);
  const [deleteSourceCodeIds, setDeleteSourceCodeIds] = useState<number[]>([]);

  const categoryProps = useCategory(template.category);

  const initTags = template.tags.map((tag) => tag.name);
  const [tags, setTags] = useState<string[]>(initTags);
  const [value, handleValue, resetValue] = useInput('');

  const { mutateAsync, error } = useTemplateEditMutation(template.id);

  const handleAddButtonClick = () => {
    setSourceCodes((prevSourceCode) => [
      ...prevSourceCode,
      {
        filename: '',
        content: '',
        ordinal: prevSourceCode.length + 1,
      },
    ]);
  };

  const handleCancelButton = () => {
    toggleEditButton();
  };

  const handleCodeChange = useCallback((newContent: string, idx: number) => {
    setSourceCodes((prevSourceCodes) =>
      prevSourceCodes.map((sourceCodes, index) =>
        index === idx ? { ...sourceCodes, content: newContent } : sourceCodes,
      ),
    );
  }, []);

  const handleFileNameChange = useCallback((newFileName: string, idx: number) => {
    setSourceCodes((prevSourceCodes) =>
      prevSourceCodes.map((sourceCodes, index) =>
        index === idx ? { ...sourceCodes, filename: newFileName } : sourceCodes,
      ),
    );
  }, []);

  const handleDeleteSourceCode = (index: number) => {
    const deletedSourceCodeId = sourceCodes[index].id;

    if (!sourceCodes[index]) {
      console.error('존재하지 않는 스니펫 인덱스입니다.');
    }

    if (deletedSourceCodeId) {
      setDeleteSourceCodeIds((prevSourceCodeId) => [...prevSourceCodeId, deletedSourceCodeId]);
    }

    setSourceCodes((prevSourceCodes) => prevSourceCodes.filter((_, idx) => index !== idx));
  };

  const validateTemplate = () => {
    if (!title) {
      return '제목을 입력해주세요';
    }

    if (sourceCodes.filter((sourceCode) => !sourceCode.filename).length) {
      return '파일 명을 입력해주세요';
    }

    if (sourceCodes.filter((sourceCode) => !sourceCode.content).length) {
      return '스니펫 내용을 입력해주세요';
    }

    return '';
  };

  const handleSaveButtonClick = async () => {
    if (validateTemplate()) {
      failAlert(validateTemplate());

      return;
    }

    const orderedSourceCodes = sourceCodes.map((sourceCode, index) => ({
      ...sourceCode,
      ordinal: index + 1,
    }));
    const createSourceCodes = orderedSourceCodes.filter((sourceCode) => !sourceCode.id);
    const updateSourceCodes = orderedSourceCodes.filter((sourceCode) => sourceCode.id);

    const templateUpdate: TemplateEditRequest = {
      title,
      description,
      createSourceCodes,
      updateSourceCodes,
      deleteSourceCodeIds,
      categoryId: categoryProps.currentValue.id,
      tags,
    };

    try {
      await mutateAsync({ id: template.id, template: templateUpdate });
      toggleEditButton();
    } catch (error) {
      console.error('Failed to update template:', error);
    }
  };

  return {
    title,
    description,
    sourceCodes,
    categoryProps,
    tagProps: {
      tags,
      setTags,
      value,
      handleValue,
      resetValue,
    },
    handleTitleChange,
    handleDescriptionChange,
    handleAddButtonClick,
    handleCancelButton,
    handleCodeChange,
    handleFileNameChange,
    handleDeleteSourceCode,
    handleSaveButtonClick,
    error,
  };
};
