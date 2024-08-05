import { useCallback, useState } from 'react';

import { Template, TemplateEditRequest } from '@/types/template';
import { useCategory } from '../category/useCategory';
import { useInput } from '../utils/useInput';
import { useTemplateEditQuery } from './useTemplateEditQuery';

interface Props {
  template: Template;
  toggleEditButton: () => void;
}

export const useTemplateEdit = ({ template, toggleEditButton }: Props) => {
  const [title, handleTitleChange] = useInput(template.title);
  const [description, handleDescriptionChange] = useInput(template.description);

  const [snippets, setSnippets] = useState([...template.snippets]);
  const [deleteSnippetIds, setDeleteSnippetIds] = useState<number[]>([]);

  const categoryProps = useCategory(template.category);

  const initTags = template.tags.map((tag) => tag.name);
  const [tags, setTags] = useState<string[]>(initTags);
  const [value, handleValue, resetValue] = useInput('');

  const { mutateAsync, error } = useTemplateEditQuery(template.id);

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

  const handleCancelButton = () => {
    toggleEditButton();
  };

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

  const handleDeleteSnippet = (index: number) => {
    const deletedSnippetId = snippets[index].id;

    if (!snippets[index]) {
      console.error('존재하지 않는 스니펫 인덱스입니다.');
    }

    if (deletedSnippetId) {
      setDeleteSnippetIds((prevSnippetsId) => [...prevSnippetsId, deletedSnippetId]);
    }

    setSnippets((prevSnippets) => prevSnippets.filter((_, idx) => index !== idx));
  };

  const handleSaveButtonClick = async () => {
    const orderedSnippets = snippets.map((snippet, index) => ({
      ...snippet,
      ordinal: index + 1,
    }));
    const createSnippets = orderedSnippets.filter((snippet) => !snippet.id);
    const updateSnippets = orderedSnippets.filter((snippet) => snippet.id);

    const templateUpdate: TemplateEditRequest = {
      title,
      description,
      createSnippets,
      updateSnippets,
      deleteSnippetIds,
      categoryId: categoryProps.currentValue.id,
      tags,
    };

    console.log(templateUpdate, '???');

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
    snippets,
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
    handleDeleteSnippet,
    handleSaveButtonClick,
    error,
  };
};
